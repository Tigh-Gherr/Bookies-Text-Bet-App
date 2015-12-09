package com.tommyfrenchbookmakers.officialapp.customutils;

import com.tommyfrenchbookmakers.officialapp.Global;
import com.tommyfrenchbookmakers.officialapp.betslipobjects.BetSlip;
import com.tommyfrenchbookmakers.officialapp.betslipobjects.BetSlipSelection;
import com.tommyfrenchbookmakers.officialapp.betslipobjects.BetSlipWager;
import com.tommyfrenchbookmakers.officialapp.enumerators.WagerType;

import org.paukov.combinatorics.Factory;
import org.paukov.combinatorics.Generator;
import org.paukov.combinatorics.ICombinatoricsVector;

/**
 * Created by Tíghearnán on 06/09/2015.
 */
public class BettingUtils {

    public static int numberOfCombinations(int numberOfSelections, int minimumAllowedByWager) {
        int index, combinations;
        int factorial, temp, result;

        for (index = 1, factorial = 1; index <= numberOfSelections; index++) {
            factorial = factorial * index;
        }

        for (index = 1, temp = 1; index <= minimumAllowedByWager; index++) {
            temp = temp * index;
        }

        for (index = 1, result = 1; index <= (numberOfSelections - minimumAllowedByWager); index++) {
            result = result * index;
        }

        combinations = factorial / (temp * result);

        return combinations;
    }

    public static double calculateRunningTotal(BetSlipWager wager, BetSlip betSlip) {
        double totalWinningsSoFar = 0d;
        double totalEachWayWinningsSoFar = 0d;

        switch (wager.getWagerType().getCategory()) {
            case Global.WAGER_CATEGORY_MULTIPLE:
                totalWinningsSoFar = 0d;
                ICombinatoricsVector<BetSlipSelection> initialVector = Factory.createVector(betSlip.getSelections());
                Generator<BetSlipSelection> generator =
                        Factory.createSimpleCombinationGenerator(initialVector,
                                WagerType.getValueOf(wager.getWagerType().toString()) == WagerType.ACCUM ? betSlip.getSelections().size() : wager.getWagerType().getMinNumberOfSelections());

                for(ICombinatoricsVector<BetSlipSelection> combination : generator) {
                    double combinationWinnings = Double.parseDouble(wager.getUnitStake());
                    double combinationEachWay = combinationWinnings;
                    for (BetSlipSelection s : combination) {
                        if (s.getOdds().equals("SP")) return 0;

                        double oddsDecimal = Double.parseDouble(s.getOddsDecimal());
                        double oddsEachWayDecimal = 0d;
                        if(wager.isEachWay()) {
                            oddsEachWayDecimal = (oddsDecimal - 1) / Double.parseDouble(s.getEachWayOdds().charAt(s.getEachWayOdds().length() - 1)+"") + 1;
                        }

                        combinationWinnings = oddsDecimal * combinationWinnings;
                        combinationEachWay = oddsEachWayDecimal * combinationEachWay;
                    }

                    totalWinningsSoFar += (combinationWinnings + combinationEachWay);
                }
                break;
            case Global.WAGER_CATEGORY_FULL_COVER_WITH_SINGLES:
                totalWinningsSoFar = 1d;
                totalEachWayWinningsSoFar = 1d;
                for (BetSlipSelection s : betSlip.getSelections()) {
                    if (s.getOdds().equals("SP")) return 0;

                    double oddsDecimal = Double.parseDouble(s.getOddsDecimal());
                    double oddsEachWayDecimal = 0d;
                    if(wager.isEachWay()) {
                        oddsEachWayDecimal = (oddsDecimal - 1) / Double.parseDouble(s.getEachWayOdds().charAt(s.getEachWayOdds().length() - 1)+"") + 1;
                    }

                    totalWinningsSoFar = ((oddsDecimal + 1) * totalWinningsSoFar);
                    totalEachWayWinningsSoFar = ((oddsEachWayDecimal + 1) * totalEachWayWinningsSoFar);
                }
                double bonus = (WagerType.getValueOf(wager.getWagerType().toString()) == WagerType.PATENT ? 1d : 1.20d);
                totalWinningsSoFar = (totalWinningsSoFar - 1) * Double.parseDouble(wager.getUnitStake()) * bonus;
                totalEachWayWinningsSoFar = (totalEachWayWinningsSoFar - 1) * Double.parseDouble(wager.getUnitStake());// * bonus;
                totalWinningsSoFar += totalEachWayWinningsSoFar;
                break;
            case Global.WAGER_CATEGORY_FULL_COVER_WITHOUT_SINGLES:
                totalWinningsSoFar = Double.parseDouble(wager.getUnitStake());
                totalEachWayWinningsSoFar = Double.parseDouble(wager.getUnitStake());
                double singlesToSubtract = 0d;
                double eachWaySinglesToSubtract = 0d;
                for (BetSlipSelection s : betSlip.getSelections()) {
                    if (s.getOddsDecimal().equals("SP")) return 0;

                    double oddsDecimal = Double.parseDouble(s.getOddsDecimal());
                    double oddsEachWayDecimal = 0d;
                    if(wager.isEachWay()) {
                        oddsEachWayDecimal = (oddsDecimal - 1) / Double.parseDouble(s.getEachWayOdds().charAt(s.getEachWayOdds().length() - 1)+"") + 1;
                    }

                    totalWinningsSoFar = ((oddsDecimal + 1) * totalWinningsSoFar);
                    totalEachWayWinningsSoFar = ((oddsEachWayDecimal + 1) * totalEachWayWinningsSoFar);
                    singlesToSubtract += oddsDecimal * Double.parseDouble(wager.getUnitStake());
                    eachWaySinglesToSubtract += oddsEachWayDecimal * Double.parseDouble(wager.getUnitStake());
                }
                totalWinningsSoFar -= (singlesToSubtract + (Double.parseDouble(wager.getUnitStake()) * 2));
                if(wager.isEachWay()) totalEachWayWinningsSoFar -= eachWaySinglesToSubtract;
                totalWinningsSoFar += totalEachWayWinningsSoFar;
            case Global.WAGER_CATEGORY_UP_AND_DOWN:
                break;
            case Global.WAGER_CATEGORY_SPECIAL:
                break;
            case Global.WAGER_CATEGORY_PENDING_RETURN:
                break;
            default:
                return 0;
        }

        return totalWinningsSoFar;
    }
}
