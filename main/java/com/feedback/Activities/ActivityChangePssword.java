package com.feedback.Activities;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.TextInputLayout;
import android.view.View;
import android.widget.EditText;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.feedback.Model.UserResponse;
import com.feedback.Presenter.UserPresenter;
import com.feedback.R;
import com.feedback.Session.SessionLogin;
import com.feedback.Utils.Utils;

import butterknife.BindView;
import butterknife.OnClick;
import butterknife.OnTextChanged;

public class ActivityChangePssword extends BaseActivity implements UserPresenter.PasswordChangeListener {

    @BindView(R.id.til_pwd_old)
    TextInputLayout tilOldPwd;
    @BindView(R.id.til_pwd)
    TextInputLayout tilPwd;
    @BindView(R.id.til_pwd_again)
    TextInputLayout tilPwdAgain;

    @BindView(R.id.et_pwd_old)
    EditText etOldPwd;
    @BindView(R.id.et_pwd)
    EditText etPwd;
    @BindView(R.id.et_pwd_again)
    EditText etPwdAgain;

    private String oldPwd = "", pwd = "";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_password);
        init();
    }

    private void init() {
        setTitle("Change password");
        setBackVisibility(View.VISIBLE);
    }

    @OnClick(R.id.btn_change_password)
    void change() {
        if (validation()) {
            utils.showProgress();
            UserResponse req = new UserResponse();
            req.setPassword(oldPwd);
            req.setNew_password(pwd);
            req.setMobile(SessionLogin.getmobile());

            UserPresenter userPresenter = new UserPresenter(null, null, this, mActivity);
            userPresenter.changePassword(req);
        }
    }


    @OnTextChanged(R.id.et_pwd_again)
    void onPwdChanging() {
        String pwdAgain = etPwdAgain.getText().toString().trim();
        if (pwdAgain.length() > 0 && pwd.equals(pwdAgain))
            tilPwdAgain.setError(null);
    }

    @OnTextChanged(R.id.et_pwd)
    void onPwdChangingAgain() {
        pwd = etPwd.getText().toString().trim();
        if (pwd.length() > 0)
            tilPwd.setError(null);
        String pwdAgain = etPwdAgain.getText().toString().trim();
        if (pwdAgain.length() > 0 && pwd.equals(pwdAgain))
            tilPwdAgain.setError(null);
    }

    @OnTextChanged(R.id.et_pwd_old)
    void onOldPwdChanging() {
        oldPwd = etOldPwd.getText().toString().trim();
        if (oldPwd.length() > 0)
            tilOldPwd.setError(null);
    }

    private boolean validation() {
        boolean flag = false;

        String pwdAgain = etPwdAgain.getText().toString().trim();
        if (etOldPwd.length() == 0)
            tilOldPwd.setError("Old password is empty!");
        else if (pwd.length() == 0)
            tilPwd.setError("New password is empty!");
        else if (pwdAgain.length() == 0)
            tilPwdAgain.setError("New Password again is empty!");
        else if (!pwd.equals(pwdAgain))
            tilPwdAgain.setError("Password doesn't match");
        else
            flag = true;

        return flag;
    }


    private void showSuccessDialog() {
        new MaterialDialog.Builder(mActivity)
                .content("Password changed successfully!")
                .backgroundColorRes(R.color.white)
                .contentColorRes(R.color.black)
                .positiveColorRes(R.color.colorPrimaryDark)
                .positiveText("OK")
                .onPositive(new MaterialDialog.SingleButtonCallback() {
                    @Override
                    public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                        clear();
                        finish();
                    }

                })
                .show();
    }

    private void clear() {
        etOldPwd.setText("");
        etPwd.setText("");
        etPwdAgain.setText("");
    }

    @Override
    public void passwordChanged() {
        utils.hideProgress();
        showSuccessDialog();
    }

    @Override
    public void passwordChangedFailure(String msg) {
        utils.hideProgress();
        Utils.alertBox(mActivity, msg);
    }
}
