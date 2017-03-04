package com.amador.fridge.adapters;

import android.app.Activity;
import android.app.LoaderManager;
import android.content.Context;
import android.content.CursorLoader;
import android.content.Loader;
import android.database.Cursor;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import android.widget.TextView;

import com.amador.fridge.R;
import com.amador.fridge.interfaces.ILoaderId;
import com.amador.fridge.model.Category;
import com.amador.fridge.provider.ProviderContract;

/**
 * Created by amador on 4/03/17.
 */

public class SpinnerProductAdapter extends CursorAdapter implements LoaderManager.LoaderCallbacks<Cursor>{


    private Context context;

    public SpinnerProductAdapter(Context context) {
        super(context, null, 0);
        this.context = context;
        ((Activity)context).getLoaderManager().initLoader(ILoaderId.SPINNER_CATEGORY, null, this);
    }

    @Override
    public Loader<Cursor> onCreateLoader(int i, Bundle bundle) {

        Loader<Cursor> loader = new CursorLoader(context, ProviderContract.CategoryEntry.CONTENT_URI, ProviderContract.CategoryEntry.PROJECTIONS,
                null,null,null);
        return loader;
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor cursor) {

        swapCursor(cursor);
        cursor.setNotificationUri(context.getContentResolver(), ProviderContract.CategoryEntry.CONTENT_URI);

    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {

        swapCursor(null);
    }

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup viewGroup) {

        View view = LayoutInflater.from(context).inflate(R.layout.item_spinner_category, viewGroup, false);
        SpinnerProductHolder holder = new SpinnerProductHolder();
        holder.txvName = (TextView)view.findViewById(R.id.txv_name_category_spinner);
        view.setTag(holder);
        return view;
    }

    @Override
    public void bindView(View view, Context context, Cursor cursor) {

        SpinnerProductHolder holder = (SpinnerProductHolder) view.getTag();
        holder.txvName.setText(cursor.getString(cursor.getColumnIndex(ProviderContract.CategoryEntry.NAME)));
    }

    public Category getCategoryAtPosition(int position){

        getCursor().moveToPosition(position);
        Category category = new Category();
        category.setId(getCursor().getLong(getCursor().getColumnIndex(ProviderContract.CategoryEntry._ID)));
        category.setName(getCursor().getString(getCursor().getColumnIndex(ProviderContract.CategoryEntry.NAME)));
        return category;
    }

    class SpinnerProductHolder{

        TextView txvName;
    }
}
