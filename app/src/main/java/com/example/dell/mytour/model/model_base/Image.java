package com.example.dell.mytour.model.model_base;

import com.google.firebase.database.Exclude;

import java.util.HashMap;
import java.util.Map;

public class Image {

    private String img_id;
    private String img_link;
    private String img_description;
    private String img_time;
    private String post_id;
    private String user_id;

    public Image() {
    }

    public Image(String img_id, String img_link, String img_description, String img_time, String post_id, String user_id) {
        this.img_id = img_id;
        this.img_link = img_link;
        this.img_description = img_description;
        this.img_time = img_time;
        this.post_id = post_id;
        this.user_id = user_id;
    }

    public String getImg_id() {
        return img_id;
    }

    public void setImg_id(String img_id) {
        this.img_id = img_id;
    }

    public String getImg_link() {
        return img_link;
    }

    public void setImg_link(String img_link) {
        this.img_link = img_link;
    }

    public String getImg_description() {
        return img_description;
    }

    public void setImg_description(String img_description) {
        this.img_description = img_description;
    }

    public String getImg_time() {
        return img_time;
    }

    public void setImg_time(String img_time) {
        this.img_time = img_time;
    }

    public String getPost_id() {
        return post_id;
    }

    public void setPost_id(String post_id) {
        this.post_id = post_id;
    }

    public String getUser_id() {
        return user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }

    @Exclude
    public Map<String, Object>  toMap(){
        Map<String, Object> result  = new HashMap<>();
        result.put("img_id",img_id);
        result.put("img_link",img_link);
        result.put("img_description",img_description);
        result.put("img_time",img_time);
        result.put("post_id",post_id);
        result.put("user_id", user_id);
        return result;
    }
}
