package com.hanix.colorbook.views;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;
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
    TextView mainCode;
    Button mainSelect, mainReset;
    ImageView mainPopup;
    RecyclerView mainPalette;
    AdView adView;

    VersionCheckTask versionCheckTask;

    private PaletteAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        versionCheckTask = new VersionCheckTask(getApplicationContext(), MainActivity.this);

        MobileAds.initialize(this, (initializationStatus) -> {
        });

        mainLayout = findViewById(R.id.mainLayout);
        mainColorView = findViewById(R.id.mainColorView);
        mainPalette = findViewById(R.id.mainPalette);
        mainCode = findViewById(R.id.mainCode);
        mainPopup = findViewById(R.id.mainPopup);
        mainSelect = findViewById(R.id.mainSelect);
        mainReset = findViewById(R.id.mainReset);
        adView = findViewById(R.id.adView);

        mainLayout.setOnClickListener(mainClickListener);
        mainColorView.setOnClickListener(mainClickListener);
        mainCode.setOnClickListener(mainClickListener);
        mainSelect.setOnClickListener(mainClickListener);
        mainReset.setOnClickListener(mainClickListener);
        mainPopup.setOnClickListener(mainClickListener);

        AdRequest adRequest = new AdRequest.Builder().build();
        adView.loadAd(adRequest);

        List<String> items = PrefUtil.getColor(getApplicationContext());
        adapter = new PaletteAdapter(items, MainActivity.this);
        adapter.setItemClick((view, position) -> mainColorView.setInitialColor((int) Long.parseLong(String.valueOf(adapter.getItem(position)).replaceFirst("0x", ""), 16)));
        mainPalette.setAdapter(adapter);
        mainPalette.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        mainPalette.setItemAnimator(new DefaultItemAnimator());
        mainColorView.setInitialColor(0xFF6C93FF);

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

                case R.id.mainPopup:
                    showPopupMenu(v);
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

    private void showPopupMenu(View v) {
        PopupMenu popup = new PopupMenu(getApplicationContext(), v);
        getMenuInflater().inflate(R.menu.menu_choose, popup.getMenu());
        popup.getMenu().findItem(R.id.main).setVisible(false);
        popup.setOnMenuItemClickListener(menuClick);
        popup.show();
    }

    PopupMenu.OnMenuItemClickListener menuClick = (menuItem) -> {
        switch (menuItem.getItemId()) {
            case R.id.camera:
                goOtherPage(PickFromCameraActivity.class);
                break;

            case R.id.gallery:
                goOtherPage(PickFromGalleryActivity.class);
                break;
        }
        return false;
    };

    private void goOtherPage(Class<?> activityClass) {
        startActivity(new Intent(MainActivity.this, activityClass));
    }
}