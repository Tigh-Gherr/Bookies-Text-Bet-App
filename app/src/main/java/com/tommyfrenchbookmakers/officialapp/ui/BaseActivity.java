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
import android.view.Menu;
import android.view.MenuItem;
import android.view.SubMenu;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;

import com.android.tighearnan.frenchsscanner.R;
import com.tommyfrenchbookmakers.officialapp.Global;
import com.tommyfrenchbookmakers.officialapp.ui.AccountAndReferenceInput.AccountAndReferenceInputActivity;
import com.tommyfrenchbookmakers.officialapp.ui.BarcodeScannerActivity.BarcodeScannerActivity;
import com.tommyfrenchbookmakers.officialapp.ui.ContactUsActivity.ContactUsActivity;
import com.tommyfrenchbookmakers.officialapp.ui.LotteryPickerActivity.LotteryPickerActivity;
import com.tommyfrenchbookmakers.officialapp.ui.SelectionScreenActivity.SelectionScreenActivity;
import com.tommyfrenchbookmakers.officialapp.ui.TextBetSlipActivity.TextBetSlipActivity;
import com.tommyfrenchbookmakers.officialapp.ui.TypeBarcodeActivity.TypeBarcodeActivity;

/**
 * Created by tighearnan on 25/02/16.
 */
public abstract class BaseActivity extends AppCompatActivity {

    protected static final int NAVDRAWER_ITEM_INVALID = -1;
    protected static final int NAVDRAWER_ITEM_HOME = 0;
    protected static final int NAVDRAWER_ITEM_TEXTBET = R.id.navdrawer_item_textbet;
    protected static final int NAVDRAWER_ITEM_CHECK_RESULT = R.id.navdrawer_item_check_result;
    protected static final int NAVDRAWER_ITEM_SCAN_BARCODE = R.id.navdrawer_item_scan_barcode;
    protected static final int NAVDRAWER_ITEM_TYPE_BARCODE = R.id.navdrawer_item_type_barcode;
    protected static final int NAVDRAWER_ITEM_CONTACT_US = R.id.navdrawer_item_contact_us;
    protected static final int NAVDRAWER_ITEM_LOTTERY_PICKER = R.id.navdrawer_item_lottery;

    protected static final int REQUEST_PERMISSION_SMS = 0;
    protected static final int REQUEST_PERMISSION_CAMERA = 1;

    // delay to launch nav drawer item, to allow close animation to play
    private static final int NAVDRAWER_LAUNCH_DELAY = 200;

    private static final float HIDE_KEYBOARD_NAVDRAWER_OFFSET = 0.25f;

    private Toolbar mToolbar;
    private DrawerLayout mDrawerLayout;
    private NavigationView mNavDrawer;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        ActionBar ab = getSupportActionBar();
        if (ab != null) {
            ab.setHomeButtonEnabled(true);
            ab.setDisplayHomeAsUpEnabled(!isSelectionScreen());
        }
    }

    @Override
    protected void onPostCreate(@Nullable Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);

        setupNavDrawer();
    }

    private boolean isSelectionScreen() {
        return this instanceof SelectionScreenActivity;
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
                    builder.setTitle(getString(R.string.alert_dialog_title_request_permission_sms));
                    builder.setMessage(getString(R.string.alert_dialog_body_request_permission_sms));
                    break;
                case REQUEST_PERMISSION_CAMERA:
                    builder.setTitle(getString(R.string.alert_dialog_title_request_permission_camera));
                    builder.setMessage(getString(R.string.alert_dialog_body_request_permission_camera));
                    break;
            }
            builder.setCancelable(false);
            builder.setPositiveButton(R.string.alert_dialog_button_ok, new DialogInterface.OnClickListener() {
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
                    Snackbar.make(mDrawerLayout,
                            R.string.permission_denied_sms,
                            Snackbar.LENGTH_LONG)
                            .show();
                    finish();
                }
                break;
            case REQUEST_PERMISSION_CAMERA:
                if (granted) {
                    start(BarcodeScannerActivity.class);
                } else {
                    Snackbar.make(mDrawerLayout,
                            R.string.permission_denied_camera,
                            Snackbar.LENGTH_LONG)
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

        if (mDrawerLayout != null) {
            mNavDrawer = (NavigationView) mDrawerLayout.findViewById(R.id.navigation_view);
        }

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

        mDrawerLayout.addDrawerListener(new DrawerLayout.DrawerListener() {
            @Override
            public void onDrawerSlide(View drawerView, float slideOffset) {
                onNavDrawerSlide(drawerView, slideOffset);
            }

            @Override
            public void onDrawerOpened(View drawerView) {

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

        mDrawerLayout.closeDrawer(GravityCompat.START);
        return true;
    }

    private void setSelectedDrawerItem(int itemId) {
        if (mNavDrawer != null) {
            Menu navMenu = mNavDrawer.getMenu();
            for (int i = 0; i < navMenu.size(); i++) {
                SubMenu section = navMenu.getItem(i).getSubMenu();

                for (int j = 0; j < section.size(); j++) {
                    MenuItem item = section.getItem(j);
                    item.setChecked(item.getItemId() == itemId);
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
                boolean permissionGranted =
                        checkForPermission(Global.REQUEST_PERMISSION_CAMERA,
                                            Manifest.permission.CAMERA);
                if(permissionGranted) {
                    start(BarcodeScannerActivity.class);
                }
                break;
            case NAVDRAWER_ITEM_TYPE_BARCODE:
                start(TypeBarcodeActivity.class);
                break;
            case NAVDRAWER_ITEM_CONTACT_US:
                start(ContactUsActivity.class);
                break;
            case NAVDRAWER_ITEM_LOTTERY_PICKER:
                start(LotteryPickerActivity.class);
                break;
        }
    }

    protected void start(Class c) {
        startActivity(new Intent(this, c));
        if(!isSelectionScreen()) {
            finish();
        }
    }

    protected void onNavDrawerSlide(View drawerView, float offset) {
        if (offset > HIDE_KEYBOARD_NAVDRAWER_OFFSET && (BaseActivity.this instanceof TypeBarcodeActivity
                || BaseActivity.this instanceof AccountAndReferenceInputActivity)) {
            InputMethodManager imm = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(drawerView.getWindowToken(), 0);

        }
    }

    protected DrawerLayout getDrawerLayout() {
        return mDrawerLayout;
    }
}
