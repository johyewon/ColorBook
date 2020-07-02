package com.hanix.colorbook.task.firebase;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.PowerManager;

import com.firebase.jobdispatcher.JobParameters;
import com.firebase.jobdispatcher.JobService;
import com.hanix.colorbook.common.app.GLog;


public class MyJobService extends JobService {

    @Override
    public boolean onStartJob(JobParameters job) {

        GLog.d("My JobService service is started");

        PowerManager pm = (PowerManager) getSystemService(Context.POWER_SERVICE);
        if(pm != null) {
            @SuppressLint("InvalidWakeLockTag")
            PowerManager.WakeLock wakeLock = pm.newWakeLock(PowerManager.PARTIAL_WAKE_LOCK | PowerManager.ACQUIRE_CAUSES_WAKEUP | PowerManager.ON_AFTER_RELEASE, "NativeBaseProject");
            wakeLock.acquire(3000);
        }

        return false;
    }

    @Override
    public boolean onStopJob(JobParameters job) {return false;}
}
