package com.example.appgym.model;

public class Exercise {
    private String name;
    private String videoUrl;
    private String imageUrl;
    private String search;
    private String calo;

    public Exercise()
    {

    }

    public Exercise(String videoUrl, String imageUrl, String search) {
        this.videoUrl = videoUrl;
        this.imageUrl = imageUrl;
        this.search = search;
    }

    public Exercise(String name, String videoUrl, String imageUrl, String search, String calo) {
        this.name = name;
        this.videoUrl = videoUrl;
        this.imageUrl = imageUrl;
        this.search = search;
        this.calo = calo;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getVideoUrl() {
        return videoUrl;
    }

    public void setVideoUrl(String videoUrl) {
        this.videoUrl = videoUrl;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getSearch() {
        return search;
    }

    public void setSearch(String search) {
        this.search = search;
    }

    public String getCalo() {
        return calo;
    }

    public void setCalo(String calo) {
        this.calo = calo;
    }
}
