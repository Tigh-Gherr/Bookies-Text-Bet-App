package com.tommyfrenchbookmakers.officialapp.ui.ResultPagerActivity;

import android.content.Context;
import android.graphics.Color;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.tighearnan.frenchsscanner.R;
import com.tommyfrenchbookmakers.officialapp.docketobjects.BetSelections;
import com.tommyfrenchbookmakers.officialapp.docketobjects.BetWagers;
import com.tommyfrenchbookmakers.officialapp.docketobjects.DocketBet;

import java.util.ArrayList;

import static com.android.tighearnan.frenchsscanner.R.id.view_resultColour;

/**
 * Created by Tíghearnán on 05/07/2015.
 */
public class DocketSelectionsAdapter extends RecyclerView.Adapter<DocketSelectionsAdapter.SelectionsViewHolder> {

    private final int COLOUR_WINNER = R.color.md_green_500;
    private final int COLOUR_PLACER = R.color.md_green_300;
    private final int COLOUR_NON_RUNNER = R.color.md_amber_300;
    private final int COLOUR_LOSER = R.color.md_red_300;
    private final int COLOUR_TRANSPARENT = Color.TRANSPARENT;

    private boolean mHasEachWay = false;
    private ArrayList<BetSelections> mSelections;
    private Context mContext;

    public DocketSelectionsAdapter(DocketBet b, Context c) {
        mSelections = b.getSelections();
        mContext = c;

        for (BetWagers w : b.getWagers()) {
            if (w.isEachWay()) mHasEachWay = true;
        }
    }

    private void setColour(int colourRes, SelectionsViewHolder holder) {
        holder.mResultColour.setBackgroundColor(ContextCompat.getColor(mContext, colourRes));
    }

    private void setFinishingPosition(SelectionsViewHolder holder, int finishingPosition) {
        switch (finishingPosition) {
            case -2:
                holder.mWinnerPosition.setText("NR");
                setColour(COLOUR_NON_RUNNER, holder);
                break;
            case 0:
                holder.mWinnerPosition.setText("X");
                setColour(COLOUR_LOSER, holder);
                break;
            case 1:
                holder.mWinnerPosition.setText("1st");
                setColour(COLOUR_WINNER, holder);
                break;
            case 2:
                holder.mWinnerPosition.setText("2nd");
                setColour(mHasEachWay ? COLOUR_PLACER : COLOUR_LOSER, holder);
                break;
            case 3:
                holder.mWinnerPosition.setText("3rd");
                setColour(mHasEachWay ? COLOUR_PLACER : COLOUR_LOSER, holder);
                break;
            case 4:
                holder.mWinnerPosition.setText("4th");
                setColour(mHasEachWay ? COLOUR_PLACER : COLOUR_LOSER, holder);
                break;
            case 5:
                holder.mWinnerPosition.setText("5th");
                setColour(mHasEachWay ? COLOUR_PLACER : COLOUR_LOSER, holder);
                break;
            case 6:
                holder.mWinnerPosition.setText("6th");
                break;
            case 7:
                holder.mWinnerPosition.setText("7th");
                break;
            case 8:
                holder.mWinnerPosition.setText("8th");
                break;
            case 9:
                holder.mWinnerPosition.setText("9th");
                break;
            case 10:
                holder.mWinnerPosition.setText("10th");
                break;
            default:
                holder.mWinnerPosition.setText("");
                setColour(COLOUR_TRANSPARENT, holder);
        }
    }

    private void setFinishingStatus(SelectionsViewHolder holder, int finishingPosition) {
        switch (finishingPosition) {
            case -2:
                holder.mWinnerPosition.setText("NR");
                setColour(COLOUR_NON_RUNNER, holder);
                break;
            case 0:
                holder.mWinnerPosition.setText("L");
                setColour(COLOUR_LOSER, holder);
                break;
            case 1:
                holder.mWinnerPosition.setText("W");
                setColour(COLOUR_WINNER, holder);
                break;
            default:
                holder.mWinnerPosition.setText("");
                setColour(COLOUR_TRANSPARENT, holder);
        }
    }

