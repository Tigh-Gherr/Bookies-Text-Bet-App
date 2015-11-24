package com.tommyfrenchbookmakers.officialapp.fragments.resultchecker;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.android.tighearnan.frenchsscanner.R;
import com.sothree.slidinguppanel.SlidingUpPanelLayout;
import com.tommyfrenchbookmakers.officialapp.singletons.GlobalDocket;
import com.tommyfrenchbookmakers.officialapp.docketobjects.Docket;
import com.tommyfrenchbookmakers.officialapp.docketobjects.DocketBet;
import com.tommyfrenchbookmakers.officialapp.recyclerviewadapters.DocketSelectionsAdapter;
import com.tommyfrenchbookmakers.officialapp.recyclerviewadapters.DocketWagersAdapter;

import java.text.DecimalFormat;


public class ResultActivityFragment extends Fragment {

    private Docket mDocket;
    private DocketBet mBet;

    private TextView mBetStakeTextView;
    private TextView mBarcodeTextView;

    private TextView mBetPayoutTextView;
    private CardView mSlideLayoutCardView;
    private TextView mMoreTextView;
    private TextView mLessTextView;

    public SlidingUpPanelLayout mPanelLayout;

    private RecyclerView mSelectionsRecyclerView;
    private RecyclerView.Adapter mSelectionsAdapter;

    private RecyclerView mWagersRecyclerView;
    private RecyclerView.Adapter mWagersAdapter;
    private LinearLayoutManager mWagersLayoutManager;

    public ResultActivityFragment() {
    }

    public static ResultActivityFragment newInstance(int position) {
        Bundle bundle = new Bundle();
        bundle.putInt("BET_POSITION", position);

        ResultActivityFragment fragment = new ResultActivityFragment();
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);

        int position = getArguments().getInt("BET_POSITION");

        mDocket = GlobalDocket.get(getActivity()).getDocket();
        if(mDocket == null) getActivity().finish();
        mBet = mDocket.getBets().get(position);

    }



    @Override
    public View onCreateView(final LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_result, container, false);

        mSelectionsRecyclerView = (RecyclerView) v.findViewById(R.id.recycler_view_selections);
        mSelectionsRecyclerView.setAlpha(0f);
        mSelectionsRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        mSelectionsAdapter = new DocketSelectionsAdapter(mBet, getActivity());
        mSelectionsRecyclerView.setAdapter(mSelectionsAdapter);
        mSelectionsRecyclerView.setHasFixedSize(true);

        /*******************************************************************************************
         ***************************       SlidingPanelLayout Setup       ***************************
         *******************************************************************************************/

        mPanelLayout = (SlidingUpPanelLayout) v.findViewById(R.id.sliding_panel_selections);
        mPanelLayout.setAlpha(0f);
        mPanelLayout.setCoveredFadeColor(Color.TRANSPARENT);
        mPanelLayout.setPanelSlideListener(new SlidingUpPanelLayout.PanelSlideListener() {
            @Override
            public void onPanelSlide(View view, float v) {
                if (v <= 0.40f) {
                    mMoreTextView.setAlpha((0.40f - v) * 2.5f);
                    mLessTextView.setAlpha(0);
                } else if (v >= 0.60f) {
                    mLessTextView.setAlpha((v - 0.60f) * 2.5f);
                    mMoreTextView.setAlpha(0);
                } else {
                    mLessTextView.setAlpha(0);
                    mMoreTextView.setAlpha(0);
                }

                float fadeStartRV = 0.6f;
                if (v >= fadeStartRV) {
                    mWagersRecyclerView.setAlpha((v - fadeStartRV) * (1 / (1 - fadeStartRV)));
                } else {
                    mWagersRecyclerView.setAlpha(0f);
                }


                float fadeStartTV = 0.35f;
                if (v >= fadeStartTV) {
                    mBarcodeTextView.setTranslationX(-(100 - (v * 100)));
                    mBarcodeTextView.setAlpha((v - fadeStartTV) * (1 / (1 - fadeStartTV)));
                    mBetStakeTextView.setTranslationX(-(200 - (v * 200)));
                    mBetStakeTextView.setAlpha((v - fadeStartTV) * (1 / (1 - fadeStartTV)));
                } else {
                    mBetStakeTextView.setAlpha(0);
                    mBarcodeTextView.setAlpha(0);
                }

            }

            @Override
            public void onPanelCollapsed(View view) {
                mMoreTextView.setAlpha(1);
                mLessTextView.setAlpha(0);
                mWagersRecyclerView.scrollToPosition(0);
            }

            @Override
            public void onPanelExpanded(View view) {
                mMoreTextView.setAlpha(0);
                mLessTextView.setAlpha(1);
                mWagersRecyclerView.setAlpha(1);
            }

            @Override
            public void onPanelAnchored(View view) {

            }

            @Override
            public void onPanelHidden(View view) {

            }
        });

        DecimalFormat df = new DecimalFormat("0.00");
        mBetPayoutTextView = (TextView) v.findViewById(R.id.text_view_betPayoutNumeric);
        mBetPayoutTextView.setText("£" + df.format(mBet.getBetPayout()));

        mBarcodeTextView = (TextView) v.findViewById(R.id.text_view_barcode);
        mBarcodeTextView.setText(mBarcodeTextView.getText() + " " + mDocket.getBarcode());

        mBetStakeTextView = (TextView) v.findViewById(R.id.text_view_betStake);
        mBetStakeTextView.setText(mBetStakeTextView.getText() + df.format(mBet.getBetStake()));

        mMoreTextView = (TextView) v.findViewById(R.id.text_view_more);
        mLessTextView = (TextView) v.findViewById(R.id.text_view_less);
        mLessTextView.setAlpha(0);

        mSlideLayoutCardView = (CardView) v.findViewById(R.id.card_view_more);
        mSlideLayoutCardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mPanelLayout.getPanelState() == SlidingUpPanelLayout.PanelState.COLLAPSED) {
                    mPanelLayout.setPanelState(SlidingUpPanelLayout.PanelState.EXPANDED);
                } else {
                    mPanelLayout.setPanelState(SlidingUpPanelLayout.PanelState.COLLAPSED);
                }
            }
        });

        mWagersLayoutManager = new LinearLayoutManager(getActivity());
        mWagersLayoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
        mWagersAdapter = new DocketWagersAdapter(mBet.getWagers());

        mWagersRecyclerView = (RecyclerView) v.findViewById(R.id.recycler_view_wagerWinnings);
        mWagersRecyclerView.setLayoutManager(mWagersLayoutManager);
        mWagersRecyclerView.setAdapter(mWagersAdapter);
        mWagersRecyclerView.setHasFixedSize(true);

        mPanelLayout.animate().alpha(1f);
        mSelectionsRecyclerView.animate().alpha(1f);

        return v;
    }



    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
    }
}