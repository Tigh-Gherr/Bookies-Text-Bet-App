package com.tommyfrenchbookmakers.officialapp.customutils;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.AsyncTask;
import android.support.v4.app.NavUtils;

import com.tommyfrenchbookmakers.officialapp.interfaces.DataDownloadListener;
import com.tommyfrenchbookmakers.officialapp.meetingobjects.Market;

import org.xmlpull.v1.XmlPullParserException;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.ParseException;

/**
 * Created by Tíghearnán on 02/09/2015.
 */
public class DownloadUtils {

    // Instantiated for downloading a docket when with a barcode.
    public static class DocketFromBarcode extends AsyncTask<String, Void, Boolean> {

        // Member variables.
        private Context mContext;
        private String mDownloadedData;
        private ProgressDialog mDownloadingDocketDialog;
        private DataDownloadListener mStatusListener;

        // Constructor.
        public DocketFromBarcode(Context c, DataDownloadListener listener) {
            mContext = c;
            mStatusListener = listener;
        }

        // Before the download.
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            // TODO: Remove ProgressDialog, use ProgressBar and DataDownloadListener.
            // Create and display the ProgressDialog
            mDownloadedData = "";
            mDownloadingDocketDialog = new ProgressDialog(mContext);
            mDownloadingDocketDialog.setMessage("Downloading Docket...");
            mDownloadingDocketDialog.setIndeterminate(true);
            mDownloadingDocketDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
            mDownloadingDocketDialog.setCancelable(true);
            mDownloadingDocketDialog.setProgressNumberFormat(null);
            mDownloadingDocketDialog.setProgressPercentFormat(null);

            mDownloadingDocketDialog.setOnCancelListener(new DialogInterface.OnCancelListener() {
                @Override
                public void onCancel(DialogInterface dialog) {
                    cancel(true);
                    NavUtils.navigateUpFromSameTask((Activity) mContext);
                }
            });

            mDownloadingDocketDialog.show();
        }

        // Executed in separate thread.
        @Override
        protected Boolean doInBackground(String... params) {
            if (params == null) return null;

            // Download and parse the information to a string.
            mDownloadedData = ParseUtils.dataFromWebService(mContext, params[0]);
            // Parse the string into a Docket object and return boolean for success.
            return ParseUtils.docketFromJSON(mContext, mDownloadedData);
        }

        // After the download.
        @Override
        protected void onPostExecute(Boolean success) {
            super.onPostExecute(success);

            // TODO: Call to onDownloadComplete will instead dismiss the ProgressDialog and setup the host Fragment.
            // Dismiss the ProgressDialog. Send downloaded data and success information back to Fragment.
            mDownloadingDocketDialog.dismiss();
            mStatusListener.onDownloadComplete(success, mDownloadedData);
        }
    }

    // Instantiated for downloading a docket with an account number and reference number.
    public static class DocketFromAccountAndReference extends AsyncTask<String, Void, Boolean> {

        // Member variables.
        private Context mContext;
        private DataDownloadListener mStatus;
        private String mDownloadedData;
        private ProgressDialog mDownloadingProgressDialog;

        // Constructor.
        public DocketFromAccountAndReference(Context c, DataDownloadListener listener) {
            mContext = c;
            mStatus = listener;
        }

        // Before the download.
        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            mDownloadedData = "";

            // TODO: Remove ProgressDialog, use ProgressBar and DataDownloadListener.
            // Setup and display the ProgressDialog
            mDownloadingProgressDialog = new ProgressDialog(mContext);
            mDownloadingProgressDialog.setMessage("Downloading docket...");
            mDownloadingProgressDialog.setProgressPercentFormat(null);
            mDownloadingProgressDialog.setProgressNumberFormat(null);
            mDownloadingProgressDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
            mDownloadingProgressDialog.setIndeterminate(true);
            mDownloadingProgressDialog.setCancelable(true);
            mDownloadingProgressDialog.setOnCancelListener(new DialogInterface.OnCancelListener() {
                @Override
                public void onCancel(DialogInterface dialog) {
                    mDownloadedData = "Downloaded canceled.";
                    cancel(true);
                }
            });

            mDownloadingProgressDialog.show();
        }

        // Executed in a separate thread.
        @Override
        protected Boolean doInBackground(String... params) {
            if (params == null) return null;

            // Download and parse the information to a string.
            mDownloadedData = ParseUtils.dataFromWebService(mContext, params[0]);
            // Parse the string into a Docket object and return boolean for success.
            return ParseUtils.docketFromJSON(mContext, mDownloadedData);
        }

        // After the download.
        @Override
        protected void onPostExecute(Boolean success) {
            super.onPostExecute(success);

            // TODO: Call to onDownloadComplete will instead dismiss the ProgressDialog and setup the host Fragment.
            // Dismiss the ProgressDialog. Send downloaded data and success information back to Fragment.
            mDownloadingProgressDialog.dismiss();
            mStatus.onDownloadComplete(success, mDownloadedData);
        }
    }

    // Instantiated when downloading Horse Racing information.
    public static class WilliamHillBetting extends AsyncTask<String, Void, Boolean> {

        // Member variables.
        private Market mMarket;
        private Context mContext;
        private DataDownloadListener mDownloadListener;

        // Constructor.
        public WilliamHillBetting(Context context, DataDownloadListener listener) {
            mContext = context;
            mDownloadListener = listener;
            mMarket = null;
        }

        // Constructor.
        public WilliamHillBetting(Context context, DataDownloadListener listener, Market market) {
            mContext = context;
            mDownloadListener = listener;
            mMarket = market;
        }


        // Before the download.
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            // Notify host fragment that download is starting.
            mDownloadListener.onDownloadStart();
        }

        // Executed in a separate thread.
        @Override
        protected Boolean doInBackground(String... params) {
            // Assume success, wait to be falsified.
            boolean success = true;
            HttpURLConnection connection;

            // Download.
            try {
                // Get URL and open connection with it.
                URL url = new URL(params[0]);
                connection = (HttpURLConnection) url.openConnection();

                // Prepare Streams for reading and writing.
                ByteArrayOutputStream out = new ByteArrayOutputStream();
                InputStream inputStream = connection.getInputStream();

                // If the response code anything other than ok, return an error.
                if (connection.getResponseCode() != HttpURLConnection.HTTP_OK) return null;

                // Read the downloaded data.
                int bytesRead = 0;
                byte[] buffer = new byte[1024];
                while ((bytesRead = inputStream.read(buffer)) > 0) {
                    out.write(buffer, 0, bytesRead);
                }
                out.close();

                // Parse the downloaded data to a String.
                String xmlData = new String(out.toByteArray());

                // If no Market was passed, we are downloading the Meetings and Markets.
                if (mMarket == null) {
                    ParseUtils.meetingsAndMarketsFromXML(mContext, xmlData);
                // If a Market was passed, we are downloading the participants.
                } else {
                    ParseUtils.participantFromMarketFromXML(mContext, xmlData, mMarket);
                }

            } catch (IOException | XmlPullParserException | ParseException e) {
                e.printStackTrace();
                // Return false if error was thrown.
                success = false;
            }

            return success;
        }

        // After the download.
        @Override
        protected void onPostExecute(Boolean aBoolean) {
            super.onPostExecute(aBoolean);
            // Notify the host fragment the download is completed, and send the whether or not it was successful.
            mDownloadListener.onDownloadComplete(aBoolean, "");
        }

    }
}
