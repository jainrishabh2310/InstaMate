package com.example.instamate;

public class Profile_DataHolder {
    String username;
    String Gender;
    String Dob;

    public Profile_DataHolder(String username, String gender, String dob, String bio, String profile_pic_url) {
        this.username = username;
        Gender = gender;
        Dob = dob;
        Bio = bio;
        this.profile_pic_url = profile_pic_url;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getGender() {
        return Gender;
    }

    public void setGender(String gender) {
        Gender = gender;
    }

    public String getDob() {
        return Dob;
    }

    public void setDob(String dob) {
        Dob = dob;
    }

    public String getBio() {
        return Bio;
    }

    public void setBio(String bio) {
        Bio = bio;
    }

    public String getProfile_pic_url() {
        return profile_pic_url;
    }

    public void setProfile_pic_url(String profile_pic_url) {
        this.profile_pic_url = profile_pic_url;
    }

    Profile_DataHolder(){

    }

    String Bio;
    String profile_pic_url;

}
