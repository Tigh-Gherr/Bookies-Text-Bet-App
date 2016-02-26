package com.tommyfrenchbookmakers.officialapp.utils;

import android.content.Context;

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
import com.tommyfrenchbookmakers.officialapp.ui.ContactUsActivity.ShopInfo;

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
public final class ParseUtils {

    // Used to download JSON data from webservice and parse it to a String.
    public static String dataFromWebService(Context c, String dataToParse) {
        String dataFromWebService = "";
        HttpURLConnection connection = null;
        BufferedReader reader = null;

        // Download.
        try {
            // Create URL and open connection.
            URL url = new URL(dataToParse);
            connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");
            connection.connect();

            // Create input and output streams.
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

    public static boolean shopInfoFromJSON(String dataToParse, ArrayList<ShopInfo> shopInfos) {
        if(shopInfos == null) {
            shopInfos = new ArrayList<>();
        }

        try {
            JSONArray jsonArray = new JSONArray(dataToParse);
            int numOfOffices = jsonArray.length();

            for(int i = 0; i < numOfOffices; i++) {
                JSONObject jsonShop = jsonArray.getJSONObject(i);
                String name = jsonShop.getString("name");
                String telephone = jsonShop.getString("telephone");
                double latitude = jsonShop.getDouble("latitude");
                double longitude = jsonShop.getDouble("longitude");

                shopInfos.add(new ShopInfo(name, latitude, longitude, telephone));
            }

        } catch (JSONException e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }

    // Used to create a Docket object from JSON data provided from a String.
    public static boolean docketFromJSON(Context c, String dataToParse) {
        Docket docket;
        try {
            JSONObject jsonObject = new JSONObject(dataToParse);
            JSONObject jsonDocket = jsonObject.getJSONObject("docket");

            // Get information from downloaded JSON object and store it in relevant variables.
            int docketNumber = jsonDocket.getInt("docket_number");
            String accountNumber = jsonDocket.getString("account");
            String barcode = jsonDocket.getString("barcode");
            double totalStake = jsonDocket.getDouble("total_stake");
            double totalPayout = jsonDocket.getDouble("total_payout");
            boolean winner = jsonDocket.getInt("winner") == 1;

            // Instantiate docket object with previously extracted data.
            docket = new Docket(docketNumber, accountNumber, barcode, totalStake, totalPayout, winner);

            // Move onto the individual bets in the docket.
            JSONArray jsonArrayBets = jsonDocket.getJSONArray("bets");
            int betLength = jsonArrayBets.length();
            // For each bet in the docket.
            for (int currentBet = 0; currentBet < betLength; currentBet++) {
                JSONObject jsonBets = jsonArrayBets.getJSONObject(currentBet);

                // Get information from downloaded JSON object and store it in relevant variables.
                int betNumber = jsonBets.getInt("bet_number");
                int betDocketNumber = jsonBets.getInt("docket_number");
                double betStake = jsonBets.getDouble("bet_stake");
                double betPayout = jsonBets.getDouble("bet_payout");
                boolean betWinner = jsonBets.getInt("winner") == 1;

                // Instantiate bet object with previously extracted data and add it to the docket.
                docket.getBets().add(new DocketBet(betNumber,
                        betDocketNumber,
                        betStake,
                        betPayout,
                        betWinner));

                // Move onto individual selections in the bet.
                JSONArray jsonArraySelections = jsonBets.getJSONArray("selections");
                int selectionLength = jsonArraySelections.length();
                // For every selection in the bet.
                for (int currentSelection = 0; currentSelection < selectionLength; currentSelection++) {
                    JSONObject jsonSelections = jsonArraySelections.getJSONObject(currentSelection);

                    // Get information from downloaded JSON object and store it in relevant variables.
                    int selectionBetNumber = jsonSelections.getInt("bet_number");
                    String name = jsonSelections.getString("name");
                    String odds = jsonSelections.getString("odds");
                    int position = jsonSelections.getInt("position");
                    int rule4 = jsonSelections.getInt("rule_4");
                    Sport sport = Sport.getSport(jsonSelections.getString("sport")) != null ?
                            Sport.getSport(jsonSelections.getString("sport")) : Sport.UNKNOWN;
                    int deadHeat = jsonSelections.getInt("dead_heat");

                    // Instantiate Selection object with previously extracted data and add it to the bet.
                    docket.getBets().get(currentBet).getSelections()
                            .add(new BetSelections(selectionBetNumber, name, odds, position, sport, rule4, deadHeat));
                }

                // Move onto individual wagers in the bet.
                JSONArray jsonArrayWagers = jsonBets.getJSONArray("wagers");
                int wagerLength = jsonArrayWagers.length();
                // For each wager in the bet.
                for (int currentWager = 0; currentWager < wagerLength; currentWager++) {
                    JSONObject jsonWagers = jsonArrayWagers.getJSONObject(currentWager);

                    // Get information from downloaded JSON object and store it in relevant variables.
                    int wagerNumber = jsonWagers.getInt("wager_number");
                    int wagerBetNumber = jsonWagers.getInt("bet_number");
                    double unitStake = jsonWagers.getDouble("unit_stake");
                    boolean eachWay = jsonWagers.getInt("each_way") == 1;
                    String wagerType = jsonWagers.getString("wager_type");
                    boolean wagerWinner = jsonWagers.getInt("winner") == 1;
                    double payout = jsonWagers.getDouble("payout");

                    // Instantiate Wager object from previously extracted information and add it to the bet.
                    docket.getBets().get(currentBet).getWagers()
                            .add(new BetWagers(wagerNumber,
                                    wagerBetNumber,
                                    unitStake,
                                    eachWay,
                                    wagerType,
                                    wagerWinner,
                                    payout));
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
            return false;
        }

        // Save downloaded Docket to singleton.
        GlobalDocket.get(c).setDocket(docket);
        return true;
    }

    // Used to parse Meeting and Market objects from downloaded XML data.
    public static boolean meetingsAndMarketsFromXML(Context c, String dataToParse) throws XmlPullParserException, IOException, ParseException {
        ArrayList<Meeting> meetings = new ArrayList<>();

        // Setup the XML parser.
        XmlPullParserFactory factory = XmlPullParserFactory.newInstance();
        XmlPullParser parser = factory.newPullParser();
        parser.setInput(new StringReader(dataToParse));

        final String XML_MEETING = "type";
        final String XML_MARKET = "market";


        Meeting meeting = null;
        ArrayList<Market> markets = new ArrayList<>();
        // Used to keep track of what part of the XML data the parser is at.
        int eventType = parser.next();

        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");

        // WHile not at the end of the XML document.
        while (eventType != XmlPullParser.END_DOCUMENT) {
            // If the parser is at a start tag.
            if (eventType == XmlPullParser.START_TAG) {
                // If the parser is at a Meeting.
                if (parser.getName().equals(XML_MEETING)) {
                    // Get information from downloaded XML document and store it in relevant variables.
                    String id = parser.getAttributeValue(null, "id");
                    String name = parser.getAttributeValue(null, "name");
                    String lastUpdate = parser.getAttributeValue(null, "lastUpdateTime");

                    // Create Meeting object from previously extracted data.
                    meeting = new Meeting(name, id, lastUpdate);
                // If the parser is at a market.
                } else if (parser.getName().equals(XML_MARKET)) {
                    // Get information from downloaded XML document and store it in relevant variables.
                    String id = parser.getAttributeValue(null, "id");
                    String name = parser.getAttributeValue(null, "name");
                    Date raceDate = dateFormat.parse(parser.getAttributeValue(null, "date"));
                    String offTime = parser.getAttributeValue(null, "time");
                    offTime = offTime.substring(0, offTime.lastIndexOf(":"));
                    String ewOdds = parser.getAttributeValue(null, "ewReduction");
                    String ewPlaces = parser.getAttributeValue(null, "ewPlaces");
                    boolean tricast = parser.getAttributeValue(null, "tricastAvailable").equals("Y");

                    Date todayDate = dateFormat.parse(dateFormat.format(new Date()));
                    // If the race is today.
                    if (todayDate.equals(raceDate) && name.endsWith(" - Win")) {
                        // Create Market  object from previously extracted data and add it to the List<> of Markets.
                        markets.add(new Market(name, offTime, id, ewOdds, ewPlaces, raceDate, tricast));
                    }
                }
            // If the parser is at an end tag.
            } else if (eventType == XmlPullParser.END_TAG) {
                // If the parser is leaving a Meeting.
                if (parser.getName().equals(XML_MEETING)) {
                    // If there are any Markets in the current Meeting that are on today.
                    if (markets.size() > 0) {
                        // Save the Markets to the Meeting object and add the Meeting to the List<>.
                        meeting.setMarkets(markets);
                        meetings.add(meeting);
                    }
                    markets = new ArrayList<>();
                }
            }

            // Move the parser onto the next section.
            eventType = parser.next();
        }

        // Save the downloaded Meetings to a singleton.
        MeetingsSingleton.get(c).setMeetings(meetings);

        return true;
    }

    // Used to create Participant objects for a provided Market, using a downloaded XML document.
    public static boolean participantFromMarketFromXML(Context c, String dataToParse, Market market) throws XmlPullParserException, IOException {
        // Create instance of XML parser.
        XmlPullParserFactory factory = XmlPullParserFactory.newInstance();
        XmlPullParser parser = factory.newPullParser();
        parser.setInput(new StringReader(dataToParse));

        final String XML_MARKET = "market";
        final String XML_PARTICIPANT = "participant";
        Participant participant = null;
        boolean participantsMatchMeetingId = false;

        // Keeps track of what part of the XML document the parser is at.
        int eventType = parser.next();

        // While not at the end of the XML document.
        while (eventType != XmlPullParser.END_DOCUMENT) {

            // If the parser is at the start of a Market.
            if (eventType == XmlPullParser.START_TAG && parser.getName().equals(XML_MARKET)) {
                // If the current Markets ID equals the passed Markets ID.
                participantsMatchMeetingId = parser.getAttributeValue(null, "id").equals(market.getId());
            // If the parser is leaving a Market.
            } else if (eventType == XmlPullParser.END_TAG && parser.getName().equals(XML_MARKET)) {
                // Sort all the downloaded participants by odds.
                Collections.sort(market.getParticipants(), new Comparator<Participant>() {
                    @Override
                    public int compare(Participant lhs, Participant rhs) {
                        if (lhs.getOddsDecimal().equals("SP") || rhs.getOddsDecimal().equals("SP")) {
                            return lhs.getName().compareToIgnoreCase(rhs.getName());
                        } else {
                            return Double.compare(Double.parseDouble(lhs.getOddsDecimal()), Double.parseDouble(rhs.getOddsDecimal()));
                        }
                    }
                });
                // TODO: Test break statement here.
            }

            // If the current Market ID matches the passed Markets ID.
            if (participantsMatchMeetingId) {
                // If the pare is at the start of a Participant.
                if (eventType == XmlPullParser.START_TAG && parser.getName().equals(XML_PARTICIPANT)) {
                    // Get information from downloaded XML document and store it in relevant variables.
                    String name = parser.getAttributeValue(null, "name");
                    String id = parser.getAttributeValue(null, "id");
                    String odds = parser.getAttributeValue(null, "odds");

                    String decimalOdds;
                    // If a selections odds are SP.
                    if (parser.getAttributeValue(null, "oddsDecimal").equals("SP")) {
                        if (name.equals("Favourite")) decimalOdds = "0.25";
                        else if (name.equals("2nd Favourite")) decimalOdds = "0.5";
                        else decimalOdds = "1";
                    } else {
                        String[] absOdds = odds.split("/");
                        decimalOdds = ((Double.parseDouble(absOdds[0]) / Double.parseDouble(absOdds[1])) + 1) + "";
                    }

                    // Create Participant object from previously extracted information.
                    participant = new Participant(id, name, odds, decimalOdds);

                    // Give the Participant some information about the meeting it is in.
                    participant.setMarketId(market.getId());
                    participant.setMarketName(market.getName().substring(0, market.getName().lastIndexOf(" - ")));
                // If the parser is leaving a Participant.
                } else if (eventType == XmlPullParser.END_TAG && parser.getName().equals(XML_PARTICIPANT)) {
                    // Attach the Participant object to the passed Market object.
                    market.getParticipants().add(participant);
                }
            }

            // Move the parser to the next part of the XML document.
            eventType = parser.next();
        }

        return true;
    }

}
