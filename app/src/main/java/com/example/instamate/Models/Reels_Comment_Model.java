package com.example.instamate.Models;

import java.util.Date;
import java.util.HashMap;

public class Reels_Comment_Model {

    private String userId;

    public Reels_Comment_Model(String userId, String caption, Date timestampint , String reelid,int likes, HashMap<String, Boolean> hashMap) {
        this.userId = userId;
        this.caption = caption;
        this.hashMap = hashMap;
        this.likes = likes;
        this.reelid = reelid;
        this.timestamp = timestamp;
    }

    private String caption;
    private HashMap<String,Boolean> hashMap;
    private int likes;
    private String reelid;

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getCaption() {
        return caption;
    }

    public void setCaption(String caption) {
        this.caption = caption;
    }

    public HashMap<String, Boolean> getHashMap() {
        return hashMap;
    }

    public void setHashMap(HashMap<String, Boolean> hashMap) {
        this.hashMap = hashMap;
    }

    public int getLikes() {
        return likes;
    }

    public void setLikes(int likes) {
        this.likes = likes;
    }

    public String getReelid() {
        return reelid;
    }

    public void setReelid(String reelid) {
        this.reelid = reelid;
    }

    public Date getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Date timestamp) {
        this.timestamp = timestamp;
    }

    private Date timestamp;
    Reels_Comment_Model(){

    }



}
