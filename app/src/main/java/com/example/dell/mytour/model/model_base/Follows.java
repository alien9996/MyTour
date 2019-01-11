package com.example.dell.mytour.model.model_base;

public class Follows {
    private String follow_id;
    private boolean follow_status;
    private String follow_date;
    private String user_id;
    private String friend_id;

    public Follows() {
    }

    public Follows(String follow_id, boolean follow_status, String follow_date, String user_id, String friend_id) {
        this.follow_id = follow_id;
        this.follow_status = follow_status;
        this.follow_date = follow_date;
        this.user_id = user_id;
        this.friend_id = friend_id;
    }

    public String getFollow_id() {
        return follow_id;
    }

    public void setFollow_id(String follow_id) {
        this.follow_id = follow_id;
    }

    public boolean isFollow_status() {
        return follow_status;
    }

    public void setFollow_status(boolean follow_status) {
        this.follow_status = follow_status;
    }

    public String getFollow_date() {
        return follow_date;
    }

    public void setFollow_date(String follow_date) {
        this.follow_date = follow_date;
    }

    public String getUser_id() {
        return user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }

    public String getFriend_id() {
        return friend_id;
    }

    public void setFriend_id(String friend_id) {
        this.friend_id = friend_id;
    }
}
