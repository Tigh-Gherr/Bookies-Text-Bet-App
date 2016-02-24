package com.tommyfrenchbookmakers.officialapp.ui.TextBetSlipActivity;

import com.tommyfrenchbookmakers.officialapp.enumerators.WagerType;

/**
 * Created by Tíghearnán on 20/09/2015.
 */
public interface WagerConfirmedListener {
        void onWagerConfirmed(String unitStake, boolean eachWay, WagerType wagerType);
}
