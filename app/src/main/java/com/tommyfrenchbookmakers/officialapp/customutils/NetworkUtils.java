package com.tommyfrenchbookmakers.officialapp.customutils;

import android.content.Context;
import android.net.ConnectivityManager;

/**
 * Created by Tíghearnán on 30/08/2015.
 */
public class NetworkUtils {

    // Used to check if the device is connected to the internet.
    public static boolean networkIsAvailable(Context c) {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) c.getSystemService(Context.CONNECTIVITY_SERVICE);
        android.net.NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null;
    }

}
