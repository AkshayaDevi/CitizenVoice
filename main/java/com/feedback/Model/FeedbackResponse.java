package com.feedback.Model;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;



public class FeedbackResponse extends BaseResponse implements Serializable {

    @SerializedName("post_id")
    int post_id;
    @SerializedName("description")
    String description;
    @SerializedName("category")
    String category;
    @SerializedName("posted_date")
    String posted_date;
    @SerializedName("posted_by")
    String posted_by;
    @SerializedName("image")
    String image;
    @SerializedName("action_by")
    String action_by;
    @SerializedName("action_date")
    String action_date;
    @SerializedName("rejected_by")
    String rejected_by;


    public int getPost_id() {
        return post_id;
    }

    public void setPost_id(int post_id) {
        this.post_id = post_id;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getPosted_date() {
        return posted_date;
    }

    public void setPosted_date(String posted_date) {
        this.posted_date = posted_date;
    }

    public String getPosted_by() {
        return posted_by;
    }

    public void setPosted_by(String posted_by) {
        this.posted_by = posted_by;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getAction_by() {
        return action_by;
    }

    public void setAction_by(String action_by) {
        this.action_by = action_by;
    }

    public String getAction_date() {
        return action_date;
    }

    public void setAction_date(String action_date) {
        this.action_date = action_date;
    }

    public String getRejected_by() {
        return rejected_by;
    }

    public void setRejected_by(String rejected_by) {
        this.rejected_by = rejected_by;
    }

    @Override
    public String toString() {
        return "feedback{" +
                "post_id=" + post_id +
                ", description='" + description + '\'' +
                ", category='" + category + '\'' +
                ", posted_date='" + posted_date + '\'' +
                ", posted_by='" + posted_by + '\'' +
                ", image='" + image + '\'' +
                ", action_by='" + action_by + '\'' +
                ", action_date='" + action_date + '\'' +
                ", rejected_by='" + rejected_by + '\'' +
                '}';
    }
}
