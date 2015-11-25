package com.tommyfrenchbookmakers.officialapp.fragments.resultchecker;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.v7.widget.AppCompatImageButton;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.android.tighearnan.frenchsscanner.R;
import com.tommyfrenchbookmakers.officialapp.Global;
import com.tommyfrenchbookmakers.officialapp.activities.resultchecker.ResultPagerActivity;
import com.tommyfrenchbookmakers.officialapp.customutils.NetworkUtils;

import java.util.ArrayList;
import java.util.Collections;

import me.dm7.barcodescanner.zbar.BarcodeFormat;
import me.dm7.barcodescanner.zbar.Result;
import me.dm7.barcodescanner.zbar.ZBarScannerView;

/**
 * A placeholder fragment containing a simple view.
 */
public class BarcodeScannerActivityFragment extends Fragment implements ZBarScannerView.ResultHandler {

    private int mCameraNumber = 0;

    private ZBarScannerView mScannerView;
    private AppCompatImageButton mToggleCameraImageButton;
    private AppCompatImageButton mToggleFlashImageButton;

    public BarcodeScannerActivityFragment() {
    }

    private void createSnackBar(int stringResId) {
        Snackbar.make(getView(), stringResId, Snackbar.LENGTH_LONG).show();
    }

    private void restartCamera() {
        mScannerView.startCamera(mCameraNumber);
        setFlash(false);
    }

    private void setFlash(boolean turnOn) {
        mScannerView.setFlash(turnOn);

        if (turnOn) {
            mToggleFlashImageButton.setImageResource(R.drawable.ic_flash_off_white_36dp);
        } else {
            mToggleFlashImageButton.setImageResource(R.drawable.ic_flash_on_white_36dp);
        }
    }

    @Override
    public void handleResult(Result result) {
        String barcode = result.getContents();

        if(!barcode.startsWith("0")) {
            Toast.makeText(getActivity(), "Not a docket.", Toast.LENGTH_LONG).show();
            mScannerView.startCamera(mCameraNumber);
            return;
        } else {
            barcode = barcode.substring(1);
        }

        if(result.getBarcodeFormat() == BarcodeFormat.I25) {
            if(NetworkUtils.networkIsAvailable(getActivity())) {
                Intent i = new Intent(getActivity(), ResultPagerActivity.class);
                i.putExtra(Global.INTENT_KEY_DOWNLOAD_TYPE, Global.DOWNLOAD_TYPE_BARCODE);
                i.putExtra(Global.INTENT_KEY_BARCODE, barcode);
                startActivity(i);
            } else {
                createSnackBar(R.string.error_message_no_internet);
                restartCamera();
            }
        } else {
            createSnackBar(R.string.error_message_barcode_format);
            restartCamera();
        }

        mScannerView.startCamera(mCameraNumber);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_barcode_scanner, container, false);

        mScannerView = (ZBarScannerView) v.findViewById(R.id.scanner_view);
        mScannerView.setFormats(new ArrayList<BarcodeFormat>(Collections.singletonList(BarcodeFormat.I25)));

        mToggleCameraImageButton = (AppCompatImageButton) v.findViewById(R.id.appcompat_image_button_toggleCamera);
        if (android.hardware.Camera.getNumberOfCameras() < 2) {
            mToggleCameraImageButton.setVisibility(View.INVISIBLE);
            mToggleCameraImageButton.setClickable(false);
        }

        mToggleCameraImageButton.setTag(R.drawable.ic_camera_front_white_36dp);
        mToggleCameraImageButton.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        mToggleCameraImageButton.animate().setDuration(100)
                                .scaleX(0.75f)
                                .scaleY(0.75f);
                        return true;
                    case MotionEvent.ACTION_CANCEL:
                    case MotionEvent.ACTION_UP:
                        mToggleCameraImageButton.animate().setDuration(100)
                                .scaleX(1f)
                                .scaleY(1f);

                        mScannerView.stopCamera();
                        if (R.drawable.ic_camera_rear_white_36dp ==
                                Integer.parseInt(mToggleCameraImageButton.getTag().toString())) {
                            mScannerView.startCamera(0);
                            mCameraNumber = 0;
                            mToggleCameraImageButton.setImageResource(R.drawable.ic_camera_front_white_36dp);
                            mToggleCameraImageButton.setTag(R.drawable.ic_camera_front_white_36dp);
                        } else {
                            mScannerView.startCamera(1);
                            mCameraNumber = 1;
                            mToggleCameraImageButton.setImageResource(R.drawable.ic_camera_rear_white_36dp);
                            mToggleCameraImageButton.setTag(R.drawable.ic_camera_rear_white_36dp);
                        }
                        // Handle touch result here
                        return true;
                }
                // Dispatch touch result to parent
                return false;
            }
        });

        mToggleFlashImageButton = (AppCompatImageButton) v.findViewById(R.id.appcompat_image_button_toggleFlash);
        if (!getActivity().getPackageManager().hasSystemFeature(PackageManager.FEATURE_CAMERA_FLASH)) {
            mToggleFlashImageButton.setVisibility(View.GONE);
            mToggleFlashImageButton.setClickable(false);
        }
        mToggleFlashImageButton.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        v.animate().setDuration(100).scaleX(0.75f).scaleY(0.75f);
                        return true;
                    case MotionEvent.ACTION_CANCEL:
                    case MotionEvent.ACTION_UP:
                        v.animate().setDuration(100).scaleX(1f).scaleY(1f);

                        setFlash(!mScannerView.getFlash());

                        // Handle touch event here
                        return true;
                }
                // Dispatch touch event to parent
                return false;
            }
        });

        return v;
    }

    @Override
    public void onResume() {
        super.onResume();
        mScannerView.startCamera();
        setFlash(false);
        mScannerView.setResultHandler(this);
    }

    @Override
    public void onPause() {
        super.onPause();
        mScannerView.stopCamera();
    }
}
