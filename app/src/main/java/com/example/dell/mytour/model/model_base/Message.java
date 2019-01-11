package com.example.dell.mytour.model.model_base;

import com.google.firebase.database.Exclude;

import java.util.HashMap;
import java.util.Map;

public class Message {
    private String message_id;
    private String message_date;
    private int message_type;
    private boolean message_status;
    private String post_id;
    private String friend_id;
    private String user_id;


    public Message() {
    }

    public Message(String message_id, String message_date, int message_type,
                   boolean message_status, String post_id, String friend_id,
                   String user_id) {
        this.message_id = message_id;
        this.message_date = message_date;
        this.message_type = message_type;
        this.message_status = message_status;
        this.post_id = post_id;
        this.friend_id = friend_id;
        this.user_id = user_id;
    }

    public String getMessage_id() {
        return message_id;
    }

    public void setMessage_id(String message_id) {
        this.message_id = message_id;
    }

    public String getMessage_date() {
        return message_date;
    }

    public void setMessage_date(String message_date) {
        this.message_date = message_date;
    }

    public int getMessage_type() {
        return message_type;
    }

    public void setMessage_type(int message_type) {
        this.message_type = message_type;
    }

    public boolean isMessage_status() {
        return message_status;
    }

    public void setMessage_status(boolean message_status) {
        this.message_status = message_status;
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

    public String getUser_id() {
        return user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }


    @Exclude
    public Map<String, Object> toMap(){
        Map<String, Object> result = new HashMap<>();
        result.put("message_id",message_id);
        result.put("message_date",message_date);
        result.put("message_type",message_type);
        result.put("message_status",message_status);
        result.put("post_id",post_id);
        result.put("friend_id",friend_id);
        result.put("user_id",user_id);

        return result;
    }
}
