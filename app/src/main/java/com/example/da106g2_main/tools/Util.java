package com.example.da106g2_main.tools;

import android.app.Activity;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

public class Util {

    //連線對象
    public final static String URL = "http://10.0.2.2:8081/DA106G2/";

    public static boolean networkConnected(Activity activity) {
        ConnectivityManager connectivityManager = (ConnectivityManager) activity.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = ((connectivityManager != null) ? (connectivityManager.getActiveNetworkInfo()) : null);
        return ((networkInfo != null) && (networkInfo.isConnected()));
    }
}
