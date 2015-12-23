package com.tommyfrenchbookmakers.officialapp.activities;

import android.Manifest;
import android.annotation.TargetApi;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
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
import com.tommyfrenchbookmakers.officialapp.activities.resultchecker.AccountInputActivity;
import com.tommyfrenchbookmakers.officialapp.activities.resultchecker.BarcodeScannerActivity;
import com.tommyfrenchbookmakers.officialapp.activities.resultchecker.TypeBarcodeActivity;
import com.tommyfrenchbookmakers.officialapp.activities.textbet.TextBetSlipActivity;
import com.tommyfrenchbookmakers.officialapp.betslipobjects.BetSlip;
import com.tommyfrenchbookmakers.officialapp.singletons.BetSlipSingleton;

public class SelectionScreenActivity extends AppCompatActivity {

    private DrawerLayout mDrawerLayout;
    private NavigationView mNavigationView;
    private Toolbar mToolbar;

    private void startTextBetActivity() {
        BetSlipSingleton.get(SelectionScreenActivity.this).setBetSlip(new BetSlip());
        launchActivity(TextBetSlipActivity.class);
    }

    private void launchActivity(Class c) {
        startActivity(new Intent(SelectionScreenActivity.this, c));
        mDrawerLayout.closeDrawers();
    }

    @TargetApi(Build.VERSION_CODES.M)
    private boolean checkForPermission(final int permissionCode, final String permission) {
        int check = ContextCompat.checkSelfPermission(SelectionScreenActivity.this, permission);

        if(check != PackageManager.PERMISSION_GRANTED) {
            if(ActivityCompat.shouldShowRequestPermissionRationale(SelectionScreenActivity.this, permission)) {
                AlertDialog.Builder builder = new AlertDialog.Builder(SelectionScreenActivity.this);
                switch (permissionCode) {
                    case Global.REQUEST_PERMISSION_SMS:
                        builder.setTitle("Request Permission to use SMS");
                        builder.setMessage("TFB App's Text Betting feature requires you to allow the app to send messages " +
                                "directly from the app to TFB. To use this feature, click Allow on the forthcoming " +
                                "pop-up.");
                        break;
                    case Global.REQUEST_PERMISSION_CAMERA:
                        builder.setTitle("Request Permission to use Camera");
                        builder.setMessage("In order to scan barcodes, this app must have access to the camera to view them. " +
                                "Please grant this permission in the forth coming dialog.");
                        break;
                }

                builder.setCancelable(false);
                builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        requestPermissions(new String[] {permission}, permissionCode);
                    }
                });
                builder.show();
            } else {
                requestPermissions(new String[] {permission}, permissionCode);
            }
            return false;
        }

        return true;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        boolean granted = grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED;
        switch (requestCode) {
            case Global.REQUEST_PERMISSION_SMS:
                if(granted) {
                    startTextBetActivity();
                } else {
                    Snackbar.make(mDrawerLayout, "Cannot start Text Bet, SMS permission not granted.", Snackbar.LENGTH_LONG)
                            .show();
                }
                break;
            case Global.REQUEST_PERMISSION_CAMERA:
                if(granted) {
                    launchActivity(BarcodeScannerActivity.class);
                } else {
                    Snackbar.make(mDrawerLayout, "Cannot start Barcode Scanner, SMS permission not granted.", Snackbar.LENGTH_LONG)
                            .show();
                }
                break;
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_selection_screen, menu);
        return super.onCreateOptionsMenu(menu);
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.home:
                Snackbar.make(mDrawerLayout, "Pressed.", Snackbar.LENGTH_SHORT).show();
                mDrawerLayout.openDrawer(GravityCompat.START);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sel_scr);

        mToolbar = (Toolbar) findViewById(R.id.toolbar_app_bar);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setHomeButtonEnabled(true);
//        getSupportActionBar().setDisplayHomeAsUpEnabled(false);

        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        mNavigationView = (NavigationView) findViewById(R.id.navigation_view);

        mNavigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.nav_textBet:
                        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                            if(checkForPermission(Global.REQUEST_PERMISSION_SMS, Manifest.permission.SEND_SMS)) {
                                startTextBetActivity();
                            }
                        } else {
                            startTextBetActivity();
                        }
                        return true;
                    case R.id.nav_checkResult:
                        launchActivity(AccountInputActivity.class);
                        return true;
                    case R.id.nav_scanBarcode:
                        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                            if(checkForPermission(Global.REQUEST_PERMISSION_CAMERA, Manifest.permission.CAMERA)) {
                                launchActivity(BarcodeScannerActivity.class);
                            }
                        } else {
                            launchActivity(BarcodeScannerActivity.class);
                        }
                        return true;
                    case R.id.nav_typeBarcode:
                        launchActivity(TypeBarcodeActivity.class);
                        return true;
                }

                return false;

            }
        });

        ActionBarDrawerToggle actionBarDrawerToggle =
                new ActionBarDrawerToggle(this, mDrawerLayout, mToolbar, R.string.nav_drawer_opened, R.string.nav_drawer_closed);

        mDrawerLayout.setDrawerListener(actionBarDrawerToggle);
        actionBarDrawerToggle.syncState();
    }

}
