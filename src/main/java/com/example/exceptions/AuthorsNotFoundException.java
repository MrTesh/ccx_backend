package com.example.exceptions;

public class AuthorsNotFoundException extends RuntimeException{
    public AuthorsNotFoundException(Long id){
        super("Could not find author" + id);
    }
}
