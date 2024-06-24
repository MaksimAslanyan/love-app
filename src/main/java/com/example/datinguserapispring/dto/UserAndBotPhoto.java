package com.example.datinguserapispring.dto;

import com.example.datinguserapispring.dto.photo.response.PhotoDTO;

import java.util.List;
import java.util.Map;

public record UserAndBotPhoto(Map<String, List<PhotoDTO>> userPhotos, Map<String, List<PhotoDTO>> bot2Photos) { }
