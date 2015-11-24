package com.tommyfrenchbookmakers.officialapp.docketobjects;

import com.tommyfrenchbookmakers.officialapp.enumerators.Sport;

/**
 * Created by Tíghearnán on 15/07/2015.
 */
public class BetSelections {

    private int mBetNumber;
    private String mName;
    private String mOdds;
    private int mPosition;
    private Sport mSport;
    private int mRule4;
    private int mDeadHeat;

    public BetSelections(int betNumber, String name, String odds, int position, Sport sport, int rule4, int deadHeat) {
        mBetNumber = betNumber;
        mName = name;
        mOdds = odds;
        mPosition = position;
        mSport = sport;
        mRule4 = rule4;
        mDeadHeat = deadHeat;

    }

    public int getBetNumber() {
        return mBetNumber;
    }

    public String getName() {
        return mName;
    }

    public String getOdds() {
        return mOdds;
    }

    public int getPosition() {
        return mPosition;
    }

    public Sport getSport() {
        return mSport;
    }

    public int getRule4() {
        return mRule4;
    }

    public boolean hasRule4() {
        return mRule4 > 0;
    }

    public int getDeadHeat() {
        return mDeadHeat;
    }

    public boolean hasDeadHeat() {
        return mDeadHeat > 0;
    }

    @Override
    public String toString() {
        return "Bet Number: " + mBetNumber + "\n" +
                "Name: " + mName+ "\n" +
                "Odds: " + mOdds + "\n" +
                "Position: " + mPosition + "\n" +
                "Sport: " + mSport.toString() + "\n" +
                "Rule 4: " + mRule4 + "\n" +
                "Dead Heat: " + mDeadHeat;
    }
}