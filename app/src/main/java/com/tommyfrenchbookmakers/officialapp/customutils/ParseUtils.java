package com.tommyfrenchbookmakers.officialapp.customutils;

import android.content.Context;
import android.util.Log;

import com.tommyfrenchbookmakers.officialapp.docketobjects.BetSelections;
import com.tommyfrenchbookmakers.officialapp.docketobjects.BetWagers;
import com.tommyfrenchbookmakers.officialapp.docketobjects.Docket;
import com.tommyfrenchbookmakers.officialapp.docketobjects.DocketBet;
import com.tommyfrenchbookmakers.officialapp.enumerators.Sport;
import com.tommyfrenchbookmakers.officialapp.meetingobjects.Market;
import com.tommyfrenchbookmakers.officialapp.meetingobjects.Meeting;
import com.tommyfrenchbookmakers.officialapp.meetingobjects.Participant;
import com.tommyfrenchbookmakers.officialapp.singletons.GlobalDocket;
import com.tommyfrenchbookmakers.officialapp.singletons.MeetingsSingleton;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.StringReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;

/**
 * Created by Tíghearnán on 31/08/2015.
 */
public class ParseUtils {

    public static String dataFromWebService(Context c, String dataToParse) {
        String dataFromWebService = "";
        HttpURLConnection connection = null;
        BufferedReader reader = null;

        try {
            URL url = new URL(dataToParse);
            connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");
            connection.connect();

            InputStream inputStream = connection.getInputStream();
            if (inputStream == null) return null;

            StringBuilder builder = new StringBuilder();
            reader = new BufferedReader(new InputStreamReader(inputStream));

            String line;
            while ((line = reader.readLine()) != null) {
                builder.append(line + "\n");
            }

            dataFromWebService = builder.toString();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (connection != null) connection.disconnect();

            if (reader != null) try {
                reader.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        return dataFromWebService;
    }

    public static boolean docketFromJSON(Context c, String dataToParse) {
        Docket docket;
        try {
            JSONObject jsonObject = new JSONObject(dataToParse);
            JSONObject jsonDocket = jsonObject.getJSONObject("docket");

            int docketNumber = jsonDocket.getInt("docket_number");
            String accountNumber = jsonDocket.getString("account");
            String barcode = jsonDocket.getString("barcode");
            double totalStake = jsonDocket.getDouble("total_stake");
            double totalPayout = jsonDocket.getDouble("total_payout");
            boolean winner = jsonDocket.getInt("winner") == 1;

            docket = new Docket(docketNumber, accountNumber, barcode, totalStake, totalPayout, winner);

            JSONArray jsonArrayBets = jsonDocket.getJSONArray("bets");
            int betLength = jsonArrayBets.length();
            for (int currentBet = 0; currentBet < betLength; currentBet++) {
                JSONObject jsonBets = jsonArrayBets.getJSONObject(currentBet);
                int betNumber = jsonBets.getInt("bet_number");
                int betDocketNumber = jsonBets.getInt("docket_number");
                double betStake = jsonBets.getDouble("bet_stake");
                double betPayout = jsonBets.getDouble("bet_payout");
                boolean betWinner = jsonBets.getInt("winner") == 1;

                docket.getBets().add(new DocketBet(betNumber,
                        betDocketNumber,
                        betStake,
                        betPayout,
                        betWinner));

                JSONArray jsonArraySelections = jsonBets.getJSONArray("selections");
                int selectionLength = jsonArraySelections.length();
                for (int currentSelection = 0; currentSelection < selectionLength; currentSelection++) {
                    JSONObject jsonSelections = jsonArraySelections.getJSONObject(currentSelection);
                    int selectionBetNumber = jsonSelections.getInt("bet_number");
                    String name = jsonSelections.getString("name");
                    String odds = jsonSelections.getString("odds");
                    int position = jsonSelections.getInt("position");
                    int rule4 = jsonSelections.getInt("rule_4");
                    Sport sport = Sport.getSport(jsonSelections.getString("sport")) != null ?
                            Sport.getSport(jsonSelections.getString("sport")) : Sport.UNKNOWN;
                    int deadHeat = jsonSelections.getInt("dead_heat");

                    docket.getBets().get(currentBet).getSelections()
                            .add(new BetSelections(selectionBetNumber, name, odds, position, sport, rule4, deadHeat));

                    Log.v("ParseUtils", docket.getBets().get(currentBet)
                            .getSelections().get(currentSelection).toString());
                }

                JSONArray jsonArrayWagers = jsonBets.getJSONArray("wagers");
                int wagerLength = jsonArrayWagers.length();
                for (int currentWager = 0; currentWager < wagerLength; currentWager++) {
                    JSONObject jsonWagers = jsonArrayWagers.getJSONObject(currentWager);
                    int wagerNumber = jsonWagers.getInt("wager_number");
                    int wagerBetNumber = jsonWagers.getInt("bet_number");
                    double unitStake = jsonWagers.getDouble("unit_stake");
                    boolean eachWay = jsonWagers.getInt("each_way") == 1;
                    String wagerType = jsonWagers.getString("wager_type");
                    boolean wagerWinner = jsonWagers.getInt("winner") == 1;
                    double payout = jsonWagers.getDouble("payout");

                    docket.getBets().get(currentBet).getWagers()
                            .add(new BetWagers(wagerNumber,
                                    wagerBetNumber,
                                    unitStake,
                                    eachWay,
                                    wagerType,
                                    wagerWinner,
                                    payout));

                    Log.v("ParseUtils",
                            docket.getBets().get(currentBet)
                                    .getWagers().get(currentWager).toString());
                }

                Log.v("ParseUtils", docket.getBets().get(currentBet).toString());
            }
        } catch (JSONException e) {
            e.printStackTrace();
            return false;
        }

        GlobalDocket.get(c).setDocket(docket);
        return true;
    }

    public static boolean meetingsAndMarketsFromXML(Context c, String dataToParse) throws XmlPullParserException, IOException, ParseException {
        ArrayList<Meeting> meetings = new ArrayList<>();

        XmlPullParserFactory factory = XmlPullParserFactory.newInstance();
        XmlPullParser parser = factory.newPullParser();
        parser.setInput(new StringReader(dataToParse));

        final String XML_MEETING = "type";
        final String XML_MARKET = "market";

        Meeting meeting = null;
        ArrayList<Market> markets = new ArrayList<>();
        int eventType = parser.next();

        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");

        while (eventType != XmlPullParser.END_DOCUMENT) {
            if(eventType == XmlPullParser.START_TAG) {
                if(parser.getName().equals(XML_MEETING)) {
                    String id = parser.getAttributeValue(null, "id");
                    String name = parser.getAttributeValue(null, "name");
                    String lastUpdate = parser.getAttributeValue(null, "lastUpdateTime");

                    meeting = new Meeting(name, id, lastUpdate);
                } else if(parser.getName().equals(XML_MARKET)) {
                    String id = parser.getAttributeValue(null, "id");
                    String name = parser.getAttributeValue(null, "name");
                    Date raceDate = dateFormat.parse(parser.getAttributeValue(null, "date"));
                    String offTime = parser.getAttributeValue(null, "time");
                    offTime = offTime.substring(0, offTime.lastIndexOf(":"));
                    String ewOdds = parser.getAttributeValue(null, "ewReduction");
                    String ewPlaces = parser.getAttributeValue(null, "ewPlaces");
                    boolean tricast = parser.getAttributeValue(null, "tricastAvailable").equals("Y");

                    Date todayDate = dateFormat.parse(dateFormat.format(new Date()));
                    if(todayDate.equals(raceDate) && name.endsWith(" - Win")) {
                        markets.add(new Market(name, offTime, id, ewOdds, ewPlaces, raceDate, tricast));
                    }
                }
            } else if(eventType == XmlPullParser.END_TAG) {
                if(parser.getName().equals(XML_MEETING)) {
                    if(markets.size() > 0) {
                        meeting.setMarkets(markets);
                        meetings.add(meeting);
                    }
                    markets = new ArrayList<>();
                }
            }

            eventType = parser.next();
        }

        MeetingsSingleton.get(c).setMeetings(meetings);

        return true;
    }

    public static boolean participantFromMarketFromXML(Context c, String dataToParse, Market market) throws XmlPullParserException, IOException {
        XmlPullParserFactory factory = XmlPullParserFactory.newInstance();
        XmlPullParser parser = factory.newPullParser();
        parser.setInput(new StringReader(dataToParse));

        final String XML_MARKET = "market";
        final String XML_PARTICIPANT = "participant";
        Participant participant = null;
        boolean participantsMatchMeetingId = false;
        int eventType = parser.next();

        while (eventType != XmlPullParser.END_DOCUMENT) {

            if(eventType == XmlPullParser.START_TAG && parser.getName().equals(XML_MARKET)) {
                participantsMatchMeetingId = parser.getAttributeValue(null, "id").equals(market.getId());
            } else if (eventType == XmlPullParser.END_TAG && parser.getName().equals(XML_MARKET)) {
                Collections.sort(market.getParticipants(), new Comparator<Participant>() {
                    @Override
                    public int compare(Participant lhs, Participant rhs) {
                        if(lhs.getOddsDecimal().equals("SP") || rhs.getOddsDecimal().equals("SP")) {
                            return lhs.getName().compareToIgnoreCase(rhs.getName());
                        } else {
                            return Double.compare(Double.parseDouble(lhs.getOddsDecimal()), Double.parseDouble(rhs.getOddsDecimal()));
                        }
                    }
                });
            }

            if(participantsMatchMeetingId) {
                if(eventType == XmlPullParser.START_TAG && parser.getName().equals(XML_PARTICIPANT)) {
                    String name = parser.getAttributeValue(null, "name");
                    String id = parser.getAttributeValue(null, "id");
                    String odds = parser.getAttributeValue(null, "odds");

                    String decimalOdds;
                    if(parser.getAttributeValue(null, "oddsDecimal").equals("SP")) {
                        if(name.equals("Favourite")) decimalOdds = "0.25";
                        else if(name.equals("2nd Favourite")) decimalOdds = "0.5";
                        else decimalOdds = "1";
                    } else {
                        String[] absOdds = odds.split("/");
                        decimalOdds = ((Double.parseDouble(absOdds[0]) / Double.parseDouble(absOdds[1])) + 1)+"";
                    }

                    participant = new Participant(id, name, odds, decimalOdds);
                    participant.setMarketId(market.getId());
                    participant.setMarketName(market.getName().substring(0, market.getName().lastIndexOf(" - ")));
                } else if (eventType == XmlPullParser.END_TAG && parser.getName().equals(XML_PARTICIPANT)) {
                    market.getParticipants().add(participant);
                }
            }

            eventType = parser.next();
        }

        return true;
    }

}
