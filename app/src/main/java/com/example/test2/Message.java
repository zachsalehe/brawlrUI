package com.example.test2;

public class Message {
    /*
    This is a class to store the message object, to be passed to the realtime database.
    #TODO Will interact with chat in the future
     */
    private String message;

    public Message(){
    }

    public String getMessage(){
        return this.message;
    }

    public void setMessage(String message){
        this.message = message;
    }

}
