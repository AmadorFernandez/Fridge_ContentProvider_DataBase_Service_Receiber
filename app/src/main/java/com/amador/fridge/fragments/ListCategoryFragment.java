package com.amador.fridge.fragments;

import android.app.Activity;
import android.content.DialogInterface;
import android.database.Cursor;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;

import com.amador.fridge.R;
import com.amador.fridge.adapters.CategoryAdapter;
import com.amador.fridge.interfaces.IActivityLoad;
import com.amador.fridge.interfaces.IView;
import com.amador.fridge.model.Category;
import com.amador.fridge.provider.ProviderContract;

/**
 * Created by amador on 4/03/17.
 */

public class ListCategoryFragment extends Fragment implements IView {

    private ListView listView;
    private CategoryAdapter adapter;
    private CoordinatorLayout parent;
    private FloatingActionButton floatingActionButton;
    public static final int NEW = 1;
    public static final int UPDATE = 2;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.list_category_fragment, container, false);
        parent = (CoordinatorLayout)rootView.findViewById(R.id.parent);
        listView = (ListView)rootView.findViewById(R.id.listCategory);
        adapter = new CategoryAdapter(getContext(), this);
        registerForContextMenu(listView);
        listView.setAdapter(adapter);
        floatingActionButton = (FloatingActionButton)rootView.findViewById(R.id.fabAddCategory);

        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                showDialogCategory(new Category(), NEW);
            }
        });

        return rootView;
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {

        AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();
        Category category = adapter.getItemAtPosition(info.position);

        switch (item.getItemId()){

            case R.id.action_delte:
                adapter.delete(category.getId());
                break;
            case R.id.action_edit:
                showDialogCategory(category, UPDATE);
                break;
        }

        return super.onContextItemSelected(item);
    }

    private void showDialogCategory(final Category category, final int mode){

        View view = LayoutInflater.from(getContext()).inflate(R.layout.frm_category_dialog, null);
        final EditText edtNameCategory = (EditText)view.findViewById(R.id.edtFrmNameCategory);
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());



            edtNameCategory.setText(category.getName());


        builder.setView(view);

        builder.setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

                String nameCategory = edtNameCategory.getText().toString();

                if(nameCategory.isEmpty()){

                    showMessage(getString(R.string.name_empty));

                }else {

                    String where = ProviderContract.CategoryEntry.NAME +" = ?";
                    String[] whereParams = {nameCategory};

                    Cursor cursor = getActivity().getContentResolver().query(ProviderContract.CategoryEntry.CONTENT_URI,
                            ProviderContract.CategoryEntry.PROJECTIONS, where, whereParams, null);

                    if(cursor.getCount() > 0){

                        showMessage(getString(R.string.name_exists));

                    }else {

                        category.setName(nameCategory);
                        switch (mode){

                            case NEW:
                                adapter.addCategory(category);
                                break;
                            case UPDATE:
                                adapter.updateCategory(category);
                                break;
                        }
                    }

                }
            }
        }).setNegativeButton(android.R.string.cancel, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

                dialogInterface.dismiss();
            }
        }).show();

    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {

        getActivity().getMenuInflater().inflate(R.menu.context_crud_menu, menu);
        super.onCreateContextMenu(menu, v, menuInfo);
    }

    @Override
    public void showMessage(String message) {

        Snackbar.make(parent, message, Snackbar.LENGTH_LONG).show();
    }
}
