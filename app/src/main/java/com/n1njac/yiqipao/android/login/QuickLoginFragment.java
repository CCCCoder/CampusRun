package com.n1njac.yiqipao.android.login;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.n1njac.yiqipao.android.MainActivity;
import com.n1njac.yiqipao.android.R;
import com.n1njac.yiqipao.android.bmobObject.UserInfoBmob;
import com.n1njac.yiqipao.android.utils.RegularMatchUtil;
import com.n1njac.yiqipao.android.utils.SizeUtil;
import com.n1njac.yiqipao.android.utils.TimeCountUtil;
import com.n1njac.yiqipao.android.utils.ToastUtil;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import cn.bmob.v3.BmobSMS;
import cn.bmob.v3.BmobUser;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.LogInListener;
import cn.bmob.v3.listener.QueryListener;

/**
 * Created by N1njaC on 2017/9/1.
 */

public class QuickLoginFragment extends Fragment implements TextWatcher {


    private static final String TAG = QuickLoginFragment.class.getSimpleName();

    @BindView(R.id.quick_login_back_btn)
    ImageButton quickLoginBackBtn;
    @BindView(R.id.quick_login_phone_et)
    EditText phoneEt;
    @BindView(R.id.quick_login_verification_et)
    EditText codeEt;
    @BindView(R.id.quick_login_get_verification_code_tv)
    TextView quickLoginGetVerificationCodeTv;
    @BindView(R.id.quick_login_submit_btn)
    Button quickLoginSubmitBtn;
    @BindView(R.id.quick_login_relat)
    RelativeLayout quickLoginRelat;
    Unbinder unbinder;

    private TimeCountUtil timeCountUtil;
    private FragmentManager fm;

    private ProgressDialog progressDialog;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.new_quick_login_frag, container, false);
        unbinder = ButterKnife.bind(this, view);
        quickLoginRelat.setPadding(0, SizeUtil.getStatusBarHeight(getActivity()), 0, 0);

        NewLoginActivity newLoginActivity = (NewLoginActivity) getActivity();
        timeCountUtil = new TimeCountUtil(60000, 1000, newLoginActivity, quickLoginGetVerificationCodeTv);
        codeEt.addTextChangedListener(this);

        newLoginActivity = (NewLoginActivity) getActivity();
        fm = newLoginActivity.getSupportFragmentManager();

        progressDialog = new ProgressDialog(getActivity());
        progressDialog.setMessage("正在登录...");

        return view;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    @OnClick({R.id.quick_login_back_btn, R.id.quick_login_get_verification_code_tv, R.id.quick_login_submit_btn})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.quick_login_back_btn:

                fm.popBackStack();

                break;
            case R.id.quick_login_get_verification_code_tv:
                String phoneNum = phoneEt.getText().toString();
                if (!RegularMatchUtil.matchPhoneNum(phoneNum)) {
                    Toast.makeText(getActivity(), "请输入正确的手机号码！", Toast.LENGTH_SHORT).show();
                } else {
                    timeCountUtil.start();
                    BmobSMS.requestSMSCode(phoneEt.getText().toString(), "Campus Run", new QueryListener<Integer>() {
                        @Override
                        public void done(Integer integer, BmobException e) {
                            ToastUtil.shortToast(getActivity(), "验证码发送成功！");
                            Log.d(TAG, "code---->" + integer);
                        }
                    });
                }
                break;
            case R.id.quick_login_submit_btn:

                progressDialog.show();

                BmobUser.loginBySMSCode(phoneEt.getText().toString(), codeEt.getText().toString(), new LogInListener<UserInfoBmob>() {
                    @Override
                    public void done(UserInfoBmob userInfoBmob, BmobException e) {

                        progressDialog.dismiss();

                        if (e == null) {

                            // TODO: 2017/9/4 登录成功直接跳转到主界面
                            ToastUtil.shortToast(getActivity(), "login success");
                            Intent intent = new Intent(getActivity(), MainActivity.class);
                            startActivity(intent);


                        } else {
                            ToastUtil.shortToast(getActivity(), e.getLocalizedMessage());
                            Log.d(TAG, "login error----->" + e.getErrorCode() + "   " + e.getLocalizedMessage());
                        }
                    }
                });

                break;
        }
    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {

        if (s.length() > 0) {
            quickLoginSubmitBtn.setEnabled(true);
        } else {
            quickLoginSubmitBtn.setEnabled(false);
        }
    }

    @Override
    public void afterTextChanged(Editable s) {

    }
}
