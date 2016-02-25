package com.tommyfrenchbookmakers.officialapp.ui;

import android.Manifest;
import android.annotation.TargetApi;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.SubMenu;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;

import com.android.tighearnan.frenchsscanner.R;
import com.tommyfrenchbookmakers.officialapp.ui.AccountAndReferenceInput.AccountAndReferenceInputActivity;
import com.tommyfrenchbookmakers.officialapp.ui.BarcodeScannerActivity.BarcodeScannerActivity;
import com.tommyfrenchbookmakers.officialapp.ui.ContactUsActivity.ContactUsActivity;
import com.tommyfrenchbookmakers.officialapp.ui.SelectionScreenActivity.SelectionScreenActivity;
import com.tommyfrenchbookmakers.officialapp.ui.TextBetSlipActivity.TextBetSlipActivity;
import com.tommyfrenchbookmakers.officialapp.ui.TypeBarcodeActivity.TypeBarcodeActivity;

/**
 * Created by tighearnan on 25/02/16.
 */
public abstract class BaseActivity extends AppCompatActivity {

    protected static final int NAVDRAWER_ITEM_INVALID = -1;
    protected static final int NAVDRAWER_ITEM_TEXTBET = R.id.navdrawer_item_textbet;
    protected static final int NAVDRAWER_ITEM_CHECK_RESULT = R.id.navdrawer_item_check_result;
    protected static final int NAVDRAWER_ITEM_SCAN_BARCODE = R.id.navdrawer_item_scan_barcode;
    protected static final int NAVDRAWER_ITEM_TYPE_BARCODE = R.id.navdrawer_item_type_barcode;
    protected static final int NAVDRAWER_ITEM_CONTACT_US = R.id.navdrawer_item_contact_us;

    protected static final int REQUEST_PERMISSION_SMS = 0;
    protected static final int REQUEST_PERMISSION_CAMERA = 1;

    // delay to launch nav drawer item, to allow close animation to play
    private static final int NAVDRAWER_LAUNCH_DELAY = 250;

    private static final int MAIN_CONTENT_FADEIN_DURATION = 250;
    private static final int MAIN_CONTENT_FADEOUT_DURATION = 150;

