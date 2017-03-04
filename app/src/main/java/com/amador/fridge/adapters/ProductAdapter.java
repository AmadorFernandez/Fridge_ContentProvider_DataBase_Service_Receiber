package com.amador.fridge.adapters;

import android.app.Activity;
import android.app.LoaderManager;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.content.CursorLoader;
import android.content.Loader;
import android.database.Cursor;
import android.graphics.Color;
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
import com.amador.fridge.model.Product;
import com.amador.fridge.provider.ProviderContract;

/**
 * Created by amador on 4/03/17.
 */

public class ProductAdapter extends CursorAdapter implements LoaderManager.LoaderCallbacks<Cursor> {

    private Context context;
    private IView view;

    public ProductAdapter(Context context, IView view) {
        super(context, null, 0);
        this.context = context;
        this.view = view;
        ((Activity)context).getLoaderManager().initLoader(ILoaderId.PRODUCT_LIST, null, this);

    }

    @Override
    public Loader<Cursor> onCreateLoader(int i, Bundle bundle) {

        Loader<Cursor> loader = new CursorLoader(context, ProviderContract.ProductEntry.CONTENT_URI, ProviderContract.ProductEntry.PROJECTIONS,
                null, null,null);
        return loader;
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor cursor) {

        swapCursor(cursor);
        cursor.setNotificationUri(context.getContentResolver(), ProviderContract.ProductEntry.CONTENT_URI);
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {

        swapCursor(null);
    }

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup viewGroup) {

        View view = LayoutInflater.from(context).inflate(R.layout.item_product, viewGroup, false);
        ProductAdapterHolder holder = new ProductAdapterHolder();
        holder.txvDateCaducity = (TextView)view.findViewById(R.id.txvDateCaducity);
        holder.txvNameProduct = (TextView)view.findViewById(R.id.txvNameProduct);
        holder.txvStockProduct = (TextView)view.findViewById(R.id.txvStockProduct);
        view.setTag(holder);
        return view;
    }

    @Override
    public void bindView(View view, Context context, Cursor cursor) {

        ProductAdapterHolder holder = (ProductAdapterHolder) view.getTag();
        holder.txvDateCaducity.setText(cursor.getString(cursor.getColumnIndex(ProviderContract.ProductEntry.DATE_CADUCITY)));
        holder.txvStockProduct.setText(String.valueOf(cursor.getInt(cursor.getColumnIndex(ProviderContract.ProductEntry.STOCK))));
        holder.txvNameProduct.setText(cursor.getString(cursor.getColumnIndex(ProviderContract.ProductEntry.NAME)));

        if(cursor.getInt(cursor.getColumnIndex(ProviderContract.ProductEntry.WARNING)) == 1){

            view.setBackgroundColor(Color.RED);

        }else {

            view.setBackgroundColor(Color.WHITE);
        }

    }

    public Product getProductAtPosition(int position){

        Product product = new Product();

        Cursor cursor = getCursor();
        cursor.moveToPosition(position);
        product.setId(cursor.getLong(cursor.getColumnIndex(ProviderContract.ProductEntry._ID)));
        product.setName(cursor.getString(cursor.getColumnIndex(ProviderContract.ProductEntry.NAME)));
        product.setCategotyId(cursor.getInt(cursor.getColumnIndex(ProviderContract.ProductEntry.CATEGORY_ID)));
        product.setWarningInsert(cursor.getInt(cursor.getColumnIndex(ProviderContract.ProductEntry.WARNING)));
        product.setDateCaducity(cursor.getString(cursor.getColumnIndex(ProviderContract.ProductEntry.DATE_CADUCITY)));
        product.setTimeOfWarning(cursor.getInt(cursor.getColumnIndex(ProviderContract.ProductEntry.TIME_ALARM)));
        product.setStock(cursor.getInt(cursor.getColumnIndex(ProviderContract.ProductEntry.STOCK)));
        return product;

    }

    public void deleteProduct(long id){

        Uri uri = ContentUris.withAppendedId(ProviderContract.ProductEntry.CONTENT_URI, id);
        String where = ProviderContract.ProductEntry._ID+" = ?";
        String[] whereParams = {String.valueOf(id)};
        int result = context.getContentResolver().delete(uri, where, whereParams);

        if(result > 0){

            view.showMessage(context.getString(R.string.delete_ok));

        }else {

            view.showMessage(context.getString(R.string.delete_fail));
        }
    }

    public void updateProduct(Product product){

        Uri uri = ContentUris.withAppendedId(ProviderContract.ProductEntry.CONTENT_URI, product.getId());
        String where = ProviderContract.ProductEntry._ID+" = ?";
        String[] whereParams = {String.valueOf(product.getId())};
        ContentValues params = new ContentValues();
        params.put(ProviderContract.ProductEntry.CATEGORY_ID, product.getCategotyId());
        params.put(ProviderContract.ProductEntry.NAME, product.getName());
        params.put(ProviderContract.ProductEntry.STOCK, product.getStock());
        params.put(ProviderContract.ProductEntry.DATE_CADUCITY, product.getDateCaducity());
        params.put(ProviderContract.ProductEntry.TIME_ALARM, product.getTimeOfWarning());
        params.put(ProviderContract.ProductEntry.WARNING, product.getWarningInsert());
        int result = context.getContentResolver().update(uri, params, where, whereParams);

        if(result > 0){

            view.showMessage(context.getString(R.string.update_ok));

        }else {

            view.showMessage(context.getString(R.string.update_fail));
        }

    }

    public void addProduct(Product product){


        ContentValues params = new ContentValues();
        params.put(ProviderContract.ProductEntry.CATEGORY_ID, product.getCategotyId());
        params.put(ProviderContract.ProductEntry.NAME, product.getName());
        params.put(ProviderContract.ProductEntry.STOCK, product.getStock());
        params.put(ProviderContract.ProductEntry.DATE_CADUCITY, product.getDateCaducity());
        params.put(ProviderContract.ProductEntry.TIME_ALARM, product.getTimeOfWarning());
        params.put(ProviderContract.ProductEntry.WARNING, product.getWarningInsert());
        Uri uriResult = context.getContentResolver().insert(ProviderContract.ProductEntry.CONTENT_URI, params);

        if(uriResult != null){

            view.showMessage(context.getString(R.string.insert_ok));
            product.setId(Long.parseLong(uriResult.getLastPathSegment()));

        }else {

            view.showMessage(context.getString(R.string.insert_fail));
        }
    }

    class ProductAdapterHolder{

        TextView txvNameProduct, txvStockProduct, txvDateCaducity;
    }
}
