package com.example.datinguserapispring.api.controller.admin;


import com.example.datinguserapispring.dto.blacklist.request.BlackListedRequest;
import com.example.datinguserapispring.dto.blacklist.response.BlackListUsers;
import com.example.datinguserapispring.dto.blacklist.response.BlackListedResponse;
import com.example.datinguserapispring.dto.fetch.request.FetchRequest;
import com.example.datinguserapispring.security.SecurityContextService;
import com.example.datinguserapispring.service.blacklist.BlackListService;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequiredArgsConstructor
@RequestMapping("/admin/black-list/users")
public class BlackListController {

    private final BlackListService blackListService;
    private final SecurityContextService securityContextService;

    @PreAuthorize("hasAuthority('OWNER') OR hasAuthority('ADMIN')")
    @PostMapping
    public BlackListedResponse addUserToBlackList(@RequestBody BlackListedRequest request) {
        String appUserDetails = securityContextService.getUserDetails().getUsername();
        return blackListService.setBlackListed(request, appUserDetails);
    }

    @GetMapping
    public List<BlackListUsers> getUsers(Pageable pageable) {
        return blackListService.getUsers(pageable);
    }

    @PostMapping("/search")
    public List<BlackListUsers> findBlackListUsers(@RequestBody FetchRequest fetchRequest,
                                                   @RequestParam(defaultValue = "0") int page,
                                                   @RequestParam(defaultValue = "10") int size) {
        return blackListService.findBlackListUsers(fetchRequest, page, size);
    }

    @DeleteMapping("/{userId}")
    public BlackListedResponse removeUserFromBlackList(@PathVariable String userId) {
        return blackListService.removeUserFromBlackList(userId);
    }
}
