package com.hanix.colorbook.views;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.hanix.colorbook.R;
import com.hanix.colorbook.common.utils.PrefUtil;
import com.hanix.colorbook.task.common.VersionCheckTask;
import com.hanix.colorbook.views.adapter.PaletteAdapter;
import com.hanix.colorbook.views.event.OnSingleClickListener;

import java.util.List;
import java.util.Locale;

import top.defaults.colorpicker.ColorPickerView;


public class MainActivity extends AppCompatActivity {

    ConstraintLayout mainLayout;
    ColorPickerView mainColorView;
    LinearLayout mainLinearLayout, mainInLayout, mainInLayout2;
    ImageView mainCamera, mainGallery;
    TextView mainCameraText, mainGalleryText, mainCode;
    Button mainSelect, mainReset;
    RecyclerView mainPalette;

    VersionCheckTask versionCheckTask;

    private PaletteAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        versionCheckTask = new VersionCheckTask(getApplicationContext(), MainActivity.this);

        mainLayout = findViewById(R.id.mainLayout);
        mainColorView = findViewById(R.id.mainColorView);
        mainLinearLayout = findViewById(R.id.mainLinearLayout);
        mainInLayout = findViewById(R.id.mainInLayout);
        mainInLayout2 = findViewById(R.id.mainInLayout2);
        mainCamera = findViewById(R.id.mainCamera);
        mainGallery = findViewById(R.id.mainGallery);
        mainCameraText = findViewById(R.id.mainCameraText);
        mainGalleryText = findViewById(R.id.mainGalleryText);
        mainPalette = findViewById(R.id.mainPalette);
        mainCode = findViewById(R.id.mainCode);
        mainSelect = findViewById(R.id.mainSelect);
        mainReset = findViewById(R.id.mainReset);

        mainLayout.setOnClickListener(mainClickListener);
        mainColorView.setOnClickListener(mainClickListener);
        mainLinearLayout.setOnClickListener(mainClickListener);
        mainInLayout.setOnClickListener(mainClickListener);
        mainInLayout2.setOnClickListener(mainClickListener);
        mainCamera.setOnClickListener(mainClickListener);
        mainGallery.setOnClickListener(mainClickListener);
        mainCameraText.setOnClickListener(mainClickListener);
        mainGalleryText.setOnClickListener(mainClickListener);
        mainCode.setOnClickListener(mainClickListener);
        mainSelect.setOnClickListener(mainClickListener);
        mainReset.setOnClickListener(mainClickListener);

        List<String> items = PrefUtil.getColor(getApplicationContext());
        adapter = new PaletteAdapter(items, MainActivity.this);
        adapter.setItemClick((view, position) ->
                mainColorView.setInitialColor((int) Long.parseLong(String.valueOf(adapter.getItem(position)).replaceFirst("0x", ""), 16))
        );
        mainPalette.setAdapter(adapter);
        mainPalette.setLayoutManager(new LinearLayoutManager(this));
        mainPalette.setItemAnimator(new DefaultItemAnimator());

        mainColorView.subscribe((color, fromUser, shouldPropagate) -> {
                mainCode.setText(colorHex(color));
                mainCode.setTextColor(color);
        });

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        versionCheckTask.updateResult(resultCode, requestCode);
    }


    private String colorHex(int color) {
        return String.format(Locale.getDefault(), "0x%02X%02X%02X%02X", Color.alpha(color), Color.red(color), Color.green(color), Color.blue(color));
    }

    private OnSingleClickListener mainClickListener = new OnSingleClickListener() {
        @Override
        public void onSingleClick(View v) {
            switch (v.getId()) {

                case R.id.mainCameraText:
                case R.id.mainCamera:
                    break;

                case R.id.mainGalleryText:
                case R.id.mainGallery:
                    break;

                case R.id.mainSelect:
                    PrefUtil.addColor(getApplicationContext(), String.valueOf(mainCode.getText()));
                    resetAdapter();
                    break;

                case R.id.mainReset:
                    PrefUtil.resetColor(getApplicationContext());
                    if (adapter != null) {
                        adapter.resetItem();
                        adapter.notifyDataSetChanged();
                    }
                    break;

                default:
                    break;
            }
        }
    };

    private void resetAdapter() {
        adapter.addItem(String.valueOf(mainCode.getText()));
        adapter.notifyDataSetChanged();
    }
}