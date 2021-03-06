package com.example.da106g2_main.tools;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.ImageView;

import com.google.gson.JsonObject;

import java.io.BufferedInputStream;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.lang.ref.WeakReference;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class ImageTask extends AsyncTask<Object, Integer, Bitmap> {

    //通用來源參數
    public final static short FROM_LIVE = 0;
    public final static short FROM_ITEM = 1;
    public final static short FROM_ITEM_CONTENT = 2;
    public final static short FROM_DIARY = 3;


    private final static String TAG = "ImageTask";
    private String url, target;//連線位子、取得哪一個
    private int size;//檔案大小
    private short source;//請求來源

    //弱引用，所裝的View
    private WeakReference<ImageView> imageViewWeakReference;

    //基本創立時需參數(連線位子、請求來源、取得哪一個、檔案大小、傳送類別、所裝的View)
    public ImageTask(String url, short source, String target, int size, ImageView imageView) {
        this.url = url;
        this.source = source;
        this.target = target;
        this.size = size;
        this.imageViewWeakReference = new WeakReference<>(imageView);
    }

    //如果單純只下載的話就不用view
    public ImageTask(String url, short source, String target, int size) {
        this(url, source, target, size, null);
    }

    //取得類別參數
    private String getSendCategory() {
        switch (source) {
            case FROM_LIVE:
            case FROM_ITEM:
            case FROM_DIARY:
                return "getOnePicture";

            case FROM_ITEM_CONTENT:
                return "getOnePictureContent";
        }
        //如都不是時
        return "";
    }

    //取得來源參數
    private String getSendSource() {
        switch (source) {
            case FROM_LIVE:
                return "live_id";

            case FROM_ITEM:
            case FROM_ITEM_CONTENT:
                return "item_id";
            case FROM_DIARY:
                return "diary_id";
        }
        return "";
    }

    //背景執行
    @Override
    protected Bitmap doInBackground(Object... objects) {
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("action", getSendCategory());
        jsonObject.addProperty(getSendSource(), target);
        jsonObject.addProperty("imageSize", size);
        if (isCancelled()) {
            return null;
        }
        return getRemoteImage(url, jsonObject.toString());
    }

    //背景執行詳細方法
    private Bitmap getRemoteImage(String url, String jsonOut) {
//        byte[] img = null;
        Bitmap bitmap = null;
        try {
            HttpURLConnection con = (HttpURLConnection) new URL(url).openConnection();
            try (AutoCloseable conc = () -> con.disconnect();) {
                con.setDoInput(true);
                con.setDoOutput(true);
                con.setUseCaches(false);
                con.setRequestMethod("POST");

                try (BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(con.getOutputStream()))) {
                    bw.write(jsonOut);
                    Log.d(TAG, "output: " + jsonOut);
                }

                if (isCancelled()) {
                    return null;
                }

                //接收處理
                int responseCode = con.getResponseCode();

                if (responseCode == HttpURLConnection.HTTP_OK) {
                    if (isCancelled()) {
                        return null;
                    }
                    //使用圖形工廠的decodeStream將資料IN出來
                    bitmap = BitmapFactory.decodeStream(new BufferedInputStream(con.getInputStream()));

//                    try (BufferedInputStream bIn = new BufferedInputStream(con.getInputStream())) {
//                        img = new byte[con.getContentLength()];
//                        byte[] img_ = new byte[1];
//                        int i = 0;
//                        while (bIn.read(img_) != -1) {
//                            img[i] = img_[0];
//                            i++;
//                        }
//
//                        Log.d(TAG, "讀取量： " + bIn.available());
//                        Log.d(TAG, "imgSize: " + img.length);
//                    }

                } else {
                    Log.d(TAG, "response code: " + responseCode);
                }

            }
        } catch (MalformedURLException e) {
            Log.d(TAG, "格式錯誤：" + e.toString());
        } catch (IOException e) {
            Log.d(TAG, "IO錯誤：" + e.toString());
        } catch (Exception e) {
            Log.d(TAG, "倒大楣啦！錯誤：" + e.toString());
        }

//        return img;
        return bitmap;
    }

    //背景執行完後
    @Override
    protected void onPostExecute(Bitmap img) {
        super.onPostExecute(img);
        ImageView imageView = imageViewWeakReference.get();
        if (isCancelled() || imageView == null || img == null || img.getHeight() <= 0) {
            return;
        }
        imageView.setImageBitmap(img);
    }

}
