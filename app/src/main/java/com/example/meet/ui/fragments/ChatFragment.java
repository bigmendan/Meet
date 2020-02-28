package com.example.meet.ui.fragments;


import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.fragment.app.FragmentStatePagerAdapter;
import androidx.viewpager.widget.ViewPager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.meet.R;
import com.example.meet.ui.adapter.VpAdapter;
import com.google.android.material.tabs.TabLayout;

import java.util.ArrayList;

/**
 *
 */
public class ChatFragment extends Fragment {


    private TabLayout mTabLayout;
    private ViewPager mVp;

    private VpAdapter vpAdapter;

    private ArrayList<Fragment> fragments = new ArrayList<>();


    private ChatRecordFragment chatRecordFragment;
    private CallRecordFragment callRecordFragment;
    private AllFriendFragment allFriendFragment;

    public ChatFragment() {
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_chat, container, false);
        initWidgets(view);
        return view;
    }


    private void initWidgets(View v) {
        mTabLayout = v.findViewById(R.id.mTabLayout);
        mVp = v.findViewById(R.id.mVp);


        chatRecordFragment = new ChatRecordFragment();
        callRecordFragment = new CallRecordFragment();
        allFriendFragment = new AllFriendFragment();

        fragments.add(chatRecordFragment);
        fragments.add(callRecordFragment);
        fragments.add(allFriendFragment);

        vpAdapter = new VpAdapter(getActivity().getSupportFragmentManager(),
                1, fragments);

        mVp.setAdapter(vpAdapter);
        mTabLayout.setupWithViewPager(mVp);


    }


}
