package com.example.datinguserapispring.security;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;
import java.security.spec.InvalidKeySpecException;
import java.util.Base64;

@Service
@RequiredArgsConstructor
public class PBKDF2Encoder implements PasswordEncoder {
    @Value("${service.jjwt.secret}")
    private String secret;
    @Value("${service.jjwt.iteration}")
    private int iteration;
    @Value("${service.jjwt.keylength}")
    private int keyLength;
    private SecretKeyFactory secretKeyFactory;



    @Override
    public String encode(CharSequence cs) {
        try {
            byte[] result = secretKeyFactory.generateSecret(
                    new PBEKeySpec(cs.toString().toCharArray(), secret.getBytes(), iteration, keyLength)
            ).getEncoded();
            return Base64.getEncoder().encodeToString(result);
        } catch (InvalidKeySpecException ex) {
            throw new RuntimeException(ex);
        }
    }

    @Override
    public boolean matches(CharSequence cs, String string) {
        return encode(cs).equals(string);
    }
}