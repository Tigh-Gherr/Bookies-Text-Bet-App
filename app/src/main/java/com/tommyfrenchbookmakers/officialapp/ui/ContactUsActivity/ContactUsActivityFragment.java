package com.tommyfrenchbookmakers.officialapp.ui.ContactUsActivity;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;

import com.android.tighearnan.frenchsscanner.R;
import com.github.aakira.expandablelayout.ExpandableLayoutListener;
import com.github.aakira.expandablelayout.ExpandableRelativeLayout;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.Arrays;
import java.util.List;

/**
 * A placeholder fragment containing a simple view.
 */
public class ContactUsActivityFragment extends Fragment {

    private RecyclerView mOfficeRecyclerView;
    private RecyclerView.Adapter mOfficeAdapter;

    public ContactUsActivityFragment() {

    }

    @Override
    public View onCreateView(LayoutInflater inflater, final ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_contact_us, container, false);

        mOfficeRecyclerView = (RecyclerView) v.findViewById(R.id.recycler_view_shopInfo);
        mOfficeRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        mOfficeAdapter = new ShopInfoAdapter(ShopInfoSingleton.get(getActivity()).getShopInfos(), savedInstanceState);
        mOfficeRecyclerView.setAdapter(mOfficeAdapter);
        mOfficeRecyclerView.setHasFixedSize(true);

        return v;
    }
}
