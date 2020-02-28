package com.example.meet.ui.fragments;


import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.framework.manager.RongCloudManager;
import com.example.meet.R;

import java.util.List;

import io.rong.imlib.RongIMClient;
import io.rong.imlib.model.Conversation;

/**
 * 通话记录
 */
public class CallRecordFragment extends Fragment {

    private String TAG = "== CallReport Fragment = ";

    private TextView noView;
    private RecyclerView mRecyclerView;

    public CallRecordFragment() {
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View v = inflater.inflate(R.layout.fragment_call_record, container, false);

        initWidgets(v);
        return v;
    }

    @Override
    public void onStart() {
        super.onStart();

//        getConversationList();
    }

    private void initWidgets(View v) {
        noView = v.findViewById(R.id.noView);
        mRecyclerView = v.findViewById(R.id.mRecyclerView);
    }


    /**
     * 获取本地会话列表
     *
     * @param
     */
    private void getConversationList() {
        RongCloudManager.getInstance().getConversationList(new RongIMClient.ResultCallback<List<Conversation>>() {
            @Override
            public void onSuccess(List<Conversation> conversations) {

                Log.e(TAG, "查询会话列表onSuccess: ");
            }

            @Override
            public void onError(RongIMClient.ErrorCode errorCode) {
                Log.e(TAG, "查询会话列表 onError: " + errorCode);
            }
        });
    }

}
