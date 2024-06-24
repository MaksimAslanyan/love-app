package com.example.datinguserapispring.exception;

public class AdminAlreadyExistException extends BaseException{
    public AdminAlreadyExistException(Error error) {
        super(error);
    }
}
