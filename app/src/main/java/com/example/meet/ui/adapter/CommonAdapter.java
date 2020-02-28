package com.example.meet.ui.adapter;

import android.view.ViewGroup;

import androidx.recyclerview.widget.RecyclerView;

import com.example.meet.ui.adapter.base.CommonViewHolder;

import java.util.List;

/**
 * 万能适配器 ;
 *
 * @param <T>
 */
public class CommonAdapter<T> extends RecyclerView.Adapter<CommonViewHolder> {

    private List<T> mData;

    private OnBindDataInterface mOnBindDataInterface;
    private OnMultiTypeBindDataInterface<T> mOnMultiTypeBindDataInterface;

    public CommonAdapter(List<T> mData, OnBindDataInterface mOnBindDataInterface) {
        this.mData = mData;
        this.mOnBindDataInterface = mOnBindDataInterface;
    }

    public CommonAdapter(List<T> mData, OnMultiTypeBindDataInterface<T> mOnMultiTypeBindDataInterface) {
        this.mData = mData;
        this.mOnBindDataInterface = mOnMultiTypeBindDataInterface;
        this.mOnMultiTypeBindDataInterface = mOnMultiTypeBindDataInterface;
    }

    @Override
    public CommonViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        int layoutId = mOnBindDataInterface.getItemLayoutId(i);
        CommonViewHolder viewHolder = CommonViewHolder.getViewHolder(viewGroup, layoutId);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(CommonViewHolder commonViewHolder, int i) {
        mOnBindDataInterface.onBindData(mData.get(i), commonViewHolder, getItemViewType(i), i);
    }

    @Override
    public int getItemViewType(int position) {
        if (mOnMultiTypeBindDataInterface != null) {
            return mOnMultiTypeBindDataInterface.getItemViewType(position);
        }
        return 0;
    }

    @Override
    public int getItemCount() {
        return mData == null ? 0 : mData.size();
    }

    public interface OnBindDataInterface<T> {

        void onBindData(T model, CommonViewHolder holder, int type, int position);

        int getItemLayoutId(int viewType);
    }

    /**
     * 多类型支持
     *
     * @param <T>
     */
    public interface OnMultiTypeBindDataInterface<T> extends OnBindDataInterface<T> {

        int getItemViewType(int position);
    }
}
