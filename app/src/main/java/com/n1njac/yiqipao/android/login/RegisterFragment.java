package com.n1njac.yiqipao.android.login;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.widget.Toolbar;
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

import com.n1njac.yiqipao.android.R;
import com.n1njac.yiqipao.android.utils.RegularMatchUtil;
import com.n1njac.yiqipao.android.utils.SizeUtil;

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

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.register_frag, container, false);

        relativeLayout = (RelativeLayout) view.findViewById(R.id.register_relat);
        relativeLayout.setPadding(0, SizeUtil.getStatusBarHeight(getActivity()), 0, 0);
        mBackBtn = (ImageButton) view.findViewById(R.id.register_back_btn);
        mBackBtn.setOnClickListener(this);
        NewLoginActivity newLoginActivity = (NewLoginActivity) getActivity();
        fm = newLoginActivity.getSupportFragmentManager();
        phoneEt = (EditText) view.findViewById(R.id.register_phone_et);
        phoneEt.addTextChangedListener(this);

        verificationEt = (EditText) view.findViewById(R.id.register_verification_et);
        getVerificationCodeTv = (TextView) view.findViewById(R.id.register_get_verification_code_tv);
        getVerificationCodeTv.setOnClickListener(this);
        return view;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.register_back_btn:

                fm.popBackStack();
                break;
            case R.id.register_get_verification_code_tv:

                Log.d(TAG,"----verification_code_tv");
                String phoneNum = phoneEt.getText().toString();
                if (!(RegularMatchUtil.matchPhoneNum(phoneNum))){
                    Toast.makeText(getActivity(),"请输入正确的手机号码！",Toast.LENGTH_SHORT).show();
                    Log.d(TAG,"----not match");
                }


                break;
            default:
                break;
        }
    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {
        if (s.length() > 0) {

        }else {

        }
    }

    @Override
    public void afterTextChanged(Editable s) {

    }
}
