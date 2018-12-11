package com.example.android.kmovies.utils;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.example.android.kmovies.R;

public class Utils {

    public static boolean checkInternet(Context context, TextView textView, RecyclerView recyclerView) {
        ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
        if (networkInfo != null && networkInfo.isConnectedOrConnecting()) {
            textView.setVisibility(View.GONE);
            recyclerView.setVisibility(View.VISIBLE);
            return true;
        } else {
            textView.setText(context.getResources().getString(R.string.check_internet));
            textView.setVisibility(View.VISIBLE);
            recyclerView.setVisibility(View.GONE);
            return false;
        }
    }

    public static boolean checkInternet(Context context) {
        ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
        return (networkInfo != null && networkInfo.isConnectedOrConnecting());

    }

    public static boolean checkApi(Context context, TextView textView, RecyclerView recyclerView) {
        if (context.getResources().getString(R.string.api_key).isEmpty()) {
            recyclerView.setVisibility(View.GONE);
            textView.setVisibility(View.VISIBLE);
            textView.setText(context.getResources().getString(R.string.check_api));
            return false;
        } else {
            textView.setVisibility(View.GONE);
            recyclerView.setVisibility(View.VISIBLE);
            return true;
        }
    }

    public static boolean checkApi(Context context) {
        return !(context.getResources().getString(R.string.api_key).isEmpty());

    }
}
