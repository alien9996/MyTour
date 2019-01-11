package com.example.dell.mytour.model;

import com.example.dell.mytour.model.model_base.Comment;
import com.example.dell.mytour.model.model_base.Users;

public class PostComment {

    Users users;
    Comment comment;

    public PostComment() {
    }

    public PostComment(Users users, Comment comment) {
        this.users = users;
        this.comment = comment;
    }

    public Users getUsers() {
        return users;
    }

    public void setUsers(Users users) {
        this.users = users;
    }

    public Comment getComment() {
        return comment;
    }

    public void setComment(Comment comment) {
        this.comment = comment;
    }
}
