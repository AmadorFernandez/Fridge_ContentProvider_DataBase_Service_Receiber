package com.amador.fridge;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.amador.fridge.fragments.FrmProduct_Fragment;
import com.amador.fridge.fragments.Home_Fragment;
import com.amador.fridge.fragments.ListCategoryFragment;
import com.amador.fridge.fragments.ListProduct_Fragment;
import com.amador.fridge.fragments.ListWarning_Fragment;
import com.amador.fridge.interfaces.IActivityLoad;
import com.amador.fridge.model.Product;
import com.amador.fridge.receiber.WarningReceiber;

public class HomeActivity extends AppCompatActivity implements IActivityLoad {

    private FrmProduct_Fragment frmProduct_fragment;
    private Home_Fragment home_fragment;
    private ListCategoryFragment listCategoryFragment;
    private ListProduct_Fragment listProduct_fragment;
    private ListWarning_Fragment listWarning_fragment;
    private static final String TAG_HOME = "home";
    private static final String TAG_CATEGORY = "category";
    private static final String TAG_PRODUCT_LIST = "listproduct";
    private static final String TAG_PRODUCT_FRM = "frmProduct";
    private static final String TAG_WARNING_LIST = "warning";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        listProduct_fragment = new ListProduct_Fragment();

            if (savedInstanceState == null) {

                home_fragment = (Home_Fragment) getSupportFragmentManager().findFragmentByTag(TAG_HOME);

                if (home_fragment == null) {

                    home_fragment = new Home_Fragment();
                }

                getSupportFragmentManager().beginTransaction().add(R.id.activity_home, home_fragment, TAG_HOME).commit();

            }

    }


    @Override
    protected void onNewIntent(Intent intent) {

        loadListWarning();
    }

    @Override
    public void loadProductInFragment(Product product, int mode) {

        getSupportFragmentManager().popBackStack();
        listProduct_fragment.loadProduct(product, mode);
    }

    @Override
    public void loadListCategories() {

        listCategoryFragment = new ListCategoryFragment();
        getSupportFragmentManager().beginTransaction().replace(R.id.activity_home, listCategoryFragment, TAG_CATEGORY).addToBackStack(null).commit();

    }

    @Override
    public void loadFrmProduct(Bundle bundle) {

        FrmProduct_Fragment frmProduct_fragment = FrmProduct_Fragment.newInstance(bundle);
        getSupportFragmentManager().beginTransaction().replace(R.id.activity_home, frmProduct_fragment, TAG_PRODUCT_FRM).addToBackStack(null).commit();
    }

    @Override
    public void loadListProduct() {

        getSupportFragmentManager().beginTransaction().replace(R.id.activity_home, listProduct_fragment, TAG_PRODUCT_LIST).addToBackStack(null).commit();
    }

    @Override
    public void loadListWarning() {

        listWarning_fragment = new ListWarning_Fragment();
        getSupportFragmentManager().beginTransaction().replace(R.id.activity_home, listWarning_fragment, TAG_WARNING_LIST).addToBackStack(null).commit();

    }
}
