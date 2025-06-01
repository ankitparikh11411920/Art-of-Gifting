package com.example.artofgifting.Helper;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.Log;

import com.example.artofgifting.HomeActivity;
import com.example.artofgifting.InternetConnectionActivity;

public class NetworkHelper {

    Context context;

    public NetworkHelper(Context context) {
        this.context = context;
        isNetworkConnectionAvailable();
    }

    public boolean isNetworkConnectionAvailable() {
        ConnectivityManager cm =
                (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);

        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        boolean isConnected = activeNetwork != null &&
                activeNetwork.isConnected();
        if (isConnected) {
            Log.d("Network", "Connected");
            return true;
        } else {
            Intent intent = new Intent(context, InternetConnectionActivity.class);
            context.startActivity(intent);
            Log.d("Network", "Not Connected");
            return false;
        }
    }
}
