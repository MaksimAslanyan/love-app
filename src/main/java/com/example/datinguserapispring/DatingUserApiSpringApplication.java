package com.example.datinguserapispring;

import com.example.datinguserapispring.repository.AdminRepository;
import com.example.datinguserapispring.repository.Bot1Repository;
import com.example.datinguserapispring.repository.Bot2Repository;
import com.example.datinguserapispring.repository.BotOnlineStatusRepository;
import com.example.datinguserapispring.repository.LookingForRepository;
import com.example.datinguserapispring.repository.OnlineStatusRepository;
import com.example.datinguserapispring.repository.PhotoRepository;
import com.example.datinguserapispring.repository.UserLikeRepository;
import com.example.datinguserapispring.repository.UserRepository;
import com.example.datinguserapispring.service.bot.GenerateBotService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.Random;

@SpringBootApplication
@EnableScheduling
@EnableJpaRepositories
@EnableTransactionManagement
@RequiredArgsConstructor
@EnableConfigurationProperties
@EnableAspectJAutoProxy
@EnableAsync(proxyTargetClass = true)
@Slf4j
public class DatingUserApiSpringApplication implements CommandLineRunner {

    private final Bot1Repository bot1Repository;
    private final AdminRepository adminRepository;
    private final UserRepository userRepository;
    private final GenerateBotService generateBotService;
    private final Bot2Repository bot2Repository;
    private final PhotoRepository photoRepository;
    private final OnlineStatusRepository onlineStatusRepository;
//    private final BotLikeScheduler botLikeScheduler;
    private final BotOnlineStatusRepository botOnlineStatusRepository;
    private final LookingForRepository lookingForRepository;
    private final UserLikeRepository userLikeRepository;

    @PersistenceContext
    private EntityManager entityManager;

    public static void main(String[] args) {
        SpringApplication.run(DatingUserApiSpringApplication.class, args);
    }


