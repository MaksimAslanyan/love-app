package com.example.datinguserapispring.service.bot.impl;

import com.example.datinguserapispring.dto.bot.AgeRangeDTO;
import com.example.datinguserapispring.dto.photo.response.PhotoDataDTO;
import com.example.datinguserapispring.model.enums.Gender;
import com.example.datinguserapispring.repository.UserRepository;
import com.example.datinguserapispring.util.ExtractorAgeFromUlrUtil;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.coobird.thumbnailator.Thumbnails;
import org.springframework.stereotype.Component;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Set;
import java.util.concurrent.ThreadLocalRandom;


@Component
@AllArgsConstructor
@Slf4j
public class GenerateBotPhoto {

    //TODO Need refactor this class

    private final UserRepository userRepository;

    public Map<Integer, List<PhotoDataDTO>> retrievePhotosForSearchBots(String sourceDirectory,
                                                                        byte ageFrom,
                                                                        byte ageTo,
                                                                        Gender gender,
                                                                        int maxResults,
                                                                        String userId) {
        log.info("Start reading the photos at: {}", System.nanoTime());

        displayMemoryUsage();

        Map<Integer, List<PhotoDataDTO>> photoMap = new HashMap<>();
        Set<Integer> selectedIndices = new HashSet<>();
        File packageDir = new File(sourceDirectory);

        if (!packageDir.exists() || !packageDir.isDirectory()) {
            log.error("Invalid source directory: {}", sourceDirectory);
            return photoMap;
        }

        File[] genderDirs = packageDir.listFiles();
        if (genderDirs == null) {
            log.error("No gender directories found in: {}", sourceDirectory);
            return photoMap;
        }

        Path filePath = Paths.get(sourceDirectory, gender.getValue().toLowerCase());
        File genderDir = new File(filePath.toString());

        List<File> selectedAgeDirs = new ArrayList<>();
        if (genderDir.isDirectory() && matchesGender(genderDir.getName(), gender)) {
            File[] ageDirs = genderDir.listFiles();
            if (ageDirs != null) {
                selectedAgeDirs = Arrays.stream(ageDirs)
                        .parallel()
                        .filter(ageDir -> ageDir.isDirectory() && matchesAge(ageDir.getName(), ageFrom, ageTo))
                        .toList();
            }
        }

        int randomAgeIndex = getRandomIndex(selectedAgeDirs.size());
        File randomAgeDir = selectedAgeDirs.get(randomAgeIndex);

        File[] numberDirs = randomAgeDir.listFiles(File::isDirectory);
        if (numberDirs != null) {
            List<File> selectedNumberDirs = getRandomPackages(Arrays.asList(numberDirs), maxResults);
            int key = 0;

            for (File numberDir : selectedNumberDirs) {
                final int currentKey = key;
                List<PhotoDataDTO> photoDataList = retrievePhotoBytesFromDirectory(numberDir);
                if (!photoDataList.isEmpty()) {
                    int photoIndex = getRandomIndex(photoDataList.size());
                    int attempts = 0;
                    while (selectedIndices.contains(photoIndex) && attempts < 3) {
                        photoIndex = getRandomIndex(photoDataList.size());
                        attempts++;
                    }

                    PhotoDataDTO firstPhoto = photoDataList.get(0);
                    if (false) {
                        log.info("Photo with userId {} and url {} already exists, calling again.", userId, firstPhoto.getUrl());
                        Map<Integer, List<PhotoDataDTO>> recursiveMap = retrievePhotosRecursive(sourceDirectory, ageFrom, ageTo, gender, maxResults - 1, userId, 1);
                        recursiveMap.forEach((k, v) -> photoMap.computeIfAbsent(currentKey + k, x -> new ArrayList<>()).addAll(v));
                    } else {
                        photoMap.computeIfAbsent(currentKey, k -> new ArrayList<>()).addAll(photoDataList);
                    }
                    key++;
                }
            }
        }
        displayMemoryUsage();

        log.info("End reading the photos at: {}", System.nanoTime());

        return photoMap;
    }

    private void displayMemoryUsage() {
        Runtime runtime = Runtime.getRuntime();

        // Get memory information in bytes
        long freeMemory = runtime.freeMemory();
        long totalMemory = runtime.totalMemory();
        long maxMemory = runtime.maxMemory();
        long usedMemory = totalMemory - freeMemory;

        // Convert bytes to megabytes for better readability
        long megabyte = 1024 * 1024;
        freeMemory /= megabyte;
        totalMemory /= megabyte;
        maxMemory /= megabyte;
        usedMemory /= megabyte;

        // Display memory information
        log.info("Free Memory: {} MB", freeMemory);
        log.info("Total Memory: {} MB", totalMemory);
        log.info("Max Memory: {} MB", maxMemory);
        log.info("Used Memory: {} MB", usedMemory);
        log.info("----------------------------------");
    }

