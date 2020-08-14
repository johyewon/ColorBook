package com.hanix.colorbook.common.app;

import android.app.Activity;

import androidx.multidex.MultiDexApplication;

import java.io.File;

/*
공유 application
 */
public class ColorBookApplication extends MultiDexApplication {

    private static ColorBookApplication instance;
    public static ColorBookApplication getInstance() { return instance; }
    //로그 표시를 위한 디버그 모드인지를 판별한다.
    public boolean isDebuggable = false;
    //앱 Crash 에러 로그가 저장된 경로(cache 디렉토리)
    public String logFilePathAppCrashError = "";
    //GLog(전체로그) 로그가 저장되는 파일이름(cache 디렉토리)
    public String logFileFileNameGLog = "";
    //로그에서 사용되는 패키지명
    public String logForPkgName = "";

    @Override
    public void onCreate() {
        super.onCreate();
        ColorBookApplication.instance = this;
        isDebuggable = GLog.isDebuggable(this);

        //앱 Crash 에러 로그가 저장된 경로(cache 디렉토리)
        logFilePathAppCrashError = new File(this.getCacheDir(), "crash_log.txt").getAbsolutePath();
        //GLog(전체로그) 로그가 저장되는 파일이름(cache 디렉토리)
        logFileFileNameGLog = new File(this.getCacheDir(), "payme_glog.txt").getAbsolutePath();


        logForPkgName = this.getPackageName();
    }
}
