package com.example.datinguserapispring.service.photo;

import com.example.datinguserapispring.dto.photo.response.DeletePhotoResponse;

public interface DeletePhotoService {

    DeletePhotoResponse delete(String photoId);
}
