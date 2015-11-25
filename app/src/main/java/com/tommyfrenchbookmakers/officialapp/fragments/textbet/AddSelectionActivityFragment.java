package com.tommyfrenchbookmakers.officialapp.fragments.textbet;

import android.animation.Animator;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.app.NavUtils;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.AppCompatButton;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.LinearLayout;

import com.android.tighearnan.frenchsscanner.R;
import com.sothree.slidinguppanel.SlidingUpPanelLayout;
import com.tommyfrenchbookmakers.officialapp.activities.SelectionScreenActivity;
import com.tommyfrenchbookmakers.officialapp.activities.textbet.AddSelectionActivity;
import com.tommyfrenchbookmakers.officialapp.customutils.DownloadUtils;
import com.tommyfrenchbookmakers.officialapp.customutils.NetworkUtils;
import com.tommyfrenchbookmakers.officialapp.interfaces.DataDownloadListener;
import com.tommyfrenchbookmakers.officialapp.interfaces.OnAdapterItemSelectedListener;
import com.tommyfrenchbookmakers.officialapp.meetingobjects.Market;
import com.tommyfrenchbookmakers.officialapp.meetingobjects.Meeting;
import com.tommyfrenchbookmakers.officialapp.recyclerviewadapters.TextBetMeetingAdapter;
import com.tommyfrenchbookmakers.officialapp.singletons.MeetingsSingleton;

import java.util.ArrayList;

/**
 * A placeholder fragment containing a simple view.
 */
public class AddSelectionActivityFragment extends Fragment {

    private RecyclerView mMeetingsRecyclerView;
    private RecyclerView.Adapter mMeetingsAdapter;
    private ArrayList<Meeting> mMeetings;

    private FrameLayout mNoRacingFrameLayout;

    private SlidingUpPanelLayout mPanelLayout;
    private LinearLayout mDownloadingMeetingsLinearLayout;

    private ViewPager mMarketsPagers;
    private TabLayout mMarketTimesTabLayout;

    private int mMeetingPosition;
    private DownloadUtils.WilliamHillBetting mWilliamHillBetting;

    public AddSelectionActivityFragment() {
    }

    public SlidingUpPanelLayout.PanelState getPanelState() {
        return mPanelLayout.getPanelState();
    }

    public void closePanel() {
        mPanelLayout.setPanelState(SlidingUpPanelLayout.PanelState.COLLAPSED);
    }

