package com.n1njac.yiqipao.android.utils;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by N1njaC on 2017/9/23.
 */

public class TimeUtil {

    public static String parseTimeFormat(long startTime) {

        SimpleDateFormat ym = new SimpleDateFormat("yyyy年MM月");
        Date d1 = new Date(startTime);
        String ymStr = ym.format(d1);
        SimpleDateFormat dhm = new SimpleDateFormat("dd日HH:mm");
        String dhmStr = dhm.format(d1);

        return ymStr + "\n" + dhmStr;
    }

}
