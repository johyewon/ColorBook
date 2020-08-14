package com.hanix.colorbook.common.constants;


import android.app.Activity;

import com.hanix.colorbook.common.app.ColorBookApplication;

public class URLApi {

    private static final String URL_DEV_SERVER = "";
    private static final String URL_REAL_SERVER = "";

    // url 취득 (서버 url)
    public static String getServerUrl() {
        if(ColorBookApplication.getInstance().isDebuggable) {
            return URL_DEV_SERVER;
        }
        return URL_REAL_SERVER;
    }

    public static String getStoreUrl(Activity activity) {
        return AppConstants.STORE_URL_GOOGLE+activity.getPackageName();
    }

}
