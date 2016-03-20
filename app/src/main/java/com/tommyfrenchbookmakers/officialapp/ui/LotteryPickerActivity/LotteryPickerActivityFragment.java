package com.tommyfrenchbookmakers.officialapp.ui.LotteryPickerActivity;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.android.tighearnan.frenchsscanner.R;

/**
 * A placeholder fragment containing a simple view.
 */
public class LotteryPickerActivityFragment extends Fragment {

    public LotteryPickerActivityFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_lottery_picker, container, false);
    }
}
