package com.psx.hiddedlinearlayout.Models;

import java.util.Date;

public class Message {

    private String senderName;
    private String messagePreview;
    private Date time;

    public Message(String senderName, String messagePreview, Date time) {
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

    public Date getTime() {
        return time;
    }

    public void setTime(Date time) {
        this.time = time;
    }
}
