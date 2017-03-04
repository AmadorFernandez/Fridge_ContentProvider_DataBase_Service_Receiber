package com.amador.fridge.fragments;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import com.amador.fridge.R;
import com.amador.fridge.adapters.ProductAdapter;
import com.amador.fridge.interfaces.IActivityLoad;
import com.amador.fridge.interfaces.IView;
import com.amador.fridge.model.Product;

/**
 * Created by amador on 4/03/17.
 */

public class ListProduct_Fragment extends Fragment implements IView {

    private ListView listView;
    private CoordinatorLayout parent;
    private ProductAdapter adapter;
    private FloatingActionButton floatingActionButton;
    private IActivityLoad activityLoad;
    public static final int NEW = 1;
    public static final int UPDATE = 2;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.list_product_fragment, container, false);
        listView = (ListView)rootView.findViewById(R.id.listProduct);
        floatingActionButton = (FloatingActionButton)rootView.findViewById(R.id.fabAddProduct);
        parent = (CoordinatorLayout)rootView.findViewById(R.id.parent);
        adapter = new ProductAdapter(getContext(), this);
        registerForContextMenu(listView);
        listView.setAdapter(adapter);

        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Bundle bundle = new Bundle();
                bundle.putParcelable(FrmProduct_Fragment.RECOVERY_PRODUCT, new Product());
                bundle.putInt(FrmProduct_Fragment.RECOVERY_MODE, NEW);
                activityLoad.loadFrmProduct(bundle);
            }
        });

        return rootView;

    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {

        getActivity().getMenuInflater().inflate(R.menu.context_crud_menu, menu);
        super.onCreateContextMenu(menu, v, menuInfo);
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {

        AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();
        Product product = adapter.getProductAtPosition(info.position);

        switch (item.getItemId()){

            case R.id.action_delte:
                adapter.deleteProduct(product.getId());
                break;
            case R.id.action_edit:
                Bundle bundle = new Bundle();
                bundle.putParcelable(FrmProduct_Fragment.RECOVERY_PRODUCT, product);
                bundle.putInt(FrmProduct_Fragment.RECOVERY_MODE, UPDATE);
                activityLoad.loadFrmProduct(bundle);
                break;
        }


        return super.onContextItemSelected(item);
    }

    public void loadProduct(Product product, int mode){

        switch (mode){

            case NEW:
                adapter.addProduct(product);
                break;
            case UPDATE:
                adapter.updateProduct(product);
                break;
        }
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        activityLoad = (IActivityLoad)activity;
    }

    @Override
    public void showMessage(String message) {

        Snackbar.make(parent, message, Snackbar.LENGTH_LONG).show();
    }
}
