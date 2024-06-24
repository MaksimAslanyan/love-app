package com.example.datinguserapispring.service.bot.impl;

import com.example.datinguserapispring.constants.Races;
import com.example.datinguserapispring.dto.bot.AgeRangeDTO;
import com.example.datinguserapispring.dto.chat.request.StartChatWhenSearchRequest;
import com.example.datinguserapispring.dto.like.request.LikeRequest;
import com.example.datinguserapispring.dto.photo.request.PhotoDataRequestDTO;
import com.example.datinguserapispring.dto.photo.response.PhotoDataDTO;
import com.example.datinguserapispring.exception.AuthenticationException;
import com.example.datinguserapispring.exception.Error;
import com.example.datinguserapispring.exception.UserNotFoundException;
import com.example.datinguserapispring.model.entity.Admin;
import com.example.datinguserapispring.model.entity.Photo;
import com.example.datinguserapispring.model.entity.bot.Bot1;
import com.example.datinguserapispring.model.entity.bot.Bot2;
import com.example.datinguserapispring.model.entity.bot.BotOnlineStatus;
import com.example.datinguserapispring.model.entity.dictionary.LookingFor;
import com.example.datinguserapispring.model.entity.user.Address;
import com.example.datinguserapispring.model.entity.user.OnlineStatus;
import com.example.datinguserapispring.model.entity.user.User;
import com.example.datinguserapispring.model.enums.Gender;
import com.example.datinguserapispring.model.enums.ParentType;
import com.example.datinguserapispring.model.enums.UserType;
import com.example.datinguserapispring.repository.*;
import com.example.datinguserapispring.service.bot.GenerateBotService;
import com.example.datinguserapispring.service.file.FileDataReaderService;
import com.example.datinguserapispring.service.lookingFor.LookingForService;
import com.example.datinguserapispring.util.ExtractorAgeFromUlrUtil;
import com.example.datinguserapispring.util.NameReaderUtil;
import com.example.datinguserapispring.util.PhotoReaderUtil;
import com.example.datinguserapispring.util.RandomDistanceGeneratorUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.File;
import java.time.LocalDateTime;
import java.util.*;
import java.util.concurrent.ThreadLocalRandom;
import java.util.stream.Collectors;

import static com.example.datinguserapispring.util.RandomNumberGeneratorUtil.getRandomNumberInt;

@Slf4j
@Service
@RequiredArgsConstructor
public class GenerateBotServiceImpl implements GenerateBotService {

    private final AdminRepository adminRepository;
    private final UserRepository userRepository;
    private final Bot1Repository bot1Repository;
    private final Bot2Repository bot2Repository;
    private final AddressRepository addressRepository;
    private final LookingForRepository lookingForRepository;
    private final LookingForService lookingForService;
    private final BotOnlineStatusRepository botOnlineStatusRepository;
    private final PhotoRepository photoRepository;
    private final FileDataReaderService fileDataReaderService;
    private final List<BotOnlineStatus> botOnlineStatuses = new ArrayList<>();
    private final List<List<LookingFor>> lookingFors = new ArrayList<>();
    private final List<List<Photo>> photos = new ArrayList<>();

    @Value("${botsName.path}")
    private String botsNameFile;
    @Value("${bot.path}")
    private String photoPath;


    @Override
    public Bot2 generate(LikeRequest request, String id) {
        return createBotAndRelatedEntities(request, id);
    }

    @Override
    public Bot2 generate(StartChatWhenSearchRequest request, String id) {
        return createBotAndRelatedEntities(request, id);
    }

