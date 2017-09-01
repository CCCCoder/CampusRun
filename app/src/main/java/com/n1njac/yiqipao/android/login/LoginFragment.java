package com.n1njac.yiqipao.android.login;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.n1njac.yiqipao.android.R;
import com.n1njac.yiqipao.android.utils.SizeUtil;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

/**
 * Created by N1njaC on 2017/7/31.
 */

public class LoginFragment extends Fragment {

    private static final String TAG = LoginFragment.class.getSimpleName();

    @BindView(R.id.new_login_back_btn)
    ImageButton newLoginBackBtn;
    @BindView(R.id.login_account_et)
    EditText loginAccountEt;
    @BindView(R.id.new_login_password_et)
    EditText newLoginPasswordEt;
    @BindView(R.id.new_login_btn)
    Button newLoginBtn;
    @BindView(R.id.new_login_forget_pwd_tv)
    TextView newLoginForgetPwdTv;
    @BindView(R.id.new_login_quick_login_tv)
    TextView newLoginQuickLoginTv;
    @BindView(R.id.new_login_content_linear)
    LinearLayout newLoginContentLinear;
    @BindView(R.id.new_login_goto_register_tv)
    TextView newLoginGotoRegisterTv;
    @BindView(R.id.login_tool_bar_relat)
    RelativeLayout loginToolBarRelat;
    Unbinder unbinder;
    @BindView(R.id.new_login_via_qq)
    ImageView newLoginViaQq;
    @BindView(R.id.new_login_via_wechat)
    ImageView newLoginViaWechat;
    @BindView(R.id.new_login_via_sina)
    ImageView newLoginViaSina;

    private FragmentManager fm;
    private FragmentTransaction ft;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.new_login_frag, container, false);
        unbinder = ButterKnife.bind(this, view);
        loginToolBarRelat.setPadding(0, SizeUtil.getStatusBarHeight(getActivity()), 0, 0);

        NewLoginActivity loginActivity = (NewLoginActivity) getActivity();
        fm = loginActivity.getSupportFragmentManager();


        return view;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    @OnClick({R.id.new_login_back_btn, R.id.new_login_btn, R.id.new_login_forget_pwd_tv, R.id.new_login_quick_login_tv, R.id.new_login_goto_register_tv, R.id.new_login_via_qq, R.id.new_login_via_wechat, R.id.new_login_via_sina})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.new_login_back_btn:

                fm.popBackStack();
                break;
            case R.id.new_login_btn:

                Log.d(TAG, "login click");

                break;
            case R.id.new_login_forget_pwd_tv:
                ft = fm.beginTransaction();
                ForgetPwdFragment forgetPwdFragment = new ForgetPwdFragment();
                ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
                ft.replace(R.id.login_activity, forgetPwdFragment);
                ft.addToBackStack("forget_pwd");
                ft.commit();

                break;
            case R.id.new_login_quick_login_tv:
                ft = fm.beginTransaction();
                QuickLoginFragment quickLoginFragment = new QuickLoginFragment();
                ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
                ft.replace(R.id.login_activity,quickLoginFragment);
                ft.addToBackStack("quick_login");
                ft.commit();
                break;
            case R.id.new_login_goto_register_tv:

                ft = fm.beginTransaction();
                RegisterFragment registerFragment = new RegisterFragment();
                ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
                ft.replace(R.id.login_activity,registerFragment);
                ft.addToBackStack("register");
                ft.commit();

                break;
            case R.id.new_login_via_qq:
                break;
            case R.id.new_login_via_wechat:
                break;
            case R.id.new_login_via_sina:
                break;
        }
    }

}
