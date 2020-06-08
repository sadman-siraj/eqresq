package com.theeeceguy.eqresq;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.provider.Telephony;
import android.telephony.SmsMessage;
import android.util.Log;
import android.widget.Toast;

public class SmsListener extends BroadcastReceiver {

    String msgBody;

    @Override
    public void onReceive(Context context, Intent intent) {
        // TODO: This method is called when the BroadcastReceiver is receiving
        // an Intent broadcast.

        if(intent.getAction().equals("android.provider.Telephony.SMS_RECEIVED")){
            Bundle bundle = intent.getExtras();           //---get the SMS message passed in---
            SmsMessage[] msgs = null;
            String msg_from;
            if (bundle != null){
                //---retrieve the SMS message received---
                try{
                    Object[] pdus = (Object[]) bundle.get("pdus");
                    msgs = new SmsMessage[pdus.length];
                    for(int i=0; i<msgs.length; i++){
                        msgs[i] = SmsMessage.createFromPdu((byte[])pdus[i]);
                        msg_from = msgs[i].getOriginatingAddress();
                        msgBody = msgs[i].getMessageBody();
                    }
                    if(msgBody.contains("#earthquake")) {
                        Toast.makeText(context, "Earthquake Alert!", Toast.LENGTH_SHORT).show();
                        Intent newIntent = new Intent(context, NewsActivity.class);
                        newIntent.putExtra("smsbody", msgBody);
                        newIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        context.startActivity(newIntent);
                    }
                }catch(Exception e){
                            Log.d("Exception caught",e.getMessage());
                }
            }
        }

        //throw new UnsupportedOperationException("Not yet implemented");
    }
}
