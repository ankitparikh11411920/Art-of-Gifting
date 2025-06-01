package com.example.admin;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

public class QuartelyReportsActivity extends AppCompatActivity {

    Toolbar toolbar;
    TextView q1, q2, q3, q4;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quartely_reports);
        q1 = findViewById(R.id.tv_q1);
        q2 = findViewById(R.id.tv_q2);
        q3 = findViewById(R.id.tv_q3);
        q4 = findViewById(R.id.tv_q4);
        inittoolbar();

        q1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                startActivity(new Intent(QuartelyReportsActivity.this, GivenQuarterActivity.class)
                        .putExtra("quarter", "q1"));

            }
        });
        q2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(QuartelyReportsActivity.this, GivenQuarterActivity.class)
                        .putExtra("quarter", "q2"));
            }
        });
        q3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(QuartelyReportsActivity.this, GivenQuarterActivity.class)
                        .putExtra("quarter", "q3"));
            }
        });
        q4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(QuartelyReportsActivity.this, GivenQuarterActivity.class)
                        .putExtra("quarter", "q4"));
            }
        });
    }

    private void inittoolbar() {
        toolbar = findViewById(R.id.admin_quarterly_reports_toolbar);
        setSupportActionBar(toolbar);
        toolbar.setTitle("Quarterly Reports");
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
    }
}