package com.example.datinguserapispring.dto.admin.request;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Builder(toBuilder = true)
@Data
public class EditAdminRequest {
    private String name;
    private String nickName;
    private Integer role;
}
