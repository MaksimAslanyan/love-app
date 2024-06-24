package com.example.datinguserapispring.exception;

public class UserInBlackListException extends BaseException{
    public UserInBlackListException(Error error) {
        super(error);
    }
}
