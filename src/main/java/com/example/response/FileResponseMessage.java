package com.example.response;

public class FileResponseMessage {
    private String message;

    public FileResponseMessage(String message){
        this.message = message;
    }
    public FileResponseMessage(){

    }

    public String getMessage(){
        return message;
    }

    public void setMessage(String message){
        this.message = message;
    }
}
