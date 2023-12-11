package com.example.instamate.Models;

import java.util.Date;

public class Chat_Message_Model {
    private String senderuid;
    private String message;

    public String getSenderuid() {
        return senderuid;
    }

    public void setSenderuid(String senderuid) {
        this.senderuid = senderuid;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Date getTimestmap() {
        return timestmap;
    }

    public void setTimestmap(Date timestmap) {
        this.timestmap = timestmap;
    }

    private Date timestmap;
    Chat_Message_Model(){

    }

    public Chat_Message_Model(String senderuid, String message, Date timestmap) {
        this.senderuid = senderuid;
        this.message = message;
        this.timestmap = timestmap;
    }
}
