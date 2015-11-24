package com.tommyfrenchbookmakers.officialapp.enumerators;

/**
 * Created by Tíghearnán on 05/07/2015.
 */
public enum Sport {
    HORSE_RACING,
    GREYHOUNDS,
    GOLF,
    GAA,
    UNKNOWN,
    FOOTBALL,
    TENNIS,
    BOXING,
    RUGBY_UNION,
    RUGBY_LEAGUE,
    DARTS;

    public static Sport getSport(String value) {
        Sport sportToSend;

        try {
            sportToSend = valueOf(value);
        } catch (IllegalArgumentException iae) {
            sportToSend = null;
        }

        return sportToSend;
    }
}