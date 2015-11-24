package com.tommyfrenchbookmakers.officialapp.singletons;

import android.content.Context;

import com.tommyfrenchbookmakers.officialapp.meetingobjects.Market;

import java.util.ArrayList;

/**
 * Created by Tíghearnán on 08/09/2015.
 */
public class MarketsSingleton {

    private static MarketsSingleton sMarketsSingleton;
    private Context mAppContext;
    private ArrayList<Market> mMarkets;

    private MarketsSingleton(Context appContext) {
        mAppContext = appContext;
    }

    public static MarketsSingleton get(Context c) {
        if (sMarketsSingleton == null)
            sMarketsSingleton = new MarketsSingleton(c.getApplicationContext());
        return sMarketsSingleton;
    }

    public ArrayList<Market> getMarkets() {
        return mMarkets;
    }

    public void setMarkets(ArrayList<Market> markets) {
        mMarkets = markets;
    }
}
