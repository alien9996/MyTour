package com.example.dell.mytour.model;

public class MessageItem {
    private String message_id;
    private String user_name;
    private String user_img_link;
    private String message_content;
    private String message_date;
    private String post_id;
    private int message_type;
    private String friend_id;
    private boolean message_status;

    public MessageItem() {
    }

    public MessageItem(String message_id, String user_name, String user_img_link,
                       String message_content, String message_date,
                       String post_id, int message_type, String friend_id, boolean message_status) {
        this.message_id = message_id;
        this.user_name = user_name;
        this.user_img_link = user_img_link;
        this.message_content = message_content;
        this.message_date = message_date;
        this.post_id = post_id;
        this.message_type = message_type;
        this.friend_id = friend_id;
        this.message_status = message_status;
    }

    public String getMessage_id() {
        return message_id;
    }

    public void setMessage_id(String message_id) {
        this.message_id = message_id;
    }

    public String getUser_name() {
        return user_name;
    }

    public void setUser_name(String user_name) {
        this.user_name = user_name;
    }

    public String getUser_img_link() {
        return user_img_link;
    }

    public void setUser_img_link(String user_img_link) {
        this.user_img_link = user_img_link;
    }

    public String getMessage_content() {
        return message_content;
    }

    public void setMessage_content(String message_content) {
        this.message_content = message_content;
    }

    public String getMessage_date() {
        return message_date;
    }

    public void setMessage_date(String message_date) {
        this.message_date = message_date;
    }

    public String getPost_id() {
        return post_id;
    }

    public void setPost_id(String post_id) {
        this.post_id = post_id;
    }

    public int getMessage_type() {
        return message_type;
    }

    public void setMessage_type(int message_type) {
        this.message_type = message_type;
    }

    public String getFriend_id() {
        return friend_id;
    }

    public void setFriend_id(String friend_id) {
        this.friend_id = friend_id;
    }

    public boolean isMessage_status() {
        return message_status;
    }

    public void setMessage_status(boolean message_status) {
        this.message_status = message_status;
    }
}
