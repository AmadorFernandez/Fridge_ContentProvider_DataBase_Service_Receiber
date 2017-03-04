package com.amador.fridge.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.ListView;

import com.amador.fridge.R;
import com.amador.fridge.adapters.WarningAdapter;
import com.amador.fridge.interfaces.IView;

/**
 * Created by amador on 4/03/17.
 */

public class ListWarning_Fragment extends Fragment implements IView {


    private ListView listView;
    private WarningAdapter adapter;
    private LinearLayout parent;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.list_warnig_fragment, container, false);

        adapter = new WarningAdapter(getContext(), this);
        parent = (LinearLayout)rootView.findViewById(R.id.parent);
        listView = (ListView)rootView.findViewById(R.id.listWarning);
        registerForContextMenu(listView);
        listView.setAdapter(adapter);
        return rootView;
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {

        getActivity().getMenuInflater().inflate(R.menu.contex_menu_delete_single, menu);

        super.onCreateContextMenu(menu, v, menuInfo);
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {

        AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();
        adapter.deleteProduct(adapter.getWarningIdAtPosition(info.position));

        return super.onContextItemSelected(item);
    }

    @Override
    public void showMessage(String message) {

        Snackbar.make(parent, message, Snackbar.LENGTH_LONG).show();
    }
}
