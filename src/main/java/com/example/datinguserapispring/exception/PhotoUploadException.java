package com.example.datinguserapispring.exception;

public class PhotoUploadException extends BaseException{
    public PhotoUploadException(Error error) {
        super(error);
    }
}
