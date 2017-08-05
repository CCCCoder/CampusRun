package com.n1njac.yiqipao.android.login;

import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;


import com.n1njac.yiqipao.android.R;


/**
 * Created by N1njaC on 2017/7/31.
 */

public class NewLoginActivity extends AppCompatActivity {

    public RegisterFragment registerFragment;
    public LoginFragment loginFragment;
    public LoginGuideFragment loginGuideFragment;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.new_login_activity);
        if (Build.VERSION.SDK_INT >= 21) {
            View decorView = getWindow().getDecorView();
            int option = View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN | View.SYSTEM_UI_FLAG_LAYOUT_STABLE;
            decorView.setSystemUiVisibility(option);
            getWindow().setStatusBarColor(Color.TRANSPARENT);
        }

        registerFragment = new RegisterFragment();
        loginFragment = new LoginFragment();
        loginGuideFragment = new LoginGuideFragment();
        FragmentManager fm = this.getSupportFragmentManager();
        FragmentTransaction ft = fm.beginTransaction();
        ft.replace(R.id.login_activity, new LoginGuideFragment());
        ft.commit();
    }
}
