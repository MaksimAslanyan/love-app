package com.example.datinguserapispring.dto.apple;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Builder(toBuilder = true)
@Data
public class AppleIDTokenPayload {
    private String iss;
    private String aud;
    private long exp;
    private long iat;
    private String sub;
    private String at_hash;
    private long auth_time;
    private Boolean nonce_supported;
    private Boolean email_verified;
    private String email;
    private NameData name;

    @Override
    public String toString() {
        return "AppleIDTokenPayload(iss='" + iss + "', aud='" + aud + "', " +
                "exp=" + exp + ", iat=" + iat + ", sub='" + sub + "', " +
                "at_hash='" + at_hash + "', auth_time=" + auth_time + "," +
                " nonce_supported=" + nonce_supported + ", " +
                "email_verified=" + email_verified + ", email='" + email + "', name=" + name + ")";
    }
}
