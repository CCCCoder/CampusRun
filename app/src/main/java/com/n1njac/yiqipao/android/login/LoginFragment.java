package com.n1njac.yiqipao.android.login;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.text.TextUtils;
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

import com.n1njac.yiqipao.android.MainActivity;
import com.n1njac.yiqipao.android.R;
import com.n1njac.yiqipao.android.bmobObject.UserInfoBmob;
import com.n1njac.yiqipao.android.utils.RegularMatchUtil;
import com.n1njac.yiqipao.android.utils.SizeUtil;
import com.n1njac.yiqipao.android.utils.ToastUtil;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import cn.bmob.v3.BmobUser;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.LogInListener;

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
    EditText passwordEt;
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

    private ProgressDialog progressDialog;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.new_login_frag, container, false);
        unbinder = ButterKnife.bind(this, view);
        loginToolBarRelat.setPadding(0, SizeUtil.getStatusBarHeight(getActivity()), 0, 0);

        NewLoginActivity loginActivity = (NewLoginActivity) getActivity();
        fm = loginActivity.getSupportFragmentManager();
        progressDialog = new ProgressDialog(getActivity());
        progressDialog.setMessage("正在登录...");

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
                String account = loginAccountEt.getText().toString();
                String password = passwordEt.getText().toString();

                progressDialog.show();

                loginCheck(account, password);


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
                ft.replace(R.id.login_activity, quickLoginFragment);
                ft.addToBackStack("quick_login");
                ft.commit();
                break;
            case R.id.new_login_goto_register_tv:

                ft = fm.beginTransaction();
                RegisterFragment registerFragment = new RegisterFragment();
                ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
                ft.replace(R.id.login_activity, registerFragment);
                ft.addToBackStack("register");
                ft.commit();

                break;
            case R.id.new_login_via_qq:

                // TODO: 2017/9/4 via qq

                break;
            case R.id.new_login_via_wechat:

                // TODO: 2017/9/4 via wechat

                break;
            case R.id.new_login_via_sina:

                // TODO: 2017/9/4 via sina
                break;
        }
    }

    private void loginCheck(String account, String password) {
        if (TextUtils.isEmpty(account)) {
            ToastUtil.shortToast(getActivity(), "手机号不能为空！");
            return;
        }
        if (TextUtils.isEmpty(password)) {
            ToastUtil.shortToast(getActivity(), "密码不能为空！");
            return;
        }

        login(account, password);

    }

    private void login(String account, String password) {

        BmobUser.loginByAccount(account, password, new LogInListener<UserInfoBmob>() {
            @Override
            public void done(UserInfoBmob userInfoBmob, BmobException e) {

                progressDialog.dismiss();

                if (userInfoBmob != null) {
                    // TODO: 2017/9/4 登录成功，跳转到主界面
                    ToastUtil.shortToast(getActivity(), "登录成功");
                    Intent intent = new Intent(getActivity(), MainActivity.class);
                    startActivity(intent);

                } else {

                    Log.d(TAG, "login error:---->" + e.getErrorCode() + "  " + e.getLocalizedMessage());
                    ToastUtil.shortToast(getActivity(), e.getLocalizedMessage());
                }
            }
        });
    }

}
