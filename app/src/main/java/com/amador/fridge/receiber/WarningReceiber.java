package com.amador.fridge.receiber;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.NotificationCompat;


import com.amador.fridge.HomeActivity;
import com.amador.fridge.R;

public class WarningReceiber extends BroadcastReceiver {

    public static final String WARNING_ACTION = "com.amador.fridge.WARNING";
    public static final String RECOVERY_COUNT = "count";
    public static final String RECOVERY_HOME_ACTIVITY_ORDER = "order";


    public WarningReceiber() {
    }

    @Override
    public void onReceive(Context context, Intent intent) {

        int count = 0;
        count = intent.getIntExtra(RECOVERY_COUNT, -1);
        NotificationCompat.Builder noty = new NotificationCompat.Builder(context);
        noty.setAutoCancel(true);
        noty.setSmallIcon(android.R.drawable.ic_dialog_info);
        noty.setContentText(context.getString(R.string.text_noty)+ " "+String.valueOf(count)+" "+context.getString(R.string.caducity));
        Intent i = new Intent(context, HomeActivity.class);
        i.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
        PendingIntent pendingIntent = PendingIntent.getActivity(context,0,i, 0);
        noty.setContentIntent(pendingIntent);
        NotificationManager manager = (NotificationManager)context.getSystemService(Context.NOTIFICATION_SERVICE);
        manager.notify(1, noty.build());
    }
}
