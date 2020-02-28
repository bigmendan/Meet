package com.example.framework.utils;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class SHA1 {

    /**
     * 融云的 sha1算法
     *
     * @param data
     */
    public static String sha1(String data) {
        StringBuffer sb = new StringBuffer();

        try {
            MessageDigest md = MessageDigest.getInstance("SHA1");

            md.update(data.getBytes());
            byte[] bytes = md.digest();

            for (int i = 0; i < bytes.length; i++) {
                int a = bytes[i];
                if (a < 0) a += 256;
                if (a < 16) sb.append("0");

                sb.append(Integer.toHexString(a));
            }

        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }

        return  sb.toString();

    }
}
