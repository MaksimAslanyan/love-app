package com.example.datinguserapispring.util;


import com.example.datinguserapispring.dto.bot.AgeRangeDTO;
import com.example.datinguserapispring.dto.photo.response.PhotoDataDTO;
import com.example.datinguserapispring.model.enums.Gender;
import lombok.experimental.UtilityClass;
import lombok.extern.log4j.Log4j2;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;
import java.util.concurrent.ThreadLocalRandom;

@UtilityClass
@Log4j2
public class PhotoReaderUtil {


    public Map<Integer, List<PhotoDataDTO>> retrievePhotos(String sourceDirectory,
                                                           byte ageFrom,
                                                           byte ageTo,
                                                           Gender gender,
                                                           int maxResults) {
        log.info("Start reading the photos at: {} {} {} {} {}", sourceDirectory, ageFrom,ageTo,gender.getValue(),maxResults);

        Map<Integer, List<PhotoDataDTO>> photoMap = new HashMap<>();
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
                List<PhotoDataDTO> photoDataList = retrievePhotoBytesFromDirectory(numberDir);
                if (!photoDataList.isEmpty()) {
                    photoMap.put(key, photoDataList);
                    key++;
                }
            }
        }

        log.info("End reading the photos at: {}", System.nanoTime());

        return photoMap;
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
