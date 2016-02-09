package com.tommyfrenchbookmakers.officialapp.ui.TextBetSlipActivity;

import android.support.v4.content.ContextCompat;
import android.support.v7.widget.AppCompatTextView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.PopupMenu;

import com.android.tighearnan.frenchsscanner.R;
import com.tommyfrenchbookmakers.officialapp.betslipobjects.BetSlipSelection;

import java.util.ArrayList;

/**
 * Created by Tíghearnán on 04/10/2015.
 */
public class BetSlipSelectionsAdapter extends RecyclerView.Adapter<BetSlipSelectionsAdapter.BetterSelectionsViewHolder> {

    private ArrayList<BetSlipSelection> mSelections;

    public BetSlipSelectionsAdapter(ArrayList<BetSlipSelection> selections) {
        mSelections = selections;
    }

    private void setOddsAppearance(BetterSelectionsViewHolder holder, int position) {
        boolean takingOdds = mSelections.get(position).isTakingOdds();
        if (holder.mOdds.getText().equals("SP") || takingOdds) {
            holder.mOdds.setTextColor(ContextCompat.getColor(holder.mOdds.getContext(), R.color.textColourSecondary));
        } else {
            holder.mOdds.setTextColor(ContextCompat.getColor(holder.mOdds.getContext(), R.color.md_grey_300));
        }
//        holder.mOdds.setTextColor(ContextCompat.getColor(holder.mOdds.getContext(), (takingOdds ? R.color.textColourSecondary : R.color.md_grey_300)));
    }

    @Override
    public BetterSelectionsViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.recycler_item_betslip_selection, parent, false);

        return new BetterSelectionsViewHolder(v);
    }

    @Override
    public void onBindViewHolder(BetterSelectionsViewHolder holder, int position) {
        BetSlipSelection s = mSelections.get(position);
        holder.mSelectionName.setText(s.getName());
        holder.mMarketDetails.setText(s.getMarketName());
        holder.mOdds.setText(s.getOdds());

        if (holder.mOdds.getText().equals("SP") && (holder.mSelectionName.getText().equals("Favourite") ||
                                                    holder.mSelectionName.getText().equals("2nd Favourite"))) {
            holder.mMore.setVisibility(View.INVISIBLE);
        } else {
            holder.mMore.setVisibility(View.VISIBLE);
        }

        setOddsAppearance(holder, position);
    }

    @Override
    public int getItemCount() {
        return mSelections.size();
    }

    public class BetterSelectionsViewHolder extends RecyclerView.ViewHolder {

        AppCompatTextView mSelectionName;
        AppCompatTextView mMarketDetails;
        AppCompatTextView mOdds;
        ImageButton mMore;

        public BetterSelectionsViewHolder(View itemView) {
            super(itemView);
            mSelectionName = (AppCompatTextView) itemView.findViewById(R.id.appcompat_textview_betSlipSelectionName);
            mMarketDetails = (AppCompatTextView) itemView.findViewById(R.id.appcompat_textview_betSlipSelectionMarketDetails);

            mOdds = (AppCompatTextView) itemView.findViewById(R.id.appcompat_textview_betSlipSelectionOdds);

            mMore = (ImageButton) itemView.findViewById(R.id.image_button_moreOptions);

            mMore.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    final BetSlipSelection s = mSelections.get(getLayoutPosition());
                    final PopupMenu popupMenu = new PopupMenu(v.getContext(), v);
                    popupMenu.inflate(R.menu.popup_menu_bet_slip_selection_options);

                    final Menu menu = popupMenu.getMenu();
                    if (mOdds.getText().equals("SP")) {
                        menu.removeItem(R.id.popup_takeOdds);
                    } else {
                        menu.findItem(R.id.popup_takeOdds).setChecked(s.isTakingOdds());
                    }


                    menu.findItem(R.id.popup_nrFav).setChecked(s.isNonRunnerInsertFav());
                    menu.findItem(R.id.popup_nr2ndFav).setChecked(s.isNonRunnerInsertSecondFav());

                    popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                        @Override
                        public boolean onMenuItemClick(MenuItem item) {
                            switch (item.getItemId()) {
                                case R.id.popup_takeOdds:
                                    item.setChecked(!item.isChecked());
                                    s.setTakingOdds(item.isChecked());
                                    setOddsAppearance(BetterSelectionsViewHolder.this, getLayoutPosition());
                                    return true;
                                case R.id.popup_nrFav:
                                    item.setChecked(!item.isChecked());
                                    s.setNonRunnerInsertFav(item.isChecked());
                                    if (item.isChecked()) {
                                        s.setNonRunnerInsertSecondFav(false);
                                        menu.findItem(R.id.popup_nr2ndFav).setChecked(false);
                                    }
                                    return true;
                                case R.id.popup_nr2ndFav:
                                    item.setChecked(!item.isChecked());
                                    s.setNonRunnerInsertSecondFav(item.isChecked());
                                    if (item.isChecked()) {
                                        s.setNonRunnerInsertFav(false);
                                        menu.findItem(R.id.popup_nrFav).setChecked(false);
                                    }
                                    return true;
                                default:
                                    return false;
                            }
                        }
                    });
                    popupMenu.show();
                }
            });

            mMore.setOnTouchListener(new View.OnTouchListener() {
                @Override
                public boolean onTouch(View v, MotionEvent event) {
                    switch (event.getAction()) {
                        case MotionEvent.ACTION_DOWN:
                            v.setAlpha(1f);
                            return false;
                        case MotionEvent.ACTION_UP:
                            v.setAlpha(0.54f);
                            return false;
                        case MotionEvent.ACTION_CANCEL:
                            v.setAlpha(0.54f);
                            return false;
                    }
                    return false;
                }
            });

        }
    }
}
