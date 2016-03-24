package com.tommyfrenchbookmakers.officialapp.ui.AddSelectionActivity;

import android.animation.Animator;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.app.NavUtils;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.android.tighearnan.frenchsscanner.R;
import com.cocosw.bottomsheet.BottomSheet;
import com.tommyfrenchbookmakers.officialapp.betslipobjects.BetSlip;
import com.tommyfrenchbookmakers.officialapp.betslipobjects.BetSlipSelection;
import com.tommyfrenchbookmakers.officialapp.meetingobjects.Market;
import com.tommyfrenchbookmakers.officialapp.meetingobjects.Meeting;
import com.tommyfrenchbookmakers.officialapp.meetingobjects.Participant;
import com.tommyfrenchbookmakers.officialapp.singletons.BetSlipSingleton;
import com.tommyfrenchbookmakers.officialapp.singletons.MeetingsSingleton;
import com.tommyfrenchbookmakers.officialapp.ui.OnAdapterItemSelectedListener;
import com.tommyfrenchbookmakers.officialapp.ui.SelectionScreenActivity.SelectionScreenActivity;
import com.tommyfrenchbookmakers.officialapp.ui.TextBetSlipActivity.TextBetSlipActivity;
import com.tommyfrenchbookmakers.officialapp.utils.DataDownloadListener;
import com.tommyfrenchbookmakers.officialapp.utils.DownloadUtils;

/**
 * A placeholder fragment containing a simple view.
 */
public class MarketPageFragment extends Fragment {

    private RecyclerView mParticipantRecyclerView;
    private RecyclerView.Adapter mParticipantAdapter;

    private ProgressBar mProgressBar;
    private TextView mDownloadingTextView;

    private Market mMarket;
    private LinearLayout mRaceOverLinearLayout;

    private Animator.AnimatorListener mAnimator = new Animator.AnimatorListener() {
        @Override
        public void onAnimationStart(Animator animation) {

        }

        @Override
        public void onAnimationEnd(Animator animation) {
            mRaceOverLinearLayout.setVisibility(View.GONE);
            mParticipantRecyclerView.animate().setDuration(200).alpha(1f);
            mParticipantRecyclerView.setVisibility(View.VISIBLE);
        }

        @Override
        public void onAnimationCancel(Animator animation) {

        }

        @Override
        public void onAnimationRepeat(Animator animation) {

        }
    };

    public MarketPageFragment() {
    }

