package com.tommyfrenchbookmakers.officialapp.utils;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;

import com.android.tighearnan.frenchsscanner.R;
import com.tommyfrenchbookmakers.officialapp.Global;
import com.tommyfrenchbookmakers.officialapp.ui.SelectionScreenActivity.SelectionScreenActivity;
import com.tommyfrenchbookmakers.officialapp.ui.AccountAndReferenceInput.AccountAndReferenceInputActivity;
import com.tommyfrenchbookmakers.officialapp.ui.BarcodeScannerActivity.BarcodeScannerActivity;
import com.tommyfrenchbookmakers.officialapp.ui.TypeBarcodeActivity.TypeBarcodeActivity;
import com.tommyfrenchbookmakers.officialapp.ui.TextBetSlipActivity.TextBetSlipActivity;
import com.tommyfrenchbookmakers.officialapp.betslipobjects.BetSlip;
import com.tommyfrenchbookmakers.officialapp.singletons.BetSlipSingleton;

/**
 * Created by Tíghearnán on 17/12/2015.
 */
public final class NavigationUtils {
    private static boolean userHasGrantedPermission(int permissionCode, String permission, final Context context) {
        int check = ContextCompat.checkSelfPermission(context, permission);

        if(check != PackageManager.PERMISSION_GRANTED) {
            if(ActivityCompat.shouldShowRequestPermissionRationale((Activity) context, permission)) {
                AlertDialog.Builder builder = new AlertDialog.Builder(context);
                switch (permissionCode) {
                    case Global.REQUEST_PERMISSION_SMS:
                        builder.setTitle("Request Permission to use SMS");
                        builder.setMessage("TFB App's Text Betting feature requires you to allow the app to send messages " +
                                "directly from the app to TFB. To use this feature, click Allow on the forthcoming " +
                                "pop-up.");
                        break;
                    case Global.REQUEST_PERMISSION_CAMERA:
                        builder.setTitle("Request Permission to use Camera");
                        builder.setMessage("In order to scan barcodes, this app must have access to the camera to view them. " +
                                "Please grant this permission in the forth coming dialog.");
                        break;
                }
                builder.setCancelable(false);
                builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
            }
            return false;
        }
        return true;
    }

    public static void onNavigationMenuItemPressed(int itemId, Context c) {
        switch (itemId) {
            case R.id.nav_textBet:
                if(BetSlipSingleton.get(c).getBetSlip() == null) {
                    BetSlipSingleton.get(c).setBetSlip(new BetSlip());
                }
                c.startActivity(new Intent(c, TextBetSlipActivity.class));
                break;
            case R.id.nav_typeBarcode:
                c.startActivity(new Intent(c, TypeBarcodeActivity.class));
                break;
            case R.id.nav_scanBarcode:
                c.startActivity(new Intent(c, BarcodeScannerActivity.class));
                break;
            case R.id.nav_checkResult:
                c.startActivity(new Intent(c, AccountAndReferenceInputActivity.class));
                break;
        }
//
//        Activity activity = (Activity) c;
//        if(!(activity instanceof SelectionScreenActivity)) {
//            activity.finish();
//        }
    }
}
