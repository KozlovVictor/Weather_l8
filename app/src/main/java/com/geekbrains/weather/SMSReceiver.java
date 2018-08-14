package com.geekbrains.weather;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.telephony.SmsMessage;
import android.widget.Toast;

public class SMSReceiver extends BroadcastReceiver {

    SMSCallback smsCallback;
    Context context;

    public SMSReceiver(SMSCallback smsCallback) {
        this.smsCallback = smsCallback;
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        this.context = context;

        if (intent != null && intent.getAction() != null) {
            Object[] pdus = (Object[]) intent.getExtras().get("pdus");
            SmsMessage[] messages = new SmsMessage[pdus.length];
            for (int i = 0; i < pdus.length; i++) {
                messages[i] = SmsMessage.createFromPdu((byte[]) pdus[i]);
            }
            String bodyText = messages[0].getMessageBody();
//            Toast.makeText(context, bodyText, Toast.LENGTH_LONG).show();
            smsCallback.SMSReceived(bodyText);
            abortBroadcast();
        }
    }

    public interface SMSCallback {
        void SMSReceived(String SMSText);
    }
}
