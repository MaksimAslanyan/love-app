package com.example.datinguserapispring.config.properties;

import com.example.datinguserapispring.security.RestAuthorizationFilter;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;


@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class WebSecurityConfig {

    private static final String[] PERMITTED_URIS = {
            "/swagger-ui/**",
            "/v1/test/**",
            "/root/**",
            "/image/**",
            "/loginApple",
            "/admin/login",
            "/webjars/swagger-ui/**",
            "/image/**",
            "/send-notification",
            "/dictionary/**",
            "/v3/api-docs/**"
    };

    private final RestAuthorizationFilter restAuthorizationFilter;


    @Bean
    SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .cors()
                .and()
                .csrf()
                .disable()
                .exceptionHandling()
                .and()
                .sessionManagement()
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .and()
                .authorizeRequests()
                .antMatchers(PERMITTED_URIS).permitAll()
                .anyRequest()
                .authenticated();
        http.addFilterBefore(restAuthorizationFilter, UsernamePasswordAuthenticationFilter.class);
        return http.build();
    }
}

