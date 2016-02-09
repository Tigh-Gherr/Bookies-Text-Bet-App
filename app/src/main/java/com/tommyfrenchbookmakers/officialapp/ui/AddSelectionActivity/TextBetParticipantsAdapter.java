package com.tommyfrenchbookmakers.officialapp.ui.AddSelectionActivity;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.android.tighearnan.frenchsscanner.R;
import com.tommyfrenchbookmakers.officialapp.interfaces.OnAdapterItemSelectedListener;
import com.tommyfrenchbookmakers.officialapp.meetingobjects.Participant;

import java.util.ArrayList;

/**
 * Created by Tíghearnán on 04/09/2015.
 */
public class TextBetParticipantsAdapter extends RecyclerView.Adapter<TextBetParticipantsAdapter.ParticipantsViewHolder> {

    private ArrayList<Participant> mParticipants;
    private OnAdapterItemSelectedListener mSelectedListener;

    public TextBetParticipantsAdapter(ArrayList<Participant> participants, OnAdapterItemSelectedListener selectedListener) {
        mParticipants = participants;
        mSelectedListener = selectedListener;
    }

    @Override
    public ParticipantsViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.recycler_item_textbet_participant, parent, false);

        return new ParticipantsViewHolder(v);
    }

    @Override
    public void onBindViewHolder(ParticipantsViewHolder holder, int position) {
        holder.mName.setText(mParticipants.get(position).getName());
        holder.mOdds.setText(mParticipants.get(position).getOdds());
    }

    @Override
    public int getItemCount() {
        return mParticipants.size();
    }

    public class ParticipantsViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        private TextView mName;
        private TextView mOdds;

        public ParticipantsViewHolder(View itemView) {
            super(itemView);
            itemView.setOnClickListener(this);
            mName = (TextView) itemView.findViewById(R.id.text_view_participantName);
            mOdds = (TextView) itemView.findViewById(R.id.text_view_participantOdds);
        }

        @Override
        public void onClick(View v) {
            mSelectedListener.onItemSelected(getAdapterPosition());
        }
    }

}
