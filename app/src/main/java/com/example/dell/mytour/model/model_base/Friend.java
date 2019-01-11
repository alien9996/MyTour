package com.example.dell.mytour.model.model_base;

import com.google.firebase.database.Exclude;

import java.util.HashMap;
import java.util.Map;

public class Friend {
    private String friend_id;
    private String friend_link;
    private boolean friend_status;
    private String friend_date;
    private String user_id;

    public Friend() {
    }

    public Friend(String friend_id, String friend_link, boolean friend_status, String friend_date, String user_id) {
        this.friend_id = friend_id;
        this.friend_link = friend_link;
        this.friend_status = friend_status;
        this.user_id = user_id;
        this.friend_date = friend_date;
    }

    public String getFriend_id() {
        return friend_id;
    }

    public void setFriend_id(String friend_id) {
        this.friend_id = friend_id;
    }

    public String getFriend_link() {
        return friend_link;
    }

    public void setFriend_link(String friend_link) {
        this.friend_link = friend_link;
    }

    public boolean isFriend_status() {
        return friend_status;
    }

    public void setFriend_status(boolean friend_status) {
        this.friend_status = friend_status;
    }

    public String getUser_id() {
        return user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }

    public String getFriend_date() {
        return friend_date;
    }

    public void setFriend_date(String friend_date) {
        this.friend_date = friend_date;
    }

    @Exclude
    public Map<String, Object> toMap(){
        Map<String, Object> result = new HashMap<>();
        result.put("friend_id",friend_id);
        result.put("friend_link",friend_link);
        result.put("friend_status",friend_status);
        result.put("friend_date",friend_date);
        result.put("user_id",user_id);
        return result;
    }
}
