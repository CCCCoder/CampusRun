package com.n1njac.yiqipao.android.ui.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.n1njac.yiqipao.android.R;
import com.n1njac.yiqipao.android.ui.widget.BezierView;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by N1njaC on 2017/9/8.
 */

public class UserInfoDisplayFragment extends Fragment {

    @BindView(R.id.bezier_bg)
    BezierView bezierBg;
    @BindView(R.id.user_icon_iv)
    CircleImageView userIconIv;
    @BindView(R.id.user_id_tv)
    TextView userIdTv;
    @BindView(R.id.user_signature_tv)
    TextView userSignatureTv;
    @BindView(R.id.user_run_count_tv)
    TextView userRunCountTv;
    @BindView(R.id.user_info_distance_tv)
    TextView userInfoDistanceTv;
    @BindView(R.id.edit_user_info_btn)
    Button editUserInfoBtn;
    Unbinder unbinder;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.user_info_frag, container, false);

        unbinder = ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    @OnClick({R.id.user_icon_iv, R.id.edit_user_info_btn})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.user_icon_iv:
                break;
            case R.id.edit_user_info_btn:
                break;
        }
    }
}
