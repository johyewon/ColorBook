package com.hanix.colorbook.views;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.hanix.colorbook.R;
import com.hanix.colorbook.common.constants.AppConstants;

public class AppIntro extends AppCompatActivity {

    SharedPreferences sf;

    private static final long SPLASH_TIME = 2000;
    private static final int STOP_SPLASH = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_app_intro);
        getIntent().addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);

        sf = getSharedPreferences("Pref", MODE_PRIVATE);
    }

    @SuppressLint("HandlerLeak")
    private Handler handler = new Handler() {
        @Override
        public void handleMessage(@NonNull Message msg) {
            super.handleMessage(msg);

            Intent intent;
            if (msg.what == STOP_SPLASH) {
                intent = new Intent(AppIntro.this, MainActivity.class);
                startActivity(intent);
                overridePendingTransition(R.anim.fade_in_activity, R.anim.fade_out_activity);
                finish();
            }
        }
    };

    @Override
    protected void onResume() {
        super.onResume();

        Message msg = new Message();
        msg.what = STOP_SPLASH;
        handler.sendMessageDelayed(msg, SPLASH_TIME);
    }
}
