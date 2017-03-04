package com.amador.fridge.database;

import android.content.Context;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Build;

import com.amador.fridge.FridgeApplication;
import com.amador.fridge.provider.ProviderContract;

/**
 * Created by amador on 4/03/17.
 */

public class DataBaseHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "fidge.db";
    private static final int VERSION = 3;
    private static DataBaseHelper instance;
    private SQLiteDatabase database;



    public DataBaseHelper() {
        super(FridgeApplication.getContext(), DATABASE_NAME, null, VERSION);
        database = getWritableDatabase();
    }

    public static DataBaseHelper getInstance(){

        if(instance == null){

            instance = new DataBaseHelper();
        }

        return instance;
    }


    public SQLiteDatabase getDatabase(){

        return database;
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {

        sqLiteDatabase.beginTransaction();

        try {
            sqLiteDatabase.execSQL(ProviderContract.CategoryEntry.SQL_CREATE);
            sqLiteDatabase.execSQL(ProviderContract.CategoryEntry.SQL_INSERT_ENTRIES);
            sqLiteDatabase.execSQL(ProviderContract.ProductEntry.SQL_CREATE);
            sqLiteDatabase.execSQL(ProviderContract.WarningEntry.SQL_CREATE);
            sqLiteDatabase.setTransactionSuccessful();
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {

            sqLiteDatabase.endTransaction();
        }


    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

        sqLiteDatabase.beginTransaction();

        try {
            sqLiteDatabase.execSQL(ProviderContract.WarningEntry.DELETE_TABLE);
            sqLiteDatabase.execSQL(ProviderContract.ProductEntry.DELETE_TABLE);
            sqLiteDatabase.execSQL(ProviderContract.CategoryEntry.DELETE_TABLE);
            onCreate(sqLiteDatabase);
            sqLiteDatabase.setTransactionSuccessful();
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {

            sqLiteDatabase.endTransaction();
        }
    }

    @Override
    public void onDowngrade(SQLiteDatabase db, int oldVersion, int newVersion) {
       onUpgrade(db, newVersion, oldVersion);
    }

    @Override
    public void onOpen(SQLiteDatabase db) {
        super.onOpen(db);

        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN){

            db.setForeignKeyConstraintsEnabled(true);

        }else {

            db.execSQL("PRAGMA foreign_keys = ON");
        }
    }
}
