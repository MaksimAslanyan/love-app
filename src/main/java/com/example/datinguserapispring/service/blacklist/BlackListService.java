package com.example.datinguserapispring.service.blacklist;

import com.example.datinguserapispring.dto.blacklist.request.BlackListedRequest;
import com.example.datinguserapispring.dto.blacklist.response.BlackListUsers;
import com.example.datinguserapispring.dto.blacklist.response.BlackListedResponse;
import com.example.datinguserapispring.dto.fetch.request.FetchRequest;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface BlackListService {

    BlackListedResponse setBlackListed(BlackListedRequest request, String appUserDetails);

    BlackListedResponse removeUserFromBlackList(String userId);

    List<BlackListUsers> getUsers(Pageable pageable);

    List<BlackListUsers> findBlackListUsers(FetchRequest fetchRequest, int page, int size);
}
