package com.example.dell.mytour.model.model_base;

import com.google.firebase.database.Exclude;

import java.util.HashMap;
import java.util.Map;

public class Comment {
    private String cmm_id;
    private String cmm_content;
    private String cmm_date;
    private boolean cmm_status;
    private String post_id;
    private String friend_id;

    public Comment(String cmm_id, String cmm_content, String cmm_date, boolean cmm_status, String post_id, String friend_id) {
        this.cmm_id = cmm_id;
        this.cmm_content = cmm_content;
        this.cmm_date = cmm_date;
        this.cmm_status = cmm_status;
        this.post_id = post_id;
        this.friend_id = friend_id;
    }

    public Comment() {
    }

    public String getCmm_id() {
        return cmm_id;
    }

    public void setCmm_id(String cmm_id) {
        this.cmm_id = cmm_id;
    }

    public String getCmm_content() {
        return cmm_content;
    }

    public void setCmm_content(String cmm_content) {
        this.cmm_content = cmm_content;
    }

    public String getCmm_date() {
        return cmm_date;
    }

    public void setCmm_date(String cmm_date) {
        this.cmm_date = cmm_date;
    }

    public boolean isCmm_status() {
        return cmm_status;
    }

    public void setCmm_status(boolean cmm_status) {
        this.cmm_status = cmm_status;
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
        result.put("cmm_id",cmm_id);
        result.put("cmm_date",cmm_date);
        result.put("cmm_content",cmm_content);
        result.put("cmm_status",cmm_status);
        result.put("friend_id",friend_id);
        result.put("post_id",post_id);
        return result;
    }
}
