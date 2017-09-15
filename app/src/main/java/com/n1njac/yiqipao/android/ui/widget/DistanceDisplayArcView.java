package com.n1njac.yiqipao.android.ui.widget;

import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;

import com.n1njac.yiqipao.android.R;

/**
 * Created by huanglei on 2017/1/14.
 */

public class DistanceDisplayArcView extends View {

    //  开始角度
    private float startAngle = 135;
    //    转的度数
    private float sweepAngle = 270;

    private float borderWidth = 43f;

    private float currentAngle = 0;

    private String distance = "0";


    public DistanceDisplayArcView(Context context) {
        super(context);
    }

    public DistanceDisplayArcView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public DistanceDisplayArcView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
//        RectF rectf = new RectF(borderWidth + 110, borderWidth + 110, getWidth() - borderWidth - 110, getWidth() - borderWidth - 110);

        float left = getWidth() / 8;
//        float top = getHeight() / 2 - getWidth() / 4 - 150;
        float top = getHeight() / 2 - getWidth() / 8 * 3 - 100;
        float right = getWidth() / 2 + getWidth() / 8 * 3;
//        float bottom = getHeight() / 2 + getWidth() / 4 - 150;
        float bottom = getHeight() / 2 + getWidth() / 8 * 3 - 100;

        RectF rectf = new RectF(left, top, right, bottom);
        drawOutSideArc(canvas, rectf);
        drawInsideArc(canvas, rectf);
        drawNowDistance(canvas);
        drawStepText(canvas);
//        Log.d("xyz", "width:" + getWidth() + "height:" + getHeight());
    }


    private void drawOutSideArc(Canvas canvas, RectF rectf) {

        Paint paint = new Paint();
        paint.setColor(getResources().getColor(R.color.OutSideCircle));
//        设置绘制时各图形的结合方式，如平滑效果等
        paint.setStrokeJoin(Paint.Join.ROUND);
        paint.setStrokeWidth(borderWidth);
//        设置笔刷的图形样式
        paint.setStrokeCap(Paint.Cap.ROUND);
        paint.setStyle(Paint.Style.STROKE);
        paint.setAntiAlias(true);

        canvas.drawArc(rectf, startAngle, sweepAngle, false, paint);
    }

    private void drawInsideArc(Canvas canvas, RectF rectf) {
        Paint p = new Paint();
        p.setColor(getResources().getColor(R.color.inSideCircle));
        p.setStrokeCap(Paint.Cap.ROUND);
        p.setStrokeJoin(Paint.Join.ROUND);
        p.setStyle(Paint.Style.STROKE);
        p.setAntiAlias(true);
        p.setStrokeWidth(borderWidth);
        canvas.drawArc(rectf, startAngle, currentAngle, false, p);
    }

    private void drawNowDistance(Canvas canvas) {
        Paint p = new Paint();
//        p.setColor(getResources().getColor(R.color.text2));
        p.setColor(Color.WHITE);
        p.setTextSize(dip2px(50));
        p.setTextAlign(Paint.Align.CENTER);
        Typeface font = Typeface.create(Typeface.MONOSPACE, Typeface.NORMAL);
        p.setTypeface(font);
        canvas.drawText(distance, getWidth() / 2, getHeight() / 2 - 100, p);
    }

    private void drawStepText(Canvas canvas) {
        Paint p = new Paint();
        p.setColor(Color.GRAY);
        p.setTextSize(dip2px(18));
        p.setTextAlign(Paint.Align.CENTER);
        String content = "路程(km)";
        canvas.drawText(content, getWidth() / 2, getHeight() / 2, p);
    }

    //    dip to px
    private float dip2px(float dipValue) {
        float scale = getContext().getResources().getDisplayMetrics().density;
        return dipValue * scale + 0.5f;
    }

    public void setNowDistance(double totalDistance, double currentDistance) {

        if (currentDistance > totalDistance) {
            currentDistance = totalDistance;
        }
        distance = currentDistance + "";

        double percent = currentDistance / totalDistance;
        float current = (float) (percent * sweepAngle);
        setInsideArcAnimation(current);


    }

    private void setInsideArcAnimation(float current) {
        ValueAnimator valueAnimator = new ValueAnimator();
        valueAnimator.setDuration(3000);
        valueAnimator.setFloatValues(0, current);
        valueAnimator.setTarget(currentAngle);
        valueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                currentAngle = (float) animation.getAnimatedValue();
                invalidate();
            }
        });
        valueAnimator.start();

    }
}
