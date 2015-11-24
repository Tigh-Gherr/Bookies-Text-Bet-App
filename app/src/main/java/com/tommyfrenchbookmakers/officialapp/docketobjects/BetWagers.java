package com.tommyfrenchbookmakers.officialapp.docketobjects;

/**
 * Created by Tíghearnán on 15/07/2015.
 */
public class BetWagers {

    private int mWagerNumber;
    private int mBetNumber;
    private double mUnitStake;
    private boolean mEachWay;
    private String mWagerType;
    private boolean mWinner;
    private double mPayout;

    public BetWagers(int wagerNumber, int betNumber, double unitStake, boolean eachWay, String wagerType, boolean winner, double payout) {
        mWagerNumber = wagerNumber;
        mBetNumber = betNumber;
        mUnitStake = unitStake;
        mEachWay = eachWay;
        mWagerType = wagerType;
        mWinner = winner;
        mPayout = payout;
    }

    public int getWagerNumber() {
        return mWagerNumber;
    }

    public int getBetNumber() {
        return mBetNumber;
    }

    public double getUnitStake() {
        return mUnitStake;
    }

    public boolean isEachWay() {
        return mEachWay;
    }

    public String getWagerType() {
        return mWagerType;
    }

    public boolean isWinner() {
        return mWinner;
    }

    public double getPayout() {
        return mPayout;
    }

    @Override
    public String toString() {
        return "Wager Number: " + mWagerType + "\n" +
                "Bet Number: " + mBetNumber + "\n" +
                "Unit Stake: " + mUnitStake + "\n" +
                "Each Way: " + mEachWay + "\n" +
                "Wager Type: " + mWagerType + "\n" +
                "Winner: " + mWinner + "\n" +
                "Payout: " + mPayout;
    }
}