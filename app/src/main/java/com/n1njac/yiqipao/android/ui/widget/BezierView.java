package com.n1njac.yiqipao.android.ui.widget;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.LinearGradient;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Point;
import android.graphics.RadialGradient;
import android.graphics.Shader;
import android.graphics.SweepGradient;
import android.support.annotation.IntDef;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;

import com.n1njac.yiqipao.android.R;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.util.List;

/**
 * Created by N1njaC on 2017/9/10.
 */

public class BezierView extends View {

    private static final String TAG = BezierView.class.getSimpleName();

    //背景风格
    public final static int STYLE_SAND = 0x00;
    public final static int STYLE_CLOUD = 0x01;
    public final static int STYLE_RIPPLE = 0x02;
    public final static int STYLE_BEACH = 0x03;
    public final static int STYLE_SHELL = 0x04;

    @IntDef({STYLE_SAND, STYLE_CLOUD, STYLE_RIPPLE, STYLE_BEACH, STYLE_SHELL})
    @Retention(RetentionPolicy.SOURCE)
    public @interface Style {

    }


    //shader mode
    public final static int SHADER_MODE_RADIAL = 0x10;
    public final static int SHADER_MODE_SWEEP = 0x11;
    public final static int SHADER_MODE_LINEAR = 0x12;


    @IntDef({SHADER_MODE_RADIAL, SHADER_MODE_SWEEP, SHADER_MODE_LINEAR})
    @Retention(RetentionPolicy.SOURCE)
    public @interface ShaderMode {

    }


    //shader style
    public final static int SHADER_STYLE_LEFT_TO_BOTTOM = 0x20;
    public final static int SHADER_STYLE_RIGHT_TO_BOTTOM = 0x21;
    public final static int SHADER_STYLE_TOP_TO_BOTTOM = 0x22;
    public final static int SHADER_STYLE_CENTER = 0x23;

    @IntDef({SHADER_STYLE_LEFT_TO_BOTTOM, SHADER_STYLE_RIGHT_TO_BOTTOM, SHADER_STYLE_TOP_TO_BOTTOM, SHADER_STYLE_CENTER})
    @Retention(RetentionPolicy.SOURCE)
    public @interface ShaderStyle {

    }


    private Paint mPaint;
    private int mBackgroundStyle;
    private int mShaderStyle;
    private int mShaderMode;
    private float mSmoothness;
    private int mShaderStartColor;
    private int mShaderEndColor;
    private int mWidth, mHeight;

    private List<Point[]> points;

    //smoothing coefficient: 0 ~ 1 recommended range: 0.15 ~ 0.3
    private static final float DEFAULT_SMOOTHNESS = 0.25f;

    public BezierView(Context context) {
        this(context, null);
    }

    public BezierView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public BezierView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        mPaint = new Paint();
        mPaint.setStyle(Paint.Style.FILL);

