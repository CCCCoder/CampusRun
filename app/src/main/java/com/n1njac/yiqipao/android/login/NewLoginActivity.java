package com.n1njac.yiqipao.android.login;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;


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
        registerFragment = new RegisterFragment();
        loginFragment = new LoginFragment();
        loginGuideFragment = new LoginGuideFragment();
        FragmentManager fm = this.getSupportFragmentManager();
        FragmentTransaction ft = fm.beginTransaction();
        ft.replace(R.id.login_activity,new LoginGuideFragment());
        ft.commit();
    }
}
