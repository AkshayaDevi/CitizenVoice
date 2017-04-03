package com.feedback.Model;

import com.google.gson.annotations.SerializedName;



public class UserResponse extends BaseResponse {
    @SerializedName("firstname")
    String firstname;
    @SerializedName("lastname")
    String lasttname;
    @SerializedName("mobile")
    String mobile;
    @SerializedName("email")
    String email;
    @SerializedName("password")
    String password;
    @SerializedName("user_type")
    String user_type;
    @SerializedName("new_password")
    String new_password;
    @SerializedName("id")
    String id;
    @SerializedName("profile_picture")
    String profile_picture;
    @SerializedName("dep")
    String dep;
    @SerializedName("aadhar")
    String aadhar;

    public String getAadhar() {
        return aadhar;
    }

    public void setAadhar(String aadhar) {
        this.aadhar = aadhar;
    }

    public String getDep() {
        return dep;
    }

    public void setDep(String dep) {
        this.dep = dep;
    }

    public String getProfile_picture() {
        return profile_picture;
    }

    public void setProfile_picture(String profile_picture) {
        this.profile_picture = profile_picture;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getNew_password() {
        return new_password;
    }

    public void setNew_password(String new_password) {
        this.new_password = new_password;
    }

    public String getFirstname() {
        return firstname;
    }

    public void setFirstname(String firstname) {
        this.firstname = firstname;
    }

    public String getLasttname() {
        return lasttname;
    }

    public void setLasttname(String lasttname) {
        this.lasttname = lasttname;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getUser_type() {
        return user_type;
    }

    public void setUser_type(String user_type) {
        this.user_type = user_type;
    }

    @Override
    public String toString() {
        return "UserResponse{" +
                "firstname='" + firstname + '\'' +
                ", lasttname='" + lasttname + '\'' +
                ", mobile='" + mobile + '\'' +
                ", email='" + email + '\'' +
                ", password='" + password + '\'' +
                ", user_type='" + user_type + '\'' +
                ", new_password='" + new_password + '\'' +
                ", id='" + id + '\'' +
                ", profile_picture='" + profile_picture + '\'' +
                ", dep='" + dep + '\'' +
                ", aadhar='" + aadhar + '\'' +
                '}';
    }
}
