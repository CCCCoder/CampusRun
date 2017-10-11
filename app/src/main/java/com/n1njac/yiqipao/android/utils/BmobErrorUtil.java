package com.n1njac.yiqipao.android.utils;

/**
 * Created by N1njaC on 2017/9/5.
 */

public class BmobErrorUtil {

    public static String errorCode2Str(int code) {
        String prompt = null;
        switch (code) {
            case 9016:
                prompt = "无网络连接，请检查您的手机网络.";
                break;

            case 207:
                prompt = "验证码错误";
                break;

            default:
                prompt = "未知错误，请联系开发者.";

                break;
        }

        return prompt;
    }

}
