package com.example.meet.ui.fragments;


import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.framework.bmob.BMobManager;
import com.example.framework.bmob.IMUser;
import com.example.framework.constant.Constant;
import com.example.meet.R;
import com.example.meet.ui.NewFriendActivity;
import com.example.meet.utils.SpUtils;

/**
 * A simple {@link Fragment} subclass.
 */
public class MyFragment extends Fragment implements View.OnClickListener {


    private ImageView mHeaderIv;
    private TextView mNickTv;


    private TextView mNewFriendTv;

    public MyFragment() {
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_my, container, false);
        initWidgets(view);
        return view;
    }


    private void initWidgets(View v) {
        mHeaderIv = v.findViewById(R.id.mHeaderIv);
        mNickTv = v.findViewById(R.id.mNickTv);

        mNewFriendTv = v.findViewById(R.id.mNewFriendTv);
        mNewFriendTv.setOnClickListener(this);

        Boolean isLogin = SpUtils.getInstance().getBoolean(Constant.IS_LOGIN);

        if (isLogin) {
            IMUser user = BMobManager.getInstance().getUser();
            if (user != null) {
                Glide.with(this).load(user.getHead()).into(mHeaderIv);
                mNickTv.setText(user.getNick());

            }
        }

    }

    @Override
    public void onClick(View v) {
        Intent intent = new Intent();
        switch (v.getId()) {
            case R.id.mNewFriendTv:
                intent.setClass(getContext(), NewFriendActivity.class);
                break;
        }

        startActivity(intent);
    }
}
