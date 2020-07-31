package com.hanix.colorbook.task.common;

import android.app.Activity;
import android.content.Context;
import android.content.IntentSender;

import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.android.play.core.appupdate.AppUpdateInfo;
import com.google.android.play.core.appupdate.AppUpdateManager;
import com.google.android.play.core.appupdate.AppUpdateManagerFactory;
import com.google.android.play.core.install.InstallState;
import com.google.android.play.core.install.InstallStateUpdatedListener;
import com.google.android.play.core.install.model.ActivityResult;
import com.google.android.play.core.install.model.AppUpdateType;
import com.google.android.play.core.install.model.InstallStatus;
import com.google.android.play.core.install.model.UpdateAvailability;
import com.google.android.play.core.tasks.Task;
import com.hanix.colorbook.common.app.GLog;

public class VersionCheckTask {

    public static final int MY_REQUEST_CODE = 111;
    AppUpdateManager appUpdateManager;
    Task<AppUpdateInfo> appUpdateInfoTask;

    Context context;
    Activity activity;

    public VersionCheckTask(Context context, Activity activity) {
        this.activity = activity;
        this.context = context;

        appUpdateManager = AppUpdateManagerFactory.create(context);
        appUpdateManager.registerListener(installListener);

        appUpdateInfoTask = appUpdateManager.getAppUpdateInfo();
        appUpdateInfoTask.addOnSuccessListener(appUpdateInfo -> {
            if(appUpdateInfo.updateAvailability() == UpdateAvailability.UPDATE_AVAILABLE
            && appUpdateInfo.isUpdateTypeAllowed(AppUpdateType.FLEXIBLE)) {
                startUpdate(appUpdateInfo);
            }else if(appUpdateInfo.installStatus() == InstallStatus.DOWNLOADED) {
                showMessage();
            } else {
                GLog.e("check For App Update Availability something else");
            }
        });
    }

    InstallStateUpdatedListener installListener = new InstallStateUpdatedListener() {
        @Override
        public void onStateUpdate(InstallState state) {
            if (state.installStatus() == InstallStatus.DOWNLOADED) {
                showMessage();
            } else if (state.installStatus() == InstallStatus.INSTALLED) {
                if (appUpdateManager != null)
                    appUpdateManager.unregisterListener(installListener);
            } else {
                GLog.i("install State Update Listener state : " + state.installStatus());
            }
        }
    };

    private void showMessage() {
        new MaterialAlertDialogBuilder(context)
                .setTitle("새로운 업데이트가 기다리고 있습니다!")
                .setMessage("업데이트 되는 동안은 앱이 종료되지 않습니다. 업데이트 하시겠습니까?")
                .setNegativeButton("나중에 하기", (dialogInterface, i) -> dialogInterface.dismiss())
                .setPositiveButton("업데이트", (dialogInterface, i) -> appUpdateManager.completeUpdate())
                .show();
    }

    private void startUpdate(AppUpdateInfo appUpdateInfo) {
        try {
            if(appUpdateManager != null && appUpdateInfoTask != null) {
                appUpdateManager.startUpdateFlowForResult(
                        appUpdateInfo
                        , AppUpdateType.FLEXIBLE
                        , activity
                        , MY_REQUEST_CODE
                );
            }
        } catch (IntentSender.SendIntentException ignored){}
    }

    public void updateResult(int resultCode, int requestCode) {
        if(requestCode == MY_REQUEST_CODE && resultCode != ActivityResult.RESULT_IN_APP_UPDATE_FAILED)
            GLog.e("RESULT_IN_APP_UPDATE_FAILED : " + resultCode);
    }

}