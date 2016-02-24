package com.tommyfrenchbookmakers.officialapp.ui.CameraPreviewActivity;

import android.os.Build;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.Window;

import com.android.tighearnan.frenchsscanner.R;


public class CameraPreviewActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = getWindow();
            window.setStatusBarColor(ContextCompat.getColor(this, R.color.md_green_100));
        }

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_camera_preview);

    }
}
