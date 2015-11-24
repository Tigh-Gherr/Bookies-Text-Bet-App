package com.tommyfrenchbookmakers.officialapp.customutils;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.AsyncTask;
import android.support.v4.app.NavUtils;
import android.util.Log;

import com.android.tighearnan.frenchsscanner.R;
import com.tommyfrenchbookmakers.officialapp.interfaces.DataDownloadListener;
import com.tommyfrenchbookmakers.officialapp.meetingobjects.Market;
import com.tommyfrenchbookmakers.officialapp.meetingobjects.Meeting;
import com.tommyfrenchbookmakers.officialapp.meetingobjects.Participant;
import com.tommyfrenchbookmakers.officialapp.singletons.MeetingsSingleton;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
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
 * Created by Tíghearnán on 02/09/2015.
 */
public class DownloadUtils {

    public static class DocketFromBarcode extends AsyncTask<String, Void, Boolean> {

        private Context mContext;
        private String mDownloadedData;
        private ProgressDialog mDownloadingDocketDialog;
        private DataDownloadListener mStatusListener;

        public DocketFromBarcode(Context c, DataDownloadListener listener) {
            mContext = c;
            mStatusListener = listener;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
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

        @Override
        protected Boolean doInBackground(String... params) {
            if (params == null) return null;

            mDownloadedData = ParseUtils.dataFromWebService(mContext, params[0]);
            return ParseUtils.docketFromJSON(mContext, mDownloadedData);
        }

        @Override
        protected void onPostExecute(Boolean success) {
            super.onPostExecute(success);

            mDownloadingDocketDialog.dismiss();
            mStatusListener.onDownloadComplete(success, mDownloadedData);
        }
    }

    public static class DocketFromAccountAndReference extends AsyncTask<String, Void, Boolean> {

        private Context mContext;
        private DataDownloadListener mStatus;
        private String mDownloadedData;
        private ProgressDialog mDownloadingProgressDialog;

        public DocketFromAccountAndReference(Context c, DataDownloadListener listener) {
            mContext = c;
            mStatus = listener;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            mDownloadedData = "";

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

        @Override
        protected Boolean doInBackground(String... params) {
            if (params == null) return null;
            mDownloadedData = ParseUtils.dataFromWebService(mContext, params[0]);

            return ParseUtils.docketFromJSON(mContext, mDownloadedData);
        }

        @Override
        protected void onPostExecute(Boolean success) {
            super.onPostExecute(success);

            mDownloadingProgressDialog.dismiss();
            mStatus.onDownloadComplete(success, mDownloadedData);
        }
    }

    public static class WilliamHillBetting extends AsyncTask<String, Void, Boolean> {

        private Market mMarket;
        private Context mContext;
        private DataDownloadListener mDownloadListener;

        public WilliamHillBetting(Context context, DataDownloadListener listener) {
            mContext = context;
            mDownloadListener = listener;
            mMarket = null;
        }

        public WilliamHillBetting(Context context, DataDownloadListener listener, Market market) {
            mContext = context;
            mDownloadListener = listener;
            mMarket = market;
        }


        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            mDownloadListener.onDownloadStart();
        }

        @Override
        protected Boolean doInBackground(String... params) {
            boolean success = true;
            HttpURLConnection connection;

            try {
                URL url = new URL(params[0]);
                connection = (HttpURLConnection) url.openConnection();

                ByteArrayOutputStream out = new ByteArrayOutputStream();
                InputStream inputStream = connection.getInputStream();

                if (connection.getResponseCode() != HttpURLConnection.HTTP_OK) return null;

                int bytesRead = 0;
                byte[] buffer = new byte[1024];
                while ((bytesRead = inputStream.read(buffer)) > 0) {
                    out.write(buffer, 0, bytesRead);
                }
                out.close();

                String xmlData = new String(out.toByteArray());

                if (mMarket == null) {
                    ParseUtils.meetingsAndMarketsFromXML(mContext, xmlData);
                } else {
                    ParseUtils.participantFromMarketFromXML(mContext, xmlData, mMarket);
                }

            } catch (IOException e) {
                e.printStackTrace();
                success = false;
            } catch (XmlPullParserException | ParseException e) {
                e.printStackTrace();
            }

            return success;
        }

        @Override
        protected void onPostExecute(Boolean aBoolean) {
            super.onPostExecute(aBoolean);
            mDownloadListener.onDownloadComplete(aBoolean, "");
        }

    }
}
