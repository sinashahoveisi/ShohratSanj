package com.example.sina.specificcontact;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.telephony.SmsMessage;

public class SmsReceiver extends BroadcastReceiver {

    private static final String SMS_RECIVED = "android.provider.Telephony.SMS_RECEIVED";

    private static SmsListener mListener;

    String msg = "";

    @Override
    public void onReceive(Context context, Intent intent) {

        try {
            if (intent.getAction().equals(SMS_RECIVED))
            {
                Bundle dataBundle = intent.getExtras();
                if (dataBundle!=null)
                {
                    Object[] mypdu = (Object[])dataBundle.get("pdus");
                    assert mypdu != null;
                    final SmsMessage[] messages = new SmsMessage[mypdu.length];

                    for (int i = 0; i<mypdu.length;i++)
                    {
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M)
                        {
                            String format = dataBundle.getString("format");

                            messages[i] = SmsMessage.createFromPdu((byte[])mypdu[i],format);
                        }
                        else
                        {
                            messages[i] = SmsMessage.createFromPdu((byte[])mypdu[i]);
                        }
                        msg = messages[i].getMessageBody();
                    }
                    mListener.onMessageReceived(msg);
                }
            }
        }catch (Exception e)
        {
            e.printStackTrace();
        }
    }
    public static void bindListener(SmsListener listener) {
        mListener = listener;
    }
}
