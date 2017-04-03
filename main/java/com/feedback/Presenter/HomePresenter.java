package com.feedback.Presenter;

import android.content.Context;
import android.util.Log;

import com.feedback.Model.BaseResponse;
import com.feedback.Model.UserBaseResponse;
import com.feedback.Model.UserResponse;
import com.feedback.Service.LoginService;
import com.feedback.Session.SessionLogin;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class HomePresenter {
    private final Context context;
    private final ProfilePicChangeListener mListener;
    private final LoginService loginService;

    public interface ProfilePicChangeListener {
        void profilePicChanged();
        void failure(String msg);
    }


    public HomePresenter(ProfilePicChangeListener listener, Context context) {
        this.mListener = listener;
        this.context = context;
        this.loginService = new LoginService();
    }


    public void change(UserResponse req) {
        System.out.println(req);

        loginService
                .changeProfilePic()
                .changeProfilePicAPI(req.getMobile(), req.getProfile_picture())
                .enqueue(new Callback<UserBaseResponse>() {
                    @Override
                    public void onResponse(Call<UserBaseResponse> call, Response<UserBaseResponse> response) {
                        UserBaseResponse result = response.body();
                        System.out.println(result);
                        if (result != null) {
                            if (result.getStatus().equals("1")) {
                                SessionLogin.saveUserId(result.getUserResponse().getId());
                                SessionLogin.saveUser(result.getUserResponse());
                                mListener.profilePicChanged();
                            } else {
                                mListener.failure(result.getMessage());
                            }
                        }
                    }

                    @Override
                    public void onFailure(Call<UserBaseResponse> call, Throwable t) {
                        try {
                            Log.e("onFailure", "onFailure");
                            throw new InterruptedException(t.getMessage().toString());
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                });
    }



}