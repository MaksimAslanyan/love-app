package com.example.datinguserapispring.exception;

public class UserLikeLimitException extends BaseException{
    public UserLikeLimitException(Error error) {
        super(error);
    }
}
