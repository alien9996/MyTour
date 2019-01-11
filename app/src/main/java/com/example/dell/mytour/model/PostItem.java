package com.example.dell.mytour.model;

public class PostItem {

    private String post_id;
    private String post_title;
    private String post_content;
    private String post_date;
    private String post_rating_average;
    private String post_avatar;
    private String user_post_avatar;
    private String user_post_name;
    private String post_advertisement_title;
    private String post_price_new;
    private String post_price_old;
    private String post_location_stop;

    public PostItem() {
    }

    public PostItem(String post_id, String post_title, String post_content, String post_date,
                    String post_rating_average, String post_avatar, String user_post_avatar,
                    String user_post_name, String post_advertisement_title, String post_price_new,
                    String post_price_old, String post_location_stop) {
        this.post_id = post_id;
        this.post_title = post_title;
        this.post_content = post_content;
        this.post_date = post_date;
        this.post_rating_average = post_rating_average;
        this.post_avatar = post_avatar;
        this.user_post_avatar = user_post_avatar;
        this.user_post_name = user_post_name;
        this.post_advertisement_title = post_advertisement_title;
        this.post_price_new = post_price_new;
        this.post_price_old = post_price_old;
        this.post_location_stop = post_location_stop;

    }

    public String getPost_id() {
        return post_id;
    }

    public void setPost_id(String post_id) {
        this.post_id = post_id;
    }

    public String getPost_title() {
        return post_title;
    }

    public void setPost_title(String post_title) {
        this.post_title = post_title;
    }

    public String getPost_content() {
        return post_content;
    }

    public void setPost_content(String post_content) {
        this.post_content = post_content;
    }

    public String getPost_date() {
        return post_date;
    }

    public void setPost_date(String post_date) {
        this.post_date = post_date;
    }

    public String getPost_rating_average() {
        return post_rating_average;
    }

    public void setPost_rating_average(String post_rating_average) {
        this.post_rating_average = post_rating_average;
    }

    public String getPost_avatar() {
        return post_avatar;
    }

    public void setPost_avatar(String post_avatar) {
        this.post_avatar = post_avatar;
    }

    public String getUser_post_avatar() {
        return user_post_avatar;
    }

    public void setUser_post_avatar(String user_post_avatar) {
        this.user_post_avatar = user_post_avatar;
    }

    public String getUser_post_name() {
        return user_post_name;
    }

    public void setUser_post_name(String user_post_name) {
        this.user_post_name = user_post_name;
    }

    public String getPost_advertisement_title() {
        return post_advertisement_title;
    }

    public void setPost_advertisement_title(String post_advertisement_title) {
        this.post_advertisement_title = post_advertisement_title;
    }

    public String getPost_price_new() {
        return post_price_new;
    }

    public void setPost_price_new(String post_price_new) {
        this.post_price_new = post_price_new;
    }

    public String getPost_price_old() {
        return post_price_old;
    }

    public void setPost_price_old(String post_price_old) {
        this.post_price_old = post_price_old;
    }

    public String getPost_location_stop() {
        return post_location_stop;
    }

    public void setPost_location_stop(String post_location_stop) {
        this.post_location_stop = post_location_stop;
    }
}
