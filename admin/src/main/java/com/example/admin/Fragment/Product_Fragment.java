package com.example.admin.Fragment;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.admin.AddNewCategoryActivity;
import com.example.admin.AddSubCategoryActivity;
import com.example.admin.Add_Product;
import com.example.admin.R;
import com.example.admin.ShowAllProductsActivity;
import com.example.admin.ViewCategoryActivity;

public class Product_Fragment extends Fragment {

    LinearLayout viewallproducts;
    LinearLayout addnewcategory;
    LinearLayout addsubcategory;
    LinearLayout addnewproduct;
    LinearLayout viewcategory,viewsubcategory;

    @Nullable
    @Override

    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View v = inflater.inflate(R.layout.product_fragment, container, false);
        viewallproducts = v.findViewById(R.id.admin_linear_btn_view_all);
        addnewcategory = v.findViewById(R.id.admin_linear_btn_add_category);
        addsubcategory = v.findViewById(R.id.admin_linear_btn_add_sub_category);
        addnewproduct=v.findViewById(R.id.admin_linear_btn_add_new_product);
        viewcategory=v.findViewById(R.id.admin_linear_btn_view_category);
        viewsubcategory=v.findViewById(R.id.admin_linear_btn_view_sub_category);

        viewallproducts.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getActivity(), ShowAllProductsActivity.class));
            }
        });
        addnewcategory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getActivity(), AddNewCategoryActivity.class));
            }
        });
        addsubcategory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getActivity(), AddSubCategoryActivity.class));
            }
        });
        addnewproduct.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getActivity(), Add_Product.class));
            }
        });

        viewcategory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getActivity(), ViewCategoryActivity.class));
            }
        });

        return v;
    }
}

