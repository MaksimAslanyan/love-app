package com.example.datinguserapispring.exception;

public class IOException extends BaseException{
    public IOException(Error error) {
        super(error);
    }
}
