package com.tommyfrenchbookmakers.officialapp.ui.BarcodeScannerActivity;

import android.Manifest;
import android.os.Bundle;
import android.view.Window;
import android.view.WindowManager;

import com.android.tighearnan.frenchsscanner.R;
import com.tommyfrenchbookmakers.officialapp.ui.BaseActivity;

public class BarcodeScannerActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_barcode_scanner);

        checkForPermission(REQUEST_PERMISSION_CAMERA, Manifest.permission.CAMERA);
    }

    @Override
    protected int getSelfNavDrawerItem() {
        return NAVDRAWER_ITEM_SCAN_BARCODE;
    }
}
