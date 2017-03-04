package com.amador.fridge.provider;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteQueryBuilder;
import android.net.Uri;

import com.amador.fridge.database.DataBaseHelper;
import com.amador.fridge.model.Category;

public class FridgeProvider extends ContentProvider {

    private static final int PRODUCT = 1;
    private static final int PRODUCT_ID = 2;
    private static final int CATEGORY = 3;
    private static final int CATEGORY_ID = 4;
    private static final int WARNING = 5;
    private static final int WARNING_ID = 6;
    private static UriMatcher uriMacher = new UriMatcher(UriMatcher.NO_MATCH);
    private SQLiteDatabase database;

    static {

        uriMacher.addURI(ProviderContract.AUTHORITY, ProviderContract.ProductEntry.CONTENT_PATH, PRODUCT);
        uriMacher.addURI(ProviderContract.AUTHORITY, ProviderContract.ProductEntry.CONTENT_PATH+"/#", PRODUCT_ID);
        uriMacher.addURI(ProviderContract.AUTHORITY, ProviderContract.CategoryEntry.CONTENT_PATH, CATEGORY);
        uriMacher.addURI(ProviderContract.AUTHORITY, ProviderContract.CategoryEntry.CONTENT_PATH+"/#", CATEGORY_ID);
        uriMacher.addURI(ProviderContract.AUTHORITY, ProviderContract.WarningEntry.CONTENT_PATH, WARNING);
        uriMacher.addURI(ProviderContract.AUTHORITY, ProviderContract.WarningEntry.CONTENT_PATH+"/#", WARNING_ID);
    }



    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {

        int count = 0;

        switch (uriMacher.match(uri)){

            case PRODUCT_ID:
                count = database.delete(ProviderContract.ProductEntry.CONTENT_PATH, selection, selectionArgs);
                break;
            case CATEGORY_ID:
                count = database.delete(ProviderContract.CategoryEntry.CONTENT_PATH, selection, selectionArgs);
                break;
            case WARNING_ID:
                count = database.delete(ProviderContract.WarningEntry.CONTENT_PATH, selection, selectionArgs);
                break;
        }

        if(count > 0){

            getContext().getContentResolver().notifyChange(uri, null);
        }

        return count;
    }

    @Override
    public String getType(Uri uri) {
        // TODO: Implement this to handle requests for the MIME type of the data
        // at the given URI.
        throw new UnsupportedOperationException("Not yet implemented");
    }

    @Override
    public Uri insert(Uri uri, ContentValues values) {

        Uri newUri = null;
        long id = -1;

        switch (uriMacher.match(uri)){

            case PRODUCT:
                id = database.insert(ProviderContract.ProductEntry.CONTENT_PATH, null, values);
                break;
            case CATEGORY:
                id = database.insert(ProviderContract.CategoryEntry.CONTENT_PATH, null, values);
                break;
            case WARNING:
                id = database.insert(ProviderContract.WarningEntry.CONTENT_PATH, null, values);
                break;
        }

        if(id != -1){

            newUri = ContentUris.withAppendedId(uri, id);
            getContext().getContentResolver().notifyChange(newUri, null);
        }

        return newUri;
    }

    @Override
    public boolean onCreate() {

        database = DataBaseHelper.getInstance().getDatabase();
        return true;
    }

    @Override
    public Cursor query(Uri uri, String[] projection, String selection,
                        String[] selectionArgs, String sortOrder) {

        Cursor cursor = null;
        SQLiteQueryBuilder queryBuilder = new SQLiteQueryBuilder();


        switch (uriMacher.match(uri)){

            case PRODUCT:
                cursor = database.query(ProviderContract.ProductEntry.CONTENT_PATH, projection, selection, selectionArgs, null, null, sortOrder);
                break;
            case CATEGORY:
                cursor = database.query(ProviderContract.CategoryEntry.CONTENT_PATH, projection, selection, selectionArgs, null, null, sortOrder);
                break;
            case WARNING:
                queryBuilder.setTables(ProviderContract.WarningEntry.JOIN_PRODUCT);
                queryBuilder.setDistinct(true);
                cursor = queryBuilder.query(database, projection, selection, selectionArgs, null, null, sortOrder);
                break;

        }

        return cursor;
    }

    @Override
    public int update(Uri uri, ContentValues values, String selection,
                      String[] selectionArgs) {

        int count = 0;

        switch (uriMacher.match(uri)){

            case PRODUCT_ID:
                count = database.update(ProviderContract.ProductEntry.CONTENT_PATH, values, selection, selectionArgs);
                break;
            case CATEGORY_ID:
                count = database.update(ProviderContract.CategoryEntry.CONTENT_PATH, values, selection, selectionArgs);
                break;
            case WARNING_ID:
                count = database.update(ProviderContract.WarningEntry.CONTENT_PATH, values, selection, selectionArgs);
                break;
        }

        if(count > 0){

            getContext().getContentResolver().notifyChange(uri, null);
        }

        return count;
    }
}