    @Override
    public SelectionsViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.recycler_item_docket_selection, parent, false);

        return new SelectionsViewHolder(v);
    }

    @Override
    public void onBindViewHolder(SelectionsViewHolder holder, int position) {
        holder.mName.setText(mSelections.get(position).getName());
        holder.mOdds.setText(mSelections.get(position).getOdds());

        if (mSelections.get(position).hasRule4()) {
            holder.mRule4.setText("(Rule 4: " + mSelections.get(position).getRule4() + "%)");
        } else {
            holder.mRule4.setText("");
            holder.mRule4.setVisibility(View.INVISIBLE);
        }

        if (mSelections.get(position).hasDeadHeat()) {
            if (mSelections.get(position).hasRule4())
                holder.mDeadHeat.setText("|| (DH: " + mSelections.get(position).getDeadHeat() + ")");
            else holder.mDeadHeat.setText("(DH: " + mSelections.get(position).getDeadHeat() + ")");
        } else {
            holder.mDeadHeat.setText("");
            holder.mDeadHeat.setVisibility(View.INVISIBLE);
        }

        switch (mSelections.get(position).getSport()) {
            case HORSE_RACING:
                holder.mSport.setImageResource(R.drawable.ic_sport_horse_racing);
                setFinishingPosition(holder, mSelections.get(position).getPosition());
                break; // break HORSE_RACING
            case FOOTBALL:
                holder.mSport.setImageResource(R.drawable.ic_sport_football_64);
                setFinishingStatus(holder, mSelections.get(position).getPosition());
                break; // break FOOTBALL
            case GREYHOUNDS:
                holder.mSport.setImageResource(R.drawable.ic_sport_greyhounds_64);
                setFinishingPosition(holder, mSelections.get(position).getPosition());
                break; // break GREYHOUNDS
            case GOLF:
                holder.mSport.setImageResource(R.drawable.ic_sport_golf_64);
                setFinishingPosition(holder, mSelections.get(position).getPosition());
                break; // break GOLF
            case TENNIS:
                holder.mSport.setImageResource(R.drawable.ic_tennis_64);
                setFinishingStatus(holder, mSelections.get(position).getPosition());
                break; // break TENNIS
            case RUGBY_LEAGUE:
                holder.mSport.setImageResource(R.drawable.ic_sport_rugby_64);
                setFinishingStatus(holder, mSelections.get(position).getPosition());
                break; // break RUGBY_LEAGUE
            case BOXING:
                holder.mSport.setImageResource(R.drawable.ic_sport_boxing_64);
                setFinishingStatus(holder, mSelections.get(position).getPosition());
                break; // break BOXING
            case RUGBY_UNION:
                holder.mSport.setImageResource(R.drawable.ic_sport_rugby_64);
                setFinishingStatus(holder, mSelections.get(position).getPosition());
                break; // break RUGBY_UNION
            case DARTS:
                holder.mSport.setImageResource(R.drawable.ic_sport_darts_64);
                setFinishingPosition(holder, mSelections.get(position).getPosition());
                break; // break DARTS
            case GAA:
                holder.mSport.setImageResource(R.drawable.ic_sport_gaa_64);
                setFinishingStatus(holder, mSelections.get(position).getPosition());
                break; // break GAA
            case UNKNOWN:
                holder.mSport.setImageResource(R.drawable.ic_unkown_sport);
                setFinishingPosition(holder, mSelections.get(position).getPosition());
                break; // break UNKNOWN
            default:
                holder.mSport.setImageResource(R.mipmap.ic_launcher);
                break;
        }
    }

    @Override
    public int getItemCount() {
        return mSelections.size();
    }

    public class SelectionsViewHolder extends RecyclerView.ViewHolder {

        private TextView mName;
        private TextView mOdds;
        private TextView mRule4;
        private ImageView mSport;
        private TextView mWinnerPosition;
        private View mResultColour;
        private TextView mDeadHeat;

        public SelectionsViewHolder(View itemView) {
            super(itemView);

            mName = (TextView) itemView.findViewById(R.id.text_view_selectionName);
            mOdds = (TextView) itemView.findViewById(R.id.text_view_selectionOdds);
            mRule4 = (TextView) itemView.findViewById(R.id.text_view_selectionRule4);
            mDeadHeat = (TextView) itemView.findViewById(R.id.text_view_selectionDeadHeat);
            mSport = (ImageView) itemView.findViewById(R.id.image_view_selectionSport);
            mWinnerPosition = (TextView) itemView.findViewById(R.id.text_view_selectionResult);
            mResultColour = itemView.findViewById(view_resultColour);
        }

    }

}