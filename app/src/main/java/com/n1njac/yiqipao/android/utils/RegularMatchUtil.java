package com.n1njac.yiqipao.android.utils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by N1njaC on 2017/8/5.
 */

public class RegularMatchUtil {

    //匹配手机号码
    public static boolean matchPhoneNum(String phoneNum){
        Pattern pattern = Pattern.compile("^1[0-9]{10}$");
        Matcher matcher = pattern.matcher(phoneNum);
        return matcher.matches();
    }

    public static boolean matchVerificationCode(String verificationCode){
        Pattern pattern = Pattern.compile("^[0-9]{6}$");
        Matcher matcher = pattern.matcher(verificationCode);
        return matcher.matches();
    }
}
