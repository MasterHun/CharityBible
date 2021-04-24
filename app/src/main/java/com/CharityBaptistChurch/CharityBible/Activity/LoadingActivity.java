package com.CharityBaptistChurch.CharityBible.Activity;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;

import com.CharityBaptistChurch.CharityBible.R;

public class LoadingActivity extends Activity {
    @Override
    protected void onCreate( Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_loading);

        StartLoading();
    }

    void StartLoading()
    {
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                finish();
            }
        },2000);

    }

}
