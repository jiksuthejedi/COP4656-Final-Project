package org.cop4656.assignment2;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.provider.Telephony;
import android.telephony.SmsMessage;
import android.util.Log;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class SMSBroadcastReceiver extends BroadcastReceiver
{
    DatabaseReference databaseRef = FirebaseDatabase.getInstance().getReference();
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
                    //modify as needed

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