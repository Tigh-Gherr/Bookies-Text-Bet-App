package com.tommyfrenchbookmakers.officialapp.singletons;

import android.content.Context;

import com.tommyfrenchbookmakers.officialapp.meetingobjects.Meeting;

import java.util.ArrayList;

/**
 * Created by Tíghearnán on 04/09/2015.
 */
public class MeetingsSingleton {

    private static MeetingsSingleton sMeetingsSingleton;
    private Context mAppContext;
    private ArrayList<Meeting> mMeetings;

    private MeetingsSingleton(Context appContext) {
        mAppContext = appContext;
    }

    public static MeetingsSingleton get(Context c) {
        if (sMeetingsSingleton == null)
            sMeetingsSingleton = new MeetingsSingleton(c.getApplicationContext());
        return sMeetingsSingleton;
    }

    public ArrayList<Meeting> getMeetings() {
        return mMeetings;
    }

    public void setMeetings(ArrayList<Meeting> meetings) {
        mMeetings = meetings;
    }
}
