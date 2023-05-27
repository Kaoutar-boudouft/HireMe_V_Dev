package com.example.hireme.Exceptions;

public class ProfileAlreadyExistException extends RuntimeException{
    public ProfileAlreadyExistException(String message) {
        super(message);
    }
}
