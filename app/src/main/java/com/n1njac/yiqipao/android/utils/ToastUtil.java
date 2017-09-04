package com.n1njac.yiqipao.android.utils;

import android.content.Context;
import android.widget.Toast;

/**
 * Created by N1njaC on 2017/9/4.
 */

public class ToastUtil {


    public static void shortToast(Context context, String content) {
        Toast.makeText(context, content, Toast.LENGTH_SHORT).show();
    }

    public static void longToast(Context context, String content) {
        Toast.makeText(context, content, Toast.LENGTH_LONG).show();
    }

}
