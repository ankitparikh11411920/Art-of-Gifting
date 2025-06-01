package com.example.artofgifting;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.Handler;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

public class SplashActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
    }

    @Override
    protected void onStart() {
        super.onStart();
        if(isNetworkConnectionAvailable()){
            startAnimation();
        }
        else {
            onPause();
            startActivity(new Intent(SplashActivity.this,InternetConnectionActivity.class));
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    public boolean isNetworkConnectionAvailable() {
        ConnectivityManager cm =
                (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);

        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        boolean isConnected = activeNetwork != null &&
                activeNetwork.isConnected();
        if (isConnected) {
            return true;
        } else {
            return false;
        }
    }


    private void startAnimation() {
        Animation anim = AnimationUtils.loadAnimation(this, R.anim.alpha);
        anim.reset();
        anim = AnimationUtils.loadAnimation(this, R.anim.translate);
        anim.reset();
        ImageView imageView = findViewById(R.id.user_imageview_applogo_splash_activity);
        imageView.clearAnimation();
        imageView.startAnimation(anim);
        final TextView textView = findViewById(R.id.user_tv_appname_splash_activity);
        textView.setText("ART OF GIFTING");
        anim = AnimationUtils.loadAnimation(this, R.anim.alpha);
        textView.startAnimation(anim);
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                startActivity(new Intent(SplashActivity.this, LoginActivity.class));
                finish();
            }
        }, 2000);
    }
}
