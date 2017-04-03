package com.feedback.Model;

import com.google.gson.annotations.SerializedName;

import java.util.List;


public class FeedbackResponseList extends BaseResponse {

    @SerializedName("data")
    List<FeedbackResponse> feedbackResponseList;

    public List<FeedbackResponse> getFeedbackResponseList() {
        return feedbackResponseList;
    }

    public void setFeedbackResponseList(List<FeedbackResponse> feedbackResponseList) {
        this.feedbackResponseList = feedbackResponseList;
    }
}
