package com.example.datinguserapispring.exception;

public class FirebaseException extends BaseException{
    public FirebaseException(Error error) {
        super(error);
    }
}
