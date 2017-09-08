package com.n1njac.yiqipao.android.ui.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.bumptech.glide.Glide;
import com.n1njac.yiqipao.android.R;

import at.markushi.ui.CircleButton;

import com.n1njac.yiqipao.android.run.trackshow.RunMainActivity;

/**
 * Created by huanglei on 2017/1/12.
 */

public class RunFragment extends Fragment {

    private CircleButton circleButton;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.run_frag,container,false);

        ImageView bg = (ImageView) view.findViewById(R.id.bg_run_frag);

        Glide.with(getActivity()).load(R.drawable.run_bg).centerCrop().into(bg);

        circleButton = (CircleButton) view.findViewById(R.id.circle_btn);
        circleButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), RunMainActivity.class);
                startActivity(intent);
            }
        });

        return view;
    }



}
