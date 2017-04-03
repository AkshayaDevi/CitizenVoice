package com.feedback.Service;

import com.feedback.Model.BaseResponse;
import com.feedback.Model.FeedbackResponse;
import com.feedback.Model.FeedbackResponseList;

import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;




public class FeedbackService {
    private static String BASE_URL = "http://pics-institute.com/";
    public static String BASE_URL_IMAGE = "http://pics-institute.com/feedback/";


    public interface PostAPI {
        @FormUrlEncoded
        @POST("feedback/post.php")
        Call<FeedbackResponse> postAPI(@Field("description") String desc,
                                       @Field("category") String category,
                                       @Field("posted_by") String posted_by,






                                       @Field("image") String image,
                                       @Field("posted_date") String posted_date);
    }

    public PostAPI post() {

        Retrofit retrofit = new Retrofit
                .Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        return retrofit.create(PostAPI.class);
    }

    public interface GetAPI {
        @FormUrlEncoded
        @POST("feedback/get_posts.php")
        Call<FeedbackResponseList> getAPI(@Field("posted_by") String posted_by);
    }

    public GetAPI get() {
        Retrofit retrofit = new Retrofit
                .Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        return retrofit.create(GetAPI.class);
    }


    public interface ActionAPI {
        @FormUrlEncoded
        @POST("feedback/post_action.php")
        Call<BaseResponse> actionAPI(@Field("action_by") String action_by,
                                     @Field("action_date") String action_date,
                                     @Field("rejected_by") String rejected_by,
                                     @Field("post_id") String post_id
                                       );
    }

    public ActionAPI action() {
        Retrofit retrofit = new Retrofit
                .Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        return retrofit.create(ActionAPI.class);
    }


}
