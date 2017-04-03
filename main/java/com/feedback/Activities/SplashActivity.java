package com.feedback.Activities;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

import com.feedback.R;
import com.feedback.Session.SessionLogin;

public class SplashActivity extends Activity {
    private Activity mActivity;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        initUI();
    }

    private void initUI() {
        try {
            mActivity = SplashActivity.this;
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    moveToNextScreen();
                }
            }, 5 * 1000); // wait for 5 seconds
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void moveToNextScreen() {
        try {
            Intent i = new Intent(mActivity, ActivityLogin.class);
            if (SessionLogin.getLoginSession()) {
                i = new Intent(mActivity, ActivityMain.class);
            }
            finish();
            startActivity(i);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
