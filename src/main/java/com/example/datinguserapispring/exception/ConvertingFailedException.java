package com.example.datinguserapispring.exception;

public class ConvertingFailedException extends BaseException {
  public ConvertingFailedException(Error error) {
    super(error);
  }
}