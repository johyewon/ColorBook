package com.hanix.colorbook.views;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.hanix.colorbook.R;
import com.hanix.colorbook.task.common.VersionCheckTask;


public class MainActivity extends AppCompatActivity {

    VersionCheckTask versionCheckTask;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        versionCheckTask = new VersionCheckTask(getApplicationContext(), MainActivity.this);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        versionCheckTask.updateResult(resultCode, requestCode);
    }
}