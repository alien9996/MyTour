package com.example.dell.mytour.model.model_base;

import com.google.firebase.database.Exclude;

import java.util.HashMap;
import java.util.Map;

public class Like {

    private  String like_time;
    private String like_id;
    private boolean like_type;
    private String  post_id;
    private String friend_id;

    public Like() {
    }

    public Like(String like_time, String like_id, boolean like_type, String post_id, String friend_id) {
        this.like_time = like_time;
        this.like_id = like_id;
        this.like_type = like_type;
        this.post_id = post_id;
        this.friend_id = friend_id;
    }

    public String getLike_time() {
        return like_time;
    }

    public void setLike_time(String like_time) {
        this.like_time = like_time;
    }

    public String getLike_id() {
        return like_id;
    }

    public void setLike_id(String like_id) {
        this.like_id = like_id;
    }

    public boolean isLike_type() {
        return like_type;
    }

    public void setLike_type(boolean like_type) {
        this.like_type = like_type;
    }

    public String getPost_id() {
        return post_id;
    }

    public void setPost_id(String post_id) {
        this.post_id = post_id;
    }

    public String getFriend_id() {
        return friend_id;
    }

    public void setFriend_id(String friend_id) {
        this.friend_id = friend_id;
    }

    @Exclude
    public Map<String, Object> toMap(){
        HashMap<String, Object> result = new HashMap<>();
        result.put("like_id",like_id);
        result.put("like_time",like_time);
        result.put("like_type",like_type);
        result.put("post_id",post_id);
        result.put("friend_id",friend_id);


        return result;
    }
}
