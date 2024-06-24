package com.example.datinguserapispring.api.controller.admin;


import com.example.datinguserapispring.dto.ban.request.BanInfoRequest;
import com.example.datinguserapispring.dto.ban.response.CreateBanResponse;
import com.example.datinguserapispring.dto.ban.response.DeleteBanResponse;
import com.example.datinguserapispring.dto.ban.response.GetBanInfoResponse;
import com.example.datinguserapispring.service.baninfo.BanInfoService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/admin/ban/user")
public class BanInfoController {

    private final BanInfoService banInfoService;


    @PostMapping("/{id}")
    public CreateBanResponse createBan(@PathVariable("id") String id, @RequestBody BanInfoRequest request) {
        return banInfoService.banUser(id, request);
    }

    @GetMapping("/{id}")
    public GetBanInfoResponse getBanInfo(@PathVariable("id") String id) {
        return banInfoService.getByUserId(id);
    }

    @DeleteMapping("/{id}")
    public DeleteBanResponse deleteBanInfo(@PathVariable("id") String id) {
        return banInfoService.deleteBan(id);
    }
}
