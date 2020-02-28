package com.example.meet.utils;

import android.content.Context;
import android.content.SharedPreferences;

import com.example.meet.app.MeetApplication;

/**
 * simple  Sputils;
 */
public class SpUtils {

    private static Context context;
    private static SpUtils spUtils;
    private SharedPreferences sp;
    private SharedPreferences.Editor edit;

    private SpUtils() {
        sp = context.getSharedPreferences("", Context.MODE_PRIVATE);
        edit = sp.edit();

    }


    public static SpUtils getInstance() {
        context = MeetApplication.getContext();

        if (spUtils == null) {
            synchronized (SpUtils.class) {
                if (spUtils == null) {
                    spUtils = new SpUtils();
                }
            }
        }
        return spUtils;
    }

    public void putInt(String key, int i) {
        edit.putInt(key, i).apply();
    }

    public int getInt(String key) {
        return sp.getInt(key, -1);
    }

    public void putString(String key, String value) {
        edit.putString(key, value).apply();
    }

    public String getString(String key) {
        return sp.getString(key, "");
    }

    public void putBoolean(String key, boolean value) {
        edit.putBoolean(key, value).apply();
    }


    public Boolean getBoolean(String key) {
        return sp.getBoolean(key, false);
    }

}
