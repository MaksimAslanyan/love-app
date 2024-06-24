package com.example.datinguserapispring.repository;

import com.example.datinguserapispring.dto.photo.response.PhotoDTO;
import com.example.datinguserapispring.model.entity.Photo;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

public interface PhotoRepository extends JpaRepository<Photo, String>, JpaSpecificationExecutor<Photo> {

    List<Photo> findAllByUserId(String userId);

    Page<Photo> findAll(Pageable pageable);

    List<Photo> findAllByBot2Id(String bot2Id);

    List<Photo> findAllByAdminId(String adminId);

    Page<Photo> findByUrlContaining(String url, Pageable pageable);

    List<Photo> findPhotoByBot2Id(String bot2Id);

    List<Photo> findAllByUserName(String userName);

    Optional<Photo> findById(String id);

    @Transactional
    @Modifying
    @Query(value = "DELETE FROM dating_user_api.photo WHERE user_info_id = :userId", nativeQuery = true)
    void deleteAllByUserId(String userId);

    @Transactional
    @Modifying
    @Query(value = "DELETE FROM dating_user_api.photo WHERE bot2_id = :bot2Id", nativeQuery = true)
    void deleteAllByBot2Id(String bot2Id);

    int countAllByUserId(String id);

    void deletePhotoByIdAndAdminId(String photoId, String adminId);

    void deleteAllByUrlContaining(String url);

    Page<Photo> findAllByApproveIsFalseAndParentTypeEquals(String parentType, Pageable pageable);

    boolean existsByUserIdAndUrl(String userId, String url);

    @Modifying
    @Transactional
    @Query(value = "DELETE FROM dating_user_api.photo WHERE id = :photoId", nativeQuery = true)
    void deleteByIdNativeQuery(String photoId);

    @Modifying
    @Transactional
    @Query(value = "DELETE FROM dating_user_api.photo p WHERE p.id IN :photoIds", nativeQuery = true)
    void deletePhotosByIds(List<String> photoIds);

    @Query(value = "SELECT p.bot2_id AS bot2Id, STRING_AGG(CONCAT(p.id, ':', p.is_avatar, ':', p.url, ':', p.created_at, ':', p.photo_state), ',') AS photos " +
            "FROM dating_user_api.photo p " +
            "WHERE p.bot2_id IN :bot2Ids " +
            "GROUP BY p.bot2_id", nativeQuery = true)
    List<Object[]> findPhotoByBot2IdIn(@Param("bot2Ids") Set<String> bot2Ids);

    @Query(value = "SELECT p.user_info_id AS userId, STRING_AGG(CONCAT(p.id, ':', p.is_avatar, ':', p.url, ':', p.created_at, ':', p.photo_state), ',') AS photos " +
            "FROM dating_user_api.photo p " +
            "WHERE p.user_info_id IN :userIds " +
            "GROUP BY p.user_info_id ", nativeQuery = true)
    List<Object[]> findPhotoByUserIdIn(@Param("userIds") Set<String> userIds);

    default Map<String, List<PhotoDTO>> mapToPhotoDTO(List<Object[]> result) {
        Map<String, List<PhotoDTO>> photoMap = new HashMap<>();

        for (Object[] row : result) {
            String bot2Id = (String) row[0];
            String photosConcatenated = (String) row[1];

            List<PhotoDTO> photoList = Arrays.stream(photosConcatenated.split(","))
                    .map(photoString -> {
                        String[] parts = photoString.split(":");
                        PhotoDTO photoDTO = new PhotoDTO();
                        photoDTO.setId(parts[0]);
                        photoDTO.setIsAvatar(Byte.parseByte(parts[1]));
                        photoDTO.setUrl(parts[2]);
                        photoDTO.setCreatedAt(parts[3]);
                        return photoDTO;
                    })
                    .collect(Collectors.toList());

            photoMap.put(bot2Id, photoList);
        }

        return photoMap;
    }

    default Map<String, List<PhotoDTO>> mapToUserPhotoDTO(List<Object[]> result) {
        Map<String, List<PhotoDTO>> photoMap = new HashMap<>();

        for (Object[] row : result) {
            String userId = (String) row[0];
            String photosConcatenated = (String) row[1];

            List<PhotoDTO> photoList = Arrays.stream(photosConcatenated.split(","))
                    .map(photoString -> {
                        String[] parts = photoString.split(":");
                        PhotoDTO photoDTO = new PhotoDTO();
                        photoDTO.setId(parts[0]);
                        photoDTO.setIsAvatar(Byte.parseByte(parts[1]));
                        photoDTO.setUrl(parts[2]);
                        photoDTO.setCreatedAt(parts[3]);
                        return photoDTO;
                    })
                    .collect(Collectors.toList());

            photoMap.put(userId, photoList);
        }

        return photoMap;
    }
}
