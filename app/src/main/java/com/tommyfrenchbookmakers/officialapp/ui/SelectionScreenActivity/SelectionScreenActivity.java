package com.tommyfrenchbookmakers.officialapp.ui.SelectionScreenActivity;

import android.Manifest;
import android.annotation.TargetApi;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.android.tighearnan.frenchsscanner.R;
import com.tommyfrenchbookmakers.officialapp.Global;
import com.tommyfrenchbookmakers.officialapp.ui.AccountAndReferenceInput.AccountAndReferenceInputActivity;
import com.tommyfrenchbookmakers.officialapp.ui.BarcodeScannerActivity.BarcodeScannerActivity;
import com.tommyfrenchbookmakers.officialapp.ui.BaseActivity;
import com.tommyfrenchbookmakers.officialapp.ui.TypeBarcodeActivity.TypeBarcodeActivity;
import com.tommyfrenchbookmakers.officialapp.ui.TextBetSlipActivity.TextBetSlipActivity;
import com.tommyfrenchbookmakers.officialapp.betslipobjects.BetSlip;
import com.tommyfrenchbookmakers.officialapp.singletons.BetSlipSingleton;

public class SelectionScreenActivity extends BaseActivity {

    public void launchActivity(final Class c) {
        View view = findViewById(R.id.main_content);
        view.animate().alpha(0f).setDuration(150);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                start(c);
            }
        }, 250);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_selection_screen, menu);
        return super.onCreateOptionsMenu(menu);
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_selection_screen);

    }

    @Override
    protected void onPostCreate(@Nullable Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);

        DrawerLayout drawerLayout = getDrawerLayout();

        ActionBarDrawerToggle actionBarDrawerToggle = new ActionBarDrawerToggle(
                SelectionScreenActivity.this,
                drawerLayout,
                getActionBarToolbar(),
                R.string.nav_drawer_opened,
                R.string.nav_drawer_closed
        );

        drawerLayout.setDrawerListener(actionBarDrawerToggle);
        actionBarDrawerToggle.syncState();
    }

    @Override
    protected int getSelfNavDrawerItem() {
        return NAVDRAWER_ITEM_HOME;
    }

    @Override
    protected void onResume() {
        super.onResume();
        View view = findViewById(R.id.main_content);
        view.setAlpha(1f);
    }
}
