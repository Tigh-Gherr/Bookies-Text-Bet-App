package com.tommyfrenchbookmakers.officialapp.ui.LotteryPickerActivity;

import android.os.Bundle;
import android.support.annotation.LayoutRes;

import com.android.tighearnan.frenchsscanner.R;
import com.tommyfrenchbookmakers.officialapp.ui.BaseActivity;

public class LotteryPickerActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lottery_picker);

    }

    @Override
    public void setContentView(@LayoutRes int layoutResID) {
        super.setContentView(layoutResID);
    }

    @Override
    protected int getSelfNavDrawerItem() {
        return NAVDRAWER_ITEM_LOTTERY_PICKER;
    }
}
