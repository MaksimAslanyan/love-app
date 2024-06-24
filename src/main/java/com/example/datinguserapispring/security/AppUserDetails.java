package com.example.datinguserapispring.security;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.util.List;

@Data
@Builder(toBuilder = true)
@NoArgsConstructor
@AllArgsConstructor
public class AppUserDetails {
  private String username;
  private List<SimpleGrantedAuthority> authorities;
  private Boolean enabled;
}