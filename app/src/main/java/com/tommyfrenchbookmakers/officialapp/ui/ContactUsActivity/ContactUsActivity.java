package com.tommyfrenchbookmakers.officialapp.ui.ContactUsActivity;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;

import com.android.tighearnan.frenchsscanner.R;
import com.google.android.gms.maps.model.LatLng;

import java.util.ArrayList;

public class ContactUsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        ArrayList<ShopInfo> shopInfos = new ArrayList<>();
        shopInfos.add(new ShopInfo("Armagh", new LatLng(54.345488, -6.656665), "02845454545"));
        shopInfos.add(new ShopInfo("William Street", new LatLng(54.466068, -6.337399), "02838324545"));
        shopInfos.add(new ShopInfo("North Street", new LatLng(54.464843, -6.334288), "02854545454"));
        ShopInfoSingleton.get(this).setShopInfos(shopInfos);

        setContentView(R.layout.activity_contact_us);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

    }

}
