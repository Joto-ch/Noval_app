package com.example.demo.fragment;

public class book {
    private String name;
    private String introduction;
    private String imageId;
    private String url;

    public book(String name, String introduction, String imageId, String url) {
        this.name = name;
        this.introduction = introduction;
        this.imageId = imageId;
        this.url = url;
    }

    public String getName() {
        return name;
    }

    public String getIntroduction() {
        return introduction;
    }

    public String getImageId() {
        return imageId;
    }

    public String getUrl() {
        return url;
    }
}
