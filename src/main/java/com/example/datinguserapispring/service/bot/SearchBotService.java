package com.example.datinguserapispring.service.bot;

import com.example.datinguserapispring.dto.search.request.SearchByCriteria;
import com.example.datinguserapispring.dto.search.response.ProfileSnapshotSearchResponseBot;

import java.util.List;

public interface SearchBotService {
    List<ProfileSnapshotSearchResponseBot> searchBots(SearchByCriteria criteria, String userId);
}
