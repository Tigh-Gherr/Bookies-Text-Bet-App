package com.tommyfrenchbookmakers.officialapp.ui.ContactUsActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.TextView;

import com.android.tighearnan.frenchsscanner.R;
import com.github.aakira.expandablelayout.ExpandableLayoutListener;
import com.github.aakira.expandablelayout.ExpandableRelativeLayout;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.GoogleMapOptions;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.ArrayList;

/**
 * Created by tighearnan on 21/02/16.
 */
public class ShopInfoAdapter extends RecyclerView.Adapter<ShopInfoAdapter.ShopInfoViewHolder> {

    private ArrayList<ShopInfo> mShopInfos;
    private Bundle mSavedInstanceState;

    public ShopInfoAdapter(ArrayList<ShopInfo> shopInfos, Bundle savedInstanceState) {
        mShopInfos = shopInfos;
        mSavedInstanceState = savedInstanceState;
    }

    @Override
    public ShopInfoViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.recycler_item_shop, parent, false);

        return new ShopInfoViewHolder(v);
    }

    @Override
    public void onBindViewHolder(ShopInfoViewHolder holder, int position) {
        ShopInfo shop = mShopInfos.get(position);
        holder.mName.setText(shop.getName());
        holder.mPhoneNumber.setText(shop.getPhoneNumber());
    }

    @Override
    public int getItemCount() {
        return mShopInfos.size();
    }



    public class ShopInfoViewHolder extends RecyclerView.ViewHolder {

        TextView mName;
        TextView mPhoneNumber;

        ImageButton mToggleMoreInfo;
        ImageButton mCallShop;

        ExpandableRelativeLayout mExpandableLayout;

        MapView mMap;

        public ShopInfoViewHolder(final View itemView) {
            super(itemView);

            mName = (TextView) itemView.findViewById(R.id.text_view_shopName);

            FrameLayout container = (FrameLayout) itemView.findViewById(R.id.frame_layout_shopName);
//            container.setClickable(true);
            container.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mExpandableLayout.toggle();
                }
            });

            mToggleMoreInfo = (ImageButton) itemView.findViewById(R.id.image_button_toggleMoreInfo);

            mExpandableLayout = (ExpandableRelativeLayout) itemView.findViewById(R.id.expandable_layout_shopMoreInfo);
            mExpandableLayout.setListener(new ExpandableLayoutListener() {
                @Override
                public void onAnimationStart() {

                }

                @Override
                public void onAnimationEnd() {

                }

                @Override
                public void onPreOpen() {
//                    mMap.setVisibility(View.VISIBLE);
                    mMap.onResume();
                    mToggleMoreInfo.setSelected(true);
                }

                @Override
                public void onPreClose() {
                    mToggleMoreInfo.setSelected(false);
                }

                @Override
                public void onOpened() {

                }

                @Override
                public void onClosed() {
                    mMap.onPause();
                }
            });

            mMap = (MapView) itemView.findViewById(R.id.map_view_shop);
            mMap.onCreate(null);
            mMap.getMapAsync(new OnMapReadyCallback() {
                @Override
                public void onMapReady(GoogleMap googleMap) {
                    MapsInitializer.initialize(itemView.getContext().getApplicationContext());
                    ShopInfo shop = mShopInfos.get(getLayoutPosition());
                    googleMap.addMarker(new MarkerOptions().position(shop.getLatLng()));
                    googleMap.moveCamera(CameraUpdateFactory.newLatLng(shop.getLatLng()));
                }
            });

            mPhoneNumber = (TextView) itemView.findViewById(R.id.text_view_shopNumber);

            mCallShop = (ImageButton) itemView.findViewById(R.id.image_button_callShop);
            mCallShop.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(Intent.ACTION_DIAL);
                    intent.setData(Uri.parse("tel:" + mShopInfos.get(getAdapterPosition()).getPhoneNumber()));
                    itemView.getContext().startActivity(intent);
                }
            });

        }
    }
}
