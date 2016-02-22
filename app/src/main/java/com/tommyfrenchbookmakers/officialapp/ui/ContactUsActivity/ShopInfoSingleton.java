package com.tommyfrenchbookmakers.officialapp.ui.ContactUsActivity;

import android.content.Context;

import com.tommyfrenchbookmakers.officialapp.singletons.BetSlipSingleton;

import java.util.ArrayList;

/**
 * Created by tighearnan on 21/02/16.
 */
class ShopInfoSingleton {

    private static ShopInfoSingleton sShopInfoSingleton;
    private Context mContext;
    private ArrayList<ShopInfo> mShopInfos;

    private ShopInfoSingleton(Context context) {
        mContext = context;
    }

    static ShopInfoSingleton get(Context c) {
        if(sShopInfoSingleton == null) {
            sShopInfoSingleton = new ShopInfoSingleton(c.getApplicationContext());
        }
        return sShopInfoSingleton;
    }

    ArrayList<ShopInfo> getShopInfos() {
        return mShopInfos;
    }

    void setShopInfos(ArrayList<ShopInfo> shopInfos) {
        mShopInfos = shopInfos;
    }
}
