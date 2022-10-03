package com.example.trtc_client;

public class HandsUpItem {
    private String name;
    private String userId;
    private boolean speakControl;

    public HandsUpItem(String name, String userId, boolean speakControl) {
        this.name = name;
        this.userId = userId;
        this.speakControl = speakControl;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public boolean getSpeakControl() {
        return speakControl;
    }

    public void setSpeakControl(boolean speakControl) {
        this.speakControl = speakControl;
    }
}
