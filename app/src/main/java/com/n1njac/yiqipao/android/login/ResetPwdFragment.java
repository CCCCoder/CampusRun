package com.n1njac.yiqipao.android.login;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.n1njac.yiqipao.android.R;
import com.n1njac.yiqipao.android.utils.SizeUtil;
import com.n1njac.yiqipao.android.utils.ToastUtil;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import cn.bmob.v3.BmobUser;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.UpdateListener;

/**
 * Created by N1njaC on 2017/9/3.
 */

public class ResetPwdFragment extends Fragment {

    @BindView(R.id.new_reset_back_btn)
    ImageButton newResetBackBtn;
    @BindView(R.id.reset_pwd_old_et)
    EditText oldPwdEt;
    @BindView(R.id.reset_pwd_new_et)
    EditText newPwdEt;
    @BindView(R.id.reset_password_new_again_et)
    EditText newPwdAgainEt;
    @BindView(R.id.new_reset_login_btn)
    Button newLoginBtn;
    @BindView(R.id.reset_tool_bar_relat)
    RelativeLayout resetToolBarRelat;
    Unbinder unbinder;

    private FragmentManager fm;
    private FragmentTransaction ft;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.new_reset_pwd_frag, container, false);
        unbinder = ButterKnife.bind(this, view);
        resetToolBarRelat.setPadding(0, SizeUtil.getStatusBarHeight(getActivity()), 0, 0);

        NewLoginActivity loginActivity = (NewLoginActivity) getActivity();
        fm = loginActivity.getSupportFragmentManager();

        return view;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    @OnClick({R.id.new_reset_back_btn, R.id.new_reset_login_btn})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.new_reset_back_btn:
                fm.popBackStack();
                break;
            case R.id.new_reset_login_btn:

                String oldPwd = oldPwdEt.getText().toString();
                String newPwd = newPwdEt.getText().toString();
                String newPwdAgain = newPwdAgainEt.getText().toString();

                if (oldPwd.equals(newPwd)) {
                    ToastUtil.shortToast(getActivity(), "旧密码不能与新密码相同！");
                    return;
                } else if (!newPwd.equals(newPwdAgain)) {
                    ToastUtil.shortToast(getActivity(), "两次密码不一致！");
                    return;
                }

                BmobUser.updateCurrentUserPassword(oldPwd, newPwd, new UpdateListener() {
                    @Override
                    public void done(BmobException e) {
                        if (e == null) {
                            ToastUtil.shortToast(getActivity(), "修改密码成功:)");
                        } else {
                            ToastUtil.shortToast(getActivity(), "修改密码失败:（" + e.getLocalizedMessage());
                        }
                    }
                });

                // TODO: 2017/9/4 跳转到主界面

                break;
        }
    }
}
