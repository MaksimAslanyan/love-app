package com.example.datinguserapispring.service.photo.impl;


import com.example.datinguserapispring.constants.UserType;
import com.example.datinguserapispring.dto.photo.response.UploadPhotoResponse;
import com.example.datinguserapispring.model.entity.Photo;
import com.example.datinguserapispring.model.entity.bot.Bot2;
import com.example.datinguserapispring.repository.PhotoRepository;
import com.example.datinguserapispring.service.photo.AsyncUpdateBotPhotoService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@Slf4j
@Async
@RequiredArgsConstructor
public class AsyncUpdateBotPhotoServiceImpl implements AsyncUpdateBotPhotoService {


    private final PhotoRepository photoRepository;

    @Override
    public void updatePhotos(Bot2 bot2, String url) {
        int pageNumber = 0;
        int pageSize = 100;
        String botPackagePath = bot2.getPackagePath();
        int lastIndexOfSlash = botPackagePath.lastIndexOf("/");

        String lastSegment = botPackagePath.substring(lastIndexOfSlash + 1);

        Pageable pageable = PageRequest.of(pageNumber, pageSize);
        Page<Photo> photoPage = photoRepository.findByUrlContaining(botPackagePath, pageable);
        Map<String, List<Photo>> botPhotosSave = new HashMap<>();
        while (!photoPage.isEmpty()) {
            List<Photo> photos = photoPage.getContent();

            for (Photo photo : photos) {
                int lastIndexOfSecondSlash = photo.getUrl().lastIndexOf("/", photo.getUrl().lastIndexOf("/") - 1);
                int lastIndexOfLastSlash = photo.getUrl().lastIndexOf("/");
                String packageNumber = photo.getUrl().substring(lastIndexOfSecondSlash + 1, lastIndexOfLastSlash);
                // Check if the packageNumber already exists in the map
                if (botPhotosSave.containsKey(packageNumber)) {
                    botPhotosSave.get(packageNumber).add(photo);
                } else {
                    List<Photo> newPhotos = new ArrayList<>();
                    newPhotos.add(photo);
                    botPhotosSave.put(packageNumber, newPhotos);
                }

            }
            pageable = PageRequest.of(++pageNumber, pageSize);
            photoPage = photoRepository.findByUrlContaining(botPackagePath, pageable);
        }

        List<Photo> photos = botPhotosSave.get(lastSegment);
        for (Photo photo : photos) {
            int photoSize = photoRepository.findPhotoByBot2Id(photo.getBot2().getId()).size();
            Photo newPhoto;
            if (photoSize == 0) {
                byte photoIndex = 0;
                newPhoto = new Photo(UserType.BOT, photoIndex, url, LocalDateTime.now(), photo.getBot2());
            } else {
                int index = photoSize + 1;
                newPhoto = new Photo(UserType.BOT, (byte) index, url, LocalDateTime.now(), photo.getBot2());
            }
            builtPhotoResponse(newPhoto);
        }
    }


    private UploadPhotoResponse builtPhotoResponse(Photo photo) {
        Photo savedPhoto = photoRepository.save(photo);

        String filePath = photo.getUrl();
        UriComponentsBuilder builder = UriComponentsBuilder.newInstance()
                .scheme("file") // Use the "file" scheme for local file paths
                .path(filePath);

        URI uri = builder.build().toUri();
        String photoId = savedPhoto.getId();


        return new UploadPhotoResponse(uri, photoId);
    }
}
