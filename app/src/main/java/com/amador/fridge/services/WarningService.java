package com.amador.fridge.services;

import android.app.IntentService;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Intent;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.os.Handler;

import com.amador.fridge.provider.ProviderContract;
import com.amador.fridge.receiber.WarningReceiber;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * An {@link IntentService} subclass for handling asynchronous task requests in
 * a service on a separate handler thread.
 * <p>
 * TODO: Customize class - update intent actions, extra parameters and static
 * helper methods.
 */
public class WarningService extends IntentService {


    public WarningService() {
        super("AnyService");
    }

    @Override
    protected void onHandleIntent(final Intent intent) {




    }


    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {


        final Handler handler = new Handler();

        handler.postDelayed(new Runnable() {
            @Override
            public void run() {


                SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
                String[] prooyections = {ProviderContract.ProductEntry.TIME_ALARM, ProviderContract.ProductEntry.DATE_CADUCITY,
                        ProviderContract.ProductEntry._ID, ProviderContract.ProductEntry.WARNING};
                Cursor cursorAllProduct = getContentResolver().query(ProviderContract.ProductEntry.CONTENT_URI,prooyections,
                        null,null,null);
                int count = 0;

                if(cursorAllProduct.getCount() > 0){

                    cursorAllProduct.moveToFirst();

                    do{

                        String dateCaducity = cursorAllProduct.getString(cursorAllProduct.getColumnIndex(ProviderContract.ProductEntry.DATE_CADUCITY));
                        int timeAlarm = cursorAllProduct.getInt(cursorAllProduct.getColumnIndex(ProviderContract.ProductEntry.TIME_ALARM));
                        Calendar calendarCompare = Calendar.getInstance();
                        String x = simpleDateFormat.format(calendarCompare.getTime());
                        calendarCompare.add(Calendar.DAY_OF_YEAR, timeAlarm);
                        String futureDate = simpleDateFormat.format(calendarCompare.getTime());

                        if(dateCaducity.equals(futureDate)){

                            count++;
                            ContentValues params = new ContentValues();
                            long id = cursorAllProduct.getLong(
                                    cursorAllProduct.getColumnIndex(ProviderContract.ProductEntry._ID));
                            params.put(ProviderContract.WarningEntry.PRODUCT_ID, id);
                            Uri result = null;

                            int warning = cursorAllProduct.getInt(cursorAllProduct.getColumnIndex(ProviderContract.ProductEntry.WARNING));
                            if(warning == 0) {

                                result = getContentResolver().insert(ProviderContract.WarningEntry.CONTENT_URI, params);

                            }

                            if(result != null){

                                String where = ProviderContract.ProductEntry._ID+" = ?";
                                String[] whereParams = {String.valueOf(id)};
                                ContentValues contentValues = new ContentValues();
                                contentValues.put(ProviderContract.ProductEntry.WARNING, 1);
                                getContentResolver().update(
                                        ContentUris.withAppendedId(ProviderContract.ProductEntry.CONTENT_URI,id), contentValues, where, whereParams);
                            }
                        }


                    }while (cursorAllProduct.moveToNext());

                    if(count > 0){

                        Intent intent1 = new Intent();
                        intent1.putExtra(WarningReceiber.RECOVERY_COUNT, count);
                        intent1.setAction(WarningReceiber.WARNING_ACTION);
                        sendBroadcast(intent1);
                    }

                }




                handler.postDelayed(this,10000);
            }
        }, 10000);


        return START_STICKY;
    }
}
