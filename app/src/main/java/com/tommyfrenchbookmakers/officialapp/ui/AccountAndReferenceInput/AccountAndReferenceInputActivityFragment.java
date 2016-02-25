package com.tommyfrenchbookmakers.officialapp.ui.AccountAndReferenceInput;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatButton;
import android.support.v7.widget.Toolbar;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.Toast;

import com.android.tighearnan.frenchsscanner.R;
import com.tommyfrenchbookmakers.officialapp.Global;
import com.tommyfrenchbookmakers.officialapp.ui.ResultPagerActivity.ResultPagerActivity;
import com.tommyfrenchbookmakers.officialapp.utils.NetworkUtils;


public class AccountAndReferenceInputActivityFragment extends Fragment {

//    private Toolbar mToolbar;
    private EditText mAccountNumberEditText;
    private EditText mReferenceNumberEditText;
    private AppCompatButton mGoButton;

    public AccountAndReferenceInputActivityFragment() {
    }

    private void queryAccountAndReferenceNumber() {
        if (mAccountNumberEditText.getText().length() > 0 && mReferenceNumberEditText.getText().length() > 0) {
            if ((mAccountNumberEditText.getText().toString().length() - mAccountNumberEditText.getText().toString().replace(".", "").length()) == 1
                    && mAccountNumberEditText.getText().charAt(2) == '.') {
                if (mReferenceNumberEditText.getText().toString().length() == 4) {
                    if (NetworkUtils.networkIsAvailable(getActivity())) {
                        Intent i = new Intent(getActivity(), ResultPagerActivity.class);
                        i.putExtra(Global.INTENT_KEY_DOWNLOAD_TYPE, Global.DOWNLOAD_TYPE_ACCOUNT_AND_REFERENCE);
                        i.putExtra(Global.INTENT_KEY_ACCOUNT, mAccountNumberEditText.getText().toString().toLowerCase());
                        i.putExtra(Global.INTENT_KEY_REFERENCE, mReferenceNumberEditText.getText().toString());
                        i.putExtra(Global.INTENT_KEY_SENDER, ((AccountAndReferenceInputActivity)getActivity()).getSelfNavDrawerItem());

                        startActivity(i);
                    } else {
                        Toast.makeText(getActivity(), "Not connected to internet", Toast.LENGTH_LONG).show();
                    }
                } else {
                    Toast.makeText(getActivity(), "Incorrect Reference Number length.", Toast.LENGTH_LONG).show();
                    mReferenceNumberEditText.requestFocus();
                }
            } else {
                Toast.makeText(getActivity(), "Incorrect Account Number format.", Toast.LENGTH_LONG).show();
                mAccountNumberEditText.requestFocus();
            }
        } else {
            Toast.makeText(getActivity(), "Fill out both fields", Toast.LENGTH_LONG).show();
            if (mAccountNumberEditText.getText().length() == 0)
                mAccountNumberEditText.requestFocus();
            else if (mReferenceNumberEditText.getText().length() == 0)
                mReferenceNumberEditText.requestFocus();
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_account_input, container, false);

        /*mToolbar = (Toolbar) v.findViewById(R.id.app_bar);
        ((AppCompatActivity) getActivity()).setSupportActionBar(mToolbar);
        ((AppCompatActivity) getActivity()).getSupportActionBar().setHomeButtonEnabled(true);
        ((AppCompatActivity) getActivity()).getSupportActionBar().setDisplayHomeAsUpEnabled(true);*/

        mAccountNumberEditText = (EditText) v.findViewById(R.id.edit_text_accountInputAccountNumber);
        mAccountNumberEditText.requestFocus();
        getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE);

        mReferenceNumberEditText = (EditText) v.findViewById(R.id.edit_text_accountInputReferenceNumber);
        mReferenceNumberEditText.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if (keyCode == KeyEvent.KEYCODE_ENTER && event.getAction() == KeyEvent.ACTION_UP) {
                    queryAccountAndReferenceNumber();
                    return true;
                }
                return false;
            }
        });

        mGoButton = (AppCompatButton) v.findViewById(R.id.appcompat_button_accountInputGo);
        mGoButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                queryAccountAndReferenceNumber();
            }
        });

        return v;
    }

    @Override
    public void onResume() {
        super.onResume();

        mReferenceNumberEditText.postDelayed(new Runnable() {
            @Override
            public void run() {
                InputMethodManager keyboard = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
                keyboard.showSoftInput(mReferenceNumberEditText, 0);
            }
        }, 50);
    }
}
