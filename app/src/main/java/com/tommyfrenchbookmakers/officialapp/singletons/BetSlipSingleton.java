package com.tommyfrenchbookmakers.officialapp.singletons;

import android.content.Context;

import com.tommyfrenchbookmakers.officialapp.betslipobjects.BetSlip;

/**
 * Created by Tíghearnán on 04/09/2015.
 */
public class BetSlipSingleton {

    private static BetSlipSingleton sBetSlipSingleton;
    private Context mAppContext;
    private BetSlip mBetSlip;

    private BetSlipSingleton(Context appContext) {
        mAppContext = appContext;
    }

    public static BetSlipSingleton get(Context c) {
        if (sBetSlipSingleton == null)
            sBetSlipSingleton = new BetSlipSingleton(c.getApplicationContext());
        return sBetSlipSingleton;
    }

    public BetSlip getBetSlip() {
        return mBetSlip;
    }

    public void setBetSlip(BetSlip betSlip) {
        mBetSlip = betSlip;
    }
//  Methods to get and set store Object.
}
