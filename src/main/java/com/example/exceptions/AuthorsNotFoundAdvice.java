package com.example.exceptions;


import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

@ControllerAdvice
public class AuthorsNotFoundAdvice {


    @ResponseBody
    @ExceptionHandler(AuthorsNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    String authorsNotFoundHandler(AuthorsNotFoundException ex){
        return ex.getMessage();
    }
}
