package com.example.meet.ui.fragments;


import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.example.meet.R;
import com.example.meet.ui.AddFriendActivity;

/**
 *
 */
public class HomeFragment extends Fragment implements View.OnClickListener {

    private ImageView addFirIv;

    public HomeFragment() {
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_home, container, false);

        initWidgets(view);
        return view;
    }


    private void initWidgets(View view) {
        addFirIv = view.findViewById(R.id.mAddFir);
        addFirIv.setOnClickListener(this);

    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.mAddFir:
                startActivity(new Intent(getContext(), AddFriendActivity.class));
                break;
        }

    }
}
