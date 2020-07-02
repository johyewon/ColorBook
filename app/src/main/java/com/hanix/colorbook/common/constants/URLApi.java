package com.hanix.colorbook.common.constants;


import com.hanix.colorbook.common.app.ColorBookApplication;

public class URLApi {

    private static final String URL_DEV_SERVER = "";
    private static final String URL_REAL_SERVER = "";

    private static final String KEY_URL = "keyUrl";
    private static final String KYE_QUERY = "keyQuery";

    // url 취득 (서버 url)
    public static String getServerUrl() {
        if(ColorBookApplication.getInstance().isDebuggable) {
            return URL_DEV_SERVER;
        }
        return URL_REAL_SERVER;
    }

}