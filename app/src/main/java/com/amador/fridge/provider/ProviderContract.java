package com.amador.fridge.provider;

import android.content.Context;
import android.icu.util.ULocale;
import android.net.Uri;
import android.provider.BaseColumns;

import com.amador.fridge.model.Product;

/**
 * Created by amador on 3/03/17.
 */

public class ProviderContract {

    public static final String AUTHORITY = "com.amador.fridge";
    public static final Uri AUTHORITY_URI = Uri.parse("content://"+AUTHORITY);

    public static class CategoryEntry implements BaseColumns{

        public static final String CONTENT_PATH = "category";
        public static final Uri CONTENT_URI = Uri.withAppendedPath(AUTHORITY_URI, CONTENT_PATH);
        public static final String NAME = "ca_name";
        public static final String[] PROJECTIONS = { _ID, NAME };

        public static final String SQL_CREATE = "CREATE TABLE "+CONTENT_PATH+" ("+_ID+" INTEGER PRIMARY KEY AUTOINCREMENT," +
                NAME+" TEXT NOT NULL)";

        public static final String SQL_INSERT_ENTRIES = "INSERT INTO "+CONTENT_PATH+ " VALUES (1,'Lacteos'), (2,'Refrescos')";

        public static final String DELETE_TABLE = "DROP TABLE IF EXISTS "+CONTENT_PATH;
    }

    public static class ProductEntry implements BaseColumns{

        public static final String CONTENT_PATH = "product";
        public static final Uri CONTENT_URI = Uri.withAppendedPath(AUTHORITY_URI, CONTENT_PATH);
        public static final String NAME = "pro_name";
        public static final String TIME_ALARM = "pro_time_alarm";
        public static final String CATEGORY_ID = "pro_category";
        public static final String DATE_CADUCITY = "pro_date_caducity";
        public static final String WARNING = "pro_warnig";
        public static final String STOCK = "pro_stock";
        public static final String[] PROJECTIONS = {

                _ID, NAME, TIME_ALARM, CATEGORY_ID, DATE_CADUCITY, WARNING, STOCK
        };
        public static final String SQL_CREATE = "CREATE TABLE "+CONTENT_PATH+" ("+_ID +" INTEGER PRIMARY KEY AUTOINCREMENT," +
                NAME+" TEXT NOT NULL,"+TIME_ALARM+" TEXT NOT NULL,"+CATEGORY_ID+" INTEGER REFERENCES "+CategoryEntry.CONTENT_PATH
                +"("+CategoryEntry._ID+") ON UPDATE CASCADE ON DELETE RESTRICT,"+DATE_CADUCITY+" TEXT NOT NULL,"+WARNING+" INTEGER,"+
                STOCK+" INTEGER)";

        public static final String DELETE_TABLE = "DROP TABLE IF EXISTS "+CONTENT_PATH;
    }

    public static class WarningEntry implements BaseColumns{

        public static final String CONTENT_PATH = "warnig";
        public static final Uri CONTENT_URI = Uri.withAppendedPath(AUTHORITY_URI, CONTENT_PATH);
        public static final String PRODUCT_ID = "wa_id";
        public static final String[] PROJECTION = {

                "w."+_ID, "p."+ ProductEntry.NAME, "p."+ProductEntry._ID
        };
        public static final int POS_WARNING_ID = 0;
        public static final int POS_WARNING_PRODUCT_NAME = 1;
        public static final int POS_WARNING_PRODUCT_ID = 2;

        public static final String JOIN_PRODUCT = " "+CONTENT_PATH+" w INNER JOIN "+ProductEntry.CONTENT_PATH+" p "+
                "ON w."+PRODUCT_ID+" = p."+ProductEntry._ID;

        public static final String SQL_CREATE = "CREATE TABLE "+CONTENT_PATH+" ("+_ID+" INTEGER PRIMARY KEY AUTOINCREMENT,"+
                PRODUCT_ID+" INTEGER REFERENCES "+ProductEntry.CONTENT_PATH+"("+ProductEntry._ID+") ON UPDATE CASCADE ON DELETE CASCADE)";

        public static final String DELETE_TABLE = "DROP TABLE IF EXISTS "+CONTENT_PATH;
    }

}
