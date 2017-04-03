package com.feedback.Activities;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.LayoutRes;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.feedback.R;
import com.feedback.Utils.Constants_app;
import com.feedback.Utils.Utils;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import pl.aprilapps.easyphotopicker.EasyImage;
import pl.tajchert.nammu.Nammu;


public class BaseActivity extends AppCompatActivity {
    @Nullable
    @BindView(R.id.tv_title)
    TextView tvTitle;
    @Nullable
    @BindView(R.id.rl_back)
    RelativeLayout rlBack;
    Activity mActivity;

    Utils utils;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mActivity=BaseActivity.this;
        Nammu.init(this);
        EasyImage.configuration(this)
                .setImagesFolderName(Constants_app.folderName)
                .saveInRootPicturesDirectory().saveInAppExternalFilesDir();
        utils=new Utils(mActivity);
    }

    @Override
    public void setContentView(@LayoutRes int layoutResID) {
        super.setContentView(layoutResID);
        ButterKnife.bind(this);
    }

    @Nullable
    @OnClick(R.id.iv_back)
    void backByImage() {
        finish();
    }
    @Nullable
    @OnClick(R.id.rl_back)
    void backByRelativeLayout() {
        finish();
    }

    public void setBackVisibility(int value) {
        rlBack.setVisibility(value);
    }

    public void setTitle(String title) {
        tvTitle.setText(title);
    }

}
