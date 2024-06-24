package com.example.datinguserapispring.service.file;

import com.example.datinguserapispring.dto.photo.response.PhotoDataDTO;
import com.example.datinguserapispring.model.entity.user.Address;
import com.example.datinguserapispring.model.entity.user.User;
import com.example.datinguserapispring.model.enums.Gender;

import java.util.List;
import java.util.Map;

public interface FileDataReaderService {

    List<String> getNameFromFile(User user, Address address);

    Map<Integer, List<PhotoDataDTO>> getPhotoFromFile(byte ageFrom,
                                                      byte ageTo,
                                                      Gender gender,
                                                      int maxResult);

    Map<Integer, List<PhotoDataDTO>> getPhotoFromPhotosForSearchBots(byte ageFrom,
                                                                     byte ageTo,
                                                                     Gender gender,
                                                                     int maxResult, String userId);
}
