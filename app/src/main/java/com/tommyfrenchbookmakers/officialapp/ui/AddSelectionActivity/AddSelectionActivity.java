package com.tommyfrenchbookmakers.officialapp.ui.AddSelectionActivity;

import android.annotation.TargetApi;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.AppBarLayout;

import com.android.tighearnan.frenchsscanner.R;
import com.sothree.slidinguppanel.SlidingUpPanelLayout;
import com.tommyfrenchbookmakers.officialapp.ui.BaseActivity;

public class AddSelectionActivity extends BaseActivity {

    private AppBarLayout mAppBarLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_selection);
        mAppBarLayout = (AppBarLayout) findViewById(R.id.abl_test);
    }

    @Override
    protected int getSelfNavDrawerItem() {
        return NAVDRAWER_ITEM_TEXTBET;
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public void changeElevation(float i) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            mAppBarLayout.setElevation(i * 16);
        }
    }

    public void setToolbarTitle(String title) {
        getActionBarToolbar().setTitle(title);
    }

    @Override
    public void onBackPressed() {
        AddSelectionActivityFragment fragment = (AddSelectionActivityFragment) getSupportFragmentManager().findFragmentById(R.id.fragment);
        if (fragment.getPanelState() == SlidingUpPanelLayout.PanelState.EXPANDED)
            fragment.closePanel();
        else super.onBackPressed();
    }

}
