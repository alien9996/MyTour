package com.example.dell.mytour.model;

import com.example.dell.mytour.model.model_base.Comment;
import com.example.dell.mytour.model.model_base.Friend;
import com.example.dell.mytour.model.model_base.Image;
import com.example.dell.mytour.model.model_base.Like;
import com.example.dell.mytour.model.model_base.Post;
import com.example.dell.mytour.model.model_base.Users;

import java.util.ArrayList;

public class PersonalPost {
    private Users users;
    private Friend friend;
    private Like like;
    private Post post;
    private Comment comment;
    private ArrayList<Image> images;

    public PersonalPost() {
    }

    public PersonalPost(Users users, Post post) {
        this.users = users;
        this.post = post;
    }

    public PersonalPost(Users users, Comment comment) {

        this.users = users;
        this.comment = comment;
    }


    public PersonalPost(Users users, Like like, Post post, Comment comment, ArrayList<Image> images) {
        this.users = users;
        this.like = like;
        this.post = post;
        this.comment = comment;
        this.images = images;
    }

    public ArrayList<Image> getImages() {
        return images;
    }

    public void setImages(ArrayList<Image> images) {
        this.images = images;
    }

    public Users getUsers() {
        return users;
    }

    public void setUsers(Users users) {
        this.users = users;
    }

    public Friend getFriend() {
        return friend;
    }

    public void setFriend(Friend friend) {
        this.friend = friend;
    }

    public Like getLike() {
        return like;
    }

    public void setLike(Like like) {
        this.like = like;
    }

    public Comment getComment() {
        return comment;
    }

    public void setComment(Comment comment) {
        this.comment = comment;
    }

    public Post getPost() {
        return post;
    }

    public void setPost(Post post) {
        this.post = post;
    }
}
