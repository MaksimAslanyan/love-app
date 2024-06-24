package com.example.datinguserapispring.dto.auth.response;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Builder(toBuilder = true)
@Data
public class AuthResponse {
    @JsonProperty("token")
    private String token;
    @JsonProperty("existUser")
    private boolean existUser;
}
