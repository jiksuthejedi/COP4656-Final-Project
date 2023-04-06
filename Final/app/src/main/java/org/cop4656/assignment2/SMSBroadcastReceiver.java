package org.cop4656.assignment2;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.provider.Telephony;
import android.telephony.SmsMessage;
import android.util.Log;
import android.widget.Toast;

public class SMSBroadcastReceiver extends BroadcastReceiver
{
    @Override
    public void onReceive(Context context, Intent intent)
    {
        Bundle bundle = intent.getExtras();

        if (intent.getAction().equals(Telephony.Sms.Intents.SMS_RECEIVED_ACTION))
        {
            if (bundle != null)
            {
                Object[] pdus = (Object[]) bundle.get("pdus");
                String format = bundle.getString("format");

                for (int i = 0; i < pdus.length; i++)
                {
                    SmsMessage currentMessage = SmsMessage.createFromPdu((byte[]) pdus[i], format);
                    String sender = currentMessage.getDisplayOriginatingAddress();
                    String message = currentMessage.getDisplayMessageBody();
                    String printMessage = "SMS from " + sender + ": " + message;

                    Intent activityIntent = new Intent(context, MainActivity.class);
                    activityIntent.putExtra("sms", message);
                    activityIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    context.startActivity(activityIntent);
                }
            }
        }
    }
}