package com.example.meet.ui.fragments;


import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.framework.bmob.BMobManager;
import com.example.framework.bmob.Friend;
import com.example.framework.bmob.IMUser;
import com.example.framework.constant.Constant;
import com.example.meet.R;
import com.example.meet.ui.UserInfoActivity;
import com.example.meet.ui.adapter.AllFriendAdapter;

import java.util.ArrayList;
import java.util.List;

import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;
import cn.bmob.v3.listener.QueryListener;

/**
 * 全部联系人
 */
public class AllFriendFragment extends Fragment {

    private String TAG = "==AllFriend";

    private TextView noView;
    private RecyclerView mRecyclerView;


    private AllFriendAdapter adapter;
    private List<IMUser> mList = new ArrayList<>();

    public AllFriendFragment() {
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_all_friend, container, false);
        initWidgets(view);
        return view;
    }


    private void initWidgets(View v) {
        noView = v.findViewById(R.id.noView);
        mRecyclerView = v.findViewById(R.id.mRecyclerView);

        mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        adapter = new AllFriendAdapter(getActivity());

        mRecyclerView.setAdapter(adapter);


        adapter.setOnItemClicklistner(new AllFriendAdapter.OnItemClicklistner() {
            @Override
            public void onItemClick(int position) {
                IMUser imuser = adapter.getItemAtPosition(position);

                Intent intent = new Intent();
                intent.setClass(getContext(), UserInfoActivity.class);
                intent.putExtra(Constant.USER_ID, imuser.getObjectId());

                startActivity(intent);
            }
        });


        queryAllFriend();

    }


    /**
     * 查询我的好友
     */
    private void queryAllFriend() {


        BMobManager.getInstance().queryMyFriend(new FindListener<Friend>() {
            @Override
            public void done(List<Friend> list, BmobException e) {
                if (e == null) {
                    mList.clear();
                    if (list != null && list.size() > 0) {
                        for (int i = 0; i < list.size(); i++) {
                            Friend friend = list.get(i);
                            String userId = friend.getFriendUser().getObjectId();

                            // 通过id 查找 用户；
                            if (!TextUtils.isEmpty(userId)) {
                                BMobManager.getInstance().queryUserId(userId, new FindListener<IMUser>() {
                                    @Override
                                    public void done(List<IMUser> list, BmobException e) {
                                        if (e == null) {

                                            IMUser imUser = list.get(0);
                                            mList.add(imUser);
                                            adapter.setList(mList);
                                        }
                                    }
                                });
                            }
                        }


                    } else {
                        noView.setVisibility(View.VISIBLE);
                        mRecyclerView.setVisibility(View.GONE);
                    }
                } else {
                    Log.e(TAG, " 查找好友  失败: " + e.getErrorCode());
                }
            }
        });
    }

}
