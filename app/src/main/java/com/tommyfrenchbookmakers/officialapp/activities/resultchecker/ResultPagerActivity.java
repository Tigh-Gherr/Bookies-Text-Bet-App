package com.tommyfrenchbookmakers.officialapp.activities.resultchecker;

import android.content.DialogInterface;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.NavUtils;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import com.android.tighearnan.frenchsscanner.R;
import com.tommyfrenchbookmakers.officialapp.Global;
import com.tommyfrenchbookmakers.officialapp.interfaces.DataDownloadListener;
import com.tommyfrenchbookmakers.officialapp.singletons.GlobalDocket;
import com.tommyfrenchbookmakers.officialapp.customutils.DownloadUtils;
import com.tommyfrenchbookmakers.officialapp.customutils.SMSUtils;
import com.tommyfrenchbookmakers.officialapp.docketobjects.Docket;
import com.tommyfrenchbookmakers.officialapp.enumerators.DownloadType;
import com.tommyfrenchbookmakers.officialapp.fragments.resultchecker.ResultActivityFragment;
import com.tommyfrenchbookmakers.officialapp.transformers.ZoomOutPageTransformer;

import java.text.DecimalFormat;

/**
 * Created by Tíghearnán on 15/07/2015.
 */
public class ResultPagerActivity extends AppCompatActivity implements DataDownloadListener {

    // UI widgets
    private Toolbar mToolbar;
    private ViewPager mViewPager;

    // Custom Objects
    private Docket mDocket;

