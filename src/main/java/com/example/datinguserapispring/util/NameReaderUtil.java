package com.example.datinguserapispring.util;

import lombok.experimental.UtilityClass;
import lombok.extern.log4j.Log4j2;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

@UtilityClass
@Log4j2
public class NameReaderUtil {


    public static List<String> readFile(String directoryPath, String subdirectory, String gender, String fileName) {
        List<String> txtDocument = new ArrayList<>();

        if (gender == null || gender.isEmpty()){
            gender = getRandomGender();
        }
        Path filePath = Paths.get(directoryPath, subdirectory, gender, fileName);

        log.info("Reading file: {}", filePath);

        try (BufferedReader reader = Files.newBufferedReader(filePath, StandardCharsets.UTF_8)) {
            String line;
            while ((line = reader.readLine()) != null) {
                String trimmedLine = removeWhitespace(line);
                txtDocument.add(trimmedLine);
            }
            log.info("Read {} lines from file: {}", txtDocument.size(), filePath);

        } catch (FileNotFoundException e) {
            log.error("File not found: {}", filePath, e);
        } catch (IOException e) {
            log.error("Error reading file: {}", filePath, e);
        }

        return txtDocument;
    }

    private String getRandomGender() {
        Random random = new Random();
        return random.nextBoolean() ? "male" : "female";
    }

    private static String removeWhitespace(String line) {
        char[] chars = line.toCharArray();
        int length = chars.length;
        int newLength = 0;
        for (int i = 0; i < length; i++) {
            if (!Character.isWhitespace(chars[i])) {
                chars[newLength++] = chars[i];
            }
        }
        return new String(chars, 0, newLength);
    }

}
