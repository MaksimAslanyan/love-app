package com.example.datinguserapispring.dto.apple;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Builder(toBuilder = true)
@Data
public class SocialParametersDTO {
    private String authorizationCode;
    private String userObj;
    private String idToken;
    private String identifierFromApp;


    @Override
    public String toString() {
        return "SocialParametersDTO(authorizationCode='" + authorizationCode + "', userObj='" + userObj + "', " +
                "idToken='" + idToken + "', identifierFromApp='" + identifierFromApp + "')";
    }
}
