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



public class UserPresenter {
    private final Context context;
    private final LoginListener mListener;
    private final RegisterListener registerListener;
    private final PasswordChangeListener passwordListener;
    private final LoginService loginService;

    public interface PasswordChangeListener {
        void passwordChanged();
        void passwordChangedFailure(String msg);
    }

    public interface LoginListener {
        void loginSuccess();
        void loginFailure(String msg);
        void passwordSent();
        void passwordSentFailure(String msg);
    }

    public interface RegisterListener {
        void registerSuccess();

        void registerFailure(String msg);
    }

    public UserPresenter(LoginListener listener, RegisterListener registerListener, PasswordChangeListener passwordListener, Context context) {
        this.mListener = listener;
        this.registerListener = registerListener;
        this.context = context;
        this.loginService = new LoginService();
        this.passwordListener = passwordListener;
    }


    public void login(UserResponse req) {
        System.out.println(req);

        loginService
                .login()
                .loginAPI(req.getMobile(), req.getPassword())
                .enqueue(new Callback<UserBaseResponse>() {
                    @Override
                    public void onResponse(Call<UserBaseResponse> call, Response<UserBaseResponse> response) {
                        UserBaseResponse result = response.body();
                        System.out.println(result);
                        if (result != null) {
                            if (result.getStatus().equals("1")) {
                                SessionLogin.saveUserId(result.getUserResponse().getId());
                                SessionLogin.saveUser(result.getUserResponse());
                                mListener.loginSuccess();
                            } else {
                                mListener.loginFailure(result.getMessage());
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

    public void register(UserResponse req) {
        System.out.println(req);

        loginService
                .register()
                .registerAPI(req.getMobile(), req.getPassword(), req.getFirstname(),
                        req.getLasttname(), req.getEmail(),
                        req.getUser_type(),req.getDep(),
                        req.getAadhar())
                .enqueue(new Callback<BaseResponse>() {
                    @Override
                    public void onResponse(Call<BaseResponse> call, Response<BaseResponse> response) {
                        BaseResponse result = response.body();
                        System.out.println(result);
                        if (result != null) {
                            if (result.getStatus().equals("1")) {
                                registerListener.registerSuccess();
                            } else {
                                registerListener.registerFailure(result.getMessage());
                            }
                        }
                    }

                    @Override
                    public void onFailure(Call<BaseResponse> call, Throwable t) {
                        try {
                            Log.e("onFailure", "onFailure");
                            throw new InterruptedException(t.getMessage().toString());
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                });
    }


    public void forgetPassword(UserResponse req) {
        System.out.println(req);

        loginService
                .forgetPassword()
                .PasswordAPI(req.getMobile())
                .enqueue(new Callback<BaseResponse>() {
                    @Override
                    public void onResponse(Call<BaseResponse> call, Response<BaseResponse> response) {
                        BaseResponse result = response.body();
                        System.out.println(result);
                        if (result != null) {
                            if (result.getStatus().equals("1")) {
                                mListener.passwordSent();
                            } else {
                                mListener.passwordSentFailure(result.getMessage());
                            }
                        }
                    }

                    @Override
                    public void onFailure(Call<BaseResponse> call, Throwable t) {
                        try {
                            Log.e("onFailure", "onFailure");
                            throw new InterruptedException(t.getMessage().toString());
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                });
    }


    public void changePassword(UserResponse req) {
        System.out.println(req);

        loginService
                .changePassword()
                .changePasswordAPI(req.getMobile(), req.getPassword(), req.getNew_password())
                .enqueue(new Callback<BaseResponse>() {
                    @Override
                    public void onResponse(Call<BaseResponse> call, Response<BaseResponse> response) {
                        BaseResponse result = response.body();
                        System.out.println(result);
                        if (result != null) {
                            if (result.getStatus().equals("1")) {
                                passwordListener.passwordChanged();
                            } else {
                                passwordListener.passwordChangedFailure(result.getMessage());
                            }
                        }
                    }

                    @Override
                    public void onFailure(Call<BaseResponse> call, Throwable t) {
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