package com.feedback.Presenter;

import android.content.Context;
import android.util.Log;

import com.feedback.Model.BaseResponse;
import com.feedback.Model.FeedbackResponse;
import com.feedback.Model.FeedbackResponseList;
import com.feedback.Service.FeedbackService;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class FeedbackPresenter {
    private final Context context;
    private final FeedbackPostListener feedbackPostListener;
    private final FeedbackService feedbackService;
    private final FeedbackGetListener feedbackGetListener;


    public interface FeedbackPostListener {
        void onPostSuccess();
        void onPostFailure(String msg);
    }

    public interface FeedbackGetListener {
        void onGetPosts(List<FeedbackResponse> list);
        void onGetFailure(String msg);
        void onActionTakenSuccess();
    }

    public FeedbackPresenter(FeedbackPostListener feedbackPostListener,FeedbackGetListener feedbackGetListener, Context context) {
        this.feedbackPostListener = feedbackPostListener;
        this.feedbackGetListener=feedbackGetListener;
        this.context = context;
        feedbackService=new FeedbackService();
    }


    public void post(FeedbackResponse req) {
        System.out.println(req);

        feedbackService
                .post()
                .postAPI(req.getDescription(),req.getCategory(),req.getPosted_by(),req.getImage(),req.getPosted_date())
                .enqueue(new Callback<FeedbackResponse>() {
                    @Override
                    public void onResponse(Call<FeedbackResponse> call, Response<FeedbackResponse> response) {
                        FeedbackResponse result = response.body();
                        System.out.println(result);
                        if (result != null) {
                            if (result.getStatus().equals("1")) {
                                feedbackPostListener.onPostSuccess();
                            } else {
                                feedbackPostListener.onPostFailure(result.getMessage());
                            }
                        }
                    }

                    @Override
                    public void onFailure(Call<FeedbackResponse> call, Throwable t) {
                        try {
                            Log.e("onFailure", "onFailure");
                            throw new InterruptedException(t.getMessage().toString());
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                });
    }

    public void get(FeedbackResponse req) {
        System.out.println(req);

        feedbackService
                .get()
                .getAPI(req.getPosted_by())
                .enqueue(new Callback<FeedbackResponseList>() {
                    @Override
                    public void onResponse(Call<FeedbackResponseList> call, Response<FeedbackResponseList> response) {
                        FeedbackResponseList result = response.body();
                        System.out.println(result);
                        if (result != null) {
                            if (result.getStatus().equals("1")) {
                                feedbackGetListener.onGetPosts(result.getFeedbackResponseList());
                            } else {
                                feedbackGetListener.onGetFailure(result.getMessage());
                            }
                        }
                    }

                    @Override
                    public void onFailure(Call<FeedbackResponseList> call, Throwable t) {
                        try {
                            Log.e("onFailure", "onFailure");
                            throw new InterruptedException(t.getMessage().toString());
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                });
    }

    public void action(FeedbackResponse req) {
        System.out.println(req);

        feedbackService
                .action()
                .actionAPI(req.getAction_by(),req.getAction_date(),req.getRejected_by(),""+req.getPost_id())
                .enqueue(new Callback<BaseResponse>() {
                    @Override
                    public void onResponse(Call<BaseResponse> call, Response<BaseResponse> response) {
                        BaseResponse result = response.body();
                        System.out.println(result);
                        if (result != null) {
                            if (result.getStatus().equals("1")) {
                                feedbackGetListener.onActionTakenSuccess();
                            } else {
                                feedbackGetListener.onGetFailure(result.getMessage());
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