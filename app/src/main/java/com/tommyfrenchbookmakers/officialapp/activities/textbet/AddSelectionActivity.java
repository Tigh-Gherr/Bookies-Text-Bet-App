package com.tommyfrenchbookmakers.officialapp.activities.textbet;

import android.annotation.TargetApi;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.AppBarLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import com.android.tighearnan.frenchsscanner.R;
import com.sothree.slidinguppanel.SlidingUpPanelLayout;
import com.tommyfrenchbookmakers.officialapp.fragments.textbet.AddSelectionActivityFragment;

public class AddSelectionActivity extends AppCompatActivity {

    AppBarLayout mAppBarLayout;
    private Toolbar mToolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_selection);
        mToolbar = (Toolbar) findViewById(R.id.app_bar);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        mAppBarLayout = (AppBarLayout) findViewById(R.id.abl_test);
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public void changeElevation(float i) {
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            mAppBarLayout.setElevation(i * 16);
        }
    }

    public void setToolbarTitle(String title) {
        mToolbar.setTitle(title);
    }

    @Override
    public void onBackPressed() {
        AddSelectionActivityFragment fragment = (AddSelectionActivityFragment) getSupportFragmentManager().findFragmentById(R.id.fragment);
        if(fragment.getPanelState() == SlidingUpPanelLayout.PanelState.EXPANDED) fragment.closePanel();
        else super.onBackPressed();
    }

}
