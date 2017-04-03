package com.feedback.Activities;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.bumptech.glide.Glide;
import com.feedback.Fragments.FragmentHome;
import com.feedback.Model.FeedbackResponse;
import com.feedback.Presenter.FeedbackPresenter;
import com.feedback.R;
import com.feedback.Service.FeedbackService;
import com.feedback.Session.SessionLogin;
import com.feedback.Utils.Constants_app;
import com.feedback.Utils.Utils;

import org.w3c.dom.Text;

import java.io.File;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;
import pl.aprilapps.easyphotopicker.DefaultCallback;
import pl.aprilapps.easyphotopicker.EasyImage;
import pl.tajchert.nammu.Nammu;
import pl.tajchert.nammu.PermissionCallback;

import static com.feedback.Utils.Constants_app.dateTimeFormat;

public class ActivityPostDetails extends BaseActivity implements FeedbackPresenter.FeedbackGetListener {

    @BindView(R.id.tv_desc)
    TextView tvDesc;
    @BindView(R.id.iv_post)
    ImageView ivPost;

    @BindView(R.id.btn_reject)
    Button btnReject;
    @BindView(R.id.btn_take_action)
    Button btnAction;

private int id;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.feedback_details);
        init();
    }

    private void init() {
        setTitle("Details");
        setBackVisibility(View.VISIBLE);
        FeedbackResponse feedbackResponse = (FeedbackResponse) getIntent().getSerializableExtra("FeedbackResponse");
        String desc = feedbackResponse.getDescription();
        String image = FeedbackService.BASE_URL_IMAGE + feedbackResponse.getImage();
        tvDesc.setText(desc);
        id=feedbackResponse.getPost_id();
        Glide.with(mActivity).load(image).placeholder(R.drawable.ic_place_holder).into(ivPost);

        Log.e("action by","-->"+feedbackResponse.getAction_by());


        if (SessionLogin.getUsertype().equals("1")) {
            btnReject.setVisibility(View.GONE);
            btnAction.setVisibility(View.GONE);
        } else {
            if (feedbackResponse.getAction_by() == null) {
                btnReject.setVisibility(View.VISIBLE);
                btnAction.setVisibility(View.VISIBLE);
            } else {
                btnReject.setVisibility(View.GONE);
                btnAction.setVisibility(View.GONE);
            }
        }

    }

    @OnClick(R.id.btn_take_action)
     void actionClick() {
        utils.showProgress();
        FeedbackResponse feedbackResponse = new FeedbackResponse();
        feedbackResponse.setAction_by(SessionLogin.getUserId());
        feedbackResponse.setAction_date(Utils.getCurrentDateTime(Constants_app.dateTimeFormat));
        feedbackResponse.setRejected_by("0");
        feedbackResponse.setPost_id(id);
        FeedbackPresenter feedbackPresenter = new FeedbackPresenter(null, this, mActivity);
        feedbackPresenter.action(feedbackResponse);
    }

    @OnClick(R.id.btn_reject)
    public void rejectedClick() {
        utils.showProgress();
        FeedbackResponse feedbackResponse = new FeedbackResponse();
        feedbackResponse.setAction_by("0");
        feedbackResponse.setAction_date(Utils.getCurrentDateTime(Constants_app.dateTimeFormat));
        feedbackResponse.setRejected_by(SessionLogin.getUserId());
        feedbackResponse.setPost_id(id);
        FeedbackPresenter feedbackPresenter = new FeedbackPresenter(null, this, mActivity);
        feedbackPresenter.action(feedbackResponse);
    }

    @Override
    public void onGetPosts(List<FeedbackResponse> list) {

    }

    @Override
    public void onGetFailure(String msg) {
        utils.hideProgress();
        Utils.alertBox(mActivity, msg);
    }

    @Override
    public void onActionTakenSuccess() {
        utils.hideProgress();
        showSuccess();
    }

    private void showSuccess() {
        new MaterialDialog.Builder(mActivity)
                .content("Success!")
                .backgroundColorRes(R.color.white)
                .contentColorRes(R.color.black)
                .positiveColorRes(R.color.colorPrimaryDark)
                .positiveText("OK")
                .onPositive(new MaterialDialog.SingleButtonCallback() {
                    @Override
                    public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                        Intent intent=new Intent();
                        intent.putExtra("from","activitypostdetails");
                        setResult(RESULT_OK,intent);
                        finish();
                    }
                })
                .show();
    }

}
