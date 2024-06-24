package com.example.datinguserapispring.exception;

public class ConvertingException extends BaseException{
    public ConvertingException(Error error) {
        super(error);
    }
}
