package com.tommyfrenchbookmakers.officialapp.utils;

import android.app.Activity;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.support.design.widget.Snackbar;
import android.telephony.SmsManager;
import android.view.View;

import com.android.tighearnan.frenchsscanner.R;

import java.util.ArrayList;

/**
 * Created by Tíghearnán on 28/08/2015.
 */
public class SMSUtils {

    private Context mContext;
    private String mPhoneNumber;
    private String mSMSBody;
    private View mParentLayout;

    public SMSUtils(Context context, String phoneNumber, String SMSBody, View parentLayout) {
        mContext = context;
        mPhoneNumber = phoneNumber;
        mSMSBody = SMSBody;
        mParentLayout = parentLayout;
    }

    public void sendSMS() {
        String SENT = "SMS_SENT";
        String DELIVERED = "SMS_DELIVERED";

        mContext.registerReceiver(new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                switch (getResultCode()) {
                    case Activity.RESULT_OK:
                        Snackbar.make(mParentLayout, R.string.snackbar_body_message_sent, Snackbar.LENGTH_LONG).show();
                        break;
                    case SmsManager.RESULT_ERROR_GENERIC_FAILURE:
                        Snackbar.make(mParentLayout, R.string.snackbar_body_sending_failed, Snackbar.LENGTH_LONG).show();
                        break;
                    case SmsManager.RESULT_ERROR_NO_SERVICE:
                        Snackbar.make(mParentLayout, R.string.snackbar_body_no_signal, Snackbar.LENGTH_LONG).show();
                        break;
                    case SmsManager.RESULT_ERROR_NULL_PDU:
                        Snackbar.make(mParentLayout, R.string.snackbar_body_no_pdu, Snackbar.LENGTH_LONG).show();
                        break;
                    case SmsManager.RESULT_ERROR_RADIO_OFF:
                        Snackbar.make(mParentLayout, R.string.snackbar_body_mobile_radio_off, Snackbar.LENGTH_SHORT).show();
                        break;
                }
            }
        }, new IntentFilter(SENT));

        mContext.registerReceiver(new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                switch (getResultCode()) {
                    case Activity.RESULT_OK:
                        Snackbar.make(mParentLayout, R.string.snackbar_body_message_delivered, Snackbar.LENGTH_LONG).show();
                        break;
                    case Activity.RESULT_CANCELED:
                        Snackbar.make(mParentLayout, R.string.snackbar_body_message_not_delivered, Snackbar.LENGTH_LONG).show();
                        break;
                }
            }
        }, new IntentFilter(DELIVERED));

        SmsManager sms = SmsManager.getDefault();

        if(mSMSBody.length() > 145) {
            ArrayList<String> parts = sms.divideMessage(mSMSBody);

            ArrayList<PendingIntent> sentPIs = new ArrayList<>();
            ArrayList<PendingIntent> deliveredPIs = new ArrayList<>();

            for(int i = 0; i < parts.size(); i++) {
                sentPIs.add(PendingIntent.getBroadcast(mContext, 0, new Intent(SENT), 0));
                deliveredPIs.add(PendingIntent.getBroadcast(mContext, 0, new Intent(DELIVERED), 0));
            }

            sms.sendMultipartTextMessage(mPhoneNumber, null, parts, sentPIs, deliveredPIs);
        } else {
            PendingIntent sentPI = PendingIntent.getBroadcast(mContext, 0,
                    new Intent(SENT), 0);

            PendingIntent deliveredPI = PendingIntent.getBroadcast(mContext, 0,
                    new Intent(DELIVERED), 0);
            sms.sendTextMessage(mPhoneNumber, null, mSMSBody, sentPI, deliveredPI);
        }
    }

    public static void sendSMS(Context context, String phoneNumber, String smsBody, final View parentView) {
        String SENT = "SMS_SENT";
        String DELIVERED = "SMS_DELIVERED";

        context.registerReceiver(new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                switch (getResultCode()) {
                    case Activity.RESULT_OK:
                        Snackbar.make(parentView, R.string.snackbar_body_message_sent, Snackbar.LENGTH_LONG).show();
                        break;
                    case SmsManager.RESULT_ERROR_GENERIC_FAILURE:
                        Snackbar.make(parentView, R.string.snackbar_body_sending_failed, Snackbar.LENGTH_LONG).show();
                        break;
                    case SmsManager.RESULT_ERROR_NO_SERVICE:
                        Snackbar.make(parentView, R.string.snackbar_body_no_signal, Snackbar.LENGTH_LONG).show();
                        break;
                    case SmsManager.RESULT_ERROR_NULL_PDU:
                        Snackbar.make(parentView, R.string.snackbar_body_no_pdu, Snackbar.LENGTH_LONG).show();
                        break;
                    case SmsManager.RESULT_ERROR_RADIO_OFF:
                        Snackbar.make(parentView, R.string.snackbar_body_mobile_radio_off, Snackbar.LENGTH_SHORT).show();
                        break;
                }
            }
        }, new IntentFilter(SENT));

        context.registerReceiver(new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                switch (getResultCode()) {
                    case Activity.RESULT_OK:
                        Snackbar.make(parentView, R.string.snackbar_body_message_delivered, Snackbar.LENGTH_LONG).show();
                        break;
                    case Activity.RESULT_CANCELED:
                        Snackbar.make(parentView, R.string.snackbar_body_message_not_delivered, Snackbar.LENGTH_LONG).show();
                        break;
                }
            }
        }, new IntentFilter(DELIVERED));

        SmsManager sms = SmsManager.getDefault();

        if(smsBody.length() > 145) {
            ArrayList<String> parts = sms.divideMessage(smsBody);

            ArrayList<PendingIntent> sentPIs = new ArrayList<>();
            ArrayList<PendingIntent> deliveredPIs = new ArrayList<>();

            for(int i = 0; i < parts.size(); i++) {
                sentPIs.add(PendingIntent.getBroadcast(context, 0, new Intent(SENT), 0));
                deliveredPIs.add(PendingIntent.getBroadcast(context, 0, new Intent(DELIVERED), 0));
            }

            sms.sendMultipartTextMessage(phoneNumber, null, parts, sentPIs, deliveredPIs);
        } else {
            PendingIntent sentPI = PendingIntent.getBroadcast(context, 0,
                    new Intent(SENT), 0);

            PendingIntent deliveredPI = PendingIntent.getBroadcast(context, 0,
                    new Intent(DELIVERED), 0);
            sms.sendTextMessage(phoneNumber, null, smsBody, sentPI, deliveredPI);
        }
    }
}