package com.amador.fridge;

import android.app.Application;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;

import com.amador.fridge.services.WarningService;

/**
 * Created by amador on 3/03/17.
 */

public class FridgeApplication extends Application {

    private static Context context;

    public static Context getContext(){

        return context;
    }

    public FridgeApplication(){

        context = this;
    }

    @Override
    public void onCreate() {
        super.onCreate();

        context = this;
        startService(new Intent(context, WarningService.class));

    }
}