    private Toolbar mToolbar;
    private DrawerLayout mDrawerLayout;
    private NavigationView mNavDrawer;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        ActionBar ab = getSupportActionBar();
        if (ab != null) {
//            ab.setDisplayHomeAsUpEnabled(this instanceof ContactUsActivity);
        }
    }

    @Override
    protected void onPostCreate(@Nullable Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);

        setupNavDrawer();
        View mainContent = findViewById(R.id.main_content);

        if (mainContent != null) {
            mainContent.setAlpha(0f);
            mainContent.animate().alpha(1f).setDuration(MAIN_CONTENT_FADEIN_DURATION);
        }

    }

    @TargetApi(Build.VERSION_CODES.M)
    protected boolean checkForPermission(final int requestCode, final String permission) {
        int check = ContextCompat.checkSelfPermission(BaseActivity.this, permission);

        if (check == PackageManager.PERMISSION_GRANTED) {
            return true;
        }

        if (ActivityCompat.shouldShowRequestPermissionRationale(BaseActivity.this, permission)) {
            AlertDialog.Builder builder = new AlertDialog.Builder(BaseActivity.this);
            switch (requestCode) {
                case REQUEST_PERMISSION_SMS:
                    builder.setTitle("Request Permission to use SMS");
                    builder.setMessage("TFB App's Text Betting feature requires you to allow the app to send messages " +
                            "directly from the app to TFB. To use this feature, click Allow on the forthcoming " +
                            "pop-up.");
                    break;
                case REQUEST_PERMISSION_CAMERA:
                    builder.setTitle("Request Permission to use Camera");
                    builder.setMessage("In order to scan barcodes, this app must have access to the camera to view them. " +
                            "Please grant this permission in the forth coming dialog.");
                    break;
            }
            builder.setCancelable(false);
            builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    requestPermissions(new String[]{permission}, requestCode);
                }
            });
            builder.show();
        } else {
            requestPermissions(new String[]{permission}, requestCode);
        }

        return false;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        boolean granted = grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED;
        switch (requestCode) {
            case REQUEST_PERMISSION_SMS:
                if (granted) {
//                    start(TextBetSlipActivity.class);
                } else {
                    Snackbar.make(mDrawerLayout, "Cannot start Text Bet, SMS permission not granted.", Snackbar.LENGTH_LONG)
                            .show();
                    finish();
                }
                break;
            case REQUEST_PERMISSION_CAMERA:
                if (granted) {
//                    start(BarcodeScannerActivity.class);
                } else {
                    Snackbar.make(mDrawerLayout, "Cannot start Barcode Scanner, camera permission not granted.", Snackbar.LENGTH_LONG)
                            .show();
                    finish();
                }
                break;
        }
    }

    @Override
    public void setContentView(@LayoutRes int layoutResID) {
        super.setContentView(layoutResID);
        getActionBarToolbar();
    }

    protected Toolbar getActionBarToolbar() {
        if (mToolbar == null) {
            mToolbar = (Toolbar) findViewById(R.id.toolbar);
            if (mToolbar != null) {
                setSupportActionBar(mToolbar);
            }
        }

        return mToolbar;
    }

    protected int getSelfNavDrawerItem() {
        return NAVDRAWER_ITEM_INVALID;
    }

    private void setupNavDrawer() {
        int selfItem = getSelfNavDrawerItem();
        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);

        mNavDrawer = (NavigationView) mDrawerLayout.findViewById(R.id.navigation_view);

        if (selfItem == NAVDRAWER_ITEM_INVALID) {
            if (mNavDrawer != null) {
                ((ViewGroup) mNavDrawer.getParent()).removeView(mNavDrawer);
            }
            mDrawerLayout = null;
            return;
        }

        mNavDrawer.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(MenuItem item) {
                return onNavDrawerItemClicked(item.getItemId());
            }
        });

        if (mToolbar != null && getSelfNavDrawerItem() == NAVDRAWER_ITEM_INVALID) {
            mToolbar.setNavigationOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mDrawerLayout.openDrawer(GravityCompat.START);
                }
            });
        }

        mDrawerLayout.setDrawerListener(new DrawerLayout.DrawerListener() {
            @Override
            public void onDrawerSlide(View drawerView, float slideOffset) {
                onNavDrawerSlide(slideOffset);
            }

            @Override
            public void onDrawerOpened(View drawerView) {
                if (BaseActivity.this instanceof TypeBarcodeActivity
                        || BaseActivity.this instanceof AccountAndReferenceInputActivity) {
                    InputMethodManager imm = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(drawerView.getWindowToken(), 0);
                }
            }

            @Override
            public void onDrawerClosed(View drawerView) {

            }

            @Override
            public void onDrawerStateChanged(int newState) {

            }
        });

        setSelectedDrawerItem(getSelfNavDrawerItem());
    }

    private boolean onNavDrawerItemClicked(final int itemId) {
        if (itemId == getSelfNavDrawerItem()) {
            mDrawerLayout.closeDrawer(GravityCompat.START);
            return true;
        }

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                goToDrawerItem(itemId);
            }
        }, NAVDRAWER_LAUNCH_DELAY);

        View mainContent = findViewById(R.id.main_content);
        if (mainContent != null) {
            mainContent.animate().alpha(0f).setDuration(MAIN_CONTENT_FADEOUT_DURATION);
        }

        mDrawerLayout.closeDrawer(GravityCompat.START);
        return true;
    }

    private void setSelectedDrawerItem(int itemId) {
        if (mNavDrawer != null) {
            int size = mNavDrawer.getMenu().size();
            for (int i = 0; i < size; i++) {
                SubMenu menu = mNavDrawer.getMenu().getItem(i).getSubMenu();

                int subSize = menu.size();
                for (int j = 0; j < subSize; j++) {
                    MenuItem item = menu.getItem(j);
                    item.setChecked(item.getItemId() == getSelfNavDrawerItem());
                }
            }
        }
    }

    private void goToDrawerItem(int itemId) {
        switch (itemId) {
            case NAVDRAWER_ITEM_TEXTBET:
                start(TextBetSlipActivity.class);
                break;
            case NAVDRAWER_ITEM_CHECK_RESULT:
                start(AccountAndReferenceInputActivity.class);
                break;
            case NAVDRAWER_ITEM_SCAN_BARCODE:
                start(BarcodeScannerActivity.class);
                break;
            case NAVDRAWER_ITEM_TYPE_BARCODE:
                start(TypeBarcodeActivity.class);
                break;
            case NAVDRAWER_ITEM_CONTACT_US:
                start(ContactUsActivity.class);
                break;
        }
    }

    private void start(Class c) {
        startActivity(new Intent(this, c));
        finish();
    }

    protected void onNavDrawerSlide(float offset) {

    }

}
