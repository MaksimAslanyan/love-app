package com.example.datinguserapispring.util;

import lombok.experimental.UtilityClass;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

@UtilityClass
public class GetFilePathUtil {

    public static String getFilePathUrlString(MultipartFile filePart,
                                              Path filePath) throws IOException {
        byte[] bytes = filePart.getBytes();
        File file = filePath.toFile();
        Files.write(file.toPath(), bytes);
        return filePath.toString();

    }
}
