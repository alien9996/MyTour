package com.example.dell.mytour.model;

import com.example.dell.mytour.model.model_base.Follows;
import com.example.dell.mytour.model.model_base.Users;

public class FollowUser {

    private Users users;
    private Follows follows;

    public FollowUser() {
    }

    public FollowUser(Users users, Follows follows) {
        this.users = users;
        this.follows = follows;
    }

    public Users getUsers() {
        return users;
    }

    public void setUsers(Users users) {
        this.users = users;
    }

    public Follows getFollows() {
        return follows;
    }

    public void setFollows(Follows follows) {
        this.follows = follows;
    }
}
