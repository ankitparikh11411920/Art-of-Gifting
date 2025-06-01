package com.example.admin;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

public class MonthlyReportsActivity extends AppCompatActivity implements View.OnClickListener{

    TextView Jan,Feb,Mar,Apr,May,Jun,Jul,Aug,Sept,Oct,Nov,Dec;
    Toolbar toolbar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_monthly_reports);
        inittoolbar();
        Jan=findViewById(R.id.tv_jan);
        Feb=findViewById(R.id.tv_feb);
        Mar=findViewById(R.id.tv_mar);
        Apr=findViewById(R.id.tv_apr);
        May=findViewById(R.id.tv_may);
        Jun=findViewById(R.id.tv_jun);
        Jul=findViewById(R.id.tv_jul);
        Aug=findViewById(R.id.tv_aug);
        Sept=findViewById(R.id.tv_sep);
        Oct=findViewById(R.id.tv_oct);
        Nov=findViewById(R.id.tv_nov);
        Dec=findViewById(R.id.tv_dec);

        Jan.setOnClickListener(this);
        Feb.setOnClickListener(this);
        Mar.setOnClickListener(this);
        Apr.setOnClickListener(this);
        May.setOnClickListener(this);
        Jun.setOnClickListener(this);
        Jul.setOnClickListener(this);
        Aug.setOnClickListener(this);
        Sept.setOnClickListener(this);
        Oct.setOnClickListener(this);
        Nov.setOnClickListener(this);
        Dec.setOnClickListener(this);
    }

    private void inittoolbar() {
        toolbar = findViewById(R.id.admin_monthly_reports_toolbar);
        setSupportActionBar(toolbar);
        toolbar.setTitle("Monthly Reports");
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

        switch (v.getId()){
            case R.id.tv_jan:
                startActivity(new Intent(MonthlyReportsActivity.this,GivenMonthActivity.class)
                .putExtra("month",1));

                break;

            case R.id.tv_feb:
                startActivity(new Intent(MonthlyReportsActivity.this,GivenMonthActivity.class)
                        .putExtra("month",2));
                break;
            case R.id.tv_mar:
                startActivity(new Intent(MonthlyReportsActivity.this,GivenMonthActivity.class)
                        .putExtra("month",3));
                break;
            case R.id.tv_apr:
                startActivity(new Intent(MonthlyReportsActivity.this,GivenMonthActivity.class)
                        .putExtra("month",4));
                break;
            case R.id.tv_may:
                startActivity(new Intent(MonthlyReportsActivity.this,GivenMonthActivity.class)
                        .putExtra("month",5));
                break;
            case R.id.tv_jun:
                startActivity(new Intent(MonthlyReportsActivity.this,GivenMonthActivity.class)
                        .putExtra("month",6));
                break;
            case R.id.tv_jul:
                startActivity(new Intent(MonthlyReportsActivity.this,GivenMonthActivity.class)
                        .putExtra("month",7));
                break;
            case R.id.tv_aug:
                startActivity(new Intent(MonthlyReportsActivity.this,GivenMonthActivity.class)
                        .putExtra("month",8));
                break;

            case R.id.tv_sep:
                startActivity(new Intent(MonthlyReportsActivity.this,GivenMonthActivity.class)
                        .putExtra("month",9));
                break;
            case R.id.tv_oct:
                startActivity(new Intent(MonthlyReportsActivity.this,GivenMonthActivity.class)
                        .putExtra("month",10));
                break;
            case R.id.tv_nov:
                startActivity(new Intent(MonthlyReportsActivity.this,GivenMonthActivity.class)
                        .putExtra("month",11));
                break;
            case R.id.tv_dec:
                startActivity(new Intent(MonthlyReportsActivity.this,GivenMonthActivity.class)
                        .putExtra("month",12));
                break;
        }
    }
}