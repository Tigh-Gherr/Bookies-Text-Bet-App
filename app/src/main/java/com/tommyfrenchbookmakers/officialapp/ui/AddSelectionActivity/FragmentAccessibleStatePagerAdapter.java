package com.tommyfrenchbookmakers.officialapp.ui.AddSelectionActivity;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.view.ViewGroup;

import java.util.HashMap;

/**
 * Created by tighearnan on 22/03/16.
 */
public class FragmentAccessibleStatePagerAdapter extends FragmentStatePagerAdapter {
    private HashMap<Integer, Fragment> mFragments;

    public FragmentAccessibleStatePagerAdapter(FragmentManager fm) {
        super(fm);
        mFragments = new HashMap<>();
    }

    public Fragment getFragment(int position) {
        return mFragments.get(position);
    }

    @Override
    public Fragment getItem(int position) {
        return null;
    }

    @Override
    public int getCount() {
        return 0;
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        Object item = super.instantiateItem(container, position);
        if(item instanceof Fragment) {
            mFragments.put(position, (Fragment)item);
        }
        return item;
    }
}
