package com.feedback.Service;

import android.widget.Toast;

import com.feedback.Model.BaseResponse;
import com.feedback.Model.UserBaseResponse;

import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;



public class LoginService {
    private static String BASE_URL = "http://pics-institute.com/";

    public interface LoginAPI {
        @FormUrlEncoded
        @POST("feedback/login.php")
        Call<UserBaseResponse> loginAPI(@Field("mobile") String mobile,
                                        @Field("password") String password);


    }

    public LoginAPI login() {
        Retrofit retrofit = new Retrofit
                .Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        return retrofit.create(LoginAPI.class);
    }


    public interface RegisterAPI {
        @FormUrlEncoded
        @POST("feedback/register.php")
        Call<BaseResponse> registerAPI( @Field("mobile") String mobile,
                                     @Field("password") String password,
                                     @Field("firstname") String firstname,
                                     @Field("lastname") String lastname,
                                     @Field("email") String email,
                                     @Field("user_type") String user_type,
                                        @Field("dep") String dep,
                                        @Field("aadhar") String aadharr);
    }

    public RegisterAPI register() {
        Retrofit retrofit = new Retrofit
                .Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        return retrofit.create(RegisterAPI.class);
    }

    public interface forgetPasswordAPI {
        @FormUrlEncoded
        @POST("feedback/forget_password.php")
        Call<BaseResponse> PasswordAPI( @Field("mobile") String mobile
                                     );
    }

    public forgetPasswordAPI forgetPassword() {
        Retrofit retrofit = new Retrofit
                .Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        return retrofit.create(forgetPasswordAPI.class);
    }


    public interface changePasswordAPI {
        @FormUrlEncoded
        @POST("feedback/change_password.php")
        Call<BaseResponse> changePasswordAPI( @Field("mobile") String mobile,
                                              @Field("password") String password,
                                              @Field("new_password") String new_password
        );
    }

    public changePasswordAPI changePassword() {
        Retrofit retrofit = new Retrofit
                .Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        return retrofit.create(changePasswordAPI.class);
    }


    public interface changeProfilePicAPI {
        @FormUrlEncoded
        @POST("feedback/profile_picture.php")
        Call<UserBaseResponse> changeProfilePicAPI( @Field("mobile") String mobile,
                                              @Field("image") String password
        );
    }

    public changeProfilePicAPI changeProfilePic() {
        Retrofit retrofit = new Retrofit
                .Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        return retrofit.create(changeProfilePicAPI.class);
    }


}
