package com.example.dell.mytour.model.model_base;

import com.google.firebase.database.Exclude;

import java.util.HashMap;
import java.util.Map;

public class Rating {

    private String rating_id;
    private String rating_level;
    private String post_id;

    public Rating() {
    }

    public Rating(String rating_id, String rating_level, String post_id) {
        this.rating_id = rating_id;
        this.rating_level = rating_level;
        this.post_id = post_id;
    }

    public String getRating_id() {
        return rating_id;
    }

    public void setRating_id(String rating_id) {
        this.rating_id = rating_id;
    }

    public String getRating_level() {
        return rating_level;
    }

    public void setRating_level(String rating_level) {
        this.rating_level = rating_level;
    }

    public String getPost_id() {
        return post_id;
    }

    public void setPost_id(String post_id) {
        this.post_id = post_id;
    }

    @Exclude
    public Map<String, Object> toMap(){
        HashMap<String, Object> result = new HashMap<>();
        result.put("rating_id",rating_id);
        result.put("rating_level",rating_level);
        result.put("post_id",post_id);
        return result;
    }
}
