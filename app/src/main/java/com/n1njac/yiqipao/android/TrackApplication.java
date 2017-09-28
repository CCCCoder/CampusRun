package com.n1njac.yiqipao.android;

import android.app.Application;
import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.widget.Toast;


//import org.litepal.LitePal;
import org.litepal.LitePal;

import java.lang.ref.WeakReference;

import cn.bmob.push.BmobPush;
import cn.bmob.v3.Bmob;
import cn.bmob.v3.BmobInstallation;

public class TrackApplication extends Application {

    private static TrackApplication instance;


    public static TrackApplication getInstance() {
        return instance;
    }

    @Override
    public void onCreate() {
        // TODO Auto-generated method stub
        super.onCreate();
        instance = this;
        LitePal.initialize(this);
        Bmob.initialize(this, "eca43c1e6d34df771a7fa797a7960feb");
        BmobInstallation.getCurrentInstallation().save();
        BmobPush.startWork(this);
    }
}
