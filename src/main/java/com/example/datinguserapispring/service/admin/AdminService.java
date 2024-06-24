package com.example.datinguserapispring.service.admin;

import com.example.datinguserapispring.dto.admin.request.CreateAdminRequest;
import com.example.datinguserapispring.dto.admin.request.EditAdminRequest;
import com.example.datinguserapispring.dto.admin.request.LoginAdminRequest;
import com.example.datinguserapispring.dto.admin.response.*;
import com.example.datinguserapispring.dto.photo.response.DeletePhotoResponse;

import java.util.List;

public interface AdminService {
    CreateAdminResponse createAdmin(CreateAdminRequest request);

    LoginAdminResponse loginAdmin(LoginAdminRequest request);

    DeleteAdminResponse deleteAdmin(String adminId);

    List<ListAdminItemResponse> adminList();

    DeletePhotoResponse deletePhoto(String photoId, String adminId);

    EditAdminResponse editAdmin(String adminId, EditAdminRequest request);

    ListAdminItemResponse getById(String adminId);
}
