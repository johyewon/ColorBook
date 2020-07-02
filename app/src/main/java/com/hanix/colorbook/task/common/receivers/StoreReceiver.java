package com.hanix.colorbook.task.common.receivers;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

public class StoreReceiver extends BroadcastReceiver {

    static String returnReceiver = "";

    @Override
    public void onReceive(Context context, Intent intent) {
        String referrer = intent.getStringExtra("referrer");
        if(referrer != null && referrer.length() >0) {
            returnReceiver = referrer;
        }
    }

    public static String getReferrer() { return returnReceiver; }
}
