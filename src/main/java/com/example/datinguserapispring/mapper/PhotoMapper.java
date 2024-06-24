package com.example.datinguserapispring.mapper;


import com.example.datinguserapispring.dto.photo.request.PhotoDataRequestDTO;
import com.example.datinguserapispring.dto.photo.response.PhotoDTO;
import com.example.datinguserapispring.dto.photo.response.PhotoListResponse;
import com.example.datinguserapispring.dto.photo.response.WaitingApprovalPhotoDTO;
import com.example.datinguserapispring.model.entity.Photo;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;

import java.util.List;
import java.util.stream.Collectors;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface PhotoMapper {

    default PhotoListResponse mapToPhotoListResponseFromPhotos(List<Photo> photos) {
        List<String> urls = photos.stream()
                .map(Photo::getUrl)
                .collect(Collectors.toList());
        PhotoListResponse response = new PhotoListResponse();
        response.setUrl(urls);
        return response;
    }
    @Mapping(source = "createdAt", target = "createdAt", dateFormat = "yyyy-MM-dd HH:mm:ss")
    PhotoDTO mapToDTO(Photo photo);

    WaitingApprovalPhotoDTO mapToWaitingApprovalPhotoDTO(Photo photo);

    Photo mapToEntity(PhotoDataRequestDTO photoDataDTO);

}
