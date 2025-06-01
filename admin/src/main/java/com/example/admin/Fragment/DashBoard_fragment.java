package com.example.admin.Fragment;

import android.content.Intent;
import
        android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;

import com.example.admin.AdBannerActivity;
import com.example.admin.R;
import com.example.admin.ReportActivity;
import com.google.android.material.navigation.NavigationView;

public class DashBoard_fragment extends Fragment implements View.OnClickListener {

    CardView card_users, card_product, card_review, card_order, card_createad,card_report;
    private NavigationView navigationView;
    Toolbar toolbar;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.dashboard_fragment, null);

        card_users = v.findViewById(R.id.admin_card_users);
        card_product = v.findViewById(R.id.admin_card_product);
        card_review = v.findViewById(R.id.admin_card_review);
        card_order = v.findViewById(R.id.admin_card_order);
        card_createad = v.findViewById(R.id.admin_card_create_ad_banner);
        card_report=v.findViewById(R.id.admin_card_report);

        navigationView = getActivity().findViewById(R.id.admin_nav_view);
        toolbar = getActivity().findViewById(R.id.admin_home_toolbar);
        toolbar.setTitle("DashBoard");
        navigationView.setCheckedItem(R.id.nav_dashboard);

        card_users.setOnClickListener(this);
        card_product.setOnClickListener(this);
        card_order.setOnClickListener(this);
        card_review.setOnClickListener(this);
        card_createad.setOnClickListener(this);
        card_report.setOnClickListener(this);

        return v;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.admin_card_users:
                getFragmentManager().beginTransaction()
                        .addToBackStack(null)
                        .replace(R.id.admin_frame_container, new User_fragment())
                        .commit();
                toolbar.setTitle("Users");
                navigationView.setCheckedItem(R.id.nav_user);
                break;

            case R.id.admin_card_product:
                getFragmentManager().beginTransaction()
                        .addToBackStack(null)
                        .replace(R.id.admin_frame_container, new Product_Fragment())
                        .commit();
                toolbar.setTitle("Products");
                navigationView.setCheckedItem(R.id.nav_product);
                break;
            case R.id.admin_card_order:
                getFragmentManager().beginTransaction()
                        .addToBackStack(null)
                        .replace(R.id.admin_frame_container, new Order_Fragment())
                        .commit();
                navigationView.setCheckedItem(R.id.menu_none);
                toolbar.setTitle("Orders");
                break;
            case R.id.admin_card_review:
                getFragmentManager().beginTransaction()
                        .addToBackStack(null)
                        .replace(R.id.admin_frame_container, new Review_Fragment())
                        .commit();
                toolbar.setTitle("Reviews");
                navigationView.setCheckedItem(R.id.menu_none);
                break;

            case R.id.admin_card_create_ad_banner:
                startActivity(new Intent(getActivity(), AdBannerActivity.class));
                break;

            case R.id.admin_card_report:
                startActivity(new Intent(getActivity(), ReportActivity.class));
                break;
        }
    }
}
