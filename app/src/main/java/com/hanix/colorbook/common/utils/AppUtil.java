package com.hanix.colorbook.common.utils;

import android.app.Activity;
import android.app.ActivityManager;
import android.content.Context;
import android.content.pm.PackageManager;
import android.text.TextUtils;

import com.hanix.colorbook.common.app.GLog;
import com.hanix.colorbook.common.constants.URLApi;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;
import org.w3c.dom.Text;

import java.io.IOException;
import java.util.List;
import java.util.Objects;

public class AppUtil {

    /** 앱이 실행 중인지 확인 **/
    public static boolean isAppRunning(Context context) {
        ActivityManager activityManager = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        List<ActivityManager.RunningAppProcessInfo> procInfos = activityManager.getRunningAppProcesses();
        for(int i = 0; i < procInfos.size(); i ++) {
            if(procInfos.get(i).processName.equals(context.getPackageName())) {
                return true;
            }
        }
        return false;
    }

    /** 확인하고 싶은 서비스가 실행중인지 확인 **/
    public static boolean isServiceRunning(Context context, Class<?> serviceClass) {
        ActivityManager activityManager = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        for(ActivityManager.RunningServiceInfo service : activityManager.getRunningServices(Integer.MAX_VALUE)) {
            if(serviceClass.getName().equals(service.service.getClassName())) {
                return true;
            }
        }
        return false;
    }

    public static String getAppVersion(Activity activity) {

        try {
            return activity.getPackageManager().getPackageInfo(activity.getPackageName(), 0).versionName;
        } catch (PackageManager.NameNotFoundException ignored) {}

        return null;
    }

    public static String getMarketVersion(Activity activity) {
        try {
            Document doc = Jsoup.connect(URLApi.getStoreUrl(activity)).get();
            Elements currentVersionDiv = doc.select(".BgcNfc");
            Elements currentVersion = doc.select("div.hAyfc div span.htlgb");
            for(int i =0; i < currentVersionDiv.size(); i++) {
                if(currentVersionDiv.get(i).text().equals("Current Version"))
                    return currentVersion.get(i).text();
            }
        } catch (IOException e) {
            e.printStackTrace();
            GLog.e("문서 오류 " + e.getMessage());
        }
        return null;
    }

    public static boolean  isVersionEquals (Activity activity) {
        return getMarketVersion(activity) != null && !Objects.requireNonNull(getMarketVersion(activity)).contains("기기에 따라 다릅니다") && TextUtils.equals(getAppVersion(activity), getMarketVersion(activity));
    }

}
