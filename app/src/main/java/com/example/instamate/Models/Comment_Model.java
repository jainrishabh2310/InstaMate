package com.example.instamate.Models;

import java.util.Date;
import java.util.HashMap;

public class Comment_Model {

    private String userId;
    private String content;
    private HashMap<String,Boolean> hashMap;

    private int likes;

    public String getPostid() {
        return postid;
    }

    public void setPostid(String postid) {
        this.postid = postid;
    }

    private String postid;


    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public HashMap<String, Boolean> getHashMap() {
        return hashMap;
    }

    public void setHashMap(HashMap<String, Boolean> hashMap) {
        this.hashMap = hashMap;
    }

    public Date getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Date timestamp) {
        this.timestamp = timestamp;
    }

    public Comment_Model(String userId, String content, Date timestamp,String postid,int likes,HashMap<String,Boolean> hashMap) {
        this.userId = userId;
        this.content = content;
        this.timestamp = timestamp;
        this.postid=postid;
        this.likes=likes;
        this.hashMap=hashMap;
    }

    public int getLikes() {
        return likes;
    }

    public void setLikes(int likes) {
        this.likes = likes;
    }

    Comment_Model(){

    }

    private Date timestamp;
}