    @Override
    @Transactional
    public List<Bot2> generateBots(List<User> users) {
        List<Bot2> bots = new ArrayList<>();
        try {
            for (User user : users) {
                Bot2 generatedBot = generate(user);
                if (generatedBot != null) {
                    bots.add(generatedBot);
                }
            }

            bot2Repository.saveAll(bots);

            List<Photo> filteredBotPhotos = new ArrayList<>(photos.stream().flatMap(Collection::stream).toList());

            List<Photo> validBotPhotos = filteredBotPhotos.stream()
                    .filter(photo -> bots.stream().anyMatch(bot -> bot.getId().equals(photo.getBot2().getId())))
                    .collect(Collectors.toList());

            int savedPhotoCount = photoRepository.saveAll(validBotPhotos).size();
            log.info("Saved " + savedPhotoCount + " photos");

            List<BotOnlineStatus> validBotOnlineStatuses = botOnlineStatuses.stream()
                    .filter(status -> bots.stream().anyMatch(bot -> bot.getId().equals(status.getBot2().getId())))
                    .collect(Collectors.toList());

            int savedStatusCount = botOnlineStatusRepository.saveAll(validBotOnlineStatuses).size();
            log.info("Saved " + savedStatusCount + " bot online statuses");

            List<LookingFor> filteredBotLookingFors = new ArrayList<>(lookingFors.stream().flatMap(Collection::stream).toList());

            List<LookingFor> validLookingFors = filteredBotLookingFors.stream()
                    .filter(lookingFor -> bots.stream().anyMatch(bot -> bot.getId().equals(lookingFor.getBot2().getId())))
                    .collect(Collectors.toList());

            int savedLookingForCount = lookingForRepository.saveAll(validLookingFors).size();
            log.info("Saved " + savedLookingForCount + " looking fors");

        } finally {
            botOnlineStatuses.clear();
            lookingFors.clear();
            photos.clear();
        }

        return bots;
    }


    public void saveBots(List<Bot2> bots) {
        log.info("ALL BOT " + bots);
    }

    @Override
    public Bot2 generate(User user) {
        log.info("Generating Bot2 for user: " + user.getId());
        log.info("Generating Bot2 for user: " + user);

        Address address = addressRepository.getFirstByUserId(user.getId());
        boolean isCountryValid = isCountryValid(address.getCountry());
        if (!isCountryValid) {
            return null;
        }

        List<String> userLookingFor = lookingForService.getUserLookingFor(user.getId());

        Map<Integer, List<PhotoDataDTO>> photoList = getPhotoList(user);

        checkIfUserHasBotLikeThatPhotoAndHandle(user, photoList);

        List<PhotoDataDTO> randomPhotoDataList = photoList.get(0);
        String url = randomPhotoDataList.get(0).getUrl();

        AgeRangeDTO ageRangeDTO = ExtractorAgeFromUlrUtil.extract(url);

        List<Double> distance = RandomDistanceGeneratorUtil.generate(30, 1, user.isPremium());

        byte minAge = ageRangeDTO.getMinAge();
        byte maxAge = ageRangeDTO.getMaxAge();
//        byte minAge =18;
//        byte maxAge = 20;
        byte randomAge = (byte) ThreadLocalRandom.current().nextInt(minAge, maxAge);
        log.info("Generated random age: " + randomAge);

        Bot1 bot1 = getBot1ByCriteria(user, minAge, maxAge);


        Admin admin = checkIfHasAdmin(bot1);

        String randomBotName = getRandomNameFromFile(user, address);

        Bot2 bot2 = new Bot2(randomBotName, randomAge, distance.get(0), admin, bot1, user);
        log.info("Saved Bot2: " + bot2);

        createBotOnlineStatus(bot2);

        createLookingFor(userLookingFor, bot2);

        crateBotPhotos(randomPhotoDataList, bot2);

        log.info("Bot2 generation complete.");
        return bot2;
    }

    @Override
    @Transactional
    public void generate() {
//        retrieveAllPhotos("/root/dating-app/bots_osnova/");
        retrieveAllPhotos("D:/bots_osnova/");
    }

    private <T> Bot2 createBotAndRelatedEntities(T request, String id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new AuthenticationException(Error.USER_NOT_FOUND));

        AgeRangeDTO ageRangeDTO;
        List<String> lookingForRequest;

        if (request instanceof LikeRequest likeRequest) {
            lookingForRequest = likeRequest.getLookingForList();

            ageRangeDTO = ExtractorAgeFromUlrUtil.extract(likeRequest.getPhotoDataDTOS().get(0).getUrl());

            Bot1 bot1 = getBot1ByCriteria(user, ageRangeDTO.getMinAge(), ageRangeDTO.getMaxAge());

            Admin admin = checkIfHasAdmin(bot1);

            return createBotAndSaveEntities(
                    admin, bot1,
                    likeRequest.getId(),
                    likeRequest.getName(),
                    likeRequest.getAge(),
                    likeRequest.getDistance(),
                    lookingForRequest,
                    likeRequest.getPhotoDataDTOS(),
                    user);
        } else if (request instanceof StartChatWhenSearchRequest startChatRequest) {
            lookingForRequest = startChatRequest.getLookingForList();

            ageRangeDTO = ExtractorAgeFromUlrUtil.extract(startChatRequest.getPhotoDataDTOS().get(0).getUrl());

            Bot1 bot1 = getBot1ByCriteria(user, ageRangeDTO.getMinAge(), ageRangeDTO.getMaxAge());

            Admin admin = checkIfHasAdmin(bot1);

            return createBotAndSaveEntities(
                    admin,
                    bot1,
                    startChatRequest.getSecondUserId(),
                    startChatRequest.getName(),
                    startChatRequest.getAge(),
                    startChatRequest.getDistance(),
                    lookingForRequest,
                    startChatRequest.getPhotoDataDTOS(),
                    user);

        }

