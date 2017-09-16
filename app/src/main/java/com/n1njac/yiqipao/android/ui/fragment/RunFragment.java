package com.n1njac.yiqipao.android.ui.fragment;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.n1njac.yiqipao.android.R;
import com.n1njac.yiqipao.android.ui.activity.UserRunRecordActivity;
import com.n1njac.yiqipao.android.utils.SizeUtil;

import at.markushi.ui.CircleButton;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

/**
 * Created by huanglei on 2017/1/12.
 */

public class RunFragment extends Fragment {

    @BindView(R.id.bg_run_frag)
    ImageView bgRunFrag;
    @BindView(R.id.gps_iv)
    ImageView gpsIv;
    @BindView(R.id.gps_prompt_tv)
    TextView gpsPromptTv;
    @BindView(R.id.circle_btn)
    CircleButton circleBtn;
    @BindView(R.id.run_frag_relative)
    RelativeLayout runFragRelative;
    Unbinder unbinder;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.run_frag, container, false);
        unbinder = ButterKnife.bind(this, view);

        runFragRelative.setPadding(0, SizeUtil.getStatusBarHeight(getActivity()), 0, 0);
        Glide.with(getActivity()).load(R.drawable.run_bg).centerCrop().into(bgRunFrag);


        return view;
    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    @OnClick(R.id.circle_btn)
    public void onViewClicked() {
        Intent intent = new Intent(getActivity(), UserRunRecordActivity.class);
        startActivity(intent);
    }
}
