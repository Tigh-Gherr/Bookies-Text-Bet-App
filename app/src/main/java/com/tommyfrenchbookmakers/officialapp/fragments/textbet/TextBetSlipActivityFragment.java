package com.tommyfrenchbookmakers.officialapp.fragments.textbet;

import android.animation.Animator;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Canvas;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.app.NavUtils;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.android.tighearnan.frenchsscanner.R;
import com.github.clans.fab.FloatingActionButton;
import com.github.clans.fab.FloatingActionMenu;
import com.sothree.slidinguppanel.SlidingUpPanelLayout;
import com.tommyfrenchbookmakers.officialapp.Global;
import com.tommyfrenchbookmakers.officialapp.activities.textbet.AddSelectionActivity;
import com.tommyfrenchbookmakers.officialapp.activities.textbet.TextBetSlipActivity;
import com.tommyfrenchbookmakers.officialapp.betslipobjects.BetSlip;
import com.tommyfrenchbookmakers.officialapp.betslipobjects.BetSlipSelection;
import com.tommyfrenchbookmakers.officialapp.betslipobjects.BetSlipWager;
import com.tommyfrenchbookmakers.officialapp.customutils.BettingUtils;
import com.tommyfrenchbookmakers.officialapp.customutils.NetworkUtils;
import com.tommyfrenchbookmakers.officialapp.customutils.SMSUtils;
import com.tommyfrenchbookmakers.officialapp.enumerators.WagerType;
import com.tommyfrenchbookmakers.officialapp.interfaces.WagerConfirmedListener;
import com.tommyfrenchbookmakers.officialapp.recyclerviewadapters.BetSlipSelectionsAdapter;
import com.tommyfrenchbookmakers.officialapp.recyclerviewadapters.BetSlipWagersAdapter;
import com.tommyfrenchbookmakers.officialapp.singletons.BetSlipSingleton;

import java.text.DecimalFormat;

/**
 * A placeholder fragment containing a simple view.
 */
public class TextBetSlipActivityFragment extends Fragment {

    private BetSlip mBetSlip;

    // Main body
    private TextView mAddContentTextView;
    private RecyclerView mSelectionsRecyclerView;
    private RecyclerView.Adapter mSelectionsAdapter;
    private FloatingActionMenu mFloatingMenu;
    private FloatingActionButton mFabNewSelection;
    private FloatingActionButton mFabNewWager;
    private FloatingActionButton mFabPlaceBet;

    // SlidingPanel
    private SlidingUpPanelLayout mPanelLayout;
    private TextView mNumberOfWagersTextView;
    private TextView mTextNumberOfWagersPluralTextView;
    private TextView mPotentialWinningsTextView;
    private RecyclerView mWagersRecyclerView;
    private RecyclerView.Adapter mWagersAdapter;


    public TextBetSlipActivityFragment() {
    }

    private void updateVisibilities() {
        if (mBetSlip.getSelections().size() == 0) {
            mAddContentTextView.setVisibility(View.VISIBLE);
            mPanelLayout.setVisibility(View.INVISIBLE);
        } else {
            mAddContentTextView.setVisibility(View.INVISIBLE);
            mPanelLayout.setVisibility(View.VISIBLE);
        }
    }

    public SlidingUpPanelLayout.PanelState getPanelState() {
        return mPanelLayout.getPanelState();
    }

    public void setPanelState(SlidingUpPanelLayout.PanelState state) {
        if(state == SlidingUpPanelLayout.PanelState.COLLAPSED) {
            mPanelLayout.setPanelState(state);
        }

        if(state == SlidingUpPanelLayout.PanelState.EXPANDED) {
            if(mBetSlip.getSelections().size() == 0) {
                Snackbar.make(getView(), R.string.snackbar_body_no_wagers, Snackbar.LENGTH_SHORT).show();
            } else {
                mPanelLayout.setPanelState(state);
            }
        }
    }

