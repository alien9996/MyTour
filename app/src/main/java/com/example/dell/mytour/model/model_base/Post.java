package com.example.dell.mytour.model.model_base;

import com.google.firebase.database.Exclude;

import java.util.HashMap;
import java.util.Map;

public class Post {

    private String post_id;
    private String post_title;
    private String post_content;
    private String post_date;
    private String post_description;
    private String post_location_map;
    private String post_location_service_end;
    private String post_location_service_start;
    private String post_time_service_start;
    private String post_time_service;
    private String post_transport_service;
    private String post_title_advertisement;
    private int post_type;
    private String post_tour_price_old;
    private String post_tour_price_new;
    private boolean post_level;
    private boolean post_status;
    private String user_id;

    public Post() {
    }

    public Post(String post_id, String post_content, String post_date) {
        this.post_id = post_id;
        this.post_content = post_content;
        this.post_date = post_date;
    }

    public Post(String post_id, String post_title, String post_content, String post_date,
                String post_description, int post_type, boolean post_level, boolean post_status, String user_id) {
        this.post_id = post_id;
        this.post_title = post_title;
        this.post_content = post_content;
        this.post_date = post_date;
        this.post_description = post_description;
        this.post_type = post_type;
        this.post_level = post_level;
        this.post_status = post_status;
        this.user_id = user_id;
    }

    public Post(String post_id, String post_title, String post_content, String post_date, String post_description,
                String post_location_map, String post_location_service_end, String post_location_service_start,
                String post_time_service_start, String post_time_service, String post_transport_service,
                String post_title_advertisement, int post_type,String post_tour_price_old,
                String post_tour_price_new, boolean post_level, boolean post_status, String user_id) {
        this.post_id = post_id;
        this.post_title = post_title;
        this.post_content = post_content;
        this.post_date = post_date;
        this.post_description = post_description;
        this.post_location_map = post_location_map;
        this.post_location_service_end = post_location_service_end;
        this.post_location_service_start = post_location_service_start;
        this.post_time_service_start = post_time_service_start;
        this.post_time_service = post_time_service;
        this.post_transport_service = post_transport_service;
        this.post_title_advertisement = post_title_advertisement;
        this.post_type = post_type;
        this.post_tour_price_old = post_tour_price_old;
        this.post_tour_price_new = post_tour_price_new;
        this.post_level = post_level;
        this.post_status = post_status;
        this.user_id = user_id;
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

    public String getPost_description() {
        return post_description;
    }

    public void setPost_description(String post_description) {
        this.post_description = post_description;
    }

    public String getPost_location_map() {
        return post_location_map;
    }

    public void setPost_location_map(String post_location_map) {
        this.post_location_map = post_location_map;
    }

    public String getPost_location_service_end() {
        return post_location_service_end;
    }

    public void setPost_location_service_end(String post_location_service_end) {
        this.post_location_service_end = post_location_service_end;
    }

    public String getPost_location_service_start() {
        return post_location_service_start;
    }

    public void setPost_location_service_start(String post_location_service_start) {
        this.post_location_service_start = post_location_service_start;
    }

    public String getPost_time_service_start() {
        return post_time_service_start;
    }

    public void setPost_time_service_start(String post_time_service_start) {
        this.post_time_service_start = post_time_service_start;
    }

    public String getPost_time_service() {
        return post_time_service;
    }

    public void setPost_time_service(String post_time_service) {
        this.post_time_service = post_time_service;
    }

    public String getPost_transport_service() {
        return post_transport_service;
    }

    public void setPost_transport_service(String post_transport_service) {
        this.post_transport_service = post_transport_service;
    }

    public String getPost_title_advertisement() {
        return post_title_advertisement;
    }

    public void setPost_title_advertisement(String post_title_advertisement) {
        this.post_title_advertisement = post_title_advertisement;
    }

    public int getPost_type() {
        return post_type;
    }

    public void setPost_type(int post_type) {
        this.post_type = post_type;
    }

    public String getPost_tour_price_old() {
        return post_tour_price_old;
    }

    public void setPost_tour_price_old(String post_tour_price_old) {
        this.post_tour_price_old = post_tour_price_old;
    }

    public String getPost_tour_price_new() {
        return post_tour_price_new;
    }

    public void setPost_tour_price_new(String post_tour_price_new) {
        this.post_tour_price_new = post_tour_price_new;
    }

    public boolean isPost_level() {
        return post_level;
    }

    public void setPost_level(boolean post_level) {
        this.post_level = post_level;
    }

    public boolean isPost_status() {
        return post_status;
    }

    public void setPost_status(boolean post_status) {
        this.post_status = post_status;
    }

    public String getUser_id() {
        return user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }

    @Exclude
    public Map<String, Object> toMap(){
        Map<String, Object> result = new HashMap<>();
        result.put("post_id",post_id);
        result.put("post_title",post_title);
        result.put("post_content",post_content);
        result.put("post_date", post_date);
        result.put("post_description",post_description);
        result.put("post_location_map", post_location_map);
        result.put("post_location_service_end", post_location_service_end);
        result.put("post_location_service_start",post_location_service_start);
        result.put("post_time_service_start",post_time_service_start);
        result.put("post_time_service", post_time_service);
        result.put("post_transport_service", post_transport_service);
        result.put("post_title_advertisement", post_title_advertisement);
        result.put("post_type",post_type);
        result.put("post_tour_price_old", post_tour_price_old);
        result.put("post_tour_price_new", post_tour_price_new);
        result.put("post_level", post_level);
        result.put("post_status", post_status);
        result.put("user_id", user_id);

        return result;
    }
}
