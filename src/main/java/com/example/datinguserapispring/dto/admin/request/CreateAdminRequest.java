package com.example.datinguserapispring.dto.admin.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Builder(toBuilder = true)
@Data
public class CreateAdminRequest {
    private String login;
    private String password;
    private String name;
    private String tg;
    private int authority;
    private byte age;
}
