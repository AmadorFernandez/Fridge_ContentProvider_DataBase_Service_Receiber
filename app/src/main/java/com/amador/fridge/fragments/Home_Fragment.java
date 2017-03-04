package com.amador.fridge.fragments;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.amador.fridge.R;
import com.amador.fridge.interfaces.IActivityLoad;
import com.amador.fridge.interfaces.ILoaderId;

/**
 * Created by amador on 4/03/17.
 */

public class Home_Fragment extends Fragment {

    private TextView txvLoadCategories, txvLoadWarnings, txvLoadProducts;
    private IActivityLoad activityLoad;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.home_fragment, container, false);
        txvLoadCategories = (TextView)rootView.findViewById(R.id.txvCategories);
        txvLoadProducts = (TextView)rootView.findViewById(R.id.txvProducts);
        txvLoadWarnings = (TextView)rootView.findViewById(R.id.txvWarnings);

        txvLoadCategories.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                activityLoad.loadListCategories();
            }
        });

        txvLoadWarnings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                activityLoad.loadListWarning();
            }
        });

        txvLoadProducts.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                activityLoad.loadListProduct();
            }
        });

        return rootView;
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);

        activityLoad = (IActivityLoad)activity;
    }
}
