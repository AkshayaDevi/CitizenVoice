package com.feedback.Model;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;



public class UserBaseResponse extends BaseResponse implements Serializable {

    @SerializedName("data")
    UserResponse userResponse;

    public UserResponse getUserResponse() {
        return userResponse;
    }

    public void setUserResponse(UserResponse userResponse) {
        this.userResponse = userResponse;
    }
}
