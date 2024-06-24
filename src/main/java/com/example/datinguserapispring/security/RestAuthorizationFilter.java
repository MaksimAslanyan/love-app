package com.example.datinguserapispring.security;


import com.example.datinguserapispring.exception.AuthenticationException;
import com.example.datinguserapispring.manager.JwtTokenManager;
import io.jsonwebtoken.Claims;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Slf4j
@Component
@RequiredArgsConstructor
public class RestAuthorizationFilter extends OncePerRequestFilter {

    public static final String ERROR = "error";

    private final JwtTokenManager jwtTokenManager;
    private final SecurityContextService securityContextService;


    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {
        try {
            String authorization = request.getHeader("Authorization");
            if (authorization != null && !authorization.isEmpty()) {
//                String[] token = authorization.split(" ", 7);
//                Claims claims = jwtTokenManager.getAllClaimsFromToken(token[1]);
//                List<String> roles = Arrays.asList(claims.get("role", String.class).split(","));
//                List<SimpleGrantedAuthority> authorities = new ArrayList<>();
//                for (String role : roles) {
//                    authorities.add(new SimpleGrantedAuthority(role));
//                }
//                AppUserDetails userDetails =
//                        AppUserDetails.builder()
//                                .username(claims.get("sub").toString())
//                                .authorities(authorities)
//                                .build();
//                securityContextService.setAuthentication(
//                        new UsernamePasswordAuthenticationToken(
//                                userDetails, null, userDetails.getAuthorities()));
            }
            filterChain.doFilter(request, response);
        } catch (AuthenticationException ex) {
            response.addHeader(ERROR, ex.getError().name());
            response.setStatus(ex.getError().getHttpStatus().value());
        } finally {
            securityContextService.clearAuthentication();
        }
    }
}

