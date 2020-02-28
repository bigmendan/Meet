package com.example.framework.utils;

import android.util.Log;

import java.text.SimpleDateFormat;

public class TimeUtils {

    private static String TAG = "==TimeUtils";

    /**
     * 把 ms 格式化  hh : mm :ss
     * 1s = 1000ms
     * 1m = 60s
     * 1h = 60m
     * 1d = 24h
     *
     * @param ms
     * @return
     */
    public static String formatDuring(long ms) {

        long second = (ms % (1000 * 60)) / 1000;

        long minute = (ms % (1000 * 60 * 60)) / (1000 * 60);

        long hours = (ms % (1000 * 60 * 60 * 24)) / (1000 * 60 * 60);

        String h = hours + "";
        if (hours < 10) {
            h = "0" + hours;
        }

        String m = minute + "";
        if (minute < 10) {
            m = "0" + minute;
        }

        String s = second + "";
        if (second < 0) {
            s = "0" + second;
        }

        return h + ":" + m + ":" + s;
    }
}
