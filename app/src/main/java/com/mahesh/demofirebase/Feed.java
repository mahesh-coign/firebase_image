package com.mahesh.demofirebase;

public class Feed {
    private String title,description,imageLink,userId;
    public Feed() {
    }

    public Feed(String title, String description, String imageLink, String userId) {
        this.title = title;
        this.description = description;
        this.imageLink = imageLink;
        this.userId = userId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getImageLink() {
        return imageLink;
    }

    public void setImageLink(String imageLink) {
        this.imageLink = imageLink;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }
}
