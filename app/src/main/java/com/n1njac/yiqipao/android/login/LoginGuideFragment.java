package com.n1njac.yiqipao.android.login;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.annotation.StringDef;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.n1njac.yiqipao.android.R;

/**
 * Created by N1njaC on 2017/7/31.
 */

public class LoginGuideFragment extends Fragment {

    private static final String TAG = LoginGuideFragment.class.getSimpleName();

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.new_guide_login_frag, container, false);
        Button register = (Button) view.findViewById(R.id.btn_guide_register);
        Button login = (Button) view.findViewById(R.id.btn_guide_login);

        NewLoginActivity newLoginActivity = (NewLoginActivity) getActivity();
        FragmentManager fm = newLoginActivity.getSupportFragmentManager();

        final FragmentTransaction ft = fm.beginTransaction();
        final RegisterFragment registerFragment = new RegisterFragment();
        final LoginFragment loginFragment = new LoginFragment();
        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Log.i(TAG, "register click");
                ft.replace(R.id.login_activity,registerFragment);
                ft.addToBackStack("register");
                ft.commit();
            }
        });

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                ft.replace(R.id.login_activity, loginFragment);
                ft.addToBackStack("login");
                ft.commit();
            }
        });

        return view;
    }
}
