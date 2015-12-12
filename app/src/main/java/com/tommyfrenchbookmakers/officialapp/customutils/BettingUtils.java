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

    // Used to calculate the total number of times a wager will be applied to a quantity of selections.
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

    // Calculates the maximum amount of money that can be won on a BetSlip.
    public static double calculateRunningTotal(BetSlipWager wager, BetSlip betSlip) {
        double totalWinningsSoFar = 0d;
        double totalEachWayWinningsSoFar = 0d;

        switch (wager.getWagerType().getCategory()) {
            case Global.WAGER_CATEGORY_MULTIPLE:
                totalWinningsSoFar = 0d;
                // Used to get all the possible combinations of selections for the provided wager.
                ICombinatoricsVector<BetSlipSelection> initialVector = Factory.createVector(betSlip.getSelections());
                // If the wager is an accum, pass the total number of selections, otherwise pass the minimum required by the bet.
                Generator<BetSlipSelection> generator =
                        Factory.createSimpleCombinationGenerator(initialVector,
                                WagerType.getValueOf(wager.getWagerType().toString()) == WagerType.ACCUM ? betSlip.getSelections().size() : wager.getWagerType().getMinNumberOfSelections());

                // For every combination generated.
                for (ICombinatoricsVector<BetSlipSelection> combination : generator) {
                    // Straight and each way winnings.
                    double combinationWinnings = Double.parseDouble(wager.getUnitStake());
                    double combinationEachWay = combinationWinnings;
                    // For every selection in the current combination.
                    for (BetSlipSelection s : combination) {
                        // If any selection doesn't have odds, the running total cannot be calculated.
                        if (s.getOdds().equals("SP")) return 0;

                        // Store the decimal odds of the current selection.
                        double oddsDecimal = Double.parseDouble(s.getOddsDecimal());
                        // If the wager is each way, calculate and store the each way odds.
                        double oddsEachWayDecimal = 0d;
                        if (wager.isEachWay()) {
                            // TODO: This may need modified to work with the French's data feed.
                            oddsEachWayDecimal = (oddsDecimal - 1) / Double.parseDouble(s.getEachWayOdds().charAt(s.getEachWayOdds().length() - 1) + "") + 1;
                        }

                        // Calculate the current selections winnings and add it onto the running total.
                        combinationWinnings = oddsDecimal * combinationWinnings;
                        combinationEachWay = oddsEachWayDecimal * combinationEachWay;
                    }

                    // Return the result.
                    totalWinningsSoFar += (combinationWinnings + combinationEachWay);
                }
                break;
            case Global.WAGER_CATEGORY_FULL_COVER_WITH_SINGLES:
                totalWinningsSoFar = 1d;
                totalEachWayWinningsSoFar = 1d;

                // For each selection in the betslip
                for (BetSlipSelection s : betSlip.getSelections()) {
                    // If any selection doesn't have odds, the running total cannot be calculated.
                    if (s.getOdds().equals("SP")) return 0;

                    // Store the decimal odds of the current selection.
                    double oddsDecimal = Double.parseDouble(s.getOddsDecimal());
                    // If the wager is each way, calculate and store the each way odds.
                    double oddsEachWayDecimal = 0d;
                    if(wager.isEachWay()) {
                        // TODO: This may need modified to work with the French's data feed.
                        oddsEachWayDecimal = (oddsDecimal - 1) / Double.parseDouble(s.getEachWayOdds().charAt(s.getEachWayOdds().length() - 1)+"") + 1;
                    }

                    // Calculate the current selections winnings and add it onto the running total.
                    totalWinningsSoFar = ((oddsDecimal + 1) * totalWinningsSoFar);
                    totalEachWayWinningsSoFar = ((oddsEachWayDecimal + 1) * totalEachWayWinningsSoFar);
                }

                // Apply the bonus, current assumptions:
                // * Patent has no bonus
                // * Everything else has a % bonus
                // * Each way does not get a bonus added
                // TODO: THIS WILL NEED TO BE CHANGED AS THIS DOES NOT ACCURATELY REFLECT THE SHOPS BONUSES
                double bonus = (WagerType.getValueOf(wager.getWagerType().toString()) == WagerType.PATENT ? 1d : 1.20d);
                totalWinningsSoFar = (totalWinningsSoFar - 1) * Double.parseDouble(wager.getUnitStake()) * bonus;
                totalEachWayWinningsSoFar = (totalEachWayWinningsSoFar - 1) * Double.parseDouble(wager.getUnitStake());// * bonus;
                totalWinningsSoFar += totalEachWayWinningsSoFar;
                break;
            case Global.WAGER_CATEGORY_FULL_COVER_WITHOUT_SINGLES:
                totalWinningsSoFar = Double.parseDouble(wager.getUnitStake());
                totalEachWayWinningsSoFar = Double.parseDouble(wager.getUnitStake());

                // Stores the total value of a unit stake applied to the odds of each selection, so it can be deducted
                // after the calculation.
                double singlesToSubtract = 0d;
                double eachWaySinglesToSubtract = 0d;

                // For each selection in the betslip.
                for (BetSlipSelection s : betSlip.getSelections()) {
                    // If any selection doesn't have odds, the running total cannot be calculated.
                    if (s.getOddsDecimal().equals("SP")) return 0;

                    double oddsDecimal = Double.parseDouble(s.getOddsDecimal());
                    // If the wager is each way, calculate and store the each way odds.
                    double oddsEachWayDecimal = 0d;
                    if (wager.isEachWay()) {
                        oddsEachWayDecimal = (oddsDecimal - 1) / Double.parseDouble(s.getEachWayOdds().charAt(s.getEachWayOdds().length() - 1) + "") + 1;
                    }

                    // Calculate the current selections winnings and add it to the running total.
                    totalWinningsSoFar = ((oddsDecimal + 1) * totalWinningsSoFar);
                    totalEachWayWinningsSoFar = ((oddsEachWayDecimal + 1) * totalEachWayWinningsSoFar);
                    singlesToSubtract += oddsDecimal * Double.parseDouble(wager.getUnitStake());
                    eachWaySinglesToSubtract += oddsEachWayDecimal * Double.parseDouble(wager.getUnitStake());
                }

                // Subtract the total of the singles from the running total.
                totalWinningsSoFar -= (singlesToSubtract + (Double.parseDouble(wager.getUnitStake()) * 2));
                if(wager.isEachWay()) totalEachWayWinningsSoFar -= eachWaySinglesToSubtract;
                totalWinningsSoFar += totalEachWayWinningsSoFar;
            case Global.WAGER_CATEGORY_UP_AND_DOWN:
                // TODO: Add calculation
                break;
            case Global.WAGER_CATEGORY_SPECIAL:
                // TODO: Add interface for special bets.
                // TODO: Add calculation.
                break;
            case Global.WAGER_CATEGORY_PENDING_RETURN:
                // TODO: Think of best way to handle.
                break;
            default:
                return 0;
        }

        return totalWinningsSoFar;
    }
}
