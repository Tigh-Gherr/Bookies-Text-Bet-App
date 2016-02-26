package com.tommyfrenchbookmakers.officialapp.ui.ContactUsActivity;

import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.app.NavUtils;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;

import com.android.tighearnan.frenchsscanner.R;
import com.github.aakira.expandablelayout.ExpandableLayoutListener;
import com.github.aakira.expandablelayout.ExpandableRelativeLayout;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.MarkerOptions;
import com.tommyfrenchbookmakers.officialapp.utils.NetworkUtils;
import com.tommyfrenchbookmakers.officialapp.utils.ParseUtils;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * A placeholder fragment containing a simple view.
 */
public class ContactUsActivityFragment extends Fragment {

    private RecyclerView mOfficeRecyclerView;
    private RecyclerView.Adapter mOfficeAdapter;

    public ContactUsActivityFragment() {

    }

    private boolean needsDownloaded() {
        ArrayList<ShopInfo> shopInfos = ShopInfoSingleton.get(getActivity()).getShopInfos();
        return shopInfos == null || shopInfos.size() == 0;
    }

    private void downloadShopInformation() {
        if(needsDownloaded()) {
            if(NetworkUtils.networkIsAvailable(getActivity())) {
                new OfficeDownloader().execute();
            } else {
                Snackbar.make(getView(), R.string.error_message_no_internet, Snackbar.LENGTH_LONG)
                        .setAction(R.string.snackbar_action_return, new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                NavUtils.navigateUpFromSameTask(getActivity());
                            }
                        }).show();;
            }
        } else {
            setup();
        }
    }

    private void setup() {
        mOfficeRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        mOfficeAdapter = new ShopInfoAdapter(ShopInfoSingleton.get(getActivity()).getShopInfos());
        mOfficeRecyclerView.setAdapter(mOfficeAdapter);
        mOfficeRecyclerView.setHasFixedSize(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, final ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_contact_us, container, false);

        mOfficeRecyclerView = (RecyclerView) v.findViewById(R.id.recycler_view_shopInfo);


        return v;

    }

    @Override
    public void onResume() {
        super.onResume();
        downloadShopInformation();
    }

    public class OfficeDownloader extends AsyncTask<Void, Void, String> {

        private ProgressDialog mProgressDialog;
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            mProgressDialog = new ProgressDialog(getActivity());
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

            ShopInfoSingleton.get(getActivity()).setShopInfos(new ArrayList<ShopInfo>());
            if(ParseUtils.shopInfoFromJSON(s, ShopInfoSingleton.get(getActivity()).getShopInfos())) {
                setup();
            }

        }
    }
}
