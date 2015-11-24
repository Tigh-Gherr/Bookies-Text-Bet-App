package com.tommyfrenchbookmakers.officialapp.docketobjects;

import java.util.ArrayList;

/**
 * Created by Tíghearnán on 15/07/2015.
 */
public class DocketBet {

    private int mBetNumber;
    private int mDocketNumber;
    private double mBetStake;
    private double mBetPayout;
    private boolean mWinner;
    private ArrayList<BetSelections> mSelections;
    private ArrayList<BetWagers> mWagers;

    public DocketBet(int betNumber, int docketNumber, double betStake, double betPayout, boolean winner) {
        mBetNumber = betNumber;
        mDocketNumber = docketNumber;
        mBetStake = betStake;
        mBetPayout = betPayout;
        mWinner = winner;
        mSelections = new ArrayList<>();
        mWagers = new ArrayList<>();
    }

    public int getBetNumber() {
        return mBetNumber;
    }

    public int getDocketNumber() {
        return mDocketNumber;
    }

    public double getBetStake() {
        return mBetStake;
    }

    public double getBetPayout() {
        return mBetPayout;
    }

    public boolean isWinner() {
        return mWinner;
    }

    public ArrayList<BetSelections> getSelections() {
        return mSelections;
    }

    public ArrayList<BetWagers> getWagers() {
        return mWagers;
    }

    @Override
    public String toString() {
        return "Bet Number: " + mBetNumber + "\n" +
                "Docket Number: " + mDocketNumber + "\n" +
                "Bet Stake: " + mBetStake + "\n" +
                "Bet Payout: " + mBetPayout + "\n" +
                "Bet Winner: " + mWinner + "\n" +
                "Number of Selections: " + mSelections.size() + "\n" +
                "Number of Wagers: " + mWagers.size();
    }
}