package com.tommyfrenchbookmakers.officialapp.docketobjects;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Tíghearnán on 15/07/2015.
 */
public class Docket {

    private int mDocketNumber;
    private String mAccountNumber;
    private String mBarcode;
    private double mTotalStake;
    private double mTotalPayout;
    private boolean mWinner;
    private ArrayList<DocketBet> mBets;

    public Docket(int docketNumber, String accountNumber, String barcode, double totalStake, double totalPayout, boolean winner) {
        mDocketNumber = docketNumber;
        mAccountNumber = accountNumber;

        mBarcode = barcode;
        mTotalStake = totalStake;
        mTotalPayout = totalPayout;
        mWinner = winner;
        mBets = new ArrayList<>();
    }

    public int getDocketNumber() {
        return mDocketNumber;
    }

    public String getAccountNumber() {
        return mAccountNumber;
    }

    public String getBarcode() {
        return mBarcode;
    }

    public double getTotalStake() {
        return mTotalStake;
    }

    public double getTotalPayout() {
        return mTotalPayout;
    }

    public boolean isWinner() {
        return mWinner;
    }

    public List<DocketBet> getBets() {
        return mBets;
    }

    @Override
    public String toString() {
        return "Docket Number: " + mDocketNumber + "\n" +
                "Account Number: " + mAccountNumber + "\n" +
                "Barcode: " + mBarcode + "\n" +
                "Total Stake: " + mTotalStake + "\n" +
                "Total Payout: " + mTotalPayout + "\n" +
                "Number of Bets: " + mBets.size() + "\n" +
                "Winner: " + mWinner;
    }
}