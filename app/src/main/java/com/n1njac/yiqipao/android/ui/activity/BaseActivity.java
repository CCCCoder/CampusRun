package com.n1njac.yiqipao.android.ui.activity;

import android.os.Bundle;
import android.os.PersistableBundle;

import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

import com.n1njac.yiqipao.android.utils.ActivityManagerUtil;

/**
 * Created by N1njaC on 2017/9/5.
 */

public class BaseActivity extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActivityManagerUtil.addActivity(this);
    }
}
