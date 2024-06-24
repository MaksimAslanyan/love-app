package com.example.datinguserapispring.service.search;

import com.example.datinguserapispring.dto.user.response.ProfileSnapshotSearchResponse;
import com.example.datinguserapispring.dto.search.request.SearchByCriteria;

import java.util.List;

public interface SearchService {
    List<ProfileSnapshotSearchResponse> search(SearchByCriteria search);
}
