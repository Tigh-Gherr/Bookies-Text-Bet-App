package com.tommyfrenchbookmakers.officialapp.activities.textbet;

import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;

import com.android.tighearnan.frenchsscanner.R;
import com.sothree.slidinguppanel.SlidingUpPanelLayout;
import com.tommyfrenchbookmakers.officialapp.betslipobjects.BetSlip;
import com.tommyfrenchbookmakers.officialapp.fragments.textbet.TextBetSlipActivityFragment;
import com.tommyfrenchbookmakers.officialapp.singletons.BetSlipSingleton;

public class TextBetSlipActivity extends AppCompatActivity {

    // Set up Activity
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_better_text_bet);
        Toolbar toolbar = (Toolbar) findViewById(R.id.app_bar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
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
                fragment.setPanelState(SlidingUpPanelLayout.PanelState.EXPANDED);
        }
        return super.onOptionsItemSelected(item);
    }

    // Handle back button presses.
    @Override
    public void onBackPressed() {
        TextBetSlipActivityFragment fragment = (TextBetSlipActivityFragment) getSupportFragmentManager().findFragmentById(R.id.fragment);
        // If the fragments sliding panel is opened, close it
        if (fragment.getPanelState() != SlidingUpPanelLayout.PanelState.COLLAPSED) {
            fragment.setPanelState(SlidingUpPanelLayout.PanelState.COLLAPSED);
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
