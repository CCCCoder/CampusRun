package com.n1njac.yiqipao.android.Fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.n1njac.yiqipao.android.R;

import at.markushi.ui.CircleButton;
import run.RunActivity;

/**
 * Created by huanglei on 2017/1/12.
 */

public class RunFragment extends Fragment {

    private CircleButton circleButton;



    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.run_frag,container,false);

        circleButton = (CircleButton) view.findViewById(R.id.circle_btn);

        circleButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), RunActivity.class);
                startActivity(intent);
            }
        });

        return view;
    }



}
