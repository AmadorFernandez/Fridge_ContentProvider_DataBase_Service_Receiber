package com.amador.fridge.adapters;

import android.app.Activity;
import android.app.LoaderManager;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.content.CursorLoader;
import android.content.Loader;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import android.widget.TextView;

import com.amador.fridge.R;
import com.amador.fridge.interfaces.ILoaderId;
import com.amador.fridge.interfaces.IView;
import com.amador.fridge.provider.ProviderContract;

/**
 * Created by amador on 4/03/17.
 */

public class WarningAdapter extends CursorAdapter implements LoaderManager.LoaderCallbacks<Cursor> {

    private Context context;
    private IView view;

    public WarningAdapter(Context context, IView view) {
        super(context, null, 0);
        this.context = context;
        this.view = view;
        ((Activity)context).getLoaderManager().initLoader(ILoaderId.WARNING_LIST, null, this);
    }

    @Override
    public Loader<Cursor> onCreateLoader(int i, Bundle bundle) {

        Loader<Cursor> loader = new CursorLoader(context, ProviderContract.WarningEntry.CONTENT_URI, ProviderContract.WarningEntry.PROJECTION,
                null,null,null);
        return loader;
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor cursor) {

        swapCursor(cursor);
        cursor.setNotificationUri(context.getContentResolver(), ProviderContract.WarningEntry.CONTENT_URI);
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {

        swapCursor(null);
    }

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup viewGroup) {

        View view = LayoutInflater.from(context).inflate(R.layout.item_product, viewGroup, false);
        WarningAdapterHolder holder = new WarningAdapterHolder();
        holder.txvNameProduct = (TextView)view.findViewById(R.id.txvNameProduct);
        view.setTag(holder);
        return view;
    }

    @Override
    public void bindView(View view, Context context, Cursor cursor) {

        WarningAdapterHolder holder = (WarningAdapterHolder)view.getTag();
        holder.txvNameProduct.setText(cursor.getString(ProviderContract.WarningEntry.POS_WARNING_PRODUCT_NAME));
    }

    public void deleteWarning(long id){

        Uri uri = ContentUris.withAppendedId(ProviderContract.WarningEntry.CONTENT_URI, id);
        String where = ProviderContract.WarningEntry._ID+" = ?";
        String[] whereParams = {String.valueOf(id)};
        int result = context.getContentResolver().delete(uri, where, whereParams);

        if(result > 0){

            view.showMessage(context.getString(R.string.delete_ok));

        }else {

            view.showMessage(context.getString(R.string.delete_fail));
        }
    }

    public long getWarningIdAtPosition(int position){

        Cursor cursor = getCursor();
        cursor.moveToPosition(position);
        return cursor.getLong(ProviderContract.WarningEntry.POS_WARNING_ID);
    }

    public void deleteProduct(long id){

        Uri uri1 = ContentUris.withAppendedId(ProviderContract.WarningEntry.CONTENT_URI, id);
        String where1 = ProviderContract.WarningEntry._ID+" = ?";
        String[] whereParams1 = {String.valueOf(id)};
        int result1 = context.getContentResolver().delete(uri1, where1, whereParams1);


        if(result1 > 0 ){

            view.showMessage(context.getString(R.string.delete_ok));

        }else {

            view.showMessage(context.getString(R.string.delete_fail));
        }
    }

    class WarningAdapterHolder{

        TextView txvNameProduct;
    }
}
