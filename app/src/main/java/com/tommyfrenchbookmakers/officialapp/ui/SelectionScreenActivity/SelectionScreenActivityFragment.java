package com.tommyfrenchbookmakers.officialapp.ui.SelectionScreenActivity;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.AppCompatButton;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.android.tighearnan.frenchsscanner.R;
import com.tommyfrenchbookmakers.officialapp.ui.AccountAndReferenceInput.AccountAndReferenceInputActivity;
import com.tommyfrenchbookmakers.officialapp.ui.BarcodeScannerActivity.BarcodeScannerActivity;
import com.tommyfrenchbookmakers.officialapp.ui.ContactUsActivity.ContactUsActivity;
import com.tommyfrenchbookmakers.officialapp.ui.TextBetSlipActivity.TextBetSlipActivity;
import com.tommyfrenchbookmakers.officialapp.ui.TypeBarcodeActivity.TypeBarcodeActivity;

/**
 * A placeholder fragment containing a simple view.
 */
public class SelectionScreenActivityFragment extends Fragment {

    private AppCompatButton mScanButton;
    private AppCompatButton mTypeButton;
    private AppCompatButton mReferenceButton;
    private AppCompatButton mTextBetButton;


    public SelectionScreenActivityFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_selection_screen, container, false);

        mScanButton = (AppCompatButton) v.findViewById(R.id.appcompat_button_scan);
        mScanButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((SelectionScreenActivity)getActivity()).launchActivity(BarcodeScannerActivity.class);

            }
        });

        mTypeButton = (AppCompatButton) v.findViewById(R.id.appcompat_button_type);
        mTypeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((SelectionScreenActivity)getActivity()).launchActivity(TypeBarcodeActivity.class);
            }
        });

        mReferenceButton = (AppCompatButton) v.findViewById(R.id.appcompat_button_reference);
        mReferenceButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((SelectionScreenActivity)getActivity()).launchActivity(AccountAndReferenceInputActivity.class);
            }
        });

        mTextBetButton = (AppCompatButton) v.findViewById(R.id.appcompat_button_textBet);
        mTextBetButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((SelectionScreenActivity)getActivity()).launchActivity(TextBetSlipActivity.class);
            }
        });

        AppCompatButton belowButton = (AppCompatButton) v.findViewById(R.id.appcompat_button_contactUs);
        belowButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((SelectionScreenActivity)getActivity()).launchActivity(ContactUsActivity.class);
            }
        });

        return v;
    }
}
