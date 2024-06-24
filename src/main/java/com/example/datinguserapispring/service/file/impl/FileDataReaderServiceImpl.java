package com.example.datinguserapispring.service.file.impl;

import com.example.datinguserapispring.dto.photo.response.PhotoDataDTO;
import com.example.datinguserapispring.model.entity.user.Address;
import com.example.datinguserapispring.model.entity.user.User;
import com.example.datinguserapispring.model.enums.Gender;
import com.example.datinguserapispring.service.bot.impl.GenerateBotPhoto;
import com.example.datinguserapispring.service.file.FileDataReaderService;
import com.example.datinguserapispring.util.NameReaderUtil;
import com.example.datinguserapispring.util.PhotoReaderUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

import static com.example.datinguserapispring.service.bot.impl.SearchBotServiceImpl.NAMES_TXT;

@Service
@RequiredArgsConstructor
public class FileDataReaderServiceImpl implements FileDataReaderService {

    private final GenerateBotPhoto generateBotPhoto;

    @Value("${botsName.path}")
    private String botsNameFile;

    @Value("${bot.path}")
    private String photoPath;

    @Override
    public List<String> getNameFromFile(User user, Address address) {
        return NameReaderUtil.readFile(
                botsNameFile,
                address.getCountry().toLowerCase(),
                user.getTargetGender().toLowerCase(),
                NAMES_TXT);
    }

    @Override
    public Map<Integer, List<PhotoDataDTO>> getPhotoFromFile(byte ageFrom,
                                                             byte ageTo,
                                                             Gender gender,
                                                             int maxResult) {

        return PhotoReaderUtil.retrievePhotos(photoPath, ageFrom,
                ageTo, gender, maxResult);
    }

    @Override
    public Map<Integer, List<PhotoDataDTO>> getPhotoFromPhotosForSearchBots(byte ageFrom,
                                                                            byte ageTo,
                                                                            Gender gender,
                                                                            int maxResult, String userId) {

        return generateBotPhoto.retrievePhotosForSearchBots(photoPath, ageFrom,
                ageTo, gender, maxResult, userId);
    }
}
