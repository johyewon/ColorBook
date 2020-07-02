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

    // 디버그모드 확인
    public boolean isDebuggable = false;

    //앱 crash 에러 로그가 저장된 경로 ( cache 디렉토리 )
    public String logFilePathAppCrashError = "";

    // GLog ( 전체 로그 ) 가 저장되는 파일 이름
    public String logFileFileNameGLog = "";

    // 로그에서 사용되는 패키지 명
    public String logForPkgName = "";

    @Override
    public void onCreate() {
        super.onCreate();
        ColorBookApplication.instance = this;
        isDebuggable = GLog.isDebuggable(this);

        logFilePathAppCrashError = new File(this.getCacheDir(), "crash_log.txt").getAbsolutePath();
        logFileFileNameGLog = new File (this.getCacheDir(), "payme_glog.txt").getAbsolutePath();

        logForPkgName = this.getPackageName();

    }

    public void finishApp(Activity activity) {
        if(activity.isTaskRoot()) {
            android.os.Process.killProcess(android.os.Process.myPid());
            System.exit(0);
        } else {
            activity.finishAffinity();
        }
    }
}
