package com.tommyfrenchbookmakers.officialapp.ui.ContactUsActivity;

import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;

import com.android.tighearnan.frenchsscanner.R;
import com.google.android.gms.maps.model.LatLng;
import com.tommyfrenchbookmakers.officialapp.ui.BaseActivity;
import com.tommyfrenchbookmakers.officialapp.utils.ParseUtils;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

public class ContactUsActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contact_us);

//        overridePendingTransition(0, 0);
    }

    @Override
    protected int getSelfNavDrawerItem() {
        return NAVDRAWER_ITEM_CONTACT_US;
    }

}
