package com.n1njac.yiqipao.android.utils;

import android.content.Context;

/**
 * Created by huanglei on 2017/3/22.
 */

public class DisplayUtils {

    public static int dip2px(Context context, float dpValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dpValue * scale + 0.5f);
    }
}
