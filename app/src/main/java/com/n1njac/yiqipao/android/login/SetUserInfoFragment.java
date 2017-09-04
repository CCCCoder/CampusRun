package com.n1njac.yiqipao.android.login;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.n1njac.yiqipao.android.R;

/**
 * Created by N1njaC on 2017/9/4.
 *
 * 设置用户的登录账号，密码和相关信息。
 */

public class SetUserInfoFragment extends Fragment {
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.new_set_info_frag, container, false);



        return view;
    }
}
