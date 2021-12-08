package com.example.test2.chat;

/**
 * a chat object that is used to represent our chats
 */
public class Chat {
    private final String sender;
    private final String message;
    private final boolean currentUser;

    /**
     * constructor
     * @param sender the message sender
     * @param receiver the message reciever
     * @param message the message
     * @param currentUser whether the current user is the message sender or reciever
     */
    public Chat (String sender, String receiver, String message, boolean currentUser) {
        this.sender = sender;
        this.message = message;
        this.currentUser = currentUser;
    }

    public String getMessage() {
        return message;
    }

    public String getSender(){return sender;}

    public boolean isCurrentUser() { return currentUser; }
}
