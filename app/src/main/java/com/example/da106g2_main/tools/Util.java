package com.example.da106g2_main.tools;

import android.app.Activity;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.widget.Toast;

import com.example.da106g2_main.mall.OrderItem;

import java.util.ArrayList;

public class Util {

    //連線對象
    public final static String URL = "http://10.0.2.2:8081/DA106G2/"; // 模擬器用
//    public final static String URL = "http://192.168.0.185:8081/DA106G2/"; // 實機用

    //購物車
    public static ArrayList<OrderItem> CART = new ArrayList<>();

    //測試連線用
    public static boolean networkConnected(Activity activity) {
        ConnectivityManager connectivityManager = (ConnectivityManager) activity.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = ((connectivityManager != null) ? (connectivityManager.getActiveNetworkInfo()) : null);
        return ((networkInfo != null) && (networkInfo.isConnected()));
    }

    //吐司
    public static void showToast(Context context, int messageResId) {
        Toast.makeText(context, messageResId, Toast.LENGTH_SHORT).show();
    }

    public static void showToast(Context context, String message) {
        Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
    }
}
