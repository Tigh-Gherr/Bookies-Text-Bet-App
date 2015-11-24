package com.tommyfrenchbookmakers.officialapp.fragments.resultchecker;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatButton;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.tighearnan.frenchsscanner.R;
import com.tommyfrenchbookmakers.officialapp.activities.resultchecker.ResultPagerActivity;
import com.tommyfrenchbookmakers.officialapp.customutils.NetworkUtils;
import com.tommyfrenchbookmakers.officialapp.enumerators.DownloadType;

/**
 * A placeholder fragment containing a simple view.
 */
public class TypeBarcodeActivityFragment extends Fragment {

    private Toolbar mToolbar;
    private AppCompatButton mGoButton;
    private EditText mInputEditText;
    private TextView mBarcodeDigitTextView;

    public TypeBarcodeActivityFragment() {
    }

    private void queryBarcode() {
        if (mInputEditText.getText().length() >= 14 && mInputEditText.getText().length() <= 17) {
            if (NetworkUtils.networkIsAvailable(getActivity())) {
                Intent i = new Intent(getActivity(), ResultPagerActivity.class);
                i.putExtra(DownloadType.intentKey(), DownloadType.BARCODE);
                i.putExtra("BARCODE", mInputEditText.getText().toString());
                startActivity(i);
            } else {
                Toast toast = Toast.makeText(getActivity(),
                        R.string.error_message_no_internet,
                        Toast.LENGTH_LONG);

                ((TextView) ((LinearLayout) toast.getView()).getChildAt(0))
                        .setGravity(Gravity.CENTER_HORIZONTAL);
                toast.show();
            }
        } else {
            Toast.makeText(getActivity(),
                    R.string.error_message_barcode_length,
                    Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_type_barcode, container, false);

        mToolbar = (Toolbar) v.findViewById(R.id.app_bar);
        ((AppCompatActivity) getActivity()).setSupportActionBar(mToolbar);
        ((AppCompatActivity) getActivity()).getSupportActionBar().setHomeButtonEnabled(true);
        ((AppCompatActivity) getActivity()).getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        mBarcodeDigitTextView = (TextView) v.findViewById(R.id.text_view_currentBarcodeDigit);

        mInputEditText = (EditText) v.findViewById(R.id.edit_text_input);
        mInputEditText.requestFocus();
        getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE);
        mInputEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                mBarcodeDigitTextView.setText(s.length() + " digits");
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        mInputEditText.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if (event.getAction() == KeyEvent.ACTION_UP && keyCode == KeyEvent.KEYCODE_ENTER) {
                    queryBarcode();
                    return true;
                }
                return false;
            }
        });

        mGoButton = (AppCompatButton) v.findViewById(R.id.appcompat_button_accountInputGo);
        mGoButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                queryBarcode();
            }
        });

        return v;
    }

    @Override
    public void onResume() {
        super.onResume();

        mInputEditText.postDelayed(new Runnable() {
            @Override
            public void run() {
                InputMethodManager keyboard = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
                keyboard.showSoftInput(mInputEditText, 0);
            }
        }, 50);

        if (mBarcodeDigitTextView != null) {
            mBarcodeDigitTextView.setText(
                    mInputEditText.getText().length() + " digits"
            );
        }
    }
}
