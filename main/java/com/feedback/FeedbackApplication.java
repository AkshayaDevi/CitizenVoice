package com.feedback;

import android.app.Application;
import android.content.Context;



public class FeedbackApplication extends Application {
    private static FeedbackApplication mInstance;

    public static Context getStaticContext() {
        return FeedbackApplication.getInstance().getApplicationContext();
    }

    public static FeedbackApplication getInstance() {
        return mInstance;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        mInstance = this;
    }
}
