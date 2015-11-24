package com.tommyfrenchbookmakers.officialapp.betslipobjects;

import android.content.Context;

import com.android.tighearnan.frenchsscanner.R;
import com.tommyfrenchbookmakers.officialapp.customutils.ParseUtils;

import java.util.ArrayList;

/**
 * Created by Tíghearnán on 04/09/2015.
 */
public class BetSlip {

    private ArrayList<BetSlipSelection> mSelections;
    private ArrayList<BetSlipWager> mWagers;
    private String mUserAccount;

    public BetSlip() {
        mUserAccount = "tc.52";

        mWagers = new ArrayList<BetSlipWager>() {
            private static final long serialVersionUID = 1L;

            @Override
            public String toString() {
                String toString = "";
                for (BetSlipWager w : this) toString += w.toString() + "\n";

                return toString;
            }
        };
        mSelections = new ArrayList<BetSlipSelection>() {
            private static final long serialVersionUID = 1L;

            @Override
            public String toString() {
                String toString = "";
                for (BetSlipSelection s : this) toString += s.toString() + "\n";

                return toString;
            }
        };
    }

    private int[] getMarketIds() {
        int marketIds[] = new int[mSelections.size()];
        for(int i = 0; i < mSelections.size(); i++) {
            marketIds[i] = mSelections.get(i).getMarketId();
        }

        return marketIds;
    }

    public boolean hasSameRace() {
        int marketIds[] = getMarketIds();

        for(int i = 0; i < marketIds.length - 1; i++) {
            for(int j = i + 1; j < marketIds.length; j++) {
                if(marketIds[i] == marketIds[j]) return true;
            }
        }

        return false;
    }

    public boolean allInSameRace() {
        int marketIds[] = getMarketIds();

        for(int i = 0; i < marketIds.length - 1; i++) {
            for(int j = i + 1; j < marketIds.length; j++) {
                if(marketIds[i] != marketIds[j]) return false;
            }
        }

        return true;
    }


    public ArrayList<BetSlipSelection> getSelections() {
        return mSelections;
    }

    public ArrayList<BetSlipWager> getWagers() {
        return mWagers;
    }

    public void setWagers(ArrayList<BetSlipWager> wagers) {
        mWagers = wagers;
    }

    public String getUserAccount() {
        return mUserAccount;
    }

    public void setUserAccount(String userAccount) {
        mUserAccount = userAccount;
    }

    public String toString(Context c) {
        return c.getString(R.string.sms_body_text_bet, mUserAccount, mSelections.toString(), mWagers.toString().trim());
    }
}
