package com.hanix.colorbook.views;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.hanix.colorbook.R;
import com.hanix.colorbook.views.event.OnSingleClickListener;

public class PickFromCameraActivity extends AppCompatActivity {

    TextView cameraPick, cameraCode;
    ImageView cameraPicture, cameraMore;
    Button cameraPickPicture;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pick_from_camera);

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
                case R.id.cameraMore :
                    showPopupMenu(v);
                    break;

                case R.id.cameraPickPicture :
                    break;
            }
        }
    };

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

}