package com.tommyfrenchbookmakers.officialapp.ui;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatButton;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.android.tighearnan.frenchsscanner.R;
import com.tommyfrenchbookmakers.officialapp.betslipobjects.BetSlip;
import com.tommyfrenchbookmakers.officialapp.singletons.BetSlipSingleton;
import com.tommyfrenchbookmakers.officialapp.utils.NetworkUtils;
import com.tommyfrenchbookmakers.officialapp.ui.SelectionScreenActivity.SelectionScreenActivity;


public class SplashScreenActivity extends AppCompatActivity {

    ProgressBar mProgressBar;
    TextView mCheckingInternetTextView;
    TextView mNoInternetTextView;
    AppCompatButton mRetryButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);

        BetSlipSingleton.get(this).setBetSlip(new BetSlip());

        mProgressBar = (ProgressBar) findViewById(R.id.progress_bar_splashScreenSpinner);
        mCheckingInternetTextView = (TextView) findViewById(R.id.text_view_splashScreenCheckingInternet);

        mNoInternetTextView = (TextView) findViewById(R.id.text_view_splashScreenNoInternet);
        mNoInternetTextView.setAlpha(0);
        mRetryButton = (AppCompatButton) findViewById(R.id.appcompat_button_splashScreenRetry);
        mRetryButton.setAlpha(0);

        mRetryButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mNoInternetTextView.animate().alpha(0).setDuration(300);
                mRetryButton.animate().alpha(0).setDuration(300);
                mRetryButton.setClickable(false);
                mCheckingInternetTextView.animate().alpha(1).setDuration(300);
                mProgressBar.animate().alpha(1).setDuration(300);
                checkInternet();
            }
        });

//        Initialise.internetCheck(1);
        checkInternet();
    }

    private void checkInternet() {
        Handler h = new Handler();
        final int animDuration = 300;
        h.postDelayed(new Runnable() {
            @Override
            public void run() {
                if(NetworkUtils.networkIsAvailable(SplashScreenActivity.this)) {
                    startActivity(new Intent(SplashScreenActivity.this, SelectionScreenActivity.class));
                    overridePendingTransition(R.anim.anim_fadeout, R.anim.anim_fadein);
                } else {
                    mCheckingInternetTextView.animate().alpha(0).setDuration(animDuration);
                    mProgressBar.animate().alpha(0).setDuration(animDuration);
                    mNoInternetTextView.animate().alpha(1).setDuration(animDuration);
                    mRetryButton.animate().alpha(1).setDuration(animDuration);
                    mRetryButton.setClickable(true);
                }
            }
        }, 1500);
    }
}