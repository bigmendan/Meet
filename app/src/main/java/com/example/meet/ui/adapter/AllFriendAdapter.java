package com.example.meet.ui.adapter;

import android.content.Context;
import android.util.Log;
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

import java.util.ArrayList;
import java.util.List;

/**
 * 我的好友
 */
public class AllFriendAdapter extends RecyclerView.Adapter<AllFriendAdapter.ViewHolder> {


    private Context context;
    private LayoutInflater inflater;

    private List<IMUser> list = new ArrayList<>();

    private OnItemClicklistner onItemClicklistner;


    public void setOnItemClicklistner(OnItemClicklistner onItemClicklistner) {
        this.onItemClicklistner = onItemClicklistner;
    }

    public AllFriendAdapter(Context context) {
        this.context = context;
        inflater = LayoutInflater.from(context);
    }

    public void setList(List<IMUser> list) {
        this.list = list;
        notifyDataSetChanged();
    }


    public IMUser getItemAtPosition(int position) {

        return list.get(position);
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = inflater.inflate(R.layout.adapter_my_friend, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, final int position) {
        IMUser imUser = list.get(position);

        Glide.with(context).load(imUser.getHead()).into(holder.mHeaderIv);

        holder.mNickTv.setText(imUser.getNick());

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (onItemClicklistner != null) {
                    onItemClicklistner.onItemClick(position);
                }
            }
        });


    }

    @Override
    public int getItemCount() {
        return list == null ? 0 : list.size();
    }


    class ViewHolder extends RecyclerView.ViewHolder {

        ImageView mHeaderIv;
        TextView mNickTv;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            mHeaderIv = itemView.findViewById(R.id.mHeaderIv);
            mNickTv = itemView.findViewById(R.id.mNickTv);
        }
    }


    public interface OnItemClicklistner {
        void onItemClick(int position);
    }
}
