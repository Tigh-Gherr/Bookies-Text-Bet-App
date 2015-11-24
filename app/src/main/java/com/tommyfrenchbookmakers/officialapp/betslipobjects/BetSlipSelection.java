package com.tommyfrenchbookmakers.officialapp.betslipobjects;

import com.tommyfrenchbookmakers.officialapp.meetingobjects.Participant;

/**
 * Created by Tíghearnán on 23/09/2015.
 */
public class BetSlipSelection {

    private String mParticipantId;
    private int mMarketId;
    private String mName;
    private String mOdds;
    private String mOddsDecimal;
    private String mEachWayOdds;
    private String mMarketName;
    private boolean mTakingOdds;
    private boolean mNonRunnerInsertFav;
    private boolean mNonRunnerInsertSecondFav;

    public BetSlipSelection(Participant p, boolean isTakingPrice, String eachWayOdds) {
        mParticipantId = p.getId();
        mName = p.getName();
        mOdds = p.getOdds();
        mOddsDecimal = p.getOddsDecimal();
        mMarketName = p.getMarketName();
        mMarketId = Integer.parseInt(p.getMarketId());

        mTakingOdds = isTakingPrice;
        mEachWayOdds = eachWayOdds;

        mNonRunnerInsertFav = false;
        mNonRunnerInsertSecondFav = false;
    }

    public String getParticipantId() {
        return mParticipantId;
    }

    public String getName() {
        return mName;
    }

    public String getOdds() {
        return mOdds;
    }

    public String getOddsDecimal() {
        return mOddsDecimal;
    }

    public String getMarketName() {
        return mMarketName;
    }

    public String getEachWayOdds() {
        return mEachWayOdds;
    }

    public void setEachWayOdds(String eachWayOdds) {
        mEachWayOdds = eachWayOdds;
    }

    public boolean isTakingOdds() {
        return mTakingOdds;
    }

    public void setTakingOdds(boolean takingOdds) {
        mTakingOdds = takingOdds;
    }

    public boolean isNonRunnerInsertFav() {
        return mNonRunnerInsertFav;
    }

    public void setNonRunnerInsertFav(boolean nonRunnerInsertFav) {
        mNonRunnerInsertFav = nonRunnerInsertFav;
    }

    public boolean isNonRunnerInsertSecondFav() {
        return mNonRunnerInsertSecondFav;
    }

    public void setNonRunnerInsertSecondFav(boolean nonRunnerInsertSecondFav) {
        mNonRunnerInsertSecondFav = nonRunnerInsertSecondFav;
    }

    @Override
    public String toString() {
        return mName + " " + (mTakingOdds ? mOdds + " ": "") + (mNonRunnerInsertFav ? "NR Fav " : (mNonRunnerInsertSecondFav ? "NR 2ndFav " : "")) + "at " + mMarketName;
    }

    public int getMarketId() {
        return mMarketId;
    }

    public void setMarketId(int marketId) {
        mMarketId = marketId;
    }
}