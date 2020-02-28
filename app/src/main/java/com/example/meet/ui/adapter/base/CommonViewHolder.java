package com.example.meet.ui.adapter.base;

import android.content.Context;
import android.graphics.Bitmap;
import android.text.TextUtils;
import android.util.SparseArray;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.example.meet.utils.GlideUtils;

import java.io.File;

/**
 * ViewHolder  ;
 */
public class CommonViewHolder extends RecyclerView.ViewHolder {

    //子View集合
    private SparseArray<View> mViews;
    private View mContentView;

    /**
     * 获取当前缓冲组件
     *
     * @param parent
     * @param layoutId
     * @return
     */
    public static CommonViewHolder getViewHolder(ViewGroup parent, int layoutId) {
        return new CommonViewHolder((View.inflate(parent.getContext(), layoutId, null)));
    }

    /**
     * 构造函数
     *
     * @param itemView
     */
    public CommonViewHolder(View itemView) {
        super(itemView);
        mViews = new SparseArray<>();
        mContentView = itemView;
    }

    /**
     * 子View访问
     *
     * @param viewId
     * @param <T>
     * @return
     */
    public <T extends View> T getSubView(int viewId) {
        View view = mViews.get(viewId);
        if (view == null) {
            view = mContentView.findViewById(viewId);
            mViews.put(viewId, view);
        }
        return (T) view;
    }

    /**
     * 设置文本
     *
     * @param viewId
     * @param text
     * @return
     */
    public CommonViewHolder setText(int viewId, String text) {
        TextView tv = getSubView(viewId);
        if (!TextUtils.isEmpty(text)) {
            tv.setText(text);
        }
        return this;
    }

    /**
     * 设置网络地址
     *
     * @param mContext
     * @param viewId
     * @param url
     * @return
     */
    public CommonViewHolder setImageUrl(Context mContext, int viewId,
                                        int placeholderId, String url) {
        ImageView iv = getSubView(viewId);
        if (!TextUtils.isEmpty(url)) {
            GlideUtils.loadImg(mContext, url, placeholderId, iv);


        }
        return this;
    }

    /**
     * 设置图片地址
     *
     * @param context
     * @param viewId
     * @param url
     * @return
     */
    public CommonViewHolder setImageUrl(Context context, int viewId, String url) {
        ImageView iv = getSubView(viewId);
        if (!TextUtils.isEmpty(url)) {
            GlideUtils.loadImg(context, url, iv);


        }
        return this;
    }

    /**
     * 设置本地图片
     *
     * @param mContext
     * @param viewId
     * @param path
     * @return
     */
    public CommonViewHolder setImagePath(Context mContext, int viewId, int placeholderId, String path) {
        ImageView iv = getSubView(viewId);
        if (!TextUtils.isEmpty(path)) {
            GlideUtils.loadFile(mContext, new File(path), placeholderId, iv);
        }
        return this;
    }

    /**
     * 设置本地图片
     *
     * @param viewId
     * @param resId
     * @return
     */
    public CommonViewHolder setImageResource(int viewId, int resId) {
        ImageView iv = getSubView(viewId);
        iv.setImageResource(resId);
        return this;
    }

    /**
     * 设置位图
     *
     * @param viewId
     * @param bitmap
     * @return
     */
    public CommonViewHolder setImageBitmap(int viewId, Bitmap bitmap) {
        ImageView iv = getSubView(viewId);
        iv.setImageBitmap(bitmap);
        return this;
    }

    /**
     * 设置显示隐藏
     *
     * @param viewId
     * @param visibility
     * @return
     */
    public CommonViewHolder setVisibility(int viewId, int visibility) {
        View view = getSubView(viewId);
        view.setVisibility(visibility);
        return this;
    }
}
