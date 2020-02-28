package com.example.meet.ui.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.framework.bmob.IMUser;
import com.example.meet.R;

import java.util.List;

/**
 * 添加好友 adapter
 */
public class AddFriendAdapter extends RecyclerView.Adapter<AddFriendAdapter.ViewHolder> {

    private Context mContext;
    private LayoutInflater inflater;
    private List<IMUser> list;

    private OnItemClickListener onItemClickListener;

    public AddFriendAdapter(Context mContext) {
        this.mContext = mContext;

        inflater = LayoutInflater.from(mContext);
    }

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

    public void setList(List<IMUser> list) {
        this.list = list;
        notifyDataSetChanged();
    }

    public IMUser getItemPosition(int position) {
        return list.size() == 0 ? null : list.get(position);
    }


    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.adapter_add_friend, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, final int position) {

        IMUser imUser = list.get(position);

        Glide.with(mContext).load(imUser.getHead()).into(holder.mHeaderIV);

        holder.mNickTv.setText(imUser.getNick());

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (onItemClickListener != null) {
                    onItemClickListener.onClick(position);
                }
            }
        });


    }

    @Override
    public int getItemCount() {
        return list == null ? 0 : list.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        ImageView mHeaderIV;
        TextView mNickTv;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            mHeaderIV = itemView.findViewById(R.id.mHeaderIv);
            mNickTv = itemView.findViewById(R.id.mNickTv);
        }


    }

    /**
     * 点击事件
     */
    public interface OnItemClickListener {
        void onClick(int position);
    }


}
