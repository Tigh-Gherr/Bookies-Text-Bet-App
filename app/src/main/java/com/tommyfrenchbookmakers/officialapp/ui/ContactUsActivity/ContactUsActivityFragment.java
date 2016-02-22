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

//    private List<MapView> mOfficeMapViews;
//    private List<ExpandableRelativeLayout> mExpandableRelativeLayouts;
//    private List<ImageButton> mToggleButtons;
    private RecyclerView mOfficeRecyclerView;
    private RecyclerView.Adapter mOfficeAdapter;

    public ContactUsActivityFragment() {

    }

    @Override
    public View onCreateView(LayoutInflater inflater, final ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_contact_us, container, false);

/*
        mOfficeMapViews = Arrays.asList(
                (MapView)v.findViewById(R.id.map_view_williamStreet),
                (MapView)v.findViewById(R.id.map_view_northStreet),
                (MapView)v.findViewById(R.id.map_view_armagh)
        );

        for(int i = 0; i < mOfficeMapViews.size(); i++) {
            MapView mapView = mOfficeMapViews.get(i);
            mapView.getMapAsync(new OnMapReadyCallback() {
                @Override
                public void onMapReady(GoogleMap googleMap) {
                    googleMap.addMarker(new MarkerOptions().position(googleMap.getCameraPosition().target));
                }
            });
        }

        mExpandableRelativeLayouts = Arrays.asList(
                (ExpandableRelativeLayout)v.findViewById(R.id.expandable_layout_willamStreetMap),
                (ExpandableRelativeLayout)v.findViewById(R.id.expandable_layout_northStreetMap),
                (ExpandableRelativeLayout)v.findViewById(R.id.expandable_layout_armaghMap)
        );

        for(int i = 0; i < mExpandableRelativeLayouts.size(); i++) {
            ExpandableRelativeLayout layout = mExpandableRelativeLayouts.get(i);
            final int finalI = i;
            layout.setListener(new ExpandableLayoutListener() {
                @Override
                public void onAnimationStart() {

                }

                @Override
                public void onAnimationEnd() {

                }

                @Override
                public void onPreOpen() {
                    mOfficeMapViews.get(finalI).onResume();
                }

                @Override
                public void onPreClose() {

                }

                @Override
                public void onOpened() {

                }

                @Override
                public void onClosed() {
                    mOfficeMapViews.get(finalI).onPause();
                }
            });
        }

        mToggleButtons = Arrays.asList(
                (ImageButton)v.findViewById(R.id.image_button_toggleWilliamStreetMap),
                (ImageButton)v.findViewById(R.id.image_button_toggleNorthStreetMap),
                (ImageButton)v.findViewById(R.id.image_button_toggleArmagh)
        );

        for(int i = 0; i < mOfficeMapViews.size(); i++) {
            final int finalI = i;
            mToggleButtons.get(i).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mExpandableRelativeLayouts.get(finalI).toggle();
                    ImageButton toggle = mToggleButtons.get(finalI);
                    toggle.setSelected(!toggle.isSelected());
                }
            });
        }

        for(MapView mv : mOfficeMapViews) mv.onCreate(savedInstanceState);
*/

        // TODO: Set up RecyclerView and Adapter
        /*
        * mOfficeRecyclerView = (RecyclerView)v.findViewById(R.id.officeRecyclerView);
        * mOfficeRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        * mOfficeAdapter = new OfficeAdapter(ShopInfoSingle.get(getActivity()).getShopInfos());
        * mOfficeRecyclerView.setAdapter(mOfficeAdapter);
        * */

        mOfficeRecyclerView = (RecyclerView) v.findViewById(R.id.recycler_view_shopInfo);
        mOfficeRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        mOfficeAdapter = new ShopInfoAdapter(ShopInfoSingleton.get(getActivity()).getShopInfos(), savedInstanceState);
        mOfficeRecyclerView.setAdapter(mOfficeAdapter);

        return v;
    }

    @Override
    public void onResume() {
        super.onResume();
//        for(MapView mv : mOfficeMapViews) mv.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
//        for(MapView mv : mOfficeMapViews) mv.onPause();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
//        for(MapView mv : mOfficeMapViews) mv.onDestroy();
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
//        for(MapView mv : mOfficeMapViews) mv.onLowMemory();
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
//        for(MapView mv : mOfficeMapViews) mv.onSaveInstanceState(outState);
    }
}
