package com.example.datinguserapispring.dto.apple;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Builder(toBuilder = true)
@Data
public class TokenResponse {
    @JsonProperty("access_token")
    private String access_token;
    @JsonProperty("token_type")
    private String token_type;
    @JsonProperty("expires_in")
    private long expires_in;
    @JsonProperty("refresh_token")
    private String refresh_token;
    @JsonProperty("id_token")
    private String id_token;


    @Override
    public String toString() {
        return "TokenResponse(access_token='" + access_token + "', token_type='" + token_type + "', " +
                "expires_in=" + expires_in + ", refresh_token='" + refresh_token + "', " +
                "id_token='" + id_token + "')";
    }
}
