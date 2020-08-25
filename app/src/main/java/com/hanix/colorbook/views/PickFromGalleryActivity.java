package com.hanix.colorbook.views;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.hanix.colorbook.R;
import com.hanix.colorbook.common.app.GLog;
import com.hanix.colorbook.views.event.OnSingleClickListener;

import java.io.IOException;
import java.io.InputStream;
import java.util.Locale;
import java.util.Objects;

public class PickFromGalleryActivity extends AppCompatActivity {

    TextView galleryCode;
    Button galleryPickPicture;
    ImageView galleryMore, galleryPicture;
    private boolean isPictureOn = false;

    private static final int REQUEST_CODE = 0;

    @SuppressLint("ClickableViewAccessibility")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pick_from_gallery);

        galleryCode = findViewById(R.id.galleryCode);
        galleryPickPicture = findViewById(R.id.galleryPickPicture);
        galleryMore = findViewById(R.id.galleryMore);
        galleryPicture = findViewById(R.id.galleryPicture);

        galleryMore.setOnClickListener(galleryClick);
        galleryPickPicture.setOnClickListener(galleryClick);
        isPictureOn = false;

        galleryPicture.setOnTouchListener((view, motionEvent) -> {
            if(isPictureOn) {
                switch (motionEvent.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                    case MotionEvent.ACTION_UP:
                    case MotionEvent.ACTION_MOVE :
                    case MotionEvent.ACTION_SCROLL:
                        getColor((int)motionEvent.getX(), (int)motionEvent.getY());
                        break;

                    default:
                        return false;
                }
            }
            return false;
        });
    }

    OnSingleClickListener galleryClick = new OnSingleClickListener() {
        @Override
        public void onSingleClick(View v) {
            switch (v.getId()) {
                case R.id.galleryPickPicture:
                    callGallery();
                    break;

                case R.id.galleryMore:
                    showPopupMenu(v);
                    break;
            }
        }
    };

    private void showPopupMenu(View v) {
        PopupMenu popupMenu = new PopupMenu(getApplicationContext(), v);
        getMenuInflater().inflate(R.menu.menu_choose, popupMenu.getMenu());
        popupMenu.getMenu().findItem(R.id.gallery).setVisible(false);
        popupMenu.setOnMenuItemClickListener(menuClick);
        popupMenu.show();

    }


    PopupMenu.OnMenuItemClickListener menuClick = (menuItem) -> {
        switch (menuItem.getItemId()) {
            case R.id.main:
                goOtherPage(MainActivity.class);
                break;

            case R.id.camera:
                goOtherPage(PickFromCameraActivity.class);
                break;
        }
        return false;
    };

    private void goOtherPage(Class<?> activityClass) {
        startActivity(new Intent(PickFromGalleryActivity.this, activityClass));
        finish();
    }

    // 갤러리 시작
    private void callGallery() {
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.setType("image/*");
        startActivityForResult(intent, REQUEST_CODE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CODE) {
            if (resultCode == RESULT_OK) {
                try {
                    assert data != null;
                    InputStream in = getContentResolver().openInputStream(Objects.requireNonNull(data.getData()));

                    if (in != null) {
                        Bitmap img = BitmapFactory.decodeStream(in);
                        in.close();
                        galleryPicture.setImageBitmap(img);
                        isPictureOn = true;
                    }
                } catch (IOException e) {
                    GLog.e(e.getMessage(), e);
                }
            }
        }
    }
    // 갤러리 끝


    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if(isPictureOn) {
            switch (event.getAction()) {
                case MotionEvent.ACTION_DOWN:
                case MotionEvent.ACTION_UP:
                case MotionEvent.ACTION_MOVE :
                    break;

                default:
                    return false;
            }
        }
       return super.onTouchEvent(event);
    }

    private void getColor(int x, int y) {
        Drawable drawable = galleryPicture.getDrawable();
        Bitmap bitmap = ((BitmapDrawable)drawable).getBitmap();
        if(y < bitmap.getHeight() && x < bitmap.getWidth())
            colorHex(bitmap.getPixel(x, y));
    }
    private void colorHex(int color) {
        String colorHex = String.format(Locale.getDefault(), "0x%02X%02X%02X%02X", Color.alpha(color), Color.red(color), Color.green(color), Color.blue(color));
        galleryCode.setText(colorHex);
        galleryCode.setTextColor(color);
    }

}