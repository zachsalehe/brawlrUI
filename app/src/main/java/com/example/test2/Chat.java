package com.example.test2;

public class Chat {
    private String sender;
    private String receiver;
    private String message;
    private String chatID;

    public Chat(){

    }

    public Chat (String sender, String receiver, String message, String chatID) {
        this.sender = sender;
        this.receiver = receiver;
        this.message = message;
        this.chatID = chatID;
    }


    public String getChatID() {
        return chatID;
    }

    public void setChatID(String chatID) {
        this.chatID = chatID;
    }

    public String getSender() {
        return sender;
    }

    public void setSender(String sender) {
        this.sender = sender;
    }

    public String getReceiver() {
        return receiver;
    }

    public void setReceiver(String receiver) {
        this.receiver = receiver;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
