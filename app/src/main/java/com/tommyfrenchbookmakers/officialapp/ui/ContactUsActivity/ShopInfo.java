package com.tommyfrenchbookmakers.officialapp.ui.ContactUsActivity;

import com.google.android.gms.maps.model.LatLng;

/**
 * Created by tighearnan on 21/02/16.
 */
public class ShopInfo {
    private String mName;
    private LatLng mLatLng;
    private String mPhoneNumber;

    public ShopInfo(String name, LatLng latLng, String phoneNumber) {
        mName = name;
        mLatLng = latLng;
        mPhoneNumber = phoneNumber;
    }

    public String getName() {
        return mName;
    }

    public LatLng getLatLng() {
        return mLatLng;
    }

    public String getPhoneNumber() {
        return mPhoneNumber;
    }
}
