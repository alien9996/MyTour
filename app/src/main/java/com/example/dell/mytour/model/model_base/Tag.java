package com.example.dell.mytour.model.model_base;

import com.google.firebase.database.Exclude;

import java.util.HashMap;
import java.util.Map;

public class Tag {

    private String tag_id;
    private boolean tag_status;
    private String post_id;
    private String friend_id;

    public Tag() {
    }

    public Tag(String tag_id, boolean tag_status, String post_id, String friend_id) {
        this.tag_id = tag_id;
        this.tag_status = tag_status;
        this.post_id = post_id;
        this.friend_id = friend_id;
    }

    public String getTag_id() {
        return tag_id;
    }

    public void setTag_id(String tag_id) {
        this.tag_id = tag_id;
    }

    public boolean isTag_status() {
        return tag_status;
    }

    public void setTag_status(boolean tag_status) {
        this.tag_status = tag_status;
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
        Map<String, Object> result = new HashMap<>();
        result.put("tag_id",tag_id);
        result.put("tag_status",tag_status);
        result.put("post_id", post_id);
        result.put("friend_id",friend_id);

        return result;
    }
}
