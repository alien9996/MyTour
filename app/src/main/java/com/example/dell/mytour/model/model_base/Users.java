package com.example.dell.mytour.model.model_base;

import com.google.firebase.database.Exclude;

import java.util.HashMap;
import java.util.Map;

public class Users {
    private String user_id;
    private String user_name;
    private String user_password;
    private String user_full_name;
    private String user_email;
    private String user_birthday;
    private String user_address;
    private String user_phone;
    private String user_description;
    private String user_avatar;
    private int user_level;
    private boolean user_status;

    public Users() {
    }

    public Users(String user_id, String user_name) {
        this.user_id = user_id;
        this.user_name = user_name;
    }

    public Users(String user_id, String user_name, String user_avatar) {
        this.user_id = user_id;
        this.user_name = user_name;
        this.user_avatar = user_avatar;
    }

    public Users(String user_id, String user_name, String user_password, String user_first_name, String user_email, String user_birthday, String user_address, String user_phone, String user_description, String user_avatar, int user_level, boolean user_status) {
        this.user_id = user_id;
        this.user_name = user_name;
        this.user_password = user_password;
        this.user_full_name = user_first_name;
        this.user_email = user_email;
        this.user_birthday = user_birthday;
        this.user_address = user_address;
        this.user_phone = user_phone;
        this.user_description = user_description;
        this.user_avatar = user_avatar;
        this.user_level = user_level;
        this.user_status = user_status;
    }

    public String getUser_id() {
        return user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }

    public String getUser_name() {
        return user_name;
    }

    public void setUser_name(String user_name) {
        this.user_name = user_name;
    }

    public String getUser_password() {
        return user_password;
    }

    public void setUser_password(String user_password) {
        this.user_password = user_password;
    }

    public String getUser_full_name() {
        return user_full_name;
    }

    public void setUser_full_name(String user_full_name) {
        this.user_full_name = user_full_name;
    }


    public String getUser_email() {
        return user_email;
    }

    public void setUser_email(String user_email) {
        this.user_email = user_email;
    }

    public String getUser_birthday() {
        return user_birthday;
    }

    public void setUser_birthday(String user_birthday) {
        this.user_birthday = user_birthday;
    }

    public String getUser_address() {
        return user_address;
    }

    public void setUser_address(String user_address) {
        this.user_address = user_address;
    }

    public String getUser_phone() {
        return user_phone;
    }

    public void setUser_phone(String user_phone) {
        this.user_phone = user_phone;
    }

    public String getUser_description() {
        return user_description;
    }

    public void setUser_description(String user_description) {
        this.user_description = user_description;
    }

    public String getUser_avatar() {
        return user_avatar;
    }

    public void setUser_avatar(String user_avatar) {
        this.user_avatar = user_avatar;
    }

    public int getUser_level() {
        return user_level;
    }

    public void setUser_level(int user_level) {
        this.user_level = user_level;
    }

    public boolean isUser_status() {
        return user_status;
    }

    public void setUser_status(boolean user_status) {
        this.user_status = user_status;
    }

    @Exclude
    public Map<String, Object> toMap(){
        HashMap<String, Object> result = new HashMap<>();
        result.put("user_id",user_id);
        result.put("user_name",user_name);
        result.put("user_password",user_password);
        result.put("user_full_name",user_full_name);
        result.put("user_email",user_email);
        result.put("user_birthday",user_birthday);
        result.put("user_address",user_address);
        result.put("user_phone",user_phone);
        result.put("user_description",user_description);
        result.put("user_avatar",user_avatar);
        result.put("user_level",user_level);
        result.put("user_status",user_status);

        return result;
    }
}
