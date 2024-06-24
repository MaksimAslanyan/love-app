package com.example.datinguserapispring.service.photo.impl;

import com.example.datinguserapispring.dto.photo.response.DeletePhotoResponse;
import com.example.datinguserapispring.exception.Error;
import com.example.datinguserapispring.exception.UserNotFoundException;
import com.example.datinguserapispring.model.entity.Photo;
import com.example.datinguserapispring.model.entity.user.User;
import com.example.datinguserapispring.repository.PhotoRepository;
import com.example.datinguserapispring.repository.UserRepository;
import com.example.datinguserapispring.security.SecurityContextService;
import com.example.datinguserapispring.service.photo.DeletePhotoService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Iterator;
import java.util.List;


@Service
@Slf4j
@Transactional
@RequiredArgsConstructor
public class DeletePhotoServiceImpl implements DeletePhotoService {


    private final SecurityContextService securityContextService;
    private final UserRepository userRepository;
    private final PhotoRepository photoRepository;


    @Override
    public DeletePhotoResponse delete(String photoId) {
        String id = securityContextService.getUserDetails().getUsername();
        User user = userRepository.findById(id)
                .orElseThrow(() -> new UserNotFoundException(Error.USER_NOT_FOUND));

        List<Photo> allByAdminId = photoRepository.findAllByUserId(user.getId());
        boolean isDeleted = deletePhotoById(photoId, allByAdminId);
        updatePhotoAvatarOrder(allByAdminId);

        return new DeletePhotoResponse(photoId, isDeleted);
    }

    private boolean deletePhotoById(String photoId, List<Photo> allByAdminId) {
        Iterator<Photo> iterator = allByAdminId.iterator();
        while (iterator.hasNext()) {
            Photo photo = iterator.next();

            if (photo.getId().equals(photoId) && (allByAdminId.size() != 1)) {
                    iterator.remove(); // Remove the photo from the list
                    photoRepository.deleteById(photoId);
                    return true;
            }
        }
        return false;
    }



    private void updatePhotoAvatarOrder(List<Photo> allByAdminId) {
        for (int i = 0; i < allByAdminId.size(); i++) {
            Photo photo = allByAdminId.get(i);
            photo.setIsAvatar((byte) i);
        }
        photoRepository.saveAll(allByAdminId);
    }

}
