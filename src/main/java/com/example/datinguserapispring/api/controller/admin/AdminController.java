package com.example.datinguserapispring.api.controller.admin;


import com.example.datinguserapispring.constants.UserType;
import com.example.datinguserapispring.dto.admin.request.CreateAdminRequest;
import com.example.datinguserapispring.dto.admin.request.EditAdminRequest;
import com.example.datinguserapispring.dto.admin.request.LoginAdminRequest;
import com.example.datinguserapispring.dto.admin.response.*;
import com.example.datinguserapispring.dto.photo.response.DeletePhotoResponse;
import com.example.datinguserapispring.dto.photo.response.UploadPhotoResponse;
import com.example.datinguserapispring.service.admin.AdminService;
import com.example.datinguserapispring.service.photo.PhotoService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PostAuthorize;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/admin")
public class AdminController {

    private final AdminService adminService;
    private final PhotoService photoService;


    @PreAuthorize("hasAuthority('OWNER')")
    @PostMapping("/create")
    public CreateAdminResponse createAdmin(@RequestBody CreateAdminRequest request) {
        return adminService.createAdmin(request);
    }

    @PostMapping("/login")
    public LoginAdminResponse loginAdmin(@RequestBody LoginAdminRequest request) {
        return adminService.loginAdmin(request);
    }

    @PostMapping("/logout")
    public ResponseEntity<?> logout(@RequestHeader(value = "Authorization") String token) {
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @DeleteMapping("/{adminId}")
    public DeleteAdminResponse deleteAdmin(@PathVariable("adminId") String adminId) {
        return adminService.deleteAdmin(adminId);
    }

    @GetMapping("/{adminId}")
    public ListAdminItemResponse getAdminById(@PathVariable("adminId") String adminId) {
        return adminService.getById(adminId);
    }

    @PostMapping("/list")
    @PostAuthorize("hasAuthority('OWNER') OR hasAuthority('ADMIN') OR hasAuthority('MODERATOR')")
    public List<ListAdminItemResponse> listAdmin() {
        return adminService.adminList();
    }

    @PatchMapping("/{id}")
    @PreAuthorize("hasAuthority('OWNER')")
    public EditAdminResponse editAdmin(@PathVariable("id") String adminId,
                                       @RequestBody EditAdminRequest request) {
        return adminService.editAdmin(adminId, request);
    }

    @PostMapping(value = "/upload/photo")
    @PreAuthorize("hasAuthority('OWNER')")
    public UploadPhotoResponse uploadPhoto(@RequestParam MultipartFile photo,
                                           @RequestParam String adminId,
                                           @RequestParam byte photoIndex) {
        return photoService.uploadAdminPhoto(adminId, UserType.USER, photo, photoIndex);
    }

    @DeleteMapping("/{adminId}/image/{photoId}")
    @PreAuthorize("hasAuthority('OWNER')")
    public DeletePhotoResponse deletePhoto(@PathVariable String adminId,
                                           @PathVariable String photoId) {
        return adminService.deletePhoto(photoId, adminId);
    }
}