    private void setupFragment() {
        mMeetings = MeetingsSingleton.get(getActivity()).getMeetings();

        mDownloadingMeetingsLinearLayout.animate().alpha(0f).setDuration(200).setListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {

            }

            @Override
            public void onAnimationEnd(Animator animation) {
                mDownloadingMeetingsLinearLayout.setVisibility(View.INVISIBLE);
                if (mMeetings.size() == 0) {
                    mNoRacingFrameLayout.setVisibility(View.VISIBLE);
                    mNoRacingFrameLayout.animate().setDuration(200).alpha(1f);

                    AppCompatButton returnButton = (AppCompatButton) getView().findViewById(R.id.appcompat_button_return);
                    returnButton.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            NavUtils.navigateUpTo(getActivity(), new Intent(getActivity(), SelectionScreenActivity.class));
                        }
                    });

                } else {
                    mPanelLayout.setVisibility(View.VISIBLE);
                    mPanelLayout.animate().setDuration(200).alpha(1f);
                }
            }

            @Override
            public void onAnimationCancel(Animator animation) {

            }

            @Override
            public void onAnimationRepeat(Animator animation) {

            }
        });


        mMeetingsAdapter = new TextBetMeetingAdapter(mMeetings, new OnAdapterItemSelectedListener() {
            @Override
            public void onItemSelected(int selectedPosition) {
                mPanelLayout.setPanelState(SlidingUpPanelLayout.PanelState.EXPANDED);
                mMeetingPosition = selectedPosition;
                final ArrayList<Market> markets = mMeetings.get(selectedPosition).getMarkets();
                final String times[] = new String[markets.size()];
                for (int i = 0; i < times.length; i++) times[i] = markets.get(i).getOffTime();

                FragmentManager fm = getActivity().getSupportFragmentManager();
                mMarketsPagers.setAdapter(new FragmentStatePagerAdapter(fm) {
                    @Override
                    public Fragment getItem(int position) {
                        return MarketPageFragment.newInstance(position, mMeetingPosition);
                    }

                    @Override
                    public int getCount() {
                        return markets.size();
                    }

                    @Override
                    public CharSequence getPageTitle(int position) {
                        return times[position];
                    }
                });

                mMarketsPagers.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
                    @Override
                    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

                    }

                    @Override
                    public void onPageSelected(int position) {
                        Market market = mMeetings.get(mMeetingPosition).getMarkets().get(position);
                        ((AddSelectionActivity)getActivity()).getSupportActionBar().setSubtitle(
                                "EW: " + (!market.getEwOdds().equals("1/1") ? market.getEwOdds() : "N/A") + " Triciast: " + (market.isTricast() ? "Yes" : "No")
                        );
                    }

                    @Override
                    public void onPageScrollStateChanged(int state) {

                    }
                });

                mMarketTimesTabLayout.setupWithViewPager(mMarketsPagers);
            }
        });
        mMeetingsRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        mMeetingsRecyclerView.setAdapter(mMeetingsAdapter);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_add_selection, container, false);

        mDownloadingMeetingsLinearLayout = (LinearLayout) v.findViewById(R.id.linear_layout_downloadingMeetings);

        mNoRacingFrameLayout = (FrameLayout) v.findViewById(R.id.frame_layout_noRacing);

        mPanelLayout = (SlidingUpPanelLayout) v.findViewById(R.id.sliding_layout_meetingsAndMarkets);
        mPanelLayout.setAlpha(0f);
        mPanelLayout.setVisibility(View.INVISIBLE);
        mPanelLayout.setDragView(R.id.tab_layout_marketTimes);
        mPanelLayout.setPanelSlideListener(new SlidingUpPanelLayout.PanelSlideListener() {
            @Override
            public void onPanelSlide(View panel, float slideOffset) {
                ((AddSelectionActivity) getActivity()).changeElevation(1f - slideOffset);

                if (slideOffset > 0.75f) {
                    Meeting meeting = mMeetings.get(mMeetingPosition);
                    Market market = meeting.getMarkets().get(mMarketTimesTabLayout.getSelectedTabPosition());
                    ((AddSelectionActivity) getActivity()).setToolbarTitle(meeting.getName());
                    ((AddSelectionActivity) getActivity()).getSupportActionBar().setSubtitle(
                            "EW: " + (!market.getEwOdds().equals("1/1") ? market.getEwOdds() : "N/A") + " Tricast: " + (market.isTricast() ? "Yes" : "No")
                    );
                } else {
                    ((AddSelectionActivity) getActivity()).setToolbarTitle(getString(R.string.title_activity_add_selection));
                    ((AddSelectionActivity) getActivity()).getSupportActionBar().setSubtitle(null);
                }
            }

            @Override
            public void onPanelCollapsed(View panel) {
            }

            @Override
            public void onPanelExpanded(View panel) {
            }

            @Override
            public void onPanelAnchored(View panel) {

            }

            @Override
            public void onPanelHidden(View panel) {
            }
        });

        mMeetingsRecyclerView = (RecyclerView) v.findViewById(R.id.recycler_view_selectMeeting);

        mMarketTimesTabLayout = (TabLayout) v.findViewById(R.id.tab_layout_marketTimes);
        mMarketsPagers = (ViewPager) v.findViewById(R.id.view_pager_meetingMarkets);

        return v;
    }

    @Override
    public void onResume() {
        super.onResume();
        if (NetworkUtils.networkIsAvailable(getActivity())) {
            try {
                mWilliamHillBetting =
                        new DownloadUtils.WilliamHillBetting(getActivity(), new DataDownloadListener() {
                            @Override
                            public void onDownloadStart() {

                            }

                            @Override
                            public void onDownloadComplete(Boolean success, String downloadedData) {
                                setupFragment();
                            }
                        });

                mWilliamHillBetting.execute(getString(R.string.download_url_williamhill_xml));
            } catch (IllegalStateException ise) {
                NavUtils.navigateUpFromSameTask(getActivity());
            }
        } else {
            Snackbar.make(getView(), R.string.error_message_no_internet, Snackbar.LENGTH_LONG).show();
        }
    }

    @Override
    public void onStop() {
        super.onStop();
        mDownloadingMeetingsLinearLayout.setAlpha(1f);
        mDownloadingMeetingsLinearLayout.setVisibility(View.VISIBLE);
        mPanelLayout.setAlpha(0f);
        mPanelLayout.setVisibility(View.INVISIBLE);
        mNoRacingFrameLayout.setAlpha(0f);
        mNoRacingFrameLayout.setVisibility(View.INVISIBLE);
        if (mWilliamHillBetting != null) mWilliamHillBetting.cancel(false);
    }
}
