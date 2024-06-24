package com.example.datinguserapispring.api.controller.admin;


import com.example.datinguserapispring.dto.user.response.ProfileSnapshotForAdminResponse;
import com.example.datinguserapispring.dto.user.response.UsersCountResponse;
import com.example.datinguserapispring.dto.fetch.request.FetchRequest;
import com.example.datinguserapispring.service.user.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/admin/users")
public class UserController {

    private final UserService userService;


    //    @PreAuthorize("hasAuthority('OWNER') OR hasAuthority('ADMIN') OR hasAuthority('MODERATOR')")
    @PostMapping
    public List<ProfileSnapshotForAdminResponse> getUsersByCriteria(
            @RequestBody FetchRequest fetchRequest,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        return userService.getAllUsers(fetchRequest, page, size);
    }


    @GetMapping("/count")
//    @PreAuthorize("hasAuthority('OWNER') OR hasAuthority('ADMIN') OR hasAuthority('MODERATOR')")
    public UsersCountResponse getUsersCount() {
        return userService.getUsersCount();
    }
}