    private Map<Integer, List<PhotoDataDTO>> retrievePhotosRecursive(String sourceDirectory,
                                                                     byte ageFrom,
                                                                     byte ageTo,
                                                                     Gender gender,
                                                                     int maxResults,
                                                                     String userId,
                                                                     int recursionCount) {
        if (recursionCount > 3) {
            return Collections.emptyMap(); // Stop recursion if it exceeds 3 times
        }

        return retrievePhotosForSearchBots(sourceDirectory, ageFrom, ageTo, gender, maxResults, userId);
    }

    private boolean findUsersToGenerate(String userId, PhotoDataDTO photoDataDTO) {
        return userRepository.existsByUserIdAndUrl(userId, photoDataDTO.getUrl());
    }

    private List<PhotoDataDTO> retrievePhotoBytesFromDirectory(File directory) {
        File[] photos = directory.listFiles(File::isFile);
        if (photos == null) {
            return Collections.emptyList();
        }

        byte[] getNumber = new byte[]{0, 1, 2, 3, 4, 5};

        int totalPhotos = photos.length;
        List<PhotoDataDTO> photoDataList = new ArrayList<>(totalPhotos);

        for (int i = 0; i < totalPhotos; i++) {
            File photo = photos[i];
            try {
                byte[] photoBytes = Files.readAllBytes(photo.toPath());
                byte randomNumber = getNumber[i % getNumber.length];
                String photoPath = photo.getPath();
                AgeRangeDTO ageRangeDTO = ExtractorAgeFromUlrUtil.extract(photoPath);

                PhotoDataDTO photoData = createPhotoData(photoBytes, randomNumber, photoPath, ageRangeDTO);
                photoDataList.add(photoData);
            } catch (IOException e) {
                // Handle or log the exception
            }
        }

        return photoDataList;
    }

//    private List<PhotoDataDTO> retrieveAndCompressPhotosFromDirectory(File directory) {
//        File[] photos = directory.listFiles(File::isFile);
//        if (photos == null) {
//            return Collections.emptyList();
//        }
//
//        byte[] getNumber = new byte[]{0, 1, 2, 3, 4, 5};
//
//        int totalPhotos = photos.length;
//        List<PhotoDataDTO> photoDataList = new ArrayList<>(totalPhotos);
//
//        for (int i = 0; i < totalPhotos; i++) {
//            File photo = photos[i];
//            try {
//                byte[] compressedPhotoBytes = compressPhoto(photo);
//                byte randomNumber = getNumber[i % getNumber.length];
//                String photoPath = photo.getPath();
//                AgeRangeDTO ageRangeDTO = ExtractorAgeFromUlrUtil.extract(photoPath);
//
//                PhotoDataDTO photoData = createPhotoData(compressedPhotoBytes, randomNumber, photoPath, ageRangeDTO);
//                photoDataList.add(photoData);
//            } catch (IOException e) {
//                // Handle or log the exception
//            }
//        }
//
//        return photoDataList;
//    }
//
//    private byte[] compressPhoto(File photo) throws IOException {
//        InputStream inputImage = new ByteArrayInputStream(Files.readAllBytes(photo.toPath()));
//
//        ByteArrayOutputStream compressedImageOutputStream = new ByteArrayOutputStream();
//        Thumbnails.of(inputImage)
//                .size(800, 800)
//                .outputQuality(0.8)
//                .toOutputStream(compressedImageOutputStream);
//
//        return compressedImageOutputStream.toByteArray();
//    }

    private int getRandomIndex(int size) {
        return ThreadLocalRandom.current().nextInt(size);
    }

    private List<File> getRandomPackages(List<File> fileList, int maxPackages) {
        int totalPackages = fileList.size();
        int actualMaxPackages = Math.min(maxPackages, totalPackages);

        List<Integer> indices = new ArrayList<>();
        for (int i = 0; i < totalPackages; i++) {
            indices.add(i);
        }

        List<File> randomElements = new ArrayList<>();

        for (int i = 0; i < actualMaxPackages; i++) {
            if (indices.isEmpty()) {
                break; // No more indices to pick from, exit the loop
            }

            int randomIndex = new Random().nextInt(indices.size());
            int selectedIndex = indices.get(randomIndex);
            randomElements.add(fileList.get(selectedIndex));
            indices.remove(randomIndex);
        }
        return randomElements;
    }

    private boolean matchesGender(String dirName, Gender gender) {
        return gender == Gender.FEMALE && dirName.equalsIgnoreCase("female")
                || gender == Gender.MALE && dirName.equalsIgnoreCase("male");
    }

    private boolean matchesAge(String dirName, byte ageFrom, byte ageTo) {
        String[] ageRange = dirName.split("_");
        if (ageRange.length == 2) {
            byte from = Byte.parseByte(ageRange[0]);
            byte to = Byte.parseByte(ageRange[1]);
            return from <= ageTo && to >= ageFrom;
        }
        return false;
    }

    private PhotoDataDTO createPhotoData(byte[] photoBytes,
                                         byte randomNumber,
                                         String photoPath,
                                         AgeRangeDTO ageRangeDTO) {
        return new PhotoDataDTO(photoBytes, randomNumber, photoPath, ageRangeDTO);
    }
}
