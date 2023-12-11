package com.example.instamate.Models;

import android.net.Uri;

import java.util.Date;
import java.util.List;
import java.util.Map;

public class Reel_Model {

    private String userId;


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

    public String getReel_url() {
        return reel_url;
    }

    public void setReel_url(String reel_url) {
        this.reel_url = reel_url;
    }

    public Map<String, Boolean> getLikedBy() {
        return likedBy;
    }

    public void setLikedBy(Map<String, Boolean> likedBy) {
        this.likedBy = likedBy;
    }

    public int getLikes() {
        return likes;
    }

    public void setLikes(int likes) {
        this.likes = likes;
    }

    public int getComments() {
        return comments;
    }

    public void setComments(int comments) {
        this.comments = comments;
    }

    public Date getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Date timestamp) {
        this.timestamp = timestamp;
    }

    public List<Comment_Model> getCommentList() {
        return commentList;
    }

    public void setCommentList(List<Comment_Model> commentList) {
        this.commentList = commentList;
    }

    public Reel_Model(String userId, String caption, String reel_url,int likes, Date timestamp, Map<String, Boolean> likedBy) {
        this.userId = userId;
        this.caption = caption;
        this.reel_url = reel_url;
        this.likedBy = likedBy;
        this.likes = likes;
        this.comments = comments;
        this.timestamp = timestamp;
        this.commentList = commentList;
    }
    public Reel_Model(){

    }
    public Reel_Model(String reel_url){
        this.reel_url=reel_url;
    }

    private String caption;

    private String reel_url;
    private Map<String, Boolean> likedBy;
    private int likes;
    private int comments;
    private Date timestamp;
    private List<Comment_Model> commentList;
}
