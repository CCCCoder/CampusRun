package com.n1njac.yiqipao.android.utils;

import android.content.Context;
import android.graphics.Typeface;

import java.util.HashMap;

/**
 * Created by N1njaC on 2017/9/19.
 */

public class FontCacheUtil {

    private static HashMap<String, Typeface> cacheFont = new HashMap<>();

    public static Typeface getFont(Context context, String path) {

        Typeface tf = cacheFont.get(path);
        if (tf == null) {
            tf = Typeface.createFromAsset(context.getAssets(), path);
            cacheFont.put(path, tf);
        }
        return tf;
    }

}
