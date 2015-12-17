package com.tommyfrenchbookmakers.officialapp.activities.textbet;

import android.annotation.TargetApi;
import android.content.DialogInterface;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.NavigationView;
import android.support.v4.app.NavUtils;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;

import com.android.tighearnan.frenchsscanner.R;
import com.sothree.slidinguppanel.SlidingUpPanelLayout;
import com.tommyfrenchbookmakers.officialapp.betslipobjects.BetSlip;
import com.tommyfrenchbookmakers.officialapp.customutils.NavigationUtils;
import com.tommyfrenchbookmakers.officialapp.fragments.textbet.AddSelectionActivityFragment;
import com.tommyfrenchbookmakers.officialapp.singletons.BetSlipSingleton;

public class AddSelectionActivity extends AppCompatActivity {

    AppBarLayout mAppBarLayout;
    private Toolbar mToolbar;
    private DrawerLayout mDrawerLayout;
    private NavigationView mNavigationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_selection);
        mToolbar = (Toolbar) findViewById(R.id.app_bar);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        mAppBarLayout = (AppBarLayout) findViewById(R.id.abl_test);

        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        mNavigationView = (NavigationView) findViewById(R.id.navigation_view);
        mNavigationView.getMenu().getItem(0).getSubMenu().getItem(0).setChecked(true);

        mNavigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(MenuItem item) {
                int id = item.getItemId();
                if (id == R.id.nav_textBet) {
                    NavUtils.navigateUpFromSameTask(AddSelectionActivity.this);
                } else {
                    NavigationUtils.onNavigationMenuItemPressed(id, AddSelectionActivity.this);
                }

                mDrawerLayout.closeDrawers();
                return true;
            }
        });
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public void changeElevation(float i) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            mAppBarLayout.setElevation(i * 16);
        }
    }

    public void setToolbarTitle(String title) {
        mToolbar.setTitle(title);
    }

    @Override
    public void onBackPressed() {
        AddSelectionActivityFragment fragment = (AddSelectionActivityFragment) getSupportFragmentManager().findFragmentById(R.id.fragment);
        if (fragment.getPanelState() == SlidingUpPanelLayout.PanelState.EXPANDED)
            fragment.closePanel();
        else super.onBackPressed();
    }

}
