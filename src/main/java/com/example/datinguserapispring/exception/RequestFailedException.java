package com.example.datinguserapispring.exception;

public class RequestFailedException extends BaseException{
    public RequestFailedException(Error error) {
        super(error);
    }
}
