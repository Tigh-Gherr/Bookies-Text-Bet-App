package com.tommyfrenchbookmakers.officialapp.ui.CameraPreviewActivity;

import android.os.Build;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.view.Window;

import com.android.tighearnan.frenchsscanner.R;
import com.tommyfrenchbookmakers.officialapp.ui.BaseActivity;


public class CameraPreviewActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = getWindow();
            window.setStatusBarColor(ContextCompat.getColor(this, R.color.md_grey_700));
        }

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_camera_preview);
    }

    @Override
    protected int getSelfNavDrawerItem() {
        return NAVDRAWER_ITEM_SCAN_BARCODE;
    }
}
