package com.n1njac.yiqipao.android.utils;

import android.app.Activity;
import android.app.ActivityManager;
import android.content.Context;

import java.util.Stack;

/**
 * Created by N1njaC on 2017/9/5.
 */

public class ActivityManagerUtil {

    private static Stack<Activity> activityStack = new Stack<>();

    public static void addActivity(Activity activity) {
        activityStack.push(activity);
    }

    public static Activity getCurrentActivity() {

        return activityStack.lastElement();
    }

    public static void finishCurrentActivity() {
        activityStack.pop().finish();
    }

    public static void finishAll() {
        for (Activity a : activityStack) {
            if (a != null) {
                a.finish();
            }
        }
        activityStack.clear();
    }

    public static void exitApp(Context context){
        finishAll();
        ActivityManager manager = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        manager.killBackgroundProcesses(context.getPackageName());
        System.exit(0);
    }
}