    @Override
    public void run(String... args) throws Exception {
//        long adminCount = adminRepository.count();
//        if (adminCount == 0) {
//            adminRepository.save(new Admin("moderator", "moderator", "0408f3c997f309c03b08bf3a4bc7b730", "moder", 1024, 32));
//        }
//        long count = bot1Repository.count();
//        if (count == 0) {
//
//            bot1Repository.save(new Bot1(Gender.MALE, Races.RACES.get(0), (byte) 18, (byte) 24));
//            bot1Repository.save(new Bot1(Gender.MALE, Races.RACES.get(0), (byte) 25, (byte) 31));
//            bot1Repository.save(new Bot1(Gender.MALE, Races.RACES.get(0), (byte) 32, (byte) 39));
//            bot1Repository.save(new Bot1(Gender.MALE, Races.RACES.get(0), (byte) 40, (byte) 49));
//            bot1Repository.save(new Bot1(Gender.MALE, Races.RACES.get(0), (byte) 50, (byte) 60));
//
//            bot1Repository.save(new Bot1(Gender.FEMALE, Races.RACES.get(0), (byte) 18, (byte) 24));
//            bot1Repository.save(new Bot1(Gender.FEMALE, Races.RACES.get(0), (byte) 25, (byte) 31));
//            bot1Repository.save(new Bot1(Gender.FEMALE, Races.RACES.get(0), (byte) 32, (byte) 39));
//            bot1Repository.save(new Bot1(Gender.FEMALE, Races.RACES.get(0), (byte) 40, (byte) 49));
//            bot1Repository.save(new Bot1(Gender.FEMALE, Races.RACES.get(0), (byte) 50, (byte) 60));
//
//            bot1Repository.save(new Bot1(Gender.MALE, Races.RACES.get(1), (byte) 18, (byte) 24));
//            bot1Repository.save(new Bot1(Gender.MALE, Races.RACES.get(1), (byte) 25, (byte) 31));
//            bot1Repository.save(new Bot1(Gender.MALE, Races.RACES.get(1), (byte) 32, (byte) 39));
//            bot1Repository.save(new Bot1(Gender.MALE, Races.RACES.get(1), (byte) 40, (byte) 49));
//            bot1Repository.save(new Bot1(Gender.MALE, Races.RACES.get(1), (byte) 50, (byte) 60));
//
//            bot1Repository.save(new Bot1(Gender.FEMALE, Races.RACES.get(1), (byte) 18, (byte) 24));
//            bot1Repository.save(new Bot1(Gender.FEMALE, Races.RACES.get(1), (byte) 25, (byte) 31));
//            bot1Repository.save(new Bot1(Gender.FEMALE, Races.RACES.get(1), (byte) 32, (byte) 39));
//            bot1Repository.save(new Bot1(Gender.FEMALE, Races.RACES.get(1), (byte) 40, (byte) 49));
//            bot1Repository.save(new Bot1(Gender.FEMALE, Races.RACES.get(1), (byte) 50, (byte) 60));
//
//            bot1Repository.save(new Bot1(Gender.MALE, Races.RACES.get(2), (byte) 18, (byte) 24));
//            bot1Repository.save(new Bot1(Gender.MALE, Races.RACES.get(2), (byte) 25, (byte) 31));
//            bot1Repository.save(new Bot1(Gender.MALE, Races.RACES.get(2), (byte) 32, (byte) 39));
//            bot1Repository.save(new Bot1(Gender.MALE, Races.RACES.get(2), (byte) 40, (byte) 49));
//            bot1Repository.save(new Bot1(Gender.MALE, Races.RACES.get(2), (byte) 50, (byte) 60));
//
//            bot1Repository.save(new Bot1(Gender.FEMALE, Races.RACES.get(2), (byte) 18, (byte) 24));
//            bot1Repository.save(new Bot1(Gender.FEMALE, Races.RACES.get(2), (byte) 25, (byte) 31));
//            bot1Repository.save(new Bot1(Gender.FEMALE, Races.RACES.get(2), (byte) 32, (byte) 39));
//            bot1Repository.save(new Bot1(Gender.FEMALE, Races.RACES.get(2), (byte) 40, (byte) 49));
//            bot1Repository.save(new Bot1(Gender.FEMALE, Races.RACES.get(2), (byte) 50, (byte) 60));
//
//            bot1Repository.save(new Bot1(Gender.MALE, Races.RACES.get(3), (byte) 18, (byte) 24));
//            bot1Repository.save(new Bot1(Gender.MALE, Races.RACES.get(3), (byte) 25, (byte) 31));
//            bot1Repository.save(new Bot1(Gender.MALE, Races.RACES.get(3), (byte) 32, (byte) 39));
//            bot1Repository.save(new Bot1(Gender.MALE, Races.RACES.get(3), (byte) 40, (byte) 49));
//            bot1Repository.save(new Bot1(Gender.MALE, Races.RACES.get(3), (byte) 50, (byte) 60));
//
//            bot1Repository.save(new Bot1(Gender.FEMALE, Races.RACES.get(3), (byte) 18, (byte) 24));
//            bot1Repository.save(new Bot1(Gender.FEMALE, Races.RACES.get(3), (byte) 25, (byte) 31));
//            bot1Repository.save(new Bot1(Gender.FEMALE, Races.RACES.get(3), (byte) 32, (byte) 39));
//            bot1Repository.save(new Bot1(Gender.FEMALE, Races.RACES.get(3), (byte) 40, (byte) 49));
//            bot1Repository.save(new Bot1(Gender.FEMALE, Races.RACES.get(3), (byte) 50, (byte) 60));
//
//            bot1Repository.save(new Bot1(Gender.MALE, Races.RACES.get(4), (byte) 18, (byte) 24));
//            bot1Repository.save(new Bot1(Gender.MALE, Races.RACES.get(4), (byte) 25, (byte) 31));
//            bot1Repository.save(new Bot1(Gender.MALE, Races.RACES.get(4), (byte) 32, (byte) 39));
//            bot1Repository.save(new Bot1(Gender.MALE, Races.RACES.get(4), (byte) 40, (byte) 49));
//            bot1Repository.save(new Bot1(Gender.MALE, Races.RACES.get(4), (byte) 50, (byte) 60));
//
//            bot1Repository.save(new Bot1(Gender.FEMALE, Races.RACES.get(4), (byte) 18, (byte) 24));
//            bot1Repository.save(new Bot1(Gender.FEMALE, Races.RACES.get(4), (byte) 25, (byte) 31));
//            bot1Repository.save(new Bot1(Gender.FEMALE, Races.RACES.get(4), (byte) 32, (byte) 39));
//            bot1Repository.save(new Bot1(Gender.FEMALE, Races.RACES.get(4), (byte) 40, (byte) 49));
//            bot1Repository.save(new Bot1(Gender.FEMALE, Races.RACES.get(4), (byte) 50, (byte) 60));
//        }

//        botLikeScheduler.provideLikesThirdPhase();
//        processAllUsersOnlineUsers();

//        generateBotService.generate();

    }

//    public void processAllUsers() {
//        int page = 0;
//        int pageSize = 150; // Adjust the page size as needed
//
//        Pageable pageable = PageRequest.of(page, pageSize);
//
//        Page<Bot2> userPage;
//
//        do {
//            userPage = bot2Repository.findAll(pageable);
//
//            for (Bot2 bot : userPage.getContent()) {
//
//                List<Photo> filteredList = photoRepository.findAllByBot2Id(bot.getId());
//                Map<String, List<Photo>> photos = new HashMap<>();
//                String key = null;
//                for (Photo photo : filteredList) {
//                    String url = photo.getUrl();
//                    int lastIndex = url.lastIndexOf('/');
//                    String lastPart = url.substring(0, lastIndex);
//
//                    // Check if the key is already present in the map
//                    if (!photos.containsKey(lastPart)) {
//                        photos.put(lastPart, new ArrayList<>());
//                    }
//
//                    // Append the photo to the list associated with the key
//                    photos.get(lastPart).add(photo);
//
//                    // Set the key if it's not already set
//                    if (key == null) {
//                        key = lastPart;
//                    }
//                }
//
//                // Access photos by key
//                List<Photo> photosForKey = photos.get(key);
//                if (photosForKey != null && !photosForKey.isEmpty()) {
//                    Photo photo = photosForKey.get(0);
//                    String url = photo.getUrl();
//                    AgeRangeDTO extract = ExtractorAgeFromUlrUtil.extract(url);
//                    byte minAge = extract.getMinAge();
//                    byte maxAge = extract.getMaxAge();
//                    byte randomAge = generateRandomNumber(minAge, maxAge);
//
//                    log.info("Setting age for Bot2 with ID {} to: {}", bot.getId(), randomAge);
//
//                    bot.setAge(randomAge);
//                    bot2Repository.save(bot);
//                    photoRepository.deleteAllByBot2Id(bot.getId());
//                    photoRepository.saveAll(photosForKey);
//                    photos.clear();
//                } else {
//                    log.warn("No photos found for Bot2 with ID {}", bot.getId());
//                }
//
//
//            }
//
//            page++;
//
//            pageable = PageRequest.of(page, pageSize);
//        } while (!userPage.isLast());
//    }



    private byte generateRandomNumber(int min, int max) {
        Random random = new Random();
        return (byte) (random.nextInt((max - min) + 1) + min);
    }
}
