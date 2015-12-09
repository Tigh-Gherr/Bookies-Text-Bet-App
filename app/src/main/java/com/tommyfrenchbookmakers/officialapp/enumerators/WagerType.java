package com.tommyfrenchbookmakers.officialapp.enumerators;

import com.tommyfrenchbookmakers.officialapp.Global;

/**
 * Created by Tíghearnán on 06/09/2015.
 */
public enum WagerType {
    SINGLE("Singles", 1, false, false, null, Global.WAGER_CATEGORY_MULTIPLE),
    DOUBLE("Doubles", 2, false, false, null, Global.WAGER_CATEGORY_MULTIPLE),
    TREBLE("Trebles", 3, false, false, null, Global.WAGER_CATEGORY_MULTIPLE),
    TIMER_4("4-Timers", 4, false, false, null, Global.WAGER_CATEGORY_MULTIPLE),
    TIMER_5("5-Timers", 5, false, false, null, Global.WAGER_CATEGORY_MULTIPLE),
    TIMER_6("6-Timers", 6, false, false, null, Global.WAGER_CATEGORY_MULTIPLE),
    TIMER_7("7-Timers", 7, false, false, null, Global.WAGER_CATEGORY_MULTIPLE),
    TIMER_8("8-Timers", 8, false, false, null, Global.WAGER_CATEGORY_MULTIPLE),
    TIMER_9("9-Timers", 9, false, false, null, Global.WAGER_CATEGORY_MULTIPLE),
    TIMER_10("10-Timers", 10, false, false, null, Global.WAGER_CATEGORY_MULTIPLE),
    TRIXIE("Trixie", 3, true, false, 4, Global.WAGER_CATEGORY_FULL_COVER_WITHOUT_SINGLES),
    YANKEE("Yankee", 4, true, false, 11, Global.WAGER_CATEGORY_FULL_COVER_WITHOUT_SINGLES),
    CANADIAN("Canadian", 5, true, false, 26, Global.WAGER_CATEGORY_FULL_COVER_WITHOUT_SINGLES),
    HEINZ("Heinz", 6, true, false, 57, Global.WAGER_CATEGORY_FULL_COVER_WITHOUT_SINGLES),
    SUPER_HEINZ("Super Heinz", 7, true, false, 120, Global.WAGER_CATEGORY_FULL_COVER_WITHOUT_SINGLES),
    GOLIATH("Goliath", 8, true, false, 247, Global.WAGER_CATEGORY_FULL_COVER_WITHOUT_SINGLES),
    PATENT("Patent", 3, true, false, 7, Global.WAGER_CATEGORY_FULL_COVER_WITH_SINGLES),
    LUCKY_15("Lucky 15", 4, true, false, 15, Global.WAGER_CATEGORY_FULL_COVER_WITH_SINGLES),
    LUCKY_31("Lucky 31", 5, true, false, 31, Global.WAGER_CATEGORY_FULL_COVER_WITH_SINGLES),
    LUCKY_63("Lucky 63", 6, true, false, 63, Global.WAGER_CATEGORY_FULL_COVER_WITH_SINGLES),
    ALPHABET("Alphabet", 6, true, false, 26, Global.WAGER_CATEGORY_SPECIAL),
    ACCUM("Accumulator", 2, false, false, null, Global.WAGER_CATEGORY_MULTIPLE),
    UP_AND_DOWN("Double Laps", 2, true, false, 2, Global.WAGER_CATEGORY_UP_AND_DOWN),
    ROUND_ROBIN("Round Robin", 3, true, false, 10, Global.WAGER_CATEGORY_UP_AND_DOWN),
    FLAG("Flag", 4, true, false, 23, Global.WAGER_CATEGORY_UP_AND_DOWN),
    SUPER_FLAG("Super Flag", 5, true, false, 46, Global.WAGER_CATEGORY_UP_AND_DOWN),
    FORECAST("Forecast", 2, true, true, 1, Global.WAGER_CATEGORY_PENDING_RETURN),
    REV_FORECAST("Reverse Forecast", 2, true, true, 2, Global.WAGER_CATEGORY_PENDING_RETURN),
    COMB_FORECAST("Combination Forecast", 3, true, true, 3, Global.WAGER_CATEGORY_PENDING_RETURN),
    TRICAST("Tricast", 3, true, true, 1, Global.WAGER_CATEGORY_PENDING_RETURN),
    COMB_TRICAST("Combination Tricast", 3, true, true, 6, Global.WAGER_CATEGORY_PENDING_RETURN);

    private String mName;
    private int mMinNumberOfSelections;
    private boolean mHasFixedNumberOfSelections;
    private boolean mSameRace;
    private Integer mNumberOfBets;
    private int mCategory;

    WagerType(String name, int minNumberOfSelections, boolean fixedNumberOfSelections, boolean sameRace, Integer numberOfBets, int category) {
        mName = name;
        mMinNumberOfSelections = minNumberOfSelections;
        mHasFixedNumberOfSelections = fixedNumberOfSelections;
        mSameRace = sameRace;
        mNumberOfBets = numberOfBets;
        mCategory = category;
    }

    public int getMinNumberOfSelections() {
        return mMinNumberOfSelections;
    }

    public boolean getHasFixedNumberOfSelections() {
        return mHasFixedNumberOfSelections;
    }

    public boolean isSameRace() {
        return mSameRace;
    }

    public static WagerType getValueOf(String name) {
        for (WagerType w :
                WagerType.values()) {
            if(w.getName().equals(name)) return w;
        }

        return null;
    }

    public Integer getNumberOfBets() {
        return mNumberOfBets;
    }

    public String getName() {
        return mName;
    }

    public boolean isHasMaximumNumberOfSelections() {
        return mHasFixedNumberOfSelections;
    }

    public int getCategory() {
        return mCategory;
    }

    @Override
    public String toString() {
        return mName;
    }

}
