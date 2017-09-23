package com.n1njac.yiqipao.android.login;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.text.Editable;
import android.text.TextUtils;
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

import com.n1njac.yiqipao.android.ui.activity.MainActivity;
import com.n1njac.yiqipao.android.R;
import com.n1njac.yiqipao.android.bmobObject.UserInfoBmob;
import com.n1njac.yiqipao.android.utils.RegularMatchUtil;
import com.n1njac.yiqipao.android.utils.SizeUtil;
import com.n1njac.yiqipao.android.utils.TimeCountUtil;
import com.n1njac.yiqipao.android.utils.ToastUtil;

import cn.bmob.v3.BmobSMS;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.QueryListener;
import cn.bmob.v3.listener.SaveListener;

/**
 * Created by N1njaC on 2017/7/31.
 */

public class RegisterFragment extends Fragment implements View.OnClickListener, TextWatcher {

    private static final String TAG = RegisterFragment.class.getSimpleName();

    private RelativeLayout relativeLayout;
    private ImageButton mBackBtn;
    private FragmentManager fm;
    private EditText phoneEt;
    private EditText verificationEt;
    private TextView getVerificationCodeTv;
    private TimeCountUtil timeCountUtil;
    private Button mSubmitBtn;
    private NewLoginActivity newLoginActivity;

    private EditText account;
    private EditText password;
    private EditText passwordAgain;

    private ProgressDialog progressDialog;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.new_register_frag, container, false);

        relativeLayout = (RelativeLayout) view.findViewById(R.id.register_relat);
        relativeLayout.setPadding(0, SizeUtil.getStatusBarHeight(getActivity()), 0, 0);
        mBackBtn = (ImageButton) view.findViewById(R.id.register_back_btn);

        mBackBtn.setOnClickListener(this);
        newLoginActivity = (NewLoginActivity) getActivity();
        fm = newLoginActivity.getSupportFragmentManager();
        phoneEt = (EditText) view.findViewById(R.id.register_phone_et);

        mSubmitBtn = (Button) view.findViewById(R.id.register_submit_btn);
        mSubmitBtn.setOnClickListener(this);

        verificationEt = (EditText) view.findViewById(R.id.register_verification_et);
        verificationEt.addTextChangedListener(this);

        getVerificationCodeTv = (TextView) view.findViewById(R.id.register_get_verification_code_tv);
        getVerificationCodeTv.setOnClickListener(this);
        timeCountUtil = new TimeCountUtil(60000, 1000, newLoginActivity, getVerificationCodeTv);

        account = (EditText) view.findViewById(R.id.register_account_et);
        password = (EditText) view.findViewById(R.id.register_pwd_et);
        passwordAgain = (EditText) view.findViewById(R.id.register_pwd_again_et);

        progressDialog = new ProgressDialog(getActivity());
        progressDialog.setMessage("正在登录...");

        return view;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.register_back_btn:

                fm.popBackStack();
                break;
            case R.id.register_get_verification_code_tv:

                Log.d(TAG, "----verification_code_tv");
                String phoneNum = phoneEt.getText().toString();
                if (!(RegularMatchUtil.matchPhoneNum(phoneNum))) {
                    Toast.makeText(getActivity(), "请输入正确的手机号码！", Toast.LENGTH_SHORT).show();
                    Log.d(TAG, "----not match");
                } else {
                    timeCountUtil.start();
                    String phone = phoneEt.getText().toString();
                    BmobSMS.requestSMSCode(phone, "Campus Run", new QueryListener<Integer>() {
                        @Override
                        public void done(Integer integer, BmobException e) {
                            if (e == null) {
                                ToastUtil.shortToast(getActivity(), "验证码发送成功！");
                                Log.d(TAG, "code---->" + integer);
                            }
                        }
                    });
                }

                break;
            case R.id.register_submit_btn:

                Log.d(TAG, "click submit");
                String accountStr = account.getText().toString();
                String passwordStr = password.getText().toString();
                String passwordAgainStr = passwordAgain.getText().toString();
                String phoneStr = phoneEt.getText().toString();
                String codeStr = verificationEt.getText().toString();
                if (TextUtils.isEmpty(accountStr)) {
                    ToastUtil.shortToast(getActivity(), "账号不能为空！");
                    return;
                }

                if (TextUtils.isEmpty(passwordStr) || TextUtils.isEmpty(passwordAgainStr)) {
                    ToastUtil.shortToast(getActivity(), "密码不能为空！");
                    return;
                }

                if (!passwordStr.equals(passwordAgainStr)) {
                    ToastUtil.shortToast(getActivity(), "两次密码不一致！");
                    return;
                }

                progressDialog.show();

                UserInfoBmob userInfo = new UserInfoBmob();
                userInfo.setMobilePhoneNumber(phoneStr);
                userInfo.setUsername(accountStr);
                userInfo.setPassword(passwordStr);
                userInfo.setpSex("男");
                userInfo.signOrLogin(codeStr, new SaveListener<UserInfoBmob>() {
                    @Override
                    public void done(UserInfoBmob userInfoBmob, BmobException e) {

                        progressDialog.dismiss();

                        if (e == null) {
                            // TODO: 2017/9/5 跳转到主界面

                            ToastUtil.shortToast(getActivity(), "恭喜你！注册成功");
                            Intent intent = new Intent(getActivity(), MainActivity.class);
                            startActivity(intent);

                        } else {
                            ToastUtil.shortToast(getActivity(), e.getLocalizedMessage());
                            Log.d(TAG, "login error----->" + e.getErrorCode() + "   " + e.getLocalizedMessage());
                        }

                    }
                });

                break;
            default:
                break;
        }
    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {
        Log.d(TAG, "beforeTextChanged");
    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {
        if (s.length() > 0) {
            mSubmitBtn.setEnabled(true);
            Log.d(TAG, "length>0");
        } else {
            mSubmitBtn.setEnabled(false);
            Log.d(TAG, "length=0");
        }
    }

    @Override
    public void afterTextChanged(Editable s) {
        Log.d(TAG, "afterTextChanged");
    }
}
