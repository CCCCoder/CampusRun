package com.n1njac.yiqipao.android.ui.widget;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.view.View;

import com.n1njac.yiqipao.android.R;
import com.n1njac.yiqipao.android.utils.DisplayUtils;

/**
 * Created by huanglei on 2017/3/22.
 */

/*
 雷达上小圆点的绘制，两种情况，一种即扫描出来的小圆点，另外一种即viewpager选中小圆点变成头像的绘制
 */
public class DotView extends View {

    private Paint mPaint;
    //    选中时候的图片
    private Bitmap mBitmap;

    //    小圆点半径
    private float radius = DisplayUtils.dip2px(getContext(), 9);
    //    坐标
    private float disX;
    private float disY;
    //    旋转的角度
    private float angle;
    //    根据远近距离的不同计算得到的应该占的半径比例
    private float proportion;

    public float getDisX() {
        return disX;
    }

    public void setDisX(float disX) {
        this.disX = disX;
    }

    public float getDisY() {
        return disY;
    }

    public void setDisY(float disY) {
        this.disY = disY;
    }

    public float getAngle() {
        return angle;
    }

    public void setAngle(float angle) {
        this.angle = angle;
    }

    public float getProportion() {
        return proportion;
    }

    public void setProportion(float proportion) {
        this.proportion = proportion;
    }

    public DotView(Context context) {
        this(context, null);
    }

    public DotView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public DotView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        mPaint = new Paint();
        mPaint.setColor(getResources().getColor(R.color.bg_color_pink));
        mPaint.setAntiAlias(true);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        setMeasuredDimension(measureSize(widthMeasureSpec), measureSize(heightMeasureSpec));
    }

    private int measureSize(int measureSpec) {

        int result = 0;
        int specMode = MeasureSpec.getMode(measureSpec);
        int specSize = MeasureSpec.getSize(measureSpec);
        if (specMode == MeasureSpec.EXACTLY) {
            result = specSize;
        } else {
            result = DisplayUtils.dip2px(getContext(), 18);
            if (specMode == MeasureSpec.AT_MOST) {
                result = Math.min(result, specSize);
            }
        }
        return result;
    }

    @Override
    protected void onDraw(Canvas canvas) {
//        绘制小圆点
        canvas.drawCircle(radius, radius, radius, mPaint);
        if (mBitmap != null) {
            canvas.drawBitmap(mBitmap, null, new Rect(0, 0, 2 * (int) radius, 2 * (int) radius), mPaint);
        }
    }

    public void setPaintColor(int color) {
        mPaint.setColor(color);
        invalidate();
    }

//    设置点击小圆点变成图像
    public void setCheckedIcon(int resId) {
        mBitmap = BitmapFactory.decodeResource(getResources(), resId);
        invalidate();
    }
    public void clearIcon(){
        mBitmap = null;
        invalidate();
    }
}
