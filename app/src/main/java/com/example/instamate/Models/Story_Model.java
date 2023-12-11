package com.example.instamate.Models;

import java.util.Date;

public class Story_Model {
    private String userid;

    public Story_Model(String userid, String storyid, Date timestamp, String story_url) {
        this.userid = userid;
        this.storyid = storyid;
        this.timestamp = timestamp;
        this.story_url = story_url;
    }
    Story_Model(){

    }

    public String getUserid() {
        return userid;
    }

    public void setUserid(String userid) {
        this.userid = userid;
    }

    public String getStoryid() {
        return storyid;
    }

    public void setStoryid(String storyid) {
        this.storyid = storyid;
    }

    public Date getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Date timestamp) {
        this.timestamp = timestamp;
    }

    public String getStory_url() {
        return story_url;
    }

    public void setStory_url(String story_url) {
        this.story_url = story_url;
    }

    private String storyid;
    private Date timestamp;

    private  String story_url;






}
