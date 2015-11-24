package com.tommyfrenchbookmakers.officialapp.meetingobjects;

import java.util.ArrayList;
import java.util.Date;

/**
 * Created by Tíghearnán on 04/09/2015.
 */
public class Market {
    private String mName;
    private String mOffTime;
    private String mId;
    private String mEwOdds;
    private String mEwPlaces;
    private Date mRaceDate;
    private boolean mTricast;
    private ArrayList<Participant> mParticipants;

    public Market(String name, String offTime, String id, String ewOdds, String ewPlaces, Date raceDate, boolean tricast) {
        mName = name;
        mOffTime = offTime;
        mId = id;
        mEwOdds = ewOdds;
        mEwPlaces = ewPlaces;
        mRaceDate = raceDate;
        mTricast = tricast;
        mParticipants = new ArrayList<>();
    }



    public ArrayList<Participant> getParticipants() {
        return mParticipants;
    }

    public void setParticipants(ArrayList<Participant> participants) {
        mParticipants = participants;
    }

    public Date getRaceDate() {
        return mRaceDate;
    }

    public void setRaceDate(Date raceDate) {
        mRaceDate = raceDate;
    }

    public String getEwOdds() {
        return mEwOdds;
    }

    public void setEwOdds(String ewOdds) {
        mEwOdds = ewOdds;
    }

    public String getEwPlaces() {
        return mEwPlaces;
    }

    public void setEwPlaces(String ewPlaces) {
        mEwPlaces = ewPlaces;
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

    public String getOffTime() {
        return mOffTime;
    }

    public void setOffTime(String offTime) {
        mOffTime = offTime;
    }

    public boolean isTricast() {
        return mTricast;
    }

    public void setTricast(boolean tricast) {
        mTricast = tricast;
    }
}
