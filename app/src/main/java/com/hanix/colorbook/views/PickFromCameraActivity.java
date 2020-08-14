package com.hanix.colorbook.views;

import android.Manifest;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.FileProvider;

import com.gun0912.tedpermission.PermissionListener;
import com.gun0912.tedpermission.TedPermission;
import com.hanix.colorbook.R;
import com.hanix.colorbook.common.app.GLog;
import com.hanix.colorbook.common.utils.ToastUtil;
import com.hanix.colorbook.views.event.OnSingleClickListener;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class PickFromCameraActivity extends AppCompatActivity {

    TextView cameraPick, cameraCode;
    ImageView cameraPicture, cameraMore;
    Button cameraPickPicture;

    private Uri imgUri;
    private String mCurrentPhotoPath;
    private static final int FROM_CAMERA = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pick_from_camera);

        permissionCheck();

        cameraCode = findViewById(R.id.cameraCode);
        cameraMore = findViewById(R.id.cameraMore);
        cameraPick = findViewById(R.id.cameraPick);
        cameraPickPicture = findViewById(R.id.cameraPickPicture);
        cameraPicture = findViewById(R.id.cameraPicture);

        cameraMore.setOnClickListener(cameraClick);
        cameraPickPicture.setOnClickListener(cameraClick);

    }

    OnSingleClickListener cameraClick = new OnSingleClickListener() {
        @Override
        public void onSingleClick(View v) {
            switch (v.getId()) {
                case R.id.cameraMore:
                    showPopupMenu(v);
                    break;

                case R.id.cameraPickPicture:
                    takePhoto();
                    break;
            }
        }
    };

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == FROM_CAMERA && resultCode == RESULT_OK) {
            try {
                galleryAddPic();
                cameraPicture.setImageURI(imgUri);
            } catch (Exception e) {
                GLog.e(e.getMessage(), e);
            }
        }

    }

    private void showPopupMenu(View v) {
        PopupMenu popup = new PopupMenu(getApplicationContext(), v);
        getMenuInflater().inflate(R.menu.menu_choose, popup.getMenu());
        popup.getMenu().findItem(R.id.camera).setVisible(false);
        popup.setOnMenuItemClickListener(menuClick);
        popup.show();
    }

    PopupMenu.OnMenuItemClickListener menuClick = (menuItem) -> {
        switch (menuItem.getItemId()) {
            case R.id.main:
                goOtherPage(MainActivity.class);
                break;

            case R.id.gallery:
                goOtherPage(PickFromGalleryActivity.class);
                break;
        }
        return false;
    };

    private void goOtherPage(Class<?> activityClass) {
        startActivity(new Intent(PickFromCameraActivity.this, activityClass));
        finish();
    }

    // 카메라 시작
    private void takePhoto() {
        String state = Environment.getExternalStorageState();

        if (Environment.MEDIA_MOUNTED.equals(state)) {
            Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            if (intent.resolveActivity(getPackageManager()) != null) {
                File photoFile = createImageFile();
                Uri providerURI = FileProvider.getUriForFile(PickFromCameraActivity.this, getPackageName(), photoFile);
                imgUri = providerURI;
                intent.putExtra(MediaStore.EXTRA_OUTPUT, providerURI);
                startActivityForResult(intent, FROM_CAMERA);
            }
        } else {
            GLog.d("저장 공간에 접근 불가능");
        }
    }

    private File createImageFile() {
        String imgFileName = System.currentTimeMillis() + ".jpg";
        File imageFile;
        File storageDir = new File(Environment.getExternalStorageDirectory()+"/Pictures", "colorbook");

        if (!storageDir.exists()) {
            storageDir.mkdirs();
        }

        imageFile = new File(storageDir, imgFileName);
        mCurrentPhotoPath = imageFile.getAbsolutePath();

        return imageFile;
    }

    private void galleryAddPic() {
        Intent mediaScanIntent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
        File f = new File(mCurrentPhotoPath);
        Uri contentUri = Uri.fromFile(f);
        mediaScanIntent.setData(contentUri);
        sendBroadcast(mediaScanIntent);
        ToastUtil.showToastL(PickFromCameraActivity.this, "사진이 저장되었습니다.");
    }
    // 카메라 끝

    // 권한 체크
    private void permissionCheck() {
        PermissionListener listener = new PermissionListener() {
            @Override
            public void onPermissionGranted() {

            }

            @Override
            public void onPermissionDenied(List<String> deniedPermissions) {

            }
        };

        TedPermission.with(PickFromCameraActivity.this)
                .setPermissions(new String[] {
                        Manifest.permission.CAMERA,
                        Manifest.permission.WRITE_EXTERNAL_STORAGE,
                        Manifest.permission.READ_EXTERNAL_STORAGE
                })
                .setPermissionListener(listener)
                .setDeniedMessage("권한 설정은 [설정] > [권한] 에서 변경 가능합니다.")
                .check();

    }
}