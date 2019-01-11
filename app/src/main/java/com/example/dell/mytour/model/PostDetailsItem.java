package com.example.dell.mytour.model;

public class PostDetailsItem {
    private String post_content;
    private String post_image_link;
    private String post_image_description;

    public PostDetailsItem() {
    }

    public PostDetailsItem(String post_content, String post_image_link, String post_image_description) {
        this.post_content = post_content;
        this.post_image_link = post_image_link;
        this.post_image_description = post_image_description;
    }

    public String getPost_content() {
        return post_content;
    }

    public void setPost_content(String post_content) {
        this.post_content = post_content;
    }

    public String getPost_image_link() {
        return post_image_link;
    }

    public void setPost_image_link(String post_image_link) {
        this.post_image_link = post_image_link;
    }

    public String getPost_image_description() {
        return post_image_description;
    }

    public void setPost_image_description(String post_image_description) {
        this.post_image_description = post_image_description;
    }
}
