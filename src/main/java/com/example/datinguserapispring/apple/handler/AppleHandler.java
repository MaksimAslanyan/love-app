package com.example.datinguserapispring.apple.handler;


import com.example.datinguserapispring.dto.apple.AppleIDTokenPayload;
import com.example.datinguserapispring.dto.apple.TokenResponse;
import com.example.datinguserapispring.exception.Error;
import com.example.datinguserapispring.exception.RequestFailedException;
import com.google.gson.Gson;
import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.Unirest;
import com.mashape.unirest.http.exceptions.UnirestException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.bouncycastle.openssl.PEMException;
import org.bouncycastle.openssl.PEMParser;
import org.bouncycastle.openssl.jcajce.JcaPEMKeyConverter;
import org.bouncycastle.openssl.jcajce.JceOpenSSLPKCS8DecryptorProviderBuilder;
import org.bouncycastle.operator.OperatorCreationException;
import org.bouncycastle.pkcs.PKCS8EncryptedPrivateKeyInfo;
import org.bouncycastle.pkcs.PKCSException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.io.StringReader;
import java.security.PrivateKey;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Date;

@Component
public class AppleHandler {
    @Value("${apple.authUrl}")
    private String appleAuthUrl;
    @Value("${apple.appleKeyId}")
    private String appleKeyId;
    @Value("${apple.appleTeamId}")
    private String appleTeamId;
    @Value("${apple.appleClientId}")
    private String appleClientId;

    private PrivateKey generatePrivateKey() {
        String privateKeyPEM = "-----BEGIN PRIVATE KEY-----\n" +
                "MIGTAgEAMBMGByqGSM49AgEGCCqGSM49AwEHBHkwdwIBAQQg/WWaZEBsmsLF+yq/\n" +
                "9lKqqRFUCZjX1xbGg7tCDFs48R6gCgYIKoZIzj0DAQehRANCAASkZhcQVXS9GIYK\n" +
                "6JURaC7YNdR2laJBQjIoIL1c5rd8/ltv+Lg/CcLBQ+Wh2oFQ6vxgJM/Ek8gPYAUM\n" +
                "RrZeIqmc\n" +
                "-----END PRIVATE KEY-----";

        PEMParser pemParser = new PEMParser(new StringReader(privateKeyPEM));
        Object pemObject;
        try {
            pemObject = pemParser.readObject();
            pemParser.close();
        } catch (IOException e) {
            throw new com.example.datinguserapispring.exception.IOException(Error.FAILED_KEY_READ);
        }

        JcaPEMKeyConverter converter = new JcaPEMKeyConverter();
        if (pemObject instanceof PKCS8EncryptedPrivateKeyInfo) {
            PKCS8EncryptedPrivateKeyInfo encryptedPrivateKeyInfo = (PKCS8EncryptedPrivateKeyInfo) pemObject;
            try {
                char[] password = "password".toCharArray();
                JceOpenSSLPKCS8DecryptorProviderBuilder decryptorProviderBuilder = new JceOpenSSLPKCS8DecryptorProviderBuilder();
                decryptorProviderBuilder.setProvider("BC");
                return converter.getPrivateKey(encryptedPrivateKeyInfo
                        .decryptPrivateKeyInfo(decryptorProviderBuilder.build(password)));
            } catch (OperatorCreationException e) {
                throw new com.example.datinguserapispring.exception.
                        IOException(Error.FAILED_KEY_READ);
            } catch (PEMException e) {
                throw new com.example.datinguserapispring.exception.
                        IOException(Error.FAILED_KEY_READ);
            } catch (PKCSException e) {
                throw new com.example.datinguserapispring.exception.
                        IOException(Error.FAILED_KEY_READ);
            }
        } else if (pemObject instanceof org.bouncycastle.asn1.pkcs.PrivateKeyInfo) {
            org.bouncycastle.asn1.pkcs.PrivateKeyInfo privateKeyInfo = (org.bouncycastle.asn1.pkcs.PrivateKeyInfo) pemObject;
            try {
                return converter.getPrivateKey(privateKeyInfo);
            } catch (Exception e) {
                throw new com.example.datinguserapispring.exception
                        .IOException(Error.FAILED_KEY_READ);
            }
        } else {
            throw new com.example.datinguserapispring.exception
                    .IOException(Error.FAILED_KEY_READ);
        }
    }

    private String generateJWT() {
        PrivateKey privateKey = generatePrivateKey();

        return Jwts.builder()
                .setHeaderParam("kid", appleKeyId)
                .setIssuer(appleTeamId)
                .setAudience("https://appleid.apple.com")
                .setSubject(appleClientId)
                .setExpiration(Date.from(Instant.now().plus(5, ChronoUnit.DAYS)))
                .setIssuedAt(Date.from(Instant.now()))
                .signWith(privateKey, SignatureAlgorithm.ES256)
                .compact();
    }

    public AppleIDTokenPayload appleAuth(String authorizationCode) {
        try {
            HttpResponse<String> response = Unirest.post(appleAuthUrl)
                    .header("Content-Type", "application/x-www-form-urlencoded")
                    .field("client_id", appleClientId)
                    .field("client_secret", generateJWT())
                    .field("grant_type", "authorization_code")
                    .field("code", authorizationCode)
                    .field("scope", "name")
                    .asString();

            if (response.getStatus() != 200) {
                throw new RequestFailedException(Error.APPLE_AUTH_FAILED);
            }

            TokenResponse tokenResponse = new Gson().fromJson(response.getBody(), TokenResponse.class);
            String idToken = tokenResponse.getId_token();
            String payload = idToken.split("\\.")[1]; // 0 is header, we ignore it for now
            String decoded = new String(java.util.Base64.getUrlDecoder().decode(payload));

            return new Gson().fromJson(decoded, AppleIDTokenPayload.class);
        } catch (UnirestException e) {
            throw new RequestFailedException(Error.APPLE_AUTH_FAILED);
        }
    }

}
