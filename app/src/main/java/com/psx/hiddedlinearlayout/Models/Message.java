package com.psx.hiddedlinearlayout.Models;

public class Message {

    private String senderName;
    private String messagePreview;
    private String time;

    public Message(String senderName, String messagePreview, String time) {
        this.senderName = senderName;
        this.messagePreview = messagePreview;
        this.time = time;
    }

    public String getSenderName() {
        return senderName;
    }

    public void setSenderName(String senderName) {
        this.senderName = senderName;
    }

    public String getMessagePreview() {
        return messagePreview;
    }

    public void setMessagePreview(String messagePreview) {
        this.messagePreview = messagePreview;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }
}
