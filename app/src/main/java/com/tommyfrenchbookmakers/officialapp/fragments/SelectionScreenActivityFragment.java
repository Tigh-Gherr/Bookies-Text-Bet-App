package com.tommyfrenchbookmakers.officialapp.fragments;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatButton;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.android.tighearnan.frenchsscanner.R;
import com.tommyfrenchbookmakers.officialapp.Global;
import com.tommyfrenchbookmakers.officialapp.TestCorrectScoreActivity;
import com.tommyfrenchbookmakers.officialapp.activities.resultchecker.AccountInputActivity;
import com.tommyfrenchbookmakers.officialapp.activities.resultchecker.BarcodeScannerActivity;
import com.tommyfrenchbookmakers.officialapp.activities.resultchecker.TypeBarcodeActivity;
import com.tommyfrenchbookmakers.officialapp.activities.textbet.TextBetSlipActivity;
import com.tommyfrenchbookmakers.officialapp.betslipobjects.BetSlip;
import com.tommyfrenchbookmakers.officialapp.singletons.BetSlipSingleton;

/**
 * A placeholder fragment containing a simple view.
 */
public class SelectionScreenActivityFragment extends Fragment {

    private AppCompatButton mScanButton;
    private AppCompatButton mTypeButton;
    private AppCompatButton mReferenceButton;
    private AppCompatButton mTextBetButton;
    private Toolbar mToolbar;

    public SelectionScreenActivityFragment() {
    }

    private void startTextBetActivity() {
        BetSlipSingleton.get(getActivity()).setBetSlip(new BetSlip());
        startActivity(new Intent(getActivity(), TextBetSlipActivity.class));
    }

    private boolean checkForPermission(final int permissionCode, final String permission) {
        int check = ContextCompat.checkSelfPermission(getActivity(), permission);

        if(check != PackageManager.PERMISSION_GRANTED) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(getActivity(), permission)) {
                AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                switch (permissionCode) {
                    case Global.REQUEST_PERMISSION_CAMERA:
                        builder.setTitle("Request Permission to use Camera");
                        builder.setMessage("In order to scan barcodes, this app must have access to the camera to view them. " +
                                "Please grant this permission in the forth coming dialog.");
                        break;
                    case Global.REQUEST_PERMISSION_SMS:
                        builder.setTitle("Request Permission to use SMS");
                        builder.setMessage("TFB App's Text Betting feature requires you to allow the app to send messages " +
                                "directly from the app to TFB. To use this feature, click Allow on the forthcoming " +
                                "pop-up.");
                        break;
                }

                builder.setCancelable(false);
                builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        requestPermissions(new String[] { permission }, permissionCode);
                    }
                });
                builder.show();
            } else {
                requestPermissions(new String[]{permission}, permissionCode);
            }
            return false;
        }

        return true;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        boolean granted = grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED;
        switch (requestCode) {
            case Global.REQUEST_PERMISSION_CAMERA:
                if(granted) {
                    // Granted
                    startActivity(new Intent(getActivity(), BarcodeScannerActivity.class));
                } else {
                    Snackbar.make(getView(), "Cannot start Barcode Scanner, Camera permission not granted.", Snackbar.LENGTH_LONG)
                            .show();
                }
                break;
            case Global.REQUEST_PERMISSION_SMS:
                if(granted) {
                    startTextBetActivity();
                } else {
                    Snackbar.make(getView(), "Cannot start Text Bet, SMS permission not granted", Snackbar.LENGTH_LONG)
                            .show();
                }
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_selection_screen, container, false);

        mToolbar = (Toolbar) v.findViewById(R.id.app_bar);
        ((AppCompatActivity) getActivity()).setSupportActionBar(mToolbar);
        mToolbar.setLogo(R.drawable.frenches_logo_128);
        mToolbar.setNavigationIcon(null);


        mScanButton = (AppCompatButton) v.findViewById(R.id.appcompat_button_scan);
        mScanButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    if(checkForPermission(Global.REQUEST_PERMISSION_CAMERA, Manifest.permission.CAMERA))
                        startActivity(new Intent(getActivity(), BarcodeScannerActivity.class));
                } else {
                    startActivity(new Intent(getActivity(), BarcodeScannerActivity.class));
                }
            }
        });

        mTypeButton = (AppCompatButton) v.findViewById(R.id.appcompat_button_type);
        mTypeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getActivity(), TypeBarcodeActivity.class));
            }
        });

        mReferenceButton = (AppCompatButton) v.findViewById(R.id.appcompat_button_reference);
        mReferenceButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getActivity(), AccountInputActivity.class));
            }
        });

        mTextBetButton = (AppCompatButton) v.findViewById(R.id.appcompat_button_textBet);
        mTextBetButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    if(checkForPermission(Global.REQUEST_PERMISSION_SMS, Manifest.permission.SEND_SMS))
                        startTextBetActivity();

                } else {
                    startTextBetActivity();
                }

            }
        });

        AppCompatButton belowButton = (AppCompatButton) v.findViewById(R.id.appcompat_button_below);
        belowButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getActivity(), TestCorrectScoreActivity.class));
            }
        });

//        AppCompatButton growButton = (AppCompatButton) v.findViewById(R.id.appcompat_button_grow);
//        growButton.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                startActivity(new Intent(getActivity(), GrowCorrectScoreActivity.class));
//            }
//        });

//        belowButton.setVisibility(View.GONE);
//        growButton.setVisibility(View.GONE);
        return v;
    }
}
