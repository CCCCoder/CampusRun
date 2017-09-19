package com.n1njac.yiqipao.android.ui.widget;

import android.content.Context;

import android.graphics.Typeface;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.widget.TextView;

import com.n1njac.yiqipao.android.utils.FontCacheUtil;

/**
 * Created by N1njaC on 2017/9/19.
 */

public class RunDataTextView extends android.support.v7.widget.AppCompatTextView {


    private static final String ANDROID_SCHEMA = "http://schemas.android.com/apk/res/android";

    private static final String FONT_BOLD_PATH = "fonts/Avenir_Next_Condensed_demi_bold.ttf";
    private static final String FONT_REGULAR_PATH = "fonts/Avenir_Next_Condensed_regular.ttf";


    public RunDataTextView(Context context) {
        super(context);
    }

    public RunDataTextView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public RunDataTextView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        Typeface tfBold = FontCacheUtil.getFont(context, FONT_BOLD_PATH);
        Typeface tfRegular = FontCacheUtil.getFont(context, FONT_REGULAR_PATH);
        assert attrs != null;
        int textStyle = attrs.getAttributeIntValue(ANDROID_SCHEMA, "textStyle", Typeface.NORMAL);

        if (textStyle == Typeface.BOLD) {
            setTypeface(tfBold);
        } else if (textStyle == Typeface.NORMAL) {
            setTypeface(tfRegular);
        }
    }
}
