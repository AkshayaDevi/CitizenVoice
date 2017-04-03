package com.feedback.Activities;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.bumptech.glide.Glide;
import com.feedback.Model.FeedbackResponse;
import com.feedback.Presenter.FeedbackPresenter;
import com.feedback.R;
import com.feedback.Session.SessionLogin;
import com.feedback.Utils.Utils;

import java.io.File;

import butterknife.BindView;
import butterknife.OnClick;
import pl.aprilapps.easyphotopicker.DefaultCallback;
import pl.aprilapps.easyphotopicker.EasyImage;
import pl.tajchert.nammu.Nammu;
import pl.tajchert.nammu.PermissionCallback;

import static com.feedback.Utils.Constants_app.dateTimeFormat;

public class ActivityNewPost extends BaseActivity implements FeedbackPresenter.FeedbackPostListener {

    @BindView(R.id.tv_category)
    TextView tvPostCategory;
    @BindView(R.id.et_desc)
    EditText etDesc;
    @BindView(R.id.iv_post)
    ImageView ivPost;

    private String category = "", desc = "", image = "";
    int imageSourceOption = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_post);
        init();
    }

    private void init() {
        setTitle("New post");
        setBackVisibility(View.VISIBLE);
    }

    @OnClick(R.id.btn_post)
    void post() {
        if (validation()) {
            utils.showProgress();
            FeedbackResponse feedbackResponse = new FeedbackResponse();
            feedbackResponse.setDescription(desc);
            feedbackResponse.setCategory(category);
            feedbackResponse.setImage(image);
            feedbackResponse.setPosted_by(SessionLogin.getUserId());
            feedbackResponse.setPosted_date(Utils.getCurrentDateTime(dateTimeFormat));
            FeedbackPresenter feedbackPresenter = new FeedbackPresenter(this, null,mActivity);
            feedbackPresenter.post(feedbackResponse);
        }
    }

    @OnClick(R.id.iv_post)
    void pickImageOption() {
        final String[] list = {"Gallery", "Camera"};

        new MaterialDialog.Builder(this)
                .title("Image from")
                .items(list)
                .itemsCallbackSingleChoice(-1, new MaterialDialog.ListCallbackSingleChoice() {
                    @Override
                    public boolean onSelection(MaterialDialog dialog, View view, int which, CharSequence text) {
                        imageSourceOption = which;
                        Log.e("imageSourceOption",""+imageSourceOption);
                        if (imageSourceOption == 0)
                            selectImageFromDeviceGallery();
                        else
                            pickImageFromCamera();

                        return true;
                    }
                })
                .positiveText("Ok")
                .show();
    }

    @OnClick(R.id.tv_category)
    void onUserSelect() {
        final String[] userTypeList = getResources().getStringArray(R.array.post_category);
        new MaterialDialog.Builder(this)
                .title("Select category")
                .items(userTypeList)
                .itemsCallbackSingleChoice(-1, new MaterialDialog.ListCallbackSingleChoice() {
                    @Override
                    public boolean onSelection(MaterialDialog dialog, View view, int which, CharSequence text) {
                        category = userTypeList[which];
                        tvPostCategory.setText(category);
                        return true;
                    }
                })
                .positiveText("Ok")
                .show();
    }

    private boolean validation() {
        boolean flag = false;
        desc=etDesc.getText().toString().trim();

        if (category.length() == 0)
            Utils.alertBox(mActivity, "Select category!");
        else if (image.length() == 0)
            Utils.alertBox(mActivity, "Choose image related to your issue!");
        else if (desc.length() == 0)
            Utils.alertBox(mActivity, "Description is empty!");
        else
            flag = true;
        return flag;
    }

    private void pickImageFromCamera() {
        try {
            EasyImage.openCamera(this, 0);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        Nammu.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    private void selectImageFromDeviceGallery() {
        try {
            int permissionCheck = ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE);
            if (permissionCheck == PackageManager.PERMISSION_GRANTED) {
                EasyImage.openGallery(mActivity, 0);
            } else {
                getWritePermissionForGallery();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void getWritePermissionForGallery() {
        Nammu.askForPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE, new PermissionCallback() {
            @Override
            public void permissionGranted() {
                EasyImage.openGallery(mActivity, 0);
            }

            @Override
            public void permissionRefused() {
            }
        });
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        EasyImage.handleActivityResult(requestCode, resultCode, data, this, new DefaultCallback() {
            @Override
            public void onImagePickerError(Exception e, EasyImage.ImageSource source, int type) {
                e.printStackTrace();
            }

            @Override
            public void onImagePicked(File imageFile, EasyImage.ImageSource source, int type) {
                setImage(imageFile);
            }


            @Override
            public void onCanceled(EasyImage.ImageSource source, int type) {
                if (source == EasyImage.ImageSource.CAMERA) {
                    File photoFile = EasyImage.lastlyTakenButCanceledPhoto(mActivity);
                    if (photoFile != null) photoFile.delete();
                }
            }
        });

        super.onActivityResult(requestCode, resultCode, data);

    }

    private void setImage(File imageFile) {
        Glide.with(mActivity).load(imageFile).placeholder(R.drawable.ic_place_holder).into(ivPost);
        File file=new File(Utils.compressImage(imageFile));
        image = Utils.fileToBase64(file);
    }

    @Override
    protected void onDestroy() {
        EasyImage.clearConfiguration(this);
        super.onDestroy();
    }

    @Override
    public void onPostSuccess() {
        utils.hideProgress();
        showSuccessDialog();
    }

    private void showSuccessDialog() {
        new MaterialDialog.Builder(mActivity)
                .content("Posted successfully!")
                .backgroundColorRes(R.color.white)
                .contentColorRes(R.color.black)
                .positiveColorRes(R.color.colorPrimaryDark)
                .positiveText("OK")
                .onPositive(new MaterialDialog.SingleButtonCallback() {
                    @Override
                    public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                        setResult(RESULT_OK);
                        finish();
                    }

                })
                .show();
    }

    @Override
    public void onPostFailure(String msg) {
        utils.hideProgress();
        Utils.alertBox(mActivity, msg);
    }
}
