package com.tommyfrenchbookmakers.officialapp.meetingobjects;

import java.util.ArrayList;

/**
 * Created by Tíghearnán on 03/09/2015.
 */
public class Meeting {

    private String mName;
    private String mId;
    private String mNextRaceTime;
    private String mLastRaceTime;
    private int mNumberOfRacesLeft;
    private ArrayList<Market> mMarkets;

    public Meeting(String name, String id, String lastUpdateDate) {
        mName = name;
        mId = id;
        mMarkets = new ArrayList<>();
    }

    public int getNumberOfRacesLeft() {
        return mNumberOfRacesLeft;
    }

    public void setNumberOfRacesLeft(int numberOfRacesLeft) {
        mNumberOfRacesLeft = numberOfRacesLeft;
    }

    public String getNextRaceTime() {
        return mNextRaceTime;
    }

    public void setNextRaceTime(String nextRaceTime) {
        mNextRaceTime = nextRaceTime;
    }

    public String getLastRaceTime() {
        return mLastRaceTime;
    }

    public void setLastRaceTime(String lastRaceTime) {
        mLastRaceTime = lastRaceTime;
    }

    public ArrayList<Market> getMarkets() {
        return mMarkets;
    }

    public void setMarkets(ArrayList<Market> markets) {
        mMarkets = markets;
    }

    public String getName() {
        return mName;
    }

    public void setName(String name) {
        mName = name;
    }

    public String getId() {
        return mId;
    }

    public void setId(String id) {
        mId = id;
    }

    public String toString() {
        return mName;
    }
}
