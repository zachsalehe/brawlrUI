package com.example.test2.chat;

public class Chat {
    private String sender;
    private String receiver;
    private String message;
    private boolean currentUser;

    public Chat (String sender, String receiver, String message, boolean currentUser) {
        this.sender = sender;
        this.receiver = receiver;
        this.message = message;
        this.currentUser = currentUser;
    }

    public String getMessage() {
        return message;
    }

    public boolean isCurrentUser() { return currentUser; }
}
