package com.example.datinguserapispring.exception;

public class UserNotFoundException extends BaseException{
    public UserNotFoundException(Error error) {
        super(error);
    }
}
