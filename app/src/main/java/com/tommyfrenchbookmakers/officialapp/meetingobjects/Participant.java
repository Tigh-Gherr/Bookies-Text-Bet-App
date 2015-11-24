package com.tommyfrenchbookmakers.officialapp.meetingobjects;

/**
 * Created by Tíghearnán on 04/09/2015.
 */
public class Participant {
    private String mId;
    private String mMarketId;
    private String mName;
    private String mOdds;
    private String mOddsDecimal;
    private boolean mTakeOdds;
    private String mMarketName;

    public Participant(String id, String name, String odds, String oddsDecimal) {
        mId = id;
        mName = name;
        mOdds = odds;
        mOddsDecimal = oddsDecimal;
        mTakeOdds = false;
    }

    public boolean isTakeOdds() {
        return mTakeOdds;
    }

    public String getMarketName() {
        return mMarketName;
    }

    public void setMarketName(String marketName) {
        mMarketName = marketName;
    }

    public void setTakeOdds(boolean takeOdds) {
        mTakeOdds = takeOdds;
    }

    public String getMarketId() {
        return mMarketId;
    }

    public void setMarketId(String marketId) {
        mMarketId = marketId;
    }

    public String getId() {
        return mId;
    }

    public void setId(String id) {
        mId = id;
    }

    public String getName() {
        return mName;
    }

    public void setName(String name) {
        mName = name;
    }

    public String getOdds() {
        return mOdds;
    }

    public void setOdds(String odds) {
        mOdds = odds;
    }

    public String getOddsDecimal() {
        return mOddsDecimal;
    }

    public void setOddsDecimal(String oddsDecimal) {
        mOddsDecimal = oddsDecimal;
    }
}
