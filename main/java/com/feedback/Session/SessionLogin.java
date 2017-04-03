package com.feedback.Session;

import android.content.Context;
import android.content.SharedPreferences;

import com.feedback.FeedbackApplication;
import com.feedback.Model.UserResponse;
import com.google.gson.Gson;


public class SessionLogin {
    private static SharedPreferences pref = FeedbackApplication.getInstance().getSharedPreferences("SessionLogin", Context.MODE_PRIVATE);


    public static void saveLoginSession() {
        pref.edit().putBoolean("isValid", true).commit();
    }

    public static void clearLoginSession() {
        pref.edit().clear().commit();
    }

    public static boolean getLoginSession() {
        return pref.getBoolean("isValid", false);
    }

    public static void saveMobile(String mobile) {
        pref.edit().putString("mobile", mobile).commit();
    }

    public static String getmobile() {
        return pref.getString("mobile", "");
    }

    public static void saveUserId(String id) {
        pref.edit().putString("userid", id).commit();
    }

    public static String getUserId() {
        return pref.getString("userid", "");
    }

    public static void saveUsertype(String type) {
        pref.edit().putString("Usertype", type).commit();
    }

    public static String getUsertype() {
        return pref.getString("Usertype", "");
    }

    public static void saveUser(UserResponse userResponse) {
        SessionLogin.saveMobile(userResponse.getMobile());
        SessionLogin.saveUsertype(userResponse.getUser_type());
        Gson gson = new Gson();
        String res = gson.toJson(userResponse);
        pref.edit().putString("userResponse", res).commit();
    }

    public static UserResponse getUser() {
        String res = pref.getString("userResponse", "");
        Gson gson = new Gson();
        UserResponse myClass = gson.fromJson(res, UserResponse.class);
        return myClass;
    }

}
