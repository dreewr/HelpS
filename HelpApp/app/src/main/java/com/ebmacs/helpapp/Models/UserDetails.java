package com.ebmacs.helpapp.Models;

public class UserDetails {
    public String userId=null;
    public String imageUrl=null;
    public String userName=null;
    public Boolean isSlected=false;


    public Boolean getSlected() {
        return isSlected;
    }

    public void setSlected(Boolean slected) {
        isSlected = slected;
    }


    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }
}
