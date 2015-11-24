package com.tommyfrenchbookmakers.officialapp.recyclerviewadapters;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.android.tighearnan.frenchsscanner.R;
import com.tommyfrenchbookmakers.officialapp.betslipobjects.BetSlipWager;

import java.text.DecimalFormat;
import java.util.ArrayList;

/**
 * Created by Tíghearnán on 05/10/2015.
 */
public class BetSlipWagersAdapter extends RecyclerView.Adapter<BetSlipWagersAdapter.BetterWagersViewHolder> {

    private ArrayList<BetSlipWager> mWagers;

    public BetSlipWagersAdapter(ArrayList<BetSlipWager> wagers) {
        mWagers = wagers;
    }


    @Override
    public BetterWagersViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.recycler_item_betslip_wager, parent, false);

        return new BetterWagersViewHolder(v);
    }

    @Override
    public void onBindViewHolder(BetterWagersViewHolder holder, int position) {
        BetSlipWager wager = mWagers.get(position);
        holder.mStake.setText("£" + wager.getUnitStake() + (wager.isEachWay() ? " ew" : ""));
        holder.mWagerType.setText(wager.getWagerType().getName());

        DecimalFormat df = new DecimalFormat("0.00");
        holder.mPotentialReturns.setText("£" + df.format(wager.getPotentialReturns()));
    }

    @Override
    public int getItemCount() {
        return mWagers.size();
    }

    public class BetterWagersViewHolder extends RecyclerView.ViewHolder {

        TextView mStake;
        TextView mWagerType;
        TextView mPotentialReturns;

        public BetterWagersViewHolder(View itemView) {
            super(itemView);
            mStake = (TextView) itemView.findViewById(R.id.text_view_bsWagerStake);
            mWagerType = (TextView) itemView.findViewById(R.id.text_view_bsWagerType);
            mPotentialReturns = (TextView) itemView.findViewById(R.id.text_view_bsWagerPotentialReturns);
        }
    }

}
