package com.skillswap.model;

public class ExchangeRequest {

    private int id;
    private int senderId;
    private int receiverId;
    private int skillId;

    private String senderName;
    private String receiverName;
    private String skillName;
    private String status;
    private String createdAt;

    private String otherName;
    private String otherEmail;
    private String otherBio;


    public int getId() {
        return id;
    }

    public void setId(int v) {
        id = v;
    }


    public int getSenderId() {
        return senderId;
    }

    public void setSenderId(int v) {
        senderId = v;
    }


    public int getReceiverId() {
        return receiverId;
    }

    public void setReceiverId(int v) {
        receiverId = v;
    }


    public int getSkillId() {
        return skillId;
    }

    public void setSkillId(int v) {
        skillId = v;
    }


    public String getSenderName() {
        return senderName;
    }

    public void setSenderName(String v) {
        senderName = v;
    }


    public String getReceiverName() {
        return receiverName;
    }

    public void setReceiverName(String v) {
        receiverName = v;
    }


    public String getSkillName() {
        return skillName;
    }

    public void setSkillName(String v) {
        skillName = v;
    }


    public String getStatus() {
        return status;
    }

    public void setStatus(String v) {
        status = v;
    }


    public String getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(String v) {
        createdAt = v;
    }


    // Details of the other person in an accepted exchange

    public String getOtherName() {
        return otherName;
    }

    public void setOtherName(String v) {
        otherName = v;
    }


    public String getOtherEmail() {
        return otherEmail;
    }

    public void setOtherEmail(String v) {
        otherEmail = v;
    }


    public String getOtherBio() {
        return otherBio;
    }

    public void setOtherBio(String v) {
        otherBio = v;
    }
}