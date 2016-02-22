package com.tommyfrenchbookmakers.officialapp.ui.ContactUsActivity;

import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;

import com.android.tighearnan.frenchsscanner.R;
import com.google.android.gms.maps.model.LatLng;
import com.tommyfrenchbookmakers.officialapp.utils.ParseUtils;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

public class ContactUsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        ArrayList<ShopInfo> shopInfos = new ArrayList<>();
//        shopInfos.add(new ShopInfo("Armagh", new LatLng(54.345488, -6.656665), "02845454545"));
//        shopInfos.add(new ShopInfo("William Street", new LatLng(54.466068, -6.337399), "02838324545"));
//        shopInfos.add(new ShopInfo("North Street", new LatLng(54.464843, -6.334288), "02854545454"));
        ShopInfoSingleton.get(this).setShopInfos(shopInfos);

        OfficeDownloader officeDownloader = new OfficeDownloader();
        officeDownloader.execute();
    }

    private void setup() {
        setContentView(R.layout.activity_contact_us);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    public class OfficeDownloader extends AsyncTask<Void, Void, String> {

        private ProgressDialog mProgressDialog;
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            mProgressDialog = new ProgressDialog(ContactUsActivity.this);
            mProgressDialog.setMessage("Fetching shops...");
            mProgressDialog.setIndeterminate(true);
            mProgressDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
            mProgressDialog.setCancelable(true);
            mProgressDialog.setProgressNumberFormat(null);
            mProgressDialog.setProgressPercentFormat(null);

            mProgressDialog.show();
        }

        @Override
        protected String doInBackground(Void... params) {

            return download(getString(R.string.download_url_offices));
        }

        private String download(String link) {
            String dl = "";
            HttpURLConnection connection;

            try {
                URL url = new URL(link);
                connection = (HttpURLConnection) url.openConnection();

                ByteArrayOutputStream out = new ByteArrayOutputStream();
                InputStream inputStream = connection.getInputStream();

                if(connection.getResponseCode() != HttpURLConnection.HTTP_OK) return null;

                int bytesRead = 0;
                byte[] buffer = new byte[1024];
                while((bytesRead = inputStream.read(buffer)) > 0) {
                    out.write(buffer, 0, bytesRead);
                }
                out.close();
                dl = new String(out.toByteArray());
            } catch (IOException e) {
                e.printStackTrace();
            }
            return dl;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);

            mProgressDialog.dismiss();

            if(ParseUtils.shopInfoFromJSON(s, ShopInfoSingleton.get(ContactUsActivity.this).getShopInfos())) {
                setup();
            }

        }
    }
}
