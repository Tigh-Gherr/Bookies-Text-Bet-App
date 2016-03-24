package com.tommyfrenchbookmakers.officialapp.ui.TextBetSlipActivity;

import android.Manifest;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.view.Menu;
import android.view.MenuItem;

import com.android.tighearnan.frenchsscanner.R;
import com.sothree.slidinguppanel.SlidingUpPanelLayout;
import com.tommyfrenchbookmakers.officialapp.betslipobjects.BetSlip;
import com.tommyfrenchbookmakers.officialapp.singletons.BetSlipSingleton;
import com.tommyfrenchbookmakers.officialapp.ui.BaseActivity;

public class TextBetSlipActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_text_bet_slip);

        checkForPermission(REQUEST_PERMISSION_SMS, Manifest.permission.SEND_SMS);
    }

    @Override
    protected int getSelfNavDrawerItem() {
        return NAVDRAWER_ITEM_TEXTBET;
    }

    // Create options menu
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_better_text_bet, menu);
        return true;
    }

    // Handle options menu selections
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.show_wagers:      // Open the wager panel
                TextBetSlipActivityFragment fragment = (TextBetSlipActivityFragment)getSupportFragmentManager().findFragmentById(R.id.fragment);
                fragment.expandPanel();
        }
        return super.onOptionsItemSelected(item);
    }

    // Handle back button presses.
    @Override
    public void onBackPressed() {
        TextBetSlipActivityFragment fragment = (TextBetSlipActivityFragment) getSupportFragmentManager().findFragmentById(R.id.fragment);
        // If the fragments sliding panel is opened, close it
        if (fragment.getPanelState() != SlidingUpPanelLayout.PanelState.COLLAPSED) {
            fragment.collapsePanel();
        } else {
            BetSlip betSlip = BetSlipSingleton.get(this).getBetSlip();
            // If there is a selection on the bet slip, warn the user that the bet slip will be erased.
            if (betSlip.getSelections().size() > 0) {
                new AlertDialog.Builder(this).setTitle("Bet not sent!")
                        .setMessage(getString(R.string.alert_dialog_body_leave_betslip_warning))
                        .setPositiveButton(getString(R.string.alert_dialog_button_leave), new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                TextBetSlipActivity.super.onBackPressed();
                            }
                        }).setNegativeButton(getString(R.string.alert_dialog_button_stay), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                }).show();
            } else {
                // Standard back button behaviour
                super.onBackPressed();
            }
        }
    }
}