        TypedArray ta = context.obtainStyledAttributes(attrs, R.styleable.BezierView);
        mBackgroundStyle = ta.getInt(R.styleable.BezierView_background_style, STYLE_SAND);
        mSmoothness = ta.getFloat(R.styleable.BezierView_smoothness, DEFAULT_SMOOTHNESS);
        if (mSmoothness >= 1) {
            mSmoothness = 0.99f;
        } else if (mSmoothness <= 0) {
            mSmoothness = 0.1f;
        }
        mShaderMode = ta.getInt(R.styleable.BezierView_shader_mode, SHADER_MODE_RADIAL);
        mShaderStyle = ta.getInt(R.styleable.BezierView_shader_style, SHADER_STYLE_LEFT_TO_BOTTOM);
        mShaderStartColor = ta.getColor(R.styleable.BezierView_start_color, Color.argb(90, 255, 255, 255));
        mShaderEndColor = ta.getColor(R.styleable.BezierView_end_color, Color.argb(90, 255, 255, 255));
        ta.recycle();


    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        mWidth = w;
        mHeight = h;
    }

    @SuppressLint("DrawAllocation")
    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        points = PointsFactory.getPoints(mBackgroundStyle, mWidth, mHeight);

        if (points != null) {
            for (Point[] p : points) {
                int length = p.length;
                if (length < 4) return;

                Path path = new Path();
                int x_min = 0;
                int x_max = 0;
                int y_min = 0;
                int y_max = 0;
                for (int i = 0; i < length; i++) {
                    Point p_i, p_1i, p_i1, p_i2;
                    float ai_x, ai_y, bi_x, bi_y;

                    p_i = p[i];
                    if (i == 0) {
                        path.moveTo(p[0].x, p[0].y);
                        x_max = x_min = p[0].x;
                        y_min = y_max = p[0].y;
                        p_1i = p[length - 1];
                        p_i1 = p[i + 1];
                        p_i2 = p[i + 2];
                    } else if (i == length - 1) {
                        p_1i = p[i - 1];
                        p_i1 = p[0];
                        p_i2 = p[1];
                    } else if (i == length - 2) {
                        p_1i = p[i - 1];
                        p_i1 = p[i + 1];
                        p_i2 = p[0];
                    } else {
                        p_1i = p[i - 1];
                        p_i1 = p[i + 1];
                        p_i2 = p[i + 2];
                    }

                    if (p_1i == null || p_i == null || p_i1 == null || p_i2 == null) {
                        return;
                    }

                    ai_x = p_i.x + (p_i1.x - p_1i.x) * mSmoothness;
                    ai_y = p_i.y + (p_i1.y - p_1i.y) * mSmoothness;

                    bi_x = p_i1.x - (p_i2.x - p_i.x) * mSmoothness;
                    bi_y = p_i1.y - (p_i2.y - p_i.y) * mSmoothness;

                    path.cubicTo(ai_x, ai_y, bi_x, bi_y, p_i1.x, p_i1.y);

                    if (p[i].x < x_min) {
                        x_min = p[i].x;
                    }
                    if (p[i].x > x_max) {
                        x_max = p[i].x;
                    }
                    if (p[i].y < y_min) {
                        y_min = p[i].y;
                    }
                    if (p[i].y > y_max) {
                        y_max = p[i].y;
                    }

                }

                Point startP, endP;
                switch (mShaderStyle) {
                    case SHADER_STYLE_LEFT_TO_BOTTOM:
                        startP = new Point(x_min, y_min);
                        endP = new Point(x_max, y_max);
                        break;
                    case SHADER_STYLE_RIGHT_TO_BOTTOM:
                        startP = new Point(x_max, y_min);
                        endP = new Point(x_min, y_max);
                        break;
                    case SHADER_STYLE_TOP_TO_BOTTOM:
                        startP = new Point((x_max - x_min) / 2 + x_min, y_min);
                        endP = new Point(x_min, (y_max - y_min / 2 + y_min));
                        break;
                    case SHADER_STYLE_CENTER:
                        startP = new Point((x_max - x_min) / 2 + x_min, (y_max - y_min) / 2 + y_min);
                        endP = new Point(x_max, (y_max - y_min) / 2 + y_min);
                        break;
                    default:
                        startP = new Point(0, 0);
                        endP = new Point(mWidth, mHeight);
                        break;
                }

                switch (mShaderMode) {
                    case SHADER_MODE_RADIAL:
                        RadialGradient radialGradient = new RadialGradient(
                                startP.x, endP.y,
                                (int) (Math.sqrt(Math.pow(Math.abs(x_max - x_min), 2) + Math.pow((Math.abs(y_max - y_min)), 2))),
                                mShaderStartColor,
                                mShaderEndColor,
                                Shader.TileMode.CLAMP);
                        mPaint.setShader(radialGradient);

                        break;
                    case SHADER_MODE_SWEEP:

                        SweepGradient sweepGradient = new SweepGradient(startP.x, endP.y, mShaderStartColor, mShaderEndColor);
                        mPaint.setShader(sweepGradient);

                        break;
                    case SHADER_MODE_LINEAR:

                        LinearGradient linearGradient = new LinearGradient(startP.x, startP.y, endP.x, endP.y,
                                mShaderStartColor, mShaderEndColor,
                                Shader.TileMode.REPEAT);
                        mPaint.setShader(linearGradient);

                        break;
                }
                canvas.drawPath(path, mPaint);
            }

        } else {
            Log.d(TAG, "points is null");
        }
    }


    public int getBackgroundStyle() {
        return mBackgroundStyle;
    }

    public void setBackgroundStyle(int mBackgroundStyle) {
        this.mBackgroundStyle = mBackgroundStyle;
    }

    public int getShaderStyle() {
        return mShaderStyle;
    }

    public void setShaderStyle(int mShaderStyle) {
        this.mShaderStyle = mShaderStyle;
    }

    public int getShaderMode() {
        return mShaderMode;
    }

    public void setShaderMode(int mShaderMode) {
        this.mShaderMode = mShaderMode;
    }

    public float getSmoothness() {
        return mSmoothness;
    }

    public void setSmoothness(float mSmoothness) {
        this.mSmoothness = mSmoothness;
    }

    public int getShaderStartColor() {
        return mShaderStartColor;
    }

    public void setShaderStartColor(int mShaderStartColor) {
        this.mShaderStartColor = mShaderStartColor;
    }

    public int getShaderEndColor() {
        return mShaderEndColor;
    }

    public void setShaderEndColor(int mShaderEndColor) {
        this.mShaderEndColor = mShaderEndColor;
    }
}










