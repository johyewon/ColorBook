package com.hanix.colorbook.common.utils;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;

import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;

public class PrefUtil {

    private static final String PREF_NAME = "Pref";
    private static final String KEY_FCM_TOKEN_ID = "KEY_FCM_TOKEN_ID";

    private static List<String> paletteColor = new ArrayList<>();

    private static SharedPreferences sf;
    private static PrefUtil instance ;

    private PrefUtil() {}

    public static PrefUtil getInstance(Context context) {

        if (sf == null) {
            sf = context.getSharedPreferences(PREF_NAME, Activity.MODE_PRIVATE);
        }

        if (instance == null) {
            instance = new PrefUtil();
        }
        return instance;
    }

    public static void setBoolean(Context context, String key, boolean value) {
        SharedPreferences pref = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = pref.edit();
        editor.putBoolean(key, value);
        editor.apply();
    }

    public static boolean getBoolean(Context context, String key) {
        SharedPreferences pref = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        return pref.getBoolean(key, false);
    }

    public static void setString(Context context, String key, String value) {
        SharedPreferences pref = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = pref.edit();
        editor.putString(key, value);
        editor.apply();
    }

    public static String getString(Context context, String key) {
        SharedPreferences pref = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        return pref.getString(key, "");
    }

    public static void setInt(Context context, String key, int value) {
        SharedPreferences pref = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = pref.edit();
        editor.putInt(key, value);
        editor.apply();
    }

    public static int getInt(Context context, String key) {
        SharedPreferences pref = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        return pref.getInt(key, 0);
    }


    /*
    ColorBook
     */
    public static void addColor(Context context, String value) {
        SharedPreferences pref = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        Editor editor = pref.edit();
        paletteColor = getColor(context);
        paletteColor.add(value);
        editor.putStringSet("colorPalette", new HashSet<>(paletteColor));
        editor.apply();
    }

    public static List<String> getColor(Context context) {
        SharedPreferences pref = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        return new ArrayList<>(Objects.requireNonNull(pref.getStringSet("colorPalette", new HashSet<>())));
    }

    public static void resetColor(Context context) {
        SharedPreferences pref = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        Editor editor = pref.edit();
        paletteColor = getColor(context);
        paletteColor.clear();
        editor.putStringSet("colorPalette", new HashSet<>(paletteColor));
        editor.apply();
    }






    /** FCM Token Id 값 설정 */
    public void setFcmTokenId(String tokenId) {
        SharedPreferences.Editor editor = sf.edit();
        editor.putString(KEY_FCM_TOKEN_ID, tokenId);
        editor.apply();
    }

    /** FCM Token Id 값을 취득한다. */
    public String getFcmTokenId() {
        return sf.getString(KEY_FCM_TOKEN_ID, "");
    }

}
