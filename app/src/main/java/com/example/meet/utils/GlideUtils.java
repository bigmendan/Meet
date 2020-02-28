package com.example.meet.utils;

import android.content.Context;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.util.Util;

import java.io.File;

public class GlideUtils {

    /**
     * 标准的图片加载函数
     *
     * @param mContext
     * @param imgUrl
     * @param resId
     * @param mImageView
     */
    public static void loadImg(Context mContext, String imgUrl,
                               int resId, ImageView mImageView) {
        if (Util.isOnMainThread()) {
            Glide.with(mContext)
                    .load(imgUrl)
                    .dontAnimate()
                    .placeholder(resId)
                    .diskCacheStrategy(DiskCacheStrategy.RESOURCE)
                    .into(mImageView);
        }
    }

    /**
     * 不要占位图
     *
     * @param mContext
     * @param imgUrl
     * @param mImageView
     */
    public static void loadImg(Context mContext, String imgUrl,
                               ImageView mImageView) {
        if (Util.isOnMainThread()) {
            Glide.with(mContext)
                    .load(imgUrl)
                    .dontAnimate()
                    .diskCacheStrategy(DiskCacheStrategy.RESOURCE)
                    .into(mImageView);
        }
    }


    /**
     * 加载文件图片
     *
     * @param mContext
     * @param imgFile
     * @param mImageView
     */
    public static void loadFile(Context mContext, File imgFile, int resId, ImageView mImageView) {
        try {
            if (Util.isOnMainThread()) {
                Glide.with(mContext).load(imgFile).dontAnimate().placeholder(resId).into(mImageView);
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
}
