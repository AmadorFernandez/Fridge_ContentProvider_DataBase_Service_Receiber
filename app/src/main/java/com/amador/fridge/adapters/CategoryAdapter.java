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
import com.amador.fridge.model.Category;
import com.amador.fridge.provider.ProviderContract;

/**
 * Created by amador on 4/03/17.
 */

public class CategoryAdapter extends CursorAdapter implements LoaderManager.LoaderCallbacks<Cursor> {



    private Context context;
    private IView view;

    public CategoryAdapter(Context context, IView view) {
        super(context, null,0);
        this.context = context;
        this.view = view;
        ((Activity)context).getLoaderManager().initLoader(ILoaderId.CATEGORY_LIST, null, this);

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

        View view = LayoutInflater.from(context).inflate(R.layout.item_category, viewGroup, false);
        ListCategoryHolder holder = new ListCategoryHolder();
        holder.txvNameCategory = (TextView)view.findViewById(R.id.txvNameCategory);
        view.setTag(holder);

        return view;
    }

    @Override
    public void bindView(View view, Context context, Cursor cursor) {

        ListCategoryHolder holder = (ListCategoryHolder) view.getTag();
        holder.txvNameCategory.setText(cursor.getString(cursor.getColumnIndex(ProviderContract.CategoryEntry.NAME)));

    }


    public Category getItemAtPosition(int position){

        Category category = new Category();
        Cursor cursor = getCursor();

        cursor.moveToPosition(position);
        category.setName(cursor.getString(cursor.getColumnIndex(ProviderContract.CategoryEntry.NAME)));
        category.setId(cursor.getLong(cursor.getColumnIndex(ProviderContract.CategoryEntry._ID)));

        return category;
    }

    public void delete(long id){

        String where = ProviderContract.CategoryEntry._ID+" = ?";
        String[] whereParam = {String.valueOf(id)};
        Uri uri = ContentUris.withAppendedId(ProviderContract.CategoryEntry.CONTENT_URI, id);
        int result = context.getContentResolver().delete(uri, where, whereParam);

        if(result > 0){

            view.showMessage(context.getString(R.string.delete_ok));

        }else {

            view.showMessage(context.getString(R.string.delete_fail));
        }

    }

    public void updateCategory(Category category){

        String where = ProviderContract.CategoryEntry._ID+" = ?";
        String[] whereParam = {String.valueOf(category.getId())};
        Uri uri = ContentUris.withAppendedId(ProviderContract.CategoryEntry.CONTENT_URI, category.getId());
        ContentValues params = new ContentValues();
        params.put(ProviderContract.CategoryEntry.NAME, category.getName());

        int result = context.getContentResolver().update(uri, params, where, whereParam);

        if(result > 0){

            view.showMessage(context.getString(R.string.update_ok));

        }else {

            view.showMessage(context.getString(R.string.update_fail));
        }

    }

    public void addCategory(Category category){

        Uri uri = ProviderContract.CategoryEntry.CONTENT_URI;
        ContentValues params = new ContentValues();
        params.put(ProviderContract.CategoryEntry.NAME, category.getName());

        Uri result = context.getContentResolver().insert(uri, params);

        if(result != null){

            view.showMessage(context.getString(R.string.insert_ok));
            category.setId(Long.parseLong(result.getLastPathSegment()));

        }else {

            view.showMessage(context.getString(R.string.insert_fail));
        }
    }

    class ListCategoryHolder{

        TextView txvNameCategory;
    }
}
