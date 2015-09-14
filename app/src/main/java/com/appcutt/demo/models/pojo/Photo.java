package com.appcutt.demo.models.pojo;

import com.google.gson.annotations.SerializedName;

public class Photo extends Model {

    @SerializedName("id")
    private String id;

    @SerializedName("title")
    private String title;

    @SerializedName("imageUrl")
    private String imageUrl;


    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

}
