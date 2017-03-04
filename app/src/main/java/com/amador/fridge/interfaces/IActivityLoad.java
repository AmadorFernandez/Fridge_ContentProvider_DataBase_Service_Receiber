package com.amador.fridge.interfaces;

import android.os.Bundle;

import com.amador.fridge.model.Category;
import com.amador.fridge.model.Product;

/**
 * Created by amador on 4/03/17.
 */

public interface IActivityLoad {

    void loadProductInFragment(Product product, int mode);

    void loadListCategories();

    void loadFrmProduct(Bundle bundle);

    void loadListProduct();

    void loadListWarning();


}
