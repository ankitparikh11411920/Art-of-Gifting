package com.example.admin;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;

public class ReportActivity extends AppCompatActivity implements View.OnClickListener {

    Toolbar toolbar;
    LinearLayout linear_btn_users_most_buys, linear_btn_out_of_stock, linear_btn_about_to_sold_out ;
    LinearLayout linear_btn_monthly_reports,quarterly_reports,yearly_reports;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_report);
        inittoolbar();
        linear_btn_about_to_sold_out = findViewById(R.id.admin_linear_btn_about_to_sold_out);
        linear_btn_users_most_buys = findViewById(R.id.admin_linear_btn_users_with_most_buys);
        linear_btn_out_of_stock = findViewById(R.id.admin_linear_btn_products_out_of_stock);
        linear_btn_monthly_reports=findViewById(R.id.admin_linear_btn_monthly_reports);
        yearly_reports=findViewById(R.id.admin_linear_btn_yearly_reports);
        quarterly_reports=findViewById(R.id.admin_linear_btn_quarterly_reports);


        linear_btn_about_to_sold_out.setOnClickListener(this);
        linear_btn_out_of_stock.setOnClickListener(this);
        linear_btn_users_most_buys.setOnClickListener(this);
        quarterly_reports.setOnClickListener(this);
        yearly_reports.setOnClickListener(this);
        linear_btn_monthly_reports.setOnClickListener(this);
    }

    private void inittoolbar() {
        toolbar = findViewById(R.id.admin_report_toolbar);
        setSupportActionBar(toolbar);
        toolbar.setTitle("Reports");
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {

            case R.id.admin_linear_btn_about_to_sold_out:
                startActivity(new Intent(ReportActivity.this, ProductsWithAlmostOutOfStockActivity.class));
                break;


            case R.id.admin_linear_btn_products_out_of_stock:
                startActivity(new Intent(ReportActivity.this, ProductsOutOfStockActivity.class));
                break;

            case R.id.admin_linear_btn_users_with_most_buys:
                startActivity(new Intent(ReportActivity.this, UsersWithMostBuysActivity.class));
                break;

            case R.id.admin_linear_btn_monthly_reports:
                startActivity(new Intent(ReportActivity.this,MonthlyReportsActivity.class));
                break;

            case R.id.admin_linear_btn_quarterly_reports:
                startActivity(new Intent(ReportActivity.this,QuartelyReportsActivity.class));
                break;

            case R.id.admin_linear_btn_yearly_reports:
                startActivity(new Intent(ReportActivity.this,YearlyReportsActivity.class));
                break;
        }
    }
}
