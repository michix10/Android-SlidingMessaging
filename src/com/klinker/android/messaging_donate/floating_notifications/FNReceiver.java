package com.klinker.android.messaging_donate.floating_notifications;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.provider.Telephony;
import android.telephony.SmsMessage;

import java.util.Arrays;
import java.util.HashSet;

import com.klinker.android.messaging_donate.R;
import com.klinker.android.messaging_sliding.MainActivity;
import robj.floating.notifications.Extension;

public class FNReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(final Context context, Intent intent) {
        Bundle extras = intent.getExtras();

        String body = "";
        String address = "";

        if (extras != null)
        {
            Object[] smsExtra = (Object[]) extras.get("pdus");

            for ( int i = 0; i < smsExtra.length; ++i )
            {
                SmsMessage sms = SmsMessage.createFromPdu((byte[])smsExtra[i]);

                body += sms.getMessageBody().toString();
                address = sms.getOriginatingAddress();
            }
        }

        long id = getOrCreateThreadId(context, address);

        Bitmap image = MainActivity.getFacebookPhoto(address, context);
        image = Bitmap.createScaledBitmap(image, 200, 200, false);

        Bitmap actionOne = BitmapFactory.decodeResource(context.getResources(), R.drawable.ic_menu_msg_compose_holo_dark);
        Bitmap actionTwo = BitmapFactory.decodeResource(context.getResources(), R.drawable.ic_menu_call);
        Bitmap actionThree = BitmapFactory.decodeResource(context.getResources(), R.drawable.ic_menu_done_holo_dark);

        Extension.addOrUpdate(image, body, id, actionOne, actionTwo, actionThree, false, true, false, context);

        // TODO stop notification from coming through if enabled
    }

    private static long getOrCreateThreadId(Context context, String number)
    {
        Set<String> recipients = new HashSet<String>();
        recipients.add(recipient);
        return Telephony.Threads.getOrCreateThreadId(context, recipients);
    }
}