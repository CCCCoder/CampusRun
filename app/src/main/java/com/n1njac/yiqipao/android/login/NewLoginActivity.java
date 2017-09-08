package com.n1njac.yiqipao.android.login;

import android.content.DialogInterface;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.View;


import com.n1njac.yiqipao.android.R;
import com.n1njac.yiqipao.android.ui.activity.BaseActivity;
import com.n1njac.yiqipao.android.utils.ActivityManagerUtil;


/**
 * Created by N1njaC on 2017/7/31.
 */

public class NewLoginActivity extends BaseActivity {

    private static final String TAG = NewLoginActivity.class.getSimpleName();

    public RegisterFragment registerFragment;
    public LoginFragment loginFragment;
    public LoginGuideFragment loginGuideFragment;

    private FragmentManager fm;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.new_login_base_activity);
        if (Build.VERSION.SDK_INT >= 21) {
            View decorView = getWindow().getDecorView();
            int option = View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN | View.SYSTEM_UI_FLAG_LAYOUT_STABLE;
            decorView.setSystemUiVisibility(option);
            getWindow().setStatusBarColor(Color.TRANSPARENT);
        }

        registerFragment = new RegisterFragment();
        loginFragment = new LoginFragment();
        loginGuideFragment = new LoginGuideFragment();
        fm = this.getSupportFragmentManager();
        FragmentTransaction ft = fm.beginTransaction();
        ft.replace(R.id.login_activity, new LoginGuideFragment(), "login_guide");
        ft.commit();
    }

    @Override
    public void onBackPressed() {
        //super.onBackPressed()这行会自己调用finish，要拦截自己处理返回事件的时候，务必注释掉。


        //当前fragment是LoginGuideFragment的时候，返回提示退出应用。
        LoginGuideFragment loginGuideFragment = (LoginGuideFragment) getSupportFragmentManager().findFragmentByTag("login_guide");
        if (loginGuideFragment != null && loginGuideFragment.isVisible()) {
            AlertDialog.Builder builder = new AlertDialog.Builder(this)
                    .setMessage("确定退出应用吗？")
                    .setTitle("提示")
                    .setCancelable(true)
                    .setNegativeButton("取消", null)
                    .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            ActivityManagerUtil.finishAll();
                        }
                    });
            builder.show();
        } else {
            super.onBackPressed();
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        Log.d(TAG, "onStop");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.d(TAG, "onDestroy");
    }
}
