package com.n1njac.yiqipao.android.utils;

import android.annotation.SuppressLint;
import android.os.CountDownTimer;
import android.support.v7.app.AppCompatActivity;
import android.widget.Button;
import android.widget.TextView;

import com.n1njac.yiqipao.android.R;

/**
 * Created by N1njaC on 2017/8/6.
 */

public class TimeCountUtil extends CountDownTimer {

    private TextView mButton;
    private AppCompatActivity mActivity;


    public TimeCountUtil(long millisInFuture, long countDownInterval, AppCompatActivity activity,TextView button) {
        super(millisInFuture, countDownInterval);
        this.mButton = button;
        this.mActivity = activity;
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onTick(long l) {
        mButton.setClickable(false);
        mButton.setBackground(mActivity.getResources().getDrawable(R.drawable.btn_noclick_tv));
        mButton.setText(l / 1000+"秒后可重新发送");
    }

    @Override
    public void onFinish() {

        mButton.setBackground(mActivity.getResources().getDrawable(R.drawable.btn_login));
        mButton.setClickable(true);
        mButton.setFocusable(true);
        mButton.setText(R.string.get_verification_code_tv);
    }
}