        return null;
    }

    private Bot1 getBot1ByCriteria(User user, byte minAge, byte maxAge) {
        log.info("getBot1ByCriteria {} {} {} ", user.getTargetGender(), minAge, maxAge);
        String targetGender = user.getTargetGender();

        List<Bot1> bot1List = bot1Repository.findRandomBot1ByGenderAndRace(1,
                targetGender,
                minAge,
                maxAge,
                Races.RACES.get(0),
                ParentType.ADMIN.getValue());

        Bot1 bot1 = (bot1List != null && !bot1List.isEmpty()) ? bot1List.get(0) : null;

        // If bot1 is still null, hardcode age range to 25-31
        if (bot1 == null) {
            minAge = 25;
            maxAge = 31;
            bot1 = bot1Repository.findRandomBot1ByGenderAndRace(1,
                    targetGender,
                    minAge,
                    maxAge,
                    Races.RACES.get(0),
                    ParentType.ADMIN.getValue()).get(0);
        }

        log.info(String.valueOf(bot1));
        return bot1;
    }


    private Bot2 createBotAndSaveEntities(Admin admin, Bot1 bot1,
                                          String id, String name,
                                          byte age, double distance,
                                          List<String> userLookingFor,
                                          List<PhotoDataRequestDTO> photoDataDTOS,
                                          User user) {
        ParentType parentType = ParentType.ADMIN;
        Bot2 bot2 = new Bot2(id, admin, name, age, distance, bot1, parentType, user);

        checkIfBot2Exists(id, userLookingFor, photoDataDTOS, bot2);

        return bot2;
    }

    private void checkIfBot2Exists(String id,
                                   List<String> userLookingFor,
                                   List<PhotoDataRequestDTO> photoDataDTOS,
                                   Bot2 bot2) {
        boolean isExists = botOnlineStatusRepository.existsByBot2Id(id);
        if (!isExists) {
            Bot2 savedBot2 = bot2Repository.save(bot2);

            saveBotOnlineStatus(savedBot2);
            saveLookingFor(userLookingFor, savedBot2);

            List<Photo> photoList = convertToPhotoList(photoDataDTOS, savedBot2);
            photoRepository.saveAll(photoList);
        }
    }

    private void saveLookingFor(List<String> userLookingFor, Bot2 savedBot2) {
        List<LookingFor> lookingForList = new ArrayList<>();
        for (String lookingFor : userLookingFor) {
            LookingFor lookingForBot = new LookingFor(lookingFor, savedBot2);
            lookingForList.add(lookingForBot);
        }
        lookingForRepository.saveAll(lookingForList);
    }

    private void createLookingFor(List<String> userLookingFor, Bot2 savedBot2) {
        List<LookingFor> lookingForList = new ArrayList<>();
        for (String lookingFor : userLookingFor) {
            LookingFor lookingForBot = new LookingFor(lookingFor, savedBot2);
            lookingForList.add(lookingForBot);
        }
        lookingFors.add(lookingForList);
    }

    private void crateBotPhotos(List<PhotoDataDTO> randomPhotoDataList, Bot2 savedBot2) {
        List<Photo> botPhotos = convertToPhoto(randomPhotoDataList, savedBot2);
        photos.add(botPhotos);
    }

    private void saveBotOnlineStatus(Bot2 savedBot2) {
        BotOnlineStatus botOnlineStatus = new BotOnlineStatus(savedBot2, true, LocalDateTime.now());
        botOnlineStatusRepository.save(botOnlineStatus);
    }

    private void createBotOnlineStatus(Bot2 savedBot2) {
        BotOnlineStatus botOnlineStatus = new BotOnlineStatus(savedBot2, true, LocalDateTime.now());
        botOnlineStatuses.add(botOnlineStatus);
    }

    private String getRandomNameFromFile(User user, Address address) {
        List<String> names = fileDataReaderService.getNameFromFile(user, address);
        if (names.isEmpty()) {
            Address newAddress = new Address(getRandomCounty().get(0));
            getRandomNameFromFile(user, newAddress);
        }
        int getRandomName = getRandomNumberInt(0, names.size() - 1);
        if (getRandomName == 0) {
            getRandomName++;
        }
        log.info("Generated random bot name index: " + getRandomName);
        String randomBotName = names.get(getRandomName);
        log.info("Generated random bot name: " + randomBotName);
        return randomBotName;
    }

    private void checkIfUserHasBotLikeThatPhotoAndHandle(User user, Map<Integer, List<PhotoDataDTO>> photoList) {
        List<User> usersToGenerate = new ArrayList<>();
        String id = user.getId();

        for (Integer integer : photoList.keySet()) {
            List<PhotoDataDTO> photoDataList = photoList.get(integer);
            for (PhotoDataDTO photoDataDTO : photoDataList) {
                String url = photoDataDTO.getUrl();
                boolean isExist = userRepository.existsByUserIdAndUrl(user.getId(), url);
                if (isExist) {
                    log.warn("Photo already exists for user: " + id + ", URL 1: " + url);
                    usersToGenerate.add(user); // Queue the user for generation
                    break;
                }
            }
        }

        for (User userToGenerate : usersToGenerate) {
            generate(userToGenerate);
        }
    }

    private Admin checkIfHasAdmin(Bot1 bot1) {
        Admin admin = bot1.getAdmin();
        if (admin != null) {
            admin = bot1.getAdmin();
        }

        if (admin == null) {
            admin = getRandomAdmin();
        }
        return admin;
    }

    private Gender getGenderFromDirectoryName(String dirName) {
        if (dirName.equalsIgnoreCase("female")) {
            return Gender.FEMALE;
        } else if (dirName.equalsIgnoreCase("male")) {
            return Gender.MALE;
        }
        return null;
    }

    private Admin getRandomAdmin() {
        return adminRepository.findRandomAdminWithRole(1).get(0);
    }

    private byte[] getAgeRangeFromDirectoryName(String dirName) {
        String[] ageRange = dirName.split("_");
        if (ageRange.length == 2) {
            byte ageFrom = Byte.parseByte(ageRange[0]);
            byte ageTo = Byte.parseByte(ageRange[1]);
            return new byte[]{ageFrom, ageTo};
        }
        return null;
    }

    private List<Photo> convertToPhotoList(List<PhotoDataRequestDTO> photoDataDTOS, Bot2 bot2) {
        List<Photo> photoList = new ArrayList<>();
        for (PhotoDataRequestDTO photoDataDTO : photoDataDTOS) {
            Photo photo = new Photo(UserType.BOT.getValue(),
                    photoDataDTO.getIsAvatar(),
                    photoDataDTO.getUrl(),
                    LocalDateTime.now(),
                    bot2);

            photoList.add(photo);
        }
        return photoList;
    }

    private Map<Integer, List<PhotoDataDTO>> getPhotoList(User user) {
        return PhotoReaderUtil.retrievePhotos(photoPath, (byte) 18,
                (byte) 60, Objects.requireNonNull(Gender.fromValue(user.getTargetGender())), 1);
    }

    private List<Photo> convertToPhoto(List<PhotoDataDTO> botPhotos, Bot2 bot2) {
        List<Photo> photoList = new ArrayList<>();
        for (PhotoDataDTO photoDataDTO : botPhotos) {
            Photo photo = new Photo(UserType.BOT.getValue(),
                    photoDataDTO.getIsAvatar(),
                    photoDataDTO.getUrl(),
                    LocalDateTime.now(),
                    bot2);

            photoList.add(photo);
        }
        return photoList;
    }

    //This method is used when there is a need to add OWNER bots from a file,
    // and this method has not been refactored yet
    private int totalBots = 0;
    private int totalSaved = 0;
    private int wave = 0;

    public void retrieveAllPhotos(String sourceDirectory) {

        File packageDir = new File(sourceDirectory);

        File[] genderDirs = packageDir.listFiles();

        Admin admin = adminRepository.findRandomAdminWithRoleOwner(1).get(0);
        int batchSize = 100_000;
        List<Bot2> bot2Batch = new ArrayList<>();
        List<Photo> photoBatch = new ArrayList<>();
        List<String> names;
        int totalPackages = genderDirs.length; // Total number of packages

        for (File genderDir : genderDirs) {
            log.info("female and male  count bots{}" + totalBots);
            if (genderDir.isDirectory()) {


                Gender gender = getGenderFromDirectoryName(genderDir.getName());
                names = NameReaderUtil.readFile(botsNameFile,
                        "russia",
                        gender.getValue().toLowerCase(),
                        "names.txt");
                File[] ageDirs = genderDir.listFiles(File::isDirectory);
                if (ageDirs != null) {
                    for (File ageDir : ageDirs) {
                        byte[] ageRange = getAgeRangeFromDirectoryName(ageDir.getName());

                        if (ageRange != null) {
                            byte ageFrom = ageRange[0];
                            byte ageTo = ageRange[1];
                            int randomIndex = new Random().nextInt(names.size());
                            String randomName = names.get(randomIndex);
                            Bot1 bot1 = bot1Repository.findRandomBot1ByGenderAndRace(
                                    1,
                                    gender.getValue(),
                                    ageFrom,
                                    ageTo,
                                    Races.RACES.get(0), ParentType.OWNER.getValue()).get(0);
                            File[] numberDirs = ageDir.listFiles(File::isDirectory);

                            if (numberDirs != null) {
                                wave++;
                                log.error("Wave N " + wave);
                                log.error("save wave " + bot2Batch.size());

                                saveBatches(bot2Batch, photoBatch);
                                for (File numberDir : numberDirs) {

                                    File[] photoFiles = numberDir.listFiles(File::isFile);
                                    if (photoFiles != null) {
                                        Bot2 bot2 = new Bot2(UUID.randomUUID().toString(), admin, randomName, bot1, ParentType.OWNER, numberDir.getAbsolutePath());
                                        totalBots++;
                                        bot2Batch.add(bot2);
                                        for (File photoFile : photoFiles) {
                                            byte isAvatar = 0;
                                            Photo photo = new Photo(UserType.OWNER_BOT.getValue(), isAvatar, photoFile.getAbsolutePath(), LocalDateTime.now(), bot2);
                                            photoBatch.add(photo);

                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }


        // Save any remaining items in the batch
        if (!bot2Batch.isEmpty()) {
            saveBatches(bot2Batch, photoBatch);
        }

        log.warn("totalBots: {}", totalBots);
        log.warn("Total items saved: {}", totalSaved);
        log.info("End reading the photos");

    }

    private void saveBatches(List<Bot2> bot2Batch, List<Photo> photoBatch) {
        if (totalBots < 9500) {
            bot2Repository.saveAll(bot2Batch);
            photoRepository.saveAll(photoBatch);
            totalSaved += bot2Batch.size() + photoBatch.size();
            bot2Batch.clear();
            photoBatch.clear();
            log.info("Saved batch (Total saved: {})", totalSaved);
        }

    }

    private List<String> getRandomCounty() {
        return List.of("united states");
    }

    private static final List<String> VALID_COUNTRIES = List.of(
            "afghanistan", "albania", "andorra", "angola", "arabia", "argentina", "armenia",
            "australia", "austria", "azerbaijan", "bahamas", "bangladesh", "belgium", "belize",
            "bhutan", "bolivia", "bosnia and herzegovina", "brazil", "brunei", "bulgaria",
            "cambodia", "canada", "cape verde", "catalonia", "chile", "china", "colombia",
            "costa rica", "côte d'ivoire", "croatia", "cuba", "cyprus", "czech republic",
            "denmark", "dominican republic", "ecuador", "el salvador", "english", "estonia",
            "ethiopia", "finland", "france", "georgia", "germany", "ghana", "greece",
            "guatemala", "honduras", "hungary", "iceland", "india", "indonesia", "iran",
            "ireland", "israel", "italia", "jamaica", "japan", "kazakhstan", "kenya",
            "kosovo", "kyrgyzstan", "latvia", "liechtenstein", "lithuania", "luxembourg",
            "malaysia", "malta", "mexico", "moldova", "mongolia", "montenegro", "morocco",
            "myanmar", "nepal", "netherlands", "new zealand", "nicaragua", "north macedonia",
            "norway", "pakistan", "portugal", "quebec", "romania", "russia", "rwanda",
            "serbia", "singapore", "slovakia", "slovenia", "south africa", "south korea",
            "spain", "sri lanka", "sweden", "switzerland", "taiwan", "panama", "tajikistan",
            "thailand", "turkey", "turkmenistan", "ukraine", "united kingdom", "united states",
            "uruguay", "uzbekistan", "venezuela", "vietnam"
    );

    public boolean isCountryValid(String country) {
        if (country == null) {
            return false;
        }

        String lowercaseCountry = country.toLowerCase(Locale.ROOT);
        return VALID_COUNTRIES.contains(lowercaseCountry);
    }
}
