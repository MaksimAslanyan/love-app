package com.example.datinguserapispring.manager;


import com.example.datinguserapispring.constants.AdminRoles;
import com.example.datinguserapispring.model.entity.Admin;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.apache.commons.codec.Charsets;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.crypto.SecretKey;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Component
public class JwtTokenManager {

    @Value("${service.jjwt.secret}")
    private String secret;

    @Value("${service.jjwt.expiration}")
    private String expirationTime;

    private SecretKey key;

    @PostConstruct
    public void init() {
        key = Keys.hmacShaKeyFor(secret.getBytes(Charsets.UTF_8));
    }

    public String generateAdminToken(Admin admin) {
        Map<String, Object> claims = new HashMap<>();
        claims.put("role", AdminRoles.getAdminRole(admin.getRole()));
        return doGenerateToken(claims, admin.getLogin());
    }

    public String generateTokenForApple(String username) {
        Map<String, Object> tokenClaims = new HashMap<>();
        tokenClaims.put("role", "USER");
        return doGenerateToken(tokenClaims, username);
    }

    public Claims getAllClaimsFromToken(String token) {
            return Jwts.parserBuilder()
                    .setSigningKey(key)
                    .build()
                    .parseClaimsJws(token)
                    .getBody();

    }

    private String doGenerateToken(Map<String, Object> claims, String username) {
        long expirationTimeLong = Long.parseLong(expirationTime);
        Date createdDate = new Date();
        Date expirationDate = new Date(createdDate.getTime() + expirationTimeLong * 1 * 24 * 60 * 60 * 1000);
        return Jwts.builder()
                .setClaims(claims)
                .setSubject(username)
                .setIssuedAt(createdDate)
                .setExpiration(expirationDate)
                .signWith(key)
                .compact();
    }
}
