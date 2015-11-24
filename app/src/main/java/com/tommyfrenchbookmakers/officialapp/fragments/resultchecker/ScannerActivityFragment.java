package com.tommyfrenchbookmakers.officialapp.fragments.resultchecker;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.NavUtils;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.tighearnan.frenchsscanner.R;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.Result;
import com.tommyfrenchbookmakers.officialapp.Global;
import com.tommyfrenchbookmakers.officialapp.activities.resultchecker.ResultPagerActivity;
import com.tommyfrenchbookmakers.officialapp.customutils.NetworkUtils;
import com.tommyfrenchbookmakers.officialapp.enumerators.DownloadType;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Random;

import me.dm7.barcodescanner.zxing.ZXingScannerView;

public class ScannerActivityFragment extends Fragment implements ZXingScannerView.ResultHandler {

    private int mCameraNumber = 0;

    private ImageView mLogoImageView;
    private ImageButton mToggleCameraImageButton;
    private ImageButton mToggleFlashImageButton;

    private ZXingScannerView mScannerView;

    public ScannerActivityFragment() {
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
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_scanner, container, false);

        mScannerView = (ZXingScannerView) v.findViewById(R.id.scanner_view);
        mScannerView.setFormats(new ArrayList<>(Arrays.asList(BarcodeFormat.ITF)));

        mLogoImageView = (ImageView) v.findViewById(R.id.image_view_frenchsLogo);
        mLogoImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Random rn = new Random();
                String barcodes[] = {
                        "121065221926915",
                        "121065281923315",
                        "121065291925315",
                        "121065201684115",
                        "121065221924715",
                        "121065261923815",
                        "121065291958115",
                        "121065272151015",
                        "121065272154215"
                };
                Intent i = new Intent(getActivity(), ResultPagerActivity.class);
                i.putExtra(DownloadType.intentKey(), DownloadType.BARCODE);
                i.putExtra("BARCODE", barcodes[rn.nextInt(barcodes.length)]);
                startActivity(i);
            }
        });

        mToggleCameraImageButton = (ImageButton) v.findViewById(R.id.image_button_toggleCamera);
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

        mToggleFlashImageButton = (ImageButton) v.findViewById(R.id.image_button_toggleFlash);
        if (!getActivity().getPackageManager().hasSystemFeature(PackageManager.FEATURE_CAMERA_FLASH)) {
            mToggleFlashImageButton.setVisibility(View.INVISIBLE);
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
    public void handleResult(Result result) {
        String barcode = result.getText();

        if(!result.getText().startsWith("0")) {
            Toast.makeText(getActivity(), "Not a docket.", Toast.LENGTH_LONG).show();
            mScannerView.startCamera(mCameraNumber);
            return;
        }

        if(result.getText().startsWith("0")) barcode = barcode.substring(1);

        if (result.getBarcodeFormat() == BarcodeFormat.ITF || result.getBarcodeFormat() == BarcodeFormat.CODE_128) {
            if (NetworkUtils.networkIsAvailable(getActivity())) {
                Intent i = new Intent(getActivity(), ResultPagerActivity.class);
                i.putExtra(DownloadType.intentKey(), DownloadType.BARCODE);
                i.putExtra("BARCODE", barcode);
                startActivity(i);
            } else {
                Toast toast = Toast.makeText(getActivity(), R.string.error_message_no_internet, Toast.LENGTH_LONG);
                ((TextView) ((LinearLayout) toast.getView()).getChildAt(0))
                        .setGravity(Gravity.CENTER_HORIZONTAL);
                toast.show();
                mScannerView.startCamera(mCameraNumber);
                setFlash(false);
            }
        } else {
            Toast toast = Toast.makeText(getActivity(), getString(R.string.error_message_barcode_format, result.getBarcodeFormat().toString()),
                    Toast.LENGTH_LONG);
            ((TextView) ((LinearLayout) toast.getView()).getChildAt(0))
                    .setGravity(Gravity.CENTER_HORIZONTAL);
            toast.show();

            mScannerView.startCamera(mCameraNumber);
            setFlash(false);
        }

    }

    @Override
    public void onPause() {
        super.onPause();
        mScannerView.stopCamera();
    }

    @Override
    public void onResume() {
        super.onResume();
        mScannerView.startCamera(mCameraNumber);
        setFlash(false);
        mScannerView.setResultHandler(this);
    }
}