    public void anchorPanel() {
        SlidingUpPanelLayout.PanelState state = mPanelLayout.getPanelState();
        if (mBetSlip.getWagers().size() > 0) {
            if (state == SlidingUpPanelLayout.PanelState.COLLAPSED) {
                mPanelLayout.setPanelState(SlidingUpPanelLayout.PanelState.ANCHORED);
            } else {
                mPanelLayout.setPanelState(SlidingUpPanelLayout.PanelState.COLLAPSED);
            }
        } else {
            Snackbar.make(getView(), R.string.snackbar_body_no_wagers, Snackbar.LENGTH_SHORT).show();
        }
    }

    private void updateWagers() {
        for (int i = mBetSlip.getWagers().size() - 1; i >= 0; i--) {
            BetSlipWager w = mBetSlip.getWagers().get(i);

            if ((w.getWagerType().getHasFixedNumberOfSelections() && mBetSlip.getSelections().size() != w.getWagerType().getMinNumberOfSelections())
                    || w.getWagerType().getMinNumberOfSelections() > mBetSlip.getSelections().size()) {
                mBetSlip.getWagers().remove(w);
                mWagersAdapter.notifyItemRemoved(i);
            }
        }

        updateWagersInfo();
    }

    private void updateWagersInfo() {
        double totalStake = 0d, totalReturn = 0d;
        for (BetSlipWager w :
                mBetSlip.getWagers()) {
            w.calculateStake(mBetSlip.getSelections().size());
            totalStake += w.getTotalStake();

            w.setPotentialReturns(BettingUtils.calculateRunningTotal(w, mBetSlip));
            totalReturn += w.getPotentialReturns();
        }

        DecimalFormat df = new DecimalFormat("0.00");
        ((TextBetSlipActivity) getActivity()).getSupportActionBar()
                .setSubtitle("Stake: £" + df.format(totalStake));
        final int size = mBetSlip.getWagers().size();
        mNumberOfWagersTextView.animate().alpha(0f).setDuration(300).setListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {
                if (size == 1)
                    mTextNumberOfWagersPluralTextView.animate().alpha(0f).setDuration(300);
            }

            @Override
            public void onAnimationEnd(Animator animation) {
                mNumberOfWagersTextView.setText(size + "");
                mNumberOfWagersTextView.animate().alpha(1f).setDuration(300);
                if (size != 1)
                    mTextNumberOfWagersPluralTextView.animate().alpha(1f).setDuration(300);
            }

            @Override
            public void onAnimationCancel(Animator animation) {

            }

            @Override
            public void onAnimationRepeat(Animator animation) {

            }
        });

        mPotentialWinningsTextView.setText("£" + df.format(totalReturn));

        mWagersAdapter.notifyDataSetChanged();

        if (mBetSlip.getWagers().size() == 0) mPanelLayout.setTouchEnabled(false);
        else mPanelLayout.setTouchEnabled(true);
    }

    private void showWagerPickerDialog() {
        WagerPickerDialog dialog = WagerPickerDialog.newInstance(mBetSlip.getSelections().size());
        dialog.setWagerConfirmedListener(new WagerConfirmedListener() {
            @Override
            public void onWagerConfirmed(String unitStake, boolean eachWay, WagerType wagerType) {
                DecimalFormat df = new DecimalFormat("0.00");

                if(unitStake.length() == 0) {
                    Snackbar.make(getView(), R.string.snackbar_body_no_stake_entered, Snackbar.LENGTH_LONG).show();
                    return;
                }

                BetSlipWager wager = null;

                // Checks to see if a bet of the same type already exists, if so it selects said BetSlipWager.
                for(BetSlipWager w : mBetSlip.getWagers()) {
                    // If they are the same type, and they're both each way or not.
                    if(w.getWagerType() == wagerType && (w.isEachWay() == eachWay)) {
                        wager = w;
                        break;
                    }
                }
                try {
                    unitStake = df.format(Double.parseDouble(unitStake));

                    // If wager isn't null; will only be true if the chosen WagerType already has been chosen before.
                    if(wager != null) {
                        // Add the new stake and the old stake together, and notify the RecyclerView of the valye changing.
                        wager.setUnitStake(df.format(Double.parseDouble(wager.getUnitStake()) + Double.parseDouble(unitStake)));
                        mWagersAdapter.notifyItemChanged(mBetSlip.getWagers().indexOf(wager));
                        Snackbar.make(getView(), "£" + unitStake + " " + wagerType.getName() + (eachWay ? "ew" : "") + " added!", Snackbar.LENGTH_SHORT).show();
                    } else {
                        // Create a new BetSlipWager with the passed values. and notify the RecycerView of this addition.
                        wager = new BetSlipWager(wagerType, unitStake, eachWay);
                        mBetSlip.getWagers().add(wager);
                        mWagersAdapter.notifyItemInserted(mBetSlip.getWagers().size());
                        Snackbar.make(getView(), getString(R.string.snackbar_body_wager_added, wager.toString()), Snackbar.LENGTH_SHORT).show();
                    }
                    updateWagersInfo();
                } catch (NumberFormatException mfe) {
                    // If an incompatible stake was entered, display an error.
                    Snackbar.make(getView(), R.string.snackbar_body_enter_valid_wager, Snackbar.LENGTH_SHORT).show();
                }
            }
        });
        dialog.show(getActivity().getSupportFragmentManager(), "WAGER");

        mFloatingMenu.close(true);

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mBetSlip = BetSlipSingleton.get(getActivity()).getBetSlip();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_text_betslip, container, false);

        mAddContentTextView = (TextView) v.findViewById(R.id.text_view_addContentToBetSlip);

        // If mBetSlip is null, return to previous activity (SelectionScreenActivity).
        try {
            mBetSlip.getSelections();
            mBetSlip.getWagers();
        } catch (NullPointerException npe) {
            NavUtils.navigateUpFromSameTask(getActivity());
            getActivity().finish();
            return v;
        }

        mPanelLayout = (SlidingUpPanelLayout) v.findViewById(R.id.sliding_layout_betSlipWagers);
        mPanelLayout.setOverlayed(true);
        mPanelLayout.setAnchorPoint(1f);
        mPanelLayout.setPanelSlideListener(new SlidingUpPanelLayout.PanelSlideListener() {
            boolean hasTouchListener = false;

            @Override
            public void onPanelSlide(View panel, float slideOffset) {
                float fadeStart = mPanelLayout.getAnchorPoint() / 2, fadeBy = mPanelLayout.getAnchorPoint();
                if (slideOffset > fadeStart) {
                    mFloatingMenu.setAlpha(1 - ((slideOffset - fadeStart) * (1 / (fadeBy - fadeStart))));
                } else {
                    mFloatingMenu.setAlpha(1f);
                }

                if (slideOffset < 1f && mFloatingMenu.getVisibility() == View.INVISIBLE) {
                    mFloatingMenu.setVisibility(View.VISIBLE);
                }

                float appearStart = mPanelLayout.getAnchorPoint() / 2, appearBy = mPanelLayout.getAnchorPoint();
                if (slideOffset >= appearStart) {
                    mWagersRecyclerView.setAlpha((slideOffset - appearStart) * ((1 / (appearBy - appearStart))));
                    mPotentialWinningsTextView.setAlpha((slideOffset - appearStart) * ((1 / (appearBy - appearStart))));
                } else if (slideOffset >= appearBy) {
                    mWagersRecyclerView.setAlpha(1f);
                    mPotentialWinningsTextView.setAlpha(1f);
                } else {
                    mWagersRecyclerView.setAlpha(0f);
                    mPotentialWinningsTextView.setAlpha(0f);
                }

                if (slideOffset > 0) {
                    if (mFloatingMenu.isOpened()) mFloatingMenu.close(true);
                    View view = getView().findViewById(R.id.view_space);
                    view.setVisibility(View.GONE);
//                    mSelectionsRecyclerView.setClickable(false);
                    if (!hasTouchListener) {
                        mSelectionsRecyclerView.setOnTouchListener(new View.OnTouchListener() {
                            @Override
                            public boolean onTouch(View v, MotionEvent event) {
                                if (event.getAction() == MotionEvent.ACTION_UP)
                                    mPanelLayout.setPanelState(SlidingUpPanelLayout.PanelState.COLLAPSED);
                                return true;
                            }
                        });
                        hasTouchListener = true;
                    }
                }

            }

            @Override
            public void onPanelCollapsed(View panel) {
                if (hasTouchListener) {
                    mSelectionsRecyclerView.setOnTouchListener(null);
                    hasTouchListener = false;
                }

                View view = getView().findViewById(R.id.view_space);
                view.setVisibility(View.VISIBLE);
            }

            @Override
            public void onPanelExpanded(View panel) {
                mFloatingMenu.setVisibility(View.INVISIBLE);
                mFloatingMenu.close(false);
            }

            @Override
            public void onPanelAnchored(View panel) {
                mFloatingMenu.setVisibility(View.INVISIBLE);
                mFloatingMenu.close(false);
            }

            @Override
            public void onPanelHidden(View panel) {

            }
        });

        mSelectionsRecyclerView = (RecyclerView) v.findViewById(R.id.recycler_view_betterBetSlipSelections);
        mSelectionsRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        mSelectionsAdapter = new BetSlipSelectionsAdapter(mBetSlip.getSelections());
        mSelectionsRecyclerView.setAdapter(mSelectionsAdapter);

        ItemTouchHelper.SimpleCallback selectionsCallBack = new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT) {
            @Override
            public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
                return false;
            }

            @Override
            public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction) {
                if (direction == ItemTouchHelper.LEFT || direction == ItemTouchHelper.RIGHT) {
                    final int position = viewHolder.getAdapterPosition();
                    final BetSlipSelection toBeDeleted = mBetSlip.getSelections().get(position);
                    mBetSlip.getSelections().remove(toBeDeleted);
                    mSelectionsAdapter.notifyItemRemoved(position);

                    Snackbar.make(getView(), getString(R.string.snackbar_body_betslip_selection_removed, toBeDeleted.getName()), Snackbar.LENGTH_SHORT)
                            .setAction(R.string.snackbar_action_undo, new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    mBetSlip.getSelections().add(position, toBeDeleted);
                                    mSelectionsAdapter.notifyItemInserted(position);
                                    updateVisibilities();
                                    updateWagers();
                                }
                            }).show();

                    updateVisibilities();
                    updateWagers();
                }
            }

            @Override
            public void onChildDraw(Canvas c, RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, float dX, float dY, int actionState, boolean isCurrentlyActive) {

                if (actionState == ItemTouchHelper.ACTION_STATE_SWIPE) {
                    float width = (float) viewHolder.itemView.getWidth();
                    float fadeBy = (Math.abs(dX) / width) * 2f;
                    float alpha = fadeBy <= 1f ? 1f - fadeBy : 0f;
                    viewHolder.itemView.setAlpha(alpha);
                    viewHolder.itemView.setTranslationX(dX);
                } else {
                    super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive);
                }
            }
        };
        ItemTouchHelper selectionsTouchHelper = new ItemTouchHelper(selectionsCallBack);
        selectionsTouchHelper.attachToRecyclerView(mSelectionsRecyclerView);

        mNumberOfWagersTextView = (TextView) v.findViewById(R.id.text_view_numberOfWagers);
        mTextNumberOfWagersPluralTextView = (TextView) v.findViewById(R.id.text_view_textNumberOfWagersPlural);
        mPotentialWinningsTextView = (TextView) v.findViewById(R.id.text_view_potentialWinnings);

        mWagersRecyclerView = (RecyclerView) v.findViewById(R.id.recycler_view_betterBetSlipWagers);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity());
        linearLayoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
        mWagersRecyclerView.setLayoutManager(linearLayoutManager);
        mWagersAdapter = new BetSlipWagersAdapter(mBetSlip.getWagers());
        mWagersRecyclerView.setAdapter(mWagersAdapter);

        ItemTouchHelper.SimpleCallback wagersCallBack = new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.UP) {
            @Override
            public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
                return false;
            }

            @Override
            public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction) {
                if (direction == ItemTouchHelper.UP) {
                    final int position = viewHolder.getAdapterPosition();
                    final BetSlipWager toBeDeleted = mBetSlip.getWagers().get(position);
                    mBetSlip.getWagers().remove(toBeDeleted);
                    mWagersAdapter.notifyItemRemoved(position);
                    if (mBetSlip.getWagers().size() == 0) {
                        mPanelLayout.setPanelState(SlidingUpPanelLayout.PanelState.COLLAPSED);
                    }

                    Snackbar.make(getView(),
                            getString(R.string.snackbar_body_betslip_wager_removed,
                                    toBeDeleted.getUnitStake(), (toBeDeleted.isEachWay() ? "ew" : ""),
                                    toBeDeleted.getWagerType().getName()),
                            Snackbar.LENGTH_SHORT)
                            .setAction(R.string.snackbar_action_undo, new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    mBetSlip.getWagers().add(position, toBeDeleted);
                                    mWagersAdapter.notifyItemInserted(position);
                                    if (mBetSlip.getWagers().size() == 1)
                                        mPanelLayout.setPanelState(SlidingUpPanelLayout.PanelState.ANCHORED);
                                    updateWagersInfo();
                                }
                            }).show();
                    updateWagersInfo();
                }
            }

        };
        ItemTouchHelper wagerTouchHelper = new ItemTouchHelper(wagersCallBack);
        wagerTouchHelper.attachToRecyclerView(mWagersRecyclerView);

        mFloatingMenu = (FloatingActionMenu) v.findViewById(R.id.fam_betSlipOptions);

        mFabNewSelection = (FloatingActionButton) v.findViewById(R.id.fab_newSelection);
        mFabNewSelection.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (NetworkUtils.networkIsAvailable(getActivity()))
                    startActivity(new Intent(getActivity(), AddSelectionActivity.class));
                else {
                    Snackbar.make(getView(), R.string.error_message_no_internet, Snackbar.LENGTH_SHORT).show();
                    mFloatingMenu.close(true);
                }
            }
        });
        mFabNewWager = (FloatingActionButton) v.findViewById(R.id.fab_newWager);
        mFabNewWager.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mBetSlip.getSelections().size() > 0) {
                    showWagerPickerDialog();
                } else {
                    Snackbar.make(getView(), R.string.snackbar_body_no_selections, Snackbar.LENGTH_SHORT).show();
                }
            }
        });
        mFabPlaceBet = (FloatingActionButton) v.findViewById(R.id.fab_placeBet);
        mFabPlaceBet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mBetSlip.getSelections().size() == 0) {
                    Snackbar.make(getView(), R.string.error_message_empty_betslip, Snackbar.LENGTH_SHORT).show();
                } else if (mBetSlip.getWagers().size() == 0) {
                    Snackbar.make(getView(), R.string.error_message_no_wagers, Snackbar.LENGTH_SHORT)
                            .setAction(R.string.snackbar_action_add_wager, new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    showWagerPickerDialog();
                                }
                            }).show();
                } else {
                    mFloatingMenu.close(true);
                    new AlertDialog.Builder(getActivity())
                            .setTitle(R.string.alert_dialog_title_confirm_text_bet)
                            .setMessage(R.string.alert_dialog_body_confirm_text_bet)
                            .setMessage("Send the following to Tommy French\'s Bookmakers via SMS? (Standard network charges may apply).\n\n" + mBetSlip.toString(getActivity()))
                            .setPositiveButton(R.string.alert_dialog_button_confirm, new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    SMSUtils.sendSMS(getActivity(), Global.PHONE_NUMBER, mBetSlip.toString(getActivity()), getView());

                                    mBetSlip.getSelections().clear();
                                    mSelectionsAdapter.notifyDataSetChanged();

                                    mBetSlip.getWagers().clear();
                                    mWagersAdapter.notifyDataSetChanged();
                                    updateWagersInfo();
                                    updateVisibilities();
                                }
                            }).setNegativeButton(R.string.alert_dialog_button_cancel, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    }).show();
                }
            }
        });

        return v;
    }

    @Override
    public void onResume() {
        super.onResume();
        mSelectionsAdapter.notifyDataSetChanged();
        updateVisibilities();
        updateWagers();
    }

    @Override
    public void onPause() {
        super.onPause();
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                mFloatingMenu.close(false);
            }
        }, 100);
    }
}