    private void displaySnackBar(String message) {
        Snackbar.make(getView(), message, Snackbar.LENGTH_LONG)
                .setAction(R.string.snackbar_action_go_to_betslip, new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        NavUtils.navigateUpTo(getActivity(), new Intent(getActivity(), TextBetSlipActivity.class));
                    }
                }).show();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mMarket = null;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_market_page, container, false);

        try {
            BetSlip betSlip = BetSlipSingleton.get(getActivity()).getBetSlip();
            betSlip.getSelections();
            betSlip.getWagers();
        } catch (NullPointerException npe) {
            NavUtils.navigateUpTo(getActivity(), new Intent(getActivity(), SelectionScreenActivity.class));
            getActivity().finish();
            return v;
        }

        int meetingPosition = getArguments().getInt("MEETING_POSITION");
        int marketPosition = getArguments().getInt("MARKET_POSITION");

        Meeting meeting = MeetingsSingleton.get(getActivity()).getMeetings().get(meetingPosition);

        mMarket = meeting.getMarkets().get(marketPosition);

        mProgressBar = (ProgressBar) v.findViewById(R.id.progress_bar_textBetMarketSpinner);
        mDownloadingTextView = (TextView) v.findViewById(R.id.text_view_textBetMarketDownloadingParticipants);

        mParticipantRecyclerView = (RecyclerView) v.findViewById(R.id.recycler_view_participants);

        mRaceOverLinearLayout = (LinearLayout) v.findViewById(R.id.linear_layout_raceOver);

        return v;
    }

    public void displayDownloadScreen() {
        mParticipantRecyclerView.setAlpha(0f);
        mRaceOverLinearLayout.setVisibility(View.VISIBLE);
        mRaceOverLinearLayout.setAlpha(1f);
    }

    public void downloadMarket() {
        mMarket.getParticipants().clear();
        DownloadUtils.WilliamHillBetting williamHillBetting =
                new DownloadUtils.WilliamHillBetting(getActivity(), new DataDownloadListener() {
                    @Override
                    public void onDownloadStart() {
                    }

                    @Override
                    public void onDownloadComplete(Boolean success, String downloadedData) {
                        setupFragment();
                    }
                }, mMarket);

        williamHillBetting.execute(getString(R.string.download_url_williamhill_xml));
    }

    public static MarketPageFragment newInstance(int marketPosition, int meetingPosition) {
        Bundle bundle = new Bundle();
        bundle.putInt("MARKET_POSITION", marketPosition);
        bundle.putInt("MEETING_POSITION", meetingPosition);

        MarketPageFragment fragment = new MarketPageFragment();
        fragment.setArguments(bundle);
        return fragment;
    }

    private void setupFragment() {
        if (mMarket.getParticipants().size() > 0) {
            mParticipantRecyclerView.setHasFixedSize(true);
            mParticipantRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
            mParticipantAdapter = new TextBetParticipantsAdapter(mMarket.getParticipants(), new OnAdapterItemSelectedListener() {
                @Override
                public void onItemSelected(int selectedPosition) {
                    final BetSlip betSlip = BetSlipSingleton.get(getActivity()).getBetSlip();
                    final Participant participant = mMarket.getParticipants().get(selectedPosition);
                    if (selectionAlreadyOnBetSlip(betSlip, participant)) {
                        return;
                    }

                    addSelectionToBetSlip(betSlip, participant);
                }
            });

            mParticipantRecyclerView.setAdapter(mParticipantAdapter);

            mRaceOverLinearLayout.animate()
                                .alpha(0f)
                                .setDuration(200)
                                .setListener(mAnimator);

        } else {
            mDownloadingTextView.setText(getString(R.string.text_view_market_over, mMarket.getName(), mMarket.getOffTime()));
            mProgressBar.animate().alpha(0f);
            mProgressBar.setVisibility(View.GONE);
        }
    }

    private void addSelectionToBetSlip(final BetSlip betSlip, final Participant participant) {
        if (participant.getOdds().equals("SP")) {
            boolean isTricast = checkIsTricast(participant);
            betSlip.getSelections().add(
                    new BetSlipSelection(participant, true, mMarket.getEwOdds(), isTricast));
            displaySnackBar(getString(R.string.snackbar_body_added_without_odds, participant.getName()));
            return;
        }

        String title = getString(R.string.bottom_sheet_title_add_selection,
                                    participant.getName(), participant.getOdds());
        new BottomSheet.Builder(getActivity()).title(title)
                .sheet(R.menu.menu_bottom_sheet_participant).listener(new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                switch (which) {
                    case R.id.add_without_odds:
                        betSlip.getSelections().add(new BetSlipSelection(
                                                                    participant,
                                                                    false,
                                                                    mMarket.getEwOdds(),
                                                                    mMarket.isTricast()));
                        displaySnackBar(getString(R.string.snackbar_body_added_at_SP, participant.getName()));
                        break;
                    case R.id.add_with_odds:
                        betSlip.getSelections().add(new BetSlipSelection(
                                                                    participant,
                                                                    true,
                                                                    mMarket.getEwOdds(),
                                                                    mMarket.isTricast()));
                        displaySnackBar(getString(R.string.snackbar_body_added_with_odds, participant.getName(), participant.getOdds()));
                        break;
                }
            }
        }).show();
    }

    private boolean checkIsTricast(Participant participant) {
        String name = participant.getName();
        return !(name.equals("Favourite") || name.equals("2nd Favourite")) && mMarket.isTricast();
    }

    private boolean selectionAlreadyOnBetSlip(BetSlip betSlip, Participant participant) {
        for (BetSlipSelection b : betSlip.getSelections()) {
            if (participant.getId().equals(b.getParticipantId())) {
                Snackbar.make(getView(),
                        getString(
                                R.string.snackbar_body_selection_already_on_betslip,
                                participant.getName()
                        ), Snackbar.LENGTH_SHORT).show();
                return true;
            }
        }
        return false;
    }
}
