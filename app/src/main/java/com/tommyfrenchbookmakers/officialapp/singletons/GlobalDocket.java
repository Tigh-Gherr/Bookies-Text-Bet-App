package com.tommyfrenchbookmakers.officialapp.singletons;

import android.content.Context;

import com.tommyfrenchbookmakers.officialapp.docketobjects.Docket;

/**
 * Created by Tíghearnán on 15/07/2015.
 */
public class GlobalDocket {

    private Docket mDocket;
    private static GlobalDocket sGlobalDocket;
    private Context mAppContext;

    private GlobalDocket(Context appContext) {
        mAppContext = appContext;
    }

    public static GlobalDocket get(Context c) {
        if (sGlobalDocket == null) sGlobalDocket = new GlobalDocket(c.getApplicationContext());
        return sGlobalDocket;
    }

    public void setDocket(Docket docket) {
        mDocket = docket;
    }

    public Docket getDocket() {
        return mDocket;
    }
}