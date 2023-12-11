package com.example.instamate.Models;

import java.util.Date;
import java.util.List;
import java.util.Map;

public class Post_Model {
    private String userId;
    private String caption;

    public String getPost_url() {
        return post_url;
    }

    public void setPost_url(String post_url) {
        this.post_url = post_url;
    }

    private String post_url;
    private Map<String, Boolean> likedBy;

    public Map<String, Boolean> getLikedBy() {
        return likedBy;
    }

    public void setLikedBy(Map<String, Boolean> likedBy) {
        this.likedBy = likedBy;
    }

    public Post_Model(String userId, String post_url, String content, int likes, Date timestamp, Map<String, Boolean> likedBy) {
        this.userId = userId;
        this.caption = content;
        this.post_url=post_url;
        this.likes = likes;
        this.comments = comments;
        this.timestamp = timestamp;
        this.commentList = commentList;
        this.likedBy=likedBy;
    }

    private int likes;



    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getCaption() {
        return caption;
    }

    public void setCaption(String content) {
        this.caption = content;
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

    private int comments;
    private Date timestamp;
    private List<Comment_Model> commentList;
    Post_Model(){

    }




}
