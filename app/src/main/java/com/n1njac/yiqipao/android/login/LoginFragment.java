package com.n1njac.yiqipao.android.login;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

import com.n1njac.yiqipao.android.R;
import com.n1njac.yiqipao.android.utils.SizeUtil;

/**
 * Created by N1njaC on 2017/7/31.
 */

public class LoginFragment extends Fragment {

    private RelativeLayout relativeLayout;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.new_login_frag,container,false);
        relativeLayout = (RelativeLayout) view.findViewById(R.id.login_tool_bar_relat);
        relativeLayout.setPadding(0, SizeUtil.getStatusBarHeight(getActivity()), 0, 0);
        return view;
    }
}
