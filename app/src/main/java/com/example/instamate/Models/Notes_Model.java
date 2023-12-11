package com.example.instamate.Models;

public class Notes_Model {
    String username;
    String pic;
    String message;

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPic() {
        return pic;
    }

    public void setPic(String pic) {
        this.pic = pic;
    }

    public String getText() {
        return message;
    }

    public void setText(String text) {
        this.message = text;
    }

    public Notes_Model(String username, String pic, String text) {
        this.username = username;
        this.pic = pic;
        this.message = text;
    }
    Notes_Model(){

    }
}
