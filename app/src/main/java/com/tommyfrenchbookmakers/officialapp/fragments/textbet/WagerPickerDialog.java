package com.tommyfrenchbookmakers.officialapp.fragments.textbet;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatDialogFragment;
import android.support.v7.widget.AppCompatCheckBox;
import android.support.v7.widget.AppCompatEditText;
import android.support.v7.widget.AppCompatSpinner;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;

import com.android.tighearnan.frenchsscanner.R;
import com.tommyfrenchbookmakers.officialapp.betslipobjects.BetSlip;
import com.tommyfrenchbookmakers.officialapp.betslipobjects.BetSlipSelection;
import com.tommyfrenchbookmakers.officialapp.enumerators.WagerType;
import com.tommyfrenchbookmakers.officialapp.interfaces.WagerConfirmedListener;
import com.tommyfrenchbookmakers.officialapp.singletons.BetSlipSingleton;

import java.util.ArrayList;

/**
 * Created by Tíghearnán on 06/09/2015.
 */
public class WagerPickerDialog extends AppCompatDialogFragment {

    private AppCompatEditText mStakeEditText;
    private AppCompatCheckBox mEachWayCheckBox;
    private AppCompatSpinner mWagersSpinner;
    private WagerConfirmedListener mWagerConfirmedListener;

    private int mNumberOfSelections;

    public static WagerPickerDialog newInstance(int numberOfSelections) {
        Bundle args = new Bundle();
        args.putInt("NUM_SELECT", numberOfSelections);
        WagerPickerDialog fragment = new WagerPickerDialog();
        fragment.setArguments(args);
        return fragment;
    }

    public void setWagerConfirmedListener(WagerConfirmedListener w) {
        mWagerConfirmedListener = w;
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        mNumberOfSelections = getArguments().getInt("NUM_SELECT");

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        View v = getActivity().getLayoutInflater().inflate(R.layout.dialog_new_wager, null);

        mStakeEditText = (AppCompatEditText) v.findViewById(R.id.appcompat_edit_text_dialogNewWagerStake);
        mStakeEditText.postDelayed(new Runnable() {
            @Override
            public void run() {
                InputMethodManager keyboard = (InputMethodManager)getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
                keyboard.showSoftInput(mStakeEditText, 0);
            }
        }, 50);
        mStakeEditText.requestFocus();

        mEachWayCheckBox = (AppCompatCheckBox) v.findViewById(R.id.appcompat_checkbox_dialogNewWagerEachWay);
        mWagersSpinner = (AppCompatSpinner) v.findViewById(R.id.appcompat_spinner_dialogNewWagerWagers);

        mWagersSpinner.setAdapter(new ArrayAdapter<WagerType>(getActivity(), R.layout.support_simple_spinner_dropdown_item, getWagerTypes()));
        mWagersSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (WagerType.getValueOf(mWagersSpinner.getSelectedItem().toString()).isSameRace()) {
                    if(mEachWayCheckBox.isEnabled()) {
                        mEachWayCheckBox.setChecked(false);
                        mEachWayCheckBox.setEnabled(false);
                    }
                } else {
                    if(!mEachWayCheckBox.isEnabled()) mEachWayCheckBox.setEnabled(true);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        builder.setView(v).setTitle("New Wager")
                .setPositiveButton("Add", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        mWagerConfirmedListener.onWagerConfirmed(mStakeEditText.getText().toString(), mEachWayCheckBox.isChecked(),
                                WagerType.getValueOf(mWagersSpinner.getSelectedItem().toString()));
                        dialog.dismiss();
                    }
                })
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        WagerPickerDialog.this.getDialog().cancel();
                    }
                });

        return builder.create();
    }

    private ArrayList<WagerType> getWagerTypes() {
        ArrayList<WagerType> wagerTypes = new ArrayList<>();

        BetSlip betslip = BetSlipSingleton.get(getActivity()).getBetSlip();

        for (WagerType w : WagerType.values()) {

            // Bare minimum for a bet to be considered.
            if (w.getMinNumberOfSelections() <= mNumberOfSelections) {

                // If the bet has a fixed number of selects and they're equal to the number number of
                // selections on the BetSlip
                if (w.getMinNumberOfSelections() == mNumberOfSelections && w.getHasFixedNumberOfSelections()) {
                    // If WagerType is for selections in the same event.
                    if(w.isSameRace()) {
                        // If all the selections are in the same event.
                        if(betslip.allInSameRace()) {
                            wagerTypes.add(w);
                        }
                    } else {

                        // If no selections are in the same event.
                        if(!betslip.hasSameRace()) {
                            wagerTypes.add(w);
                        }
                    }
                } else if (!w.getHasFixedNumberOfSelections()) {
                    // If each selection is in a different event.
                    if(!betslip.hasSameRace()) {
                        wagerTypes.add(w);
                    }
                }
            }
        }

        // If the bet has any selections in the same race, add SINGLE to the list of available
        // wager types as it would have been skipped over in the loop.
        if(betslip.hasSameRace()) wagerTypes.add(0, WagerType.SINGLE);
        return wagerTypes;
    }
}
