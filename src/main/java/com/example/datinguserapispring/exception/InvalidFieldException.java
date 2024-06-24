package com.example.datinguserapispring.exception;

public class InvalidFieldException extends BaseException {
  public InvalidFieldException(Error error) {
    super(error);
  }
}