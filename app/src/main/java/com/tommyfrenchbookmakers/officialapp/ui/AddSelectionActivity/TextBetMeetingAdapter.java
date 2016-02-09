package com.tommyfrenchbookmakers.officialapp.ui.AddSelectionActivity;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.android.tighearnan.frenchsscanner.R;
import com.tommyfrenchbookmakers.officialapp.interfaces.OnAdapterItemSelectedListener;
import com.tommyfrenchbookmakers.officialapp.meetingobjects.Market;
import com.tommyfrenchbookmakers.officialapp.meetingobjects.Meeting;

import java.util.ArrayList;

/**
 * Created by Tíghearnán on 30/09/2015.
 */
public class TextBetMeetingAdapter extends  RecyclerView.Adapter<TextBetMeetingAdapter.SelectMeetingViewHolder> {

    ArrayList<Meeting> mMeetings;
    OnAdapterItemSelectedListener mSelectedListener;

    public TextBetMeetingAdapter(ArrayList<Meeting> meetings, OnAdapterItemSelectedListener listener) {
        mMeetings = meetings;
        mSelectedListener = listener;
    }

    @Override
    public SelectMeetingViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.recycler_item_textbet_meeting, parent, false);

        return new SelectMeetingViewHolder(v);
    }

    @Override
    public void onBindViewHolder(SelectMeetingViewHolder holder, int position) {
        holder.mName.setText(mMeetings.get(position).getName());
        holder.mNumberOfRaces.setText(mMeetings.get(position).getMarkets().size() + " races");

        ArrayList<Market> markets = mMeetings.get(position).getMarkets();
        if(markets.size() > 1) {
            holder.mTimes.setText(markets.get(0).getOffTime() + " - " + markets.get(markets.size() - 1).getOffTime());
        } else {
            holder.mTimes.setText(markets.get(0).getOffTime());
        }

    }

    @Override
    public int getItemCount() {
        return mMeetings.size() ;
    }

    public class SelectMeetingViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        TextView mName;
        TextView mNumberOfRaces;
        TextView mTimes;

        public SelectMeetingViewHolder(View itemView) {
            super(itemView);

            itemView.setOnClickListener(this);

            mName = (TextView) itemView.findViewById(R.id.text_view_meetingName);
            mNumberOfRaces = (TextView) itemView.findViewById(R.id.text_view_numberOfMarkets);
            mTimes = (TextView) itemView.findViewById(R.id.text_view_meetingTimes);
        }

        @Override
        public void onClick(View v) {
            mSelectedListener.onItemSelected(getAdapterPosition());
        }
    }

}
