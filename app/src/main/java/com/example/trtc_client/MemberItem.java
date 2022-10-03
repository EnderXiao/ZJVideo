package com.example.trtc_client;

public class MemberItem {
    private String name;
    private boolean chatControl;
    private boolean speakControl;
    private boolean audioControl;
    private boolean videoControl;
    private boolean boardControl;

    public MemberItem(String name, boolean chatControl, boolean speakControl, boolean audioControl, boolean boardControl, boolean videoControl) {
        this.name = name;
        this.chatControl = chatControl;
        this.speakControl = speakControl;
        this.audioControl = audioControl;
        this.videoControl = videoControl;
        this.boardControl = boardControl;
    }

    public String getName() {
        return name;
    }


    public void setName(String name) {
        this.name = name;
    }

    public boolean getChatControl() {
        return chatControl;
    }

    public void setChatControl(boolean chatControl) {
        this.chatControl = chatControl;
    }

    public boolean getSpeakControl() {
        return speakControl;
    }

    public void setSpeakControl(boolean speakControl) {
        this.speakControl = speakControl;
    }

    public boolean getAudioControl() {
        return audioControl;
    }

    public void setAudioControl(boolean audioControl) {
        this.audioControl = audioControl;
    }

    public boolean getVideoControl() {
        return videoControl;
    }

    public void setVideoControl(boolean videoControl) {
        this.videoControl = videoControl;
    }

    public boolean getBoardControl() {
        return boardControl;
    }

    public void setBoardControl(boolean boardControl) {
        this.boardControl = boardControl;
    }
}
