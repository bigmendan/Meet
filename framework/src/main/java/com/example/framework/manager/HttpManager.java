package com.example.framework.manager;

import com.example.framework.utils.SHA1;

import java.io.IOException;
import java.util.HashMap;

import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;

public class HttpManager {

    // 获取 CloudToken Url                           api2-cn.ronghub.com
    private static final String CLOUD_URL = "http://api-cn.ronghub.com/user/getToken.json";

    // 随机数
    private String Nonce = String.valueOf(Math.floor(Math.random() * 100000));
    // 时间戳 单位 s
    private String Timestamp = String.valueOf(System.currentTimeMillis() / 1000);

    // SHA1
    private String Signature = SHA1.sha1(RongCloudManager.RONG_CLOUD_APP_SECRET + Nonce + Timestamp);

    private OkHttpClient mOkHttpCLient;
    private static HttpManager INSTANCE = null;

    private HttpManager() {

        mOkHttpCLient = new OkHttpClient();
    }


    public static HttpManager getInstance() {
        if (INSTANCE == null) {
            synchronized (HttpManager.class) {
                INSTANCE = new HttpManager();
            }

        }
        return INSTANCE;
    }


    /**
     * 获取融云 Token ;
     */
    public String getRongCloudToken(HashMap<String, String> map) {
        FormBody.Builder builder = new FormBody.Builder();
        for (String key : map.keySet()) {
            builder.add(key, map.get(key));
        }

        RequestBody requestBody = builder.build();

        Request request = new Request.Builder()
                .url("http://api-cn.ronghub.com/user/getToken.json")
                .addHeader("App-Key", RongCloudManager.RONG_CLOUD_APP_KEY)
                .addHeader("Nonce", Nonce)
                .addHeader("Timestamp", Timestamp)
                .addHeader("Signature", Signature)
                .addHeader("Content-Type", "application/x-www-form-urlencoded")
                .post(requestBody)
                .build();



        try {
            return mOkHttpCLient.newCall(request).execute().body().string();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return "";

    }

}