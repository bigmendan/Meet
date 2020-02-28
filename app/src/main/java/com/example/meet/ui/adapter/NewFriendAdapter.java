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
import com.example.framework.bmob.BMobManager;
import com.example.framework.bmob.IMUser;
import com.example.framework.db.NewFriend;
import com.example.meet.R;

import java.util.List;

import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;

/**
 * 新朋友
 */
public class NewFriendAdapter extends RecyclerView.Adapter<NewFriendAdapter.ViewHolder> {


    private Context context;
    private LayoutInflater inflater;
    private List<NewFriend> list;

    private OnItemClickListener onItemClickListener;

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

    public NewFriendAdapter(Context context, List<NewFriend> list) {
        this.context = context;
        this.list = list;
        inflater = LayoutInflater.from(context);
    }

    public void setList(List<NewFriend> list) {
        this.list = list;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = inflater.inflate(R.layout.adapter_new_friend, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, final int position) {
        NewFriend friend = list.get(position);
        if (friend != null) {
            final String userId = friend.getUserId();
            String msg = friend.getMsg();
            int agree = friend.getAgree();

            // 查询用户 ；
            BMobManager.getInstance().queryUserId(userId, new FindListener<IMUser>() {
                @Override
                public void done(List<IMUser> list, BmobException e) {
                    if (e == null) {
                        if (list != null && list.size() > 0) {
                            IMUser imUser = list.get(0);
                            if (imUser != null) {
                                Glide.with(context).load(imUser.getHead()).into(holder.mHeaderIv);
                                holder.mNickTv.setText(imUser.getNick());
                            }
                        }
                    }
                }
            });

            holder.mContentTv.setText(msg);

            String status = "";
            switch (agree) {
                case -1:        //  待确认
                    status = "等待";
                    break;
                case 0:         // 同意
                    status = "已同意";
                    break;
                case 1:         // 拒绝
                    status = "已拒绝";
                    break;
            }
            holder.mStatusTv.setText(status);

            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (onItemClickListener != null) {
                        onItemClickListener.onItemClick(position, userId);
                    }
                }
            });

            // 先暂定同意吧 ;
            if (agree == -1) {
                holder.mStatusTv.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (onItemClickListener != null) {
                            onItemClickListener.onStatus(holder,position, userId);
                        }
                    }
                });
            }

        }
    }

    @Override
    public int getItemCount() {
        return list == null ? 0 : list.size();
    }

  public   class ViewHolder extends RecyclerView.ViewHolder {
        ImageView mHeaderIv;
        TextView mNickTv;
        TextView mContentTv;
      public   TextView mStatusTv;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            mHeaderIv = itemView.findViewById(R.id.mHeaderIv);
            mNickTv = itemView.findViewById(R.id.mNickTv);
            mContentTv = itemView.findViewById(R.id.mContentTv);
            mStatusTv = itemView.findViewById(R.id.statusTv);
        }
    }

    public interface OnItemClickListener {
        void onItemClick(int position, String userId);

        void onStatus(ViewHolder holder, int position, String userId);
    }

}
