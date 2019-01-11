package com.example.dell.mytour.model;

public class ItemFriend {
    private String friend_id;
    private String full_name;
    private String avatar;
    private String address;
    private String friend_key;


    public ItemFriend() {
    }

    public ItemFriend(String full_name, String avatar, String address, String friend_id, String friend_key) {
        this.full_name = full_name;
        this.avatar = avatar;
        this.address = address;
        this.friend_id = friend_id;
        this.friend_key = friend_key;
    }

    public String getFull_name() {
        return full_name;
    }

    public void setFull_name(String full_name) {
        this.full_name = full_name;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getFriend_id() {
        return friend_id;
    }

    public void setFriend_id(String friend_id) {
        this.friend_id = friend_id;
    }

    public String getFriend_key() {
        return friend_key;
    }

    public void setFriend_key(String friend_key) {
        this.friend_key = friend_key;
    }
}
