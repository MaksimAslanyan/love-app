package com.example.datinguserapispring.exception;

import lombok.Getter;

@Getter
public class AuthenticationException extends BaseException {
  public AuthenticationException(Error error) {
    super(error);
  }
}