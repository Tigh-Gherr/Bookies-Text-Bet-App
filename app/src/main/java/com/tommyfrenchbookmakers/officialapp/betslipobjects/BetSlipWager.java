package com.tommyfrenchbookmakers.officialapp.betslipobjects;

import com.tommyfrenchbookmakers.officialapp.utils.BettingUtils;
import com.tommyfrenchbookmakers.officialapp.enumerators.WagerType;

/**
 * Created by Tíghearnán on 04/09/2015.
 */
public class BetSlipWager {

    private WagerType mWagerType;
    private String mUnitStake;
    private String mBetName;
    private double mPotentialReturns;
    private boolean mEachWay;
    private double mTotalStake;

    public BetSlipWager() {
        mUnitStake = "";
        mBetName = "";
        mEachWay = false;
    }

    public BetSlipWager(WagerType wagerType, String unitStake, boolean eachWay) {
        mWagerType = wagerType;
        mUnitStake = unitStake;
        mEachWay = eachWay;
    }

    public void setWagerType(WagerType wagerType) {
        mWagerType = wagerType;
        mBetName = wagerType.getName();
    }

    public WagerType getWagerType() {
        return mWagerType;
    }

    public String getUnitStake() {
        return mUnitStake;
    }

    public void setUnitStake(String unitStake) {
        mUnitStake = unitStake;
    }

    public String getBetName() {
        return mBetName;
    }

    public void setBetName(String betName) {
        mBetName = betName;
    }

    public boolean isEachWay() {
        return mEachWay;
    }

    public void setEachWay(boolean eachWay) {
        mEachWay = eachWay;
    }

    public double getTotalStake() {
        return mTotalStake;
    }

    public double getPotentialReturns() {
        return mPotentialReturns;
    }

    public void setPotentialReturns(double potentialReturns) {
        mPotentialReturns = potentialReturns;
    }

    public void calculateStake(int numberOfSelections) {
        double unitStake = Double.parseDouble(mUnitStake);
        double totalStake;

        if (mWagerType.getName().equals("Accumulator")) {
            totalStake = (mEachWay ? unitStake * 2 : unitStake);
        } else if (!mWagerType.getHasFixedNumberOfSelections()) {
            totalStake = (mEachWay ? unitStake * 2 : unitStake) * (double) BettingUtils.numberOfCombinations(numberOfSelections, mWagerType.getMinNumberOfSelections());
        } else {
            totalStake = (mEachWay ? unitStake * 2 : unitStake) * mWagerType.getNumberOfBets();
        }

        mTotalStake = totalStake;
    }

    @Override
    public String toString() {
        return "£" + mUnitStake + (mEachWay ? "ew " : " ") + mWagerType.getName();
    }
}
