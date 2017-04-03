package com.feedback.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.TextInputLayout;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.feedback.Model.UserResponse;
import com.feedback.Presenter.UserPresenter;
import com.feedback.R;
import com.feedback.Utils.Utils;

import butterknife.BindView;
import butterknife.OnClick;
import butterknife.OnTextChanged;

public class ActivityRegister extends BaseActivity implements UserPresenter.RegisterListener {
    @BindView(R.id.til_fname)
    TextInputLayout tilFname;
    @BindView(R.id.til_lname)
    TextInputLayout tilLName;
    @BindView(R.id.til_mobile)
    TextInputLayout tilMobile;
    @BindView(R.id.til_email)
    TextInputLayout tilEmail;
    @BindView(R.id.til_pwd)
    TextInputLayout tilPwd;
    @BindView(R.id.til_pwd_again)
    TextInputLayout tilPwdAgain;
    @BindView(R.id.til_aadhar)
    TextInputLayout tilAadhar;


    @BindView(R.id.et_fname)
    EditText etFname;
    @BindView(R.id.et_lname)
    EditText etLname;
    @BindView(R.id.et_mobile)
    EditText etMobile;
    @BindView(R.id.et_email)
    EditText etEmail;
    @BindView(R.id.et_pwd)
    EditText etPwd;
    @BindView(R.id.et_pwd_again)
    EditText etPwdAgain;
    @BindView(R.id.et_aadhar)
    EditText etAadhar;
    @BindView(R.id.tv_user_type)
    TextView tvUserType;
    @BindView(R.id.tv_dep)
    TextView tvDep;


    private String fName = "", lName = "", mobile = "", pwd = "", email = "", userType = "", department = "",aadhar="";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        init();
    }

    private void init() {
        setTitle("Register");
    }

    @OnClick(R.id.btn_register)
    void register() {
        if (validation()) {
            utils.showProgress();
            UserResponse req = new UserResponse();
            req.setMobile(mobile);
            req.setPassword(pwd);
            req.setFirstname(fName);
            req.setLasttname(lName);
            req.setUser_type(userType);
            req.setEmail(email);
            req.setAadhar(aadhar);
            if(userType.equals("2"))
                req.setDep(department);

            UserPresenter userPresenter = new UserPresenter(null, this, null, mActivity);
            userPresenter.register(req);
        }
    }

    @OnTextChanged(R.id.et_aadhar)
    void onChangeAadhar() {
        aadhar = etAadhar.getText().toString().trim();
        if (aadhar.length() > 0)
            tilAadhar.setError(null);
    }

    @OnTextChanged(R.id.et_fname)
    void onChangeFName() {
        fName = etFname.getText().toString().trim();
        if (fName.length() > 0)
            tilFname.setError(null);
    }

    @OnTextChanged(R.id.et_lname)
    void onChangeLName() {
        lName = etLname.getText().toString().trim();
        if (lName.length() > 0)
            tilLName.setError(null);
    }

    @OnTextChanged(R.id.et_email)
    void emailOnChanged() {
        email = etEmail.getText().toString().trim();
        if (email.length() > 0 && Utils.checkEmail(email))
            tilEmail.setError(null);
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
        final String[] userTypeListID = getResources().getStringArray(R.array.user_type_ids);

        new MaterialDialog.Builder(this)
                .title("User type")
                .items(userTypeList)
                .itemsCallbackSingleChoice(-1, new MaterialDialog.ListCallbackSingleChoice() {
                    @Override
                    public boolean onSelection(MaterialDialog dialog, View view, int which, CharSequence text) {
                        tvUserType.setText(userTypeList[which]);
                        userType = userTypeListID[which];
                        if (userType.equals("2")) {
                            tvDep.setVisibility(View.VISIBLE);
                        } else {
                            tvDep.setVisibility(View.GONE);
                        }
                        return true;
                    }
                })
                .positiveText("Ok")
                .show();
    }

    @OnClick(R.id.tv_dep)
    void onDepSelect() {
        final String[] dep = getResources().getStringArray(R.array.post_category);
        new MaterialDialog.Builder(this)
                .title("Department")
                .items(dep)
                .itemsCallbackSingleChoice(-1, new MaterialDialog.ListCallbackSingleChoice() {
                    @Override
                    public boolean onSelection(MaterialDialog dialog, View view, int which, CharSequence text) {
                        tvDep.setText(dep[which]);
                        department =dep[which];
                        return true;
                    }
                })
                .positiveText("Ok")
                .show();
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
    }

    private boolean validation() {
        boolean flag = false;

        String pwdAgain = etPwdAgain.getText().toString().trim();
        if (fName.length() == 0)
            tilFname.setError("First name empty!");
        else if (lName.length() == 0)
            tilLName.setError("Last name empty!");
        else if (email.length() == 0)
            tilEmail.setError("Email empty!");
        else if (!Utils.checkEmail(email))
            tilEmail.setError("Invalid email!");
        else if (mobile.length() == 0)
            tilMobile.setError("Mobile is empty!");
        else if (mobile.length() < 10)
            tilMobile.setError("Invalid mobile!");
        else if (aadhar.length() == 0)
            tilAadhar.setError("Aadhar is empty!");
        else if (aadhar.length() < 12)
            tilAadhar.setError("Invalid Aadhar!");
        else if (pwd.length() == 0)
            tilPwd.setError("Password is empty!");
        else if (pwdAgain.length() == 0)
            tilPwdAgain.setError("Password again is empty!");
        else if (!pwd.equals(pwdAgain))
            tilPwdAgain.setError("Password doesn't match");
        else if (userType.length() == 0)
            Utils.alertBox(mActivity, "Select user type!");
        else if (userType.equals("2") && department.length() == 0)
            Utils.alertBox(mActivity, "Select department!");
        else
            flag = true;

        return flag;
    }


    @Override
    public void registerSuccess() {
        utils.hideProgress();
        showSuccessDialog();
    }

    private void showSuccessDialog() {
        new MaterialDialog.Builder(mActivity)
                .content("Registration success.! login to proceed..!")
                .backgroundColorRes(R.color.white)
                .contentColorRes(R.color.black)
                .positiveColorRes(R.color.colorPrimaryDark)
                .positiveText("OK")
                .onPositive(new MaterialDialog.SingleButtonCallback() {
                    @Override
                    public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                        Intent intent = new Intent(mActivity, ActivityLogin.class);
                        finish();
                        startActivity(intent);
                    }
                })
                .show();
    }

    @Override
    public void registerFailure(String msg) {
        utils.hideProgress();
        Utils.alertBox(mActivity, msg);
    }
}
