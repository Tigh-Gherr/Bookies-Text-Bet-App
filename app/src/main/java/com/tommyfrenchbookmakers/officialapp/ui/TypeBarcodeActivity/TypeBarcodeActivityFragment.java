package com.tommyfrenchbookmakers.officialapp.ui.TypeBarcodeActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v7.widget.AppCompatButton;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.tighearnan.frenchsscanner.R;
import com.tommyfrenchbookmakers.officialapp.Global;
import com.tommyfrenchbookmakers.officialapp.ui.ResultPagerActivity.ResultPagerActivity;
import com.tommyfrenchbookmakers.officialapp.utils.NetworkUtils;

/**
 * A placeholder fragment containing a simple view.
 */
public class TypeBarcodeActivityFragment extends Fragment {

    private AppCompatButton mGoButton;
    private EditText mInputEditText;
    private TextView mBarcodeDigitTextView;

    public TypeBarcodeActivityFragment() {
    }

    private void queryBarcode() {
        if (mInputEditText.getText().length() >= 14 && mInputEditText.getText().length() <= 17) {
            if (NetworkUtils.networkIsAvailable(getActivity())) {
                Intent i = new Intent(getActivity(), ResultPagerActivity.class);
                i.putExtra(Global.INTENT_KEY_DOWNLOAD_TYPE, Global.DOWNLOAD_TYPE_BARCODE);
                i.putExtra(Global.INTENT_KEY_BARCODE, mInputEditText.getText().toString());
                i.putExtra(Global.INTENT_KEY_SENDER, ((TypeBarcodeActivity)getActivity()).getSelfNavDrawerItem());
                startActivity(i);
            } else {
                InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(getView().getWindowToken(), 0);
                Snackbar.make(getView(), R.string.error_message_no_internet, Snackbar.LENGTH_LONG)
                        .show();
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
