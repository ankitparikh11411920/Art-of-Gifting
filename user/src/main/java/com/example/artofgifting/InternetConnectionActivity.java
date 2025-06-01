package com.example.artofgifting;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.muddzdev.styleabletoastlibrary.StyleableToast;

public class InternetConnectionActivity extends AppCompatActivity implements View.OnClickListener {
    Button btnretry;
    private volatile boolean connection=false;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_internet_connection);
        btnretry = findViewById(R.id.btn_tryagain);
        btnretry.setOnClickListener(this);
        ConnectionThread connectionThread=new ConnectionThread();
        connectionThread.start();
    }

    class ConnectionThread extends Thread{
        @Override
        public void run() {
            if(connection){
                finish();
            }else {
                isNetworkConnectionAvailable();
                checkconnection();
            }
        }
    }



    private void checkconnection() {
        ConnectionThread connectionThread=new ConnectionThread();

        if(connection){
            finish();
        }else {
            isNetworkConnectionAvailable();
            connectionThread.start();
        }
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_tryagain: {
                if (connection) {
                    finish();
                } else {
                    StyleableToast.makeText(InternetConnectionActivity.this, "Please connect with Internet or try again later", R.style.exampletoast).show();

                }
            }
            break;
        }
    }

    public void isNetworkConnectionAvailable() {
        ConnectivityManager cm =
                (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);

        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        boolean isConnected = activeNetwork != null &&
                activeNetwork.isConnected();
        if (isConnected) {
             connection=true;
        } else {
             connection=false;
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finishAffinity();
    }
}
