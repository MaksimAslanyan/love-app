package com.example.datinguserapispring.exception;

public class EntityNotFoundException extends BaseException{
    public EntityNotFoundException(Error error) {
        super(error);
    }
}
