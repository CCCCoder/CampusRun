package com.n1njac.yiqipao.android.login;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.n1njac.yiqipao.android.R;
import com.n1njac.yiqipao.android.utils.RegularMatchUtil;
import com.n1njac.yiqipao.android.utils.SizeUtil;
import com.n1njac.yiqipao.android.utils.TimeCountUtil;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

/**
 * Created by N1njaC on 2017/9/1.
 */

public class ForgetPwdFragment extends android.support.v4.app.Fragment implements TextWatcher {

    @BindView(R.id.forget_pwd_back_btn)
    ImageButton registerBackBtn;
    @BindView(R.id.forget_pwd_get_verification_code_tv)
    TextView forgetPwdGetVerificationCodeTv;
    @BindView(R.id.forget_pwd_submit_btn)
    Button forgetPwdSubmitBtn;
    Unbinder unbinder;
    @BindView(R.id.forget_pwd_phone_et)
    EditText phoneEt;
    @BindView(R.id.forget_pwd_verification_et)
    EditText codeEt;
    @BindView(R.id.new_forget_relat)
    RelativeLayout newForgetRelat;

    private NewLoginActivity newLoginActivity;
    private FragmentManager fm;
    private FragmentTransaction ft;

    private TimeCountUtil timeCountUtil;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.new_forget_pwd_frag, container, false);
        unbinder = ButterKnife.bind(this, view);
        newForgetRelat.setPadding(0, SizeUtil.getStatusBarHeight(getActivity()), 0, 0);

        NewLoginActivity newLoginActivity = (NewLoginActivity) getActivity();
        timeCountUtil = new TimeCountUtil(60000, 1000, newLoginActivity, forgetPwdGetVerificationCodeTv);
        codeEt.addTextChangedListener(this);

        newLoginActivity = (NewLoginActivity) getActivity();
        fm = newLoginActivity.getSupportFragmentManager();

        return view;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    @OnClick({R.id.forget_pwd_back_btn, R.id.forget_pwd_get_verification_code_tv, R.id.forget_pwd_submit_btn})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.forget_pwd_back_btn:

                fm.popBackStack();

                break;
            case R.id.forget_pwd_get_verification_code_tv:
                String phoneNum = phoneEt.getText().toString();
                if (!RegularMatchUtil.matchPhoneNum(phoneNum)) {
                    Toast.makeText(getActivity(), "请输入正确的手机号码！", Toast.LENGTH_SHORT).show();
                } else {
                    timeCountUtil.start();
                }

                break;
            case R.id.forget_pwd_submit_btn:

                ft = fm.beginTransaction();
                ResetPwdFragment resetPwdFragment = new ResetPwdFragment();
                ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
                ft.replace(R.id.login_activity, resetPwdFragment);
                ft.addToBackStack("reset_pwd");
                ft.commit();

                break;
        }
    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {
        if (s.length() > 0) {
            forgetPwdSubmitBtn.setEnabled(true);
        }else {
            forgetPwdSubmitBtn.setEnabled(false);
        }
    }

    @Override
    public void afterTextChanged(Editable s) {

    }
}
