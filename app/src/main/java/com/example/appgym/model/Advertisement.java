package com.example.appgym.model;

public class Advertisement {
    private String imageUrl;
    private String roomname;
    private String bossname;
    private String numberphone;
    private String address;

    public Advertisement() {
    }

    public Advertisement(String imageUrl, String roomname, String bossname, String numberphone, String address) {
        this.imageUrl = imageUrl;
        this.roomname = roomname;
        this.bossname = bossname;
        this.numberphone = numberphone;
        this.address = address;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getRoomname() {
        return roomname;
    }

    public void setRoomname(String roomname) {
        this.roomname = roomname;
    }

    public String getBossname() {
        return bossname;
    }

    public void setBossname(String bossname) {
        this.bossname = bossname;
    }

    public String getNumberphone() {
        return numberphone;
    }

    public void setNumberphone(String numberphone) {
        this.numberphone = numberphone;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }
}
