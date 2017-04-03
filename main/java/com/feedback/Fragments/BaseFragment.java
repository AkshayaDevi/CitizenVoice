package com.feedback.Fragments;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.View;
import android.view.inputmethod.InputMethodManager;

import com.feedback.Utils.Constants_app;
import com.feedback.Utils.Utils;

import butterknife.ButterKnife;
import pl.aprilapps.easyphotopicker.EasyImage;
import pl.tajchert.nammu.Nammu;



public class BaseFragment extends Fragment {

    public Activity mActivity;
    Utils utils;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mActivity=getActivity();
        Nammu.init(mActivity);
        EasyImage.configuration(mActivity)
                .setImagesFolderName(Constants_app.folderName)
                .saveInRootPicturesDirectory().saveInAppExternalFilesDir();

        utils=new Utils(mActivity);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ButterKnife.bind(this, view);
        final InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }

}