    // Sets up activity
    public void setupActivity() {
        mDocket = GlobalDocket.get(this).getDocket();
        mViewPager.setOffscreenPageLimit(2);

        // If there is no docket, end the activity
        if(mDocket == null) finish();

        // Set subtitle text, reads:
        // "Status: (If the docket is a winner, display payout, otherwise display beat)
        DecimalFormat df = new DecimalFormat("0.00");
        mToolbar.setSubtitle("Status: " +
                (mDocket.isWinner() ? "£" + df.format(mDocket.getTotalPayout()) : "Beat"));

        // If there is more than one bet on the docket, format the title accordingly.
        if (mDocket.getBets().size() > 1) {
            mToolbar.setTitle(getString(R.string.toolbar_title_multiple_bets, 1, mDocket.getBets().size()));
        }

        // Set up viewpager to house Bets.
        FragmentManager fm = getSupportFragmentManager();
        mViewPager.setAdapter(new FragmentPagerAdapter(fm) {

            @Override
            public Fragment getItem(int position) {
                return ResultActivityFragment.newInstance(position);
            }

            @Override
            public int getCount() {
                return mDocket.getBets().size();
            }
        });

        mViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            // Updates the titlebar text when page is changed.
            @Override
            public void onPageSelected(int position) {
                mToolbar.setTitle(getString(R.string.toolbar_title_multiple_bets, (position + 1), mDocket.getBets().size()));
            }

            @Override
            public void onPageScrollStateChanged(int state) {
                // Sets the background to black when the page is being scrolled, restores it to transparent when the scrolling is finished.
                // This is done for both visual and performance reasons.
                if (!(state == ViewPager.SCROLL_STATE_IDLE)) {
                    mViewPager.setBackgroundColor(ContextCompat.getColor(ResultPagerActivity.this, R.color.background_material_dark));
                } else {
                    mViewPager.setBackgroundColor(Color.TRANSPARENT);
                }

            }
        });

        // Sets the page transition animation.
        mViewPager.setPageTransformer(true, new ZoomOutPageTransformer());
    }

    @Override
    protected void onResume() {
        super.onResume();
        Docket docket = GlobalDocket.get(this).getDocket();

        if(mDocket == null) {
            if(docket == null) {
                finish();
            } else {
                mDocket = docket;
            }
        }
    }

    // Sets up activity
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_result_pager);
        mViewPager = (ViewPager) findViewById(R.id.view_pager_resultPager);

        mToolbar = (Toolbar) findViewById(R.id.app_bar);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        // Fetches the download type.
        DownloadType type = (DownloadType) getIntent().getSerializableExtra(DownloadType.intentKey());

        // If the download dype was a barcode.
        if (type == DownloadType.BARCODE) {
            String barcode = getIntent().getStringExtra("BARCODE");
            DownloadUtils.DocketFromBarcode docketFromBarcode =
                    new DownloadUtils.DocketFromBarcode(ResultPagerActivity.this, this);
            docketFromBarcode.execute(getString(R.string.download_url_barcode, barcode));

        // If the download type was an account and reference
        } else if (type == DownloadType.ACCOUNT_AND_REFERENCE) {
            String account = getIntent().getStringExtra("ACCOUNT");
            String reference = getIntent().getStringExtra("REFERENCE");

            DownloadUtils.DocketFromAccountAndReference docketFromAccountAndReference =
                    new DownloadUtils.DocketFromAccountAndReference(ResultPagerActivity.this, this);
            docketFromAccountAndReference.execute(getString(R.string.download_url_account, account, reference));
        }
    }

    @Override
    public void onDownloadStart() {

    }

    // When the download is finished
    @Override
    public void onDownloadComplete(Boolean success, String downloadedData) {
        // If the download was a success
        if (success) {
            // Delay execution for one fifth of a second.
            Handler handler = new Handler();
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    setupActivity();
                }
            }, 200);
        } else {
            // Display and error message and set its content to the downloaded error message.
            new AlertDialog.Builder(ResultPagerActivity.this)
                    .setTitle("Error fetching docket")
                    .setMessage(downloadedData)
                    .setPositiveButton("Return", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            NavUtils.navigateUpFromSameTask(ResultPagerActivity.this);
                        }
                    })
                    .setOnCancelListener(new DialogInterface.OnCancelListener() {
                        @Override
                        public void onCancel(DialogInterface dialog) {
                            NavUtils.navigateUpFromSameTask(ResultPagerActivity.this);
                        }
                    }).show();
        }
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_bet_pager, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.home:
                NavUtils.navigateUpFromSameTask(ResultPagerActivity.this);
                return true;
            case R.id.action_direct:
                // Create dialog functions in advance.
                DialogInterface.OnClickListener dialogInterface = new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        switch (which) {
                            case DialogInterface.BUTTON_POSITIVE:
                                String smsBody = getString(R.string.sms_body_edited_wrong, mDocket.getBarcode());
                                SMSUtils smsUtils = new SMSUtils(ResultPagerActivity.this, Global.PHONE_NUMBER, smsBody, findViewById(R.id.view_pager_resultPager));
                                smsUtils.sendSMS();
                                break;
                            case DialogInterface.BUTTON_NEGATIVE:
                                dialog.dismiss();
                                break;
                        }
                    }
                };

                // Create alert dialog to get users confirmation before sending SMS
                AlertDialog.Builder alertDialog = new AlertDialog.Builder(ResultPagerActivity.this)
                        .setTitle(R.string.alert_dialog_title_confirm_bet_recheck)
                        .setMessage(R.string.alert_dialog_body_editing_error)
                        .setPositiveButton(R.string.alert_dialog_button_confirm, dialogInterface)
                        .setNegativeButton(R.string.alert_dialog_button_cancel, dialogInterface);
                alertDialog.show();

                return true;

            case R.id.action_test_text_bet:
                DialogInterface.OnClickListener testInterface = new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        switch (which) {
                            case DialogInterface.BUTTON_POSITIVE:
                                String smsBody = getString(R.string.sms_body_test_text_bet);
                                SMSUtils smsUtils = new SMSUtils(ResultPagerActivity.this, "+447537416036", smsBody, findViewById(R.id.view_pager_resultPager));
                                smsUtils.sendSMS();
                                break;
                            case DialogInterface.BUTTON_NEGATIVE:
                                dialog.dismiss();
                                break;
                        }
                    }
                };
                new AlertDialog.Builder(this)
                        .setTitle(R.string.alert_dialog_title_confirm_bet_recheck)
                        .setMessage(R.string.alert_dialog_body_test)
                        .setPositiveButton(R.string.alert_dialog_button_confirm, testInterface)
                        .setNegativeButton(R.string.alert_dialog_button_cancel, testInterface)
                        .show();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
    }
}