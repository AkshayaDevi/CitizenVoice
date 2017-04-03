package com.feedback.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TextInputLayout;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.afollestad.materialdialogs.MaterialDialog;
import com.feedback.Model.UserResponse;
import com.feedback.Presenter.UserPresenter;
import com.feedback.R;
import com.feedback.Session.SessionLogin;
import com.feedback.Utils.Utils;

import butterknife.BindView;
import butterknife.OnClick;
import butterknife.OnTextChanged;

public class ActivityLogin extends BaseActivity implements UserPresenter.LoginListener {

    @BindView(R.id.til_mobile)
    TextInputLayout tilMobile;
    @BindView(R.id.til_pwd)
    TextInputLayout tilPwd;
    @BindView(R.id.et_mobile)
    EditText etMobile;
    @BindView(R.id.et_pwd)
    EditText etPwd;
    @BindView(R.id.tv_user_type)
    TextView tvUserType;

    private String mobile = "", pwd = "", userType = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        init();
    }

    private void init() {
        setTitle("Login");
    }

    @OnClick(R.id.btn_login)
    void login() {
        if (validation()) {
            utils.showProgress();
            UserResponse req = new UserResponse();
            req.setMobile(mobile);
            req.setPassword(pwd);
            UserPresenter userPresenter = new UserPresenter(this, null,null, mActivity);
            userPresenter.login(req);
        }
    }

    @OnTextChanged(R.id.et_mobile)
    void mobileOnTextChanged() {
        mobile = etMobile.getText().toString().trim();
        if (mobile.length() == 10)
            tilMobile.setError(null);
    }

    @OnClick(R.id.tv_user_type)
    void onUserSelect() {
        final String[] userTypeList = getResources().getStringArray(R.array.user_types);
        new MaterialDialog.Builder(this)
                .title("User type")
                .items(userTypeList)
                .itemsCallbackSingleChoice(-1, new MaterialDialog.ListCallbackSingleChoice() {
                    @Override
                    public boolean onSelection(MaterialDialog dialog, View view, int which, CharSequence text) {
                        tvUserType.setText(userTypeList[which]);
                        int pos = which++;
                        userType = "" + pos;
                        return true;
                    }
                })
                .positiveText("Ok")
                .show();
    }


    @OnTextChanged(R.id.et_pwd)
    void onPwdChanging() {
        pwd = etPwd.getText().toString().trim();
        if (pwd.length() > 0)
            tilPwd.setError(null);
    }

    private boolean validation() {
        boolean flag = false;
        mobile = etMobile.getText().toString().trim();
        pwd = etPwd.getText().toString().trim();

        if (mobile.length() == 0)
            tilMobile.setError("Mobile is empty!");
        else if (mobile.length() < 10)
            tilMobile.setError("Invalid mobile!");
        else if (pwd.length() == 0)
            tilPwd.setError("Password is empty!");
        else
            flag = true;

        return flag;
    }

    private boolean validationPassword() {
        boolean flag = false;
        mobile = etMobile.getText().toString().trim();

        if (mobile.length() == 0)
            tilMobile.setError("Mobile is empty!");
        else if (mobile.length() < 10)
            tilMobile.setError("Invalid mobile!");
        else
            flag = true;

        return flag;
    }

    @OnClick(R.id.tv_new_user)
    void registerScreen() {
        Intent i = new Intent(mActivity, ActivityRegister.class);
        startActivity(i);
    }

    @OnClick(R.id.tv_forget_pwd)
    void forgetPassword() {
        if (validationPassword()) {
            utils.showProgress();
            UserResponse req = new UserResponse();
            req.setMobile(mobile);
            UserPresenter userPresenter = new UserPresenter(this, null,null, mActivity);
            userPresenter.forgetPassword(req);
        }
    }


    @Override
    public void loginSuccess() {
        utils.hideProgress();
        Intent intent = new Intent(mActivity, ActivityMain.class);
        finish();
        SessionLogin.saveLoginSession();
        startActivity(intent);
    }

    @Override
    public void loginFailure(String msg) {
        utils.hideProgress();
        Utils.alertBox(mActivity, msg);
    }

    @Override
    public void passwordSent() {
        utils.hideProgress();
        Utils.alertBox(mActivity, "Password sent to your register mail!");
    }

    @Override
    public void passwordSentFailure(String msg) {
        utils.hideProgress();
        Utils.alertBox(mActivity, msg);
    }
}
