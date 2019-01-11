package com.example.dell.mytour.model.model_base;

public class Answer {

    private String ans_id;
    private String ans_content;
    private String ans_date;
    private boolean ans_status;
    private String friend_id;
    private String cmm_id;

    public Answer() {
    }

    public Answer(String ans_id, String ans_content, String ans_date, boolean ans_status, String friend_id, String cmm_id) {
        this.ans_id = ans_id;
        this.ans_content = ans_content;
        this.ans_date = ans_date;
        this.ans_status = ans_status;
        this.friend_id = friend_id;
        this.cmm_id = cmm_id;
    }

    public String getCmm_id() {
        return cmm_id;
    }

    public void setCmm_id(String cmm_id) {
        this.cmm_id = cmm_id;
    }

    public String getAns_id() {
        return ans_id;
    }

    public void setAns_id(String ans_id) {
        this.ans_id = ans_id;
    }

    public String getAns_content() {
        return ans_content;
    }

    public void setAns_content(String ans_content) {
        this.ans_content = ans_content;
    }

    public String getAns_date() {
        return ans_date;
    }

    public void setAns_date(String ans_date) {
        this.ans_date = ans_date;
    }

    public boolean isAns_status() {
        return ans_status;
    }

    public void setAns_status(boolean ans_status) {
        this.ans_status = ans_status;
    }

    public String getFriend_id() {
        return friend_id;
    }

    public void setFriend_id(String friend_id) {
        this.friend_id = friend_id;
    }
}
