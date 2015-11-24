package com.tommyfrenchbookmakers.officialapp.recyclerviewadapters;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.tighearnan.frenchsscanner.R;
import com.tommyfrenchbookmakers.officialapp.docketobjects.BetWagers;

import java.text.DecimalFormat;
import java.util.ArrayList;

/**
 * Created by Tíghearnán on 11/07/2015.
 */
public class DocketWagersAdapter extends RecyclerView.Adapter<DocketWagersAdapter.WagersViewHolder> {

    private ArrayList<BetWagers> mWagers;

    public DocketWagersAdapter(ArrayList<BetWagers> wagers) {
        mWagers = wagers;
    }

    @Override
    public WagersViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.recycler_item_docket_wager, parent, false);

        return new WagersViewHolder(v);
    }

    @Override
    public void onBindViewHolder(WagersViewHolder holder, int position) {
        DecimalFormat df = new DecimalFormat("0.00");

        holder.mWagerPayoutTextView.setText(
                "£" + df.format(mWagers.get(position).getPayout()) + " "
        );
        holder.mEachWayTextView.setText(
                mWagers.get(position).isEachWay() ? "ew" : ""
        );
        holder.mWagerStakeTextView.setText(
                "£" + df.format(mWagers.get(position).getUnitStake())
        );
        holder.mWagerTypeTextView.setText(
                mWagers.get(position).getWagerType()
        );

        if (mWagers.get(position).isWinner()) {
            holder.mWagerWinnerImageView.setImageResource(R.drawable.ic_check_black_24dp);
        } else {
            holder.mWagerWinnerImageView.setImageResource(R.drawable.ic_close_black_24dp);
        }

    }

    @Override
    public int getItemCount() {
        return mWagers.size();
    }

    public class WagersViewHolder extends RecyclerView.ViewHolder {

        private TextView mWagerStakeTextView;
        private TextView mWagerTypeTextView;
        private TextView mWagerPayoutTextView;
        private ImageView mWagerWinnerImageView;
        private TextView mEachWayTextView;

        public WagersViewHolder(View itemView) {
            super(itemView);

            mWagerStakeTextView = (TextView) itemView.findViewById(R.id.text_view_bsWagerStake);
            mWagerTypeTextView = (TextView) itemView.findViewById(R.id.text_view_WagerType);
            mWagerPayoutTextView = (TextView) itemView.findViewById(R.id.text_view_wagerPayout);
            mWagerWinnerImageView = (ImageView) itemView.findViewById(R.id.image_view_wagerWinner);
            mEachWayTextView = (TextView) itemView.findViewById(R.id.text_view_eachWay);
        }
    }
}