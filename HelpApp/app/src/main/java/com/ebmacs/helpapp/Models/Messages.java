package com.ebmacs.helpapp.Models;

public class Messages {
    public String sentMessages = null;
    public String receivedMessages = null;
    public String memberId = null;
    public String userName = null;
    public String msgTime = null;

    public String getMsgTime() {
        return msgTime;
    }

    public void setMsgTime(String msgTime) {
        this.msgTime = msgTime;
    }


    public String getMemberId() {
        return memberId;
    }

    public void setMemberId(String memberId) {
        this.memberId = memberId;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getSentMessages() {
        return sentMessages;
    }

    public void setSentMessages(String sentMessages) {
        this.sentMessages = sentMessages;
    }

    public String getReceivedMessages() {
        return receivedMessages;
    }

    public void setReceivedMessages(String receivedMessages) {
        this.receivedMessages = receivedMessages;
    }
}
