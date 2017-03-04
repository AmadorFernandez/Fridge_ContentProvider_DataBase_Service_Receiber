package com.amador.fridge.fragments;

import android.app.Activity;
import android.database.Cursor;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.util.Patterns;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;

import com.amador.fridge.R;
import com.amador.fridge.adapters.SpinnerProductAdapter;
import com.amador.fridge.interfaces.IActivityLoad;
import com.amador.fridge.interfaces.IView;
import com.amador.fridge.model.Category;
import com.amador.fridge.model.Product;
import com.amador.fridge.provider.ProviderContract;

import java.text.SimpleDateFormat;

/**
 * Created by amador on 4/03/17.
 */

public class FrmProduct_Fragment extends Fragment implements IView {

    private EditText edtName, edtStock, edtDateCaducity, edtTimeAlarm;
    private Spinner spCategory;
    private SpinnerProductAdapter adapter;
    private LinearLayout parent;
    private IActivityLoad activityLoad;
    private Product product;
    private int mode;
    private long categoryId;
    public static final String RECOVERY_PRODUCT = "prooduct";
    public static final String RECOVERY_MODE = "mode";


    public static FrmProduct_Fragment newInstance(Bundle b) {

        FrmProduct_Fragment fragment = new FrmProduct_Fragment();
        fragment.setArguments(b);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.frm_prooduct_fragment, container, false);
        product = getArguments().getParcelable(RECOVERY_PRODUCT);
        mode = getArguments().getInt(RECOVERY_MODE);

        edtName = (EditText)rootView.findViewById(R.id.edtFrmNameProduct);
        edtStock = (EditText)rootView.findViewById(R.id.edtStockProduc);
        edtDateCaducity = (EditText)rootView.findViewById(R.id.edtFrmDateCaducityProduct);
        edtTimeAlarm = (EditText)rootView.findViewById(R.id.edtTimeAlarm);
        parent = (LinearLayout)rootView.findViewById(R.id.parent);
        spCategory = (Spinner)rootView.findViewById(R.id.spFrmCategoryProduct);
        adapter = new SpinnerProductAdapter(getContext());
        spCategory.setAdapter(adapter);
        spCategory.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {

                Category category = adapter.getCategoryAtPosition(i);
                categoryId = category.getId();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        edtName.setText(product.getName());
        edtTimeAlarm.setText(String.valueOf(product.getStock()));
        edtDateCaducity.setText(product.getDateCaducity());
        edtStock.setText(String.valueOf(product.getStock()));

        return rootView;
    }

    @Override
    public void showMessage(String message) {

        Snackbar.make(parent, message, Snackbar.LENGTH_LONG).show();
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {

        inflater.inflate(R.menu.menu_add, menu);

        super.onCreateOptionsMenu(menu, inflater);
    }


    public boolean validateNameProduct(){

        String name = edtName.getText().toString();
        String where = ProviderContract.ProductEntry.NAME+" = ?";
        String[] whereParams = {name};
        boolean result = true;
        Cursor cursor = getActivity().getContentResolver().query(ProviderContract.ProductEntry.CONTENT_URI,
                ProviderContract.ProductEntry.PROJECTIONS, where, whereParams, null);

        if(name.isEmpty()){

            showMessage(getString(R.string.name_empty));
            result = false;

        }else if(cursor.getCount() > 0){

            showMessage(getString(R.string.product_name_exist));
            result = false;
        }

        return result;
    }

    public boolean validateNumbers(String numbers){

        boolean result = true;

        try {

            int stock = Integer.parseInt(numbers);

            if(stock < 1){

                showMessage(getString(R.string.incorrec_count));
            }

        } catch (NumberFormatException e) {

            result = false;
            showMessage(getString(R.string.incorrec_stock));
        }

        return result;
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        if(validateDate() &&  validateNameProduct() && validateNumbers(edtStock.getText().toString()) &&
                validateNumbers(edtTimeAlarm.getText().toString())){

            product.setStock(Integer.parseInt(edtStock.getText().toString()));
            product.setName(edtName.getText().toString());
            product.setTimeOfWarning(Integer.parseInt(edtTimeAlarm.getText().toString()));
            product.setDateCaducity(edtDateCaducity.getText().toString());
            product.setCategotyId(categoryId);
            activityLoad.loadProductInFragment(product, mode);
        }

        return super.onOptionsItemSelected(item);
    }

    public boolean validateDate(){

        boolean result = true;

        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
        String date = edtDateCaducity.getText().toString();
        simpleDateFormat.setLenient(false);
        return !date.isEmpty();
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        activityLoad = (IActivityLoad)activity;
    }
}
