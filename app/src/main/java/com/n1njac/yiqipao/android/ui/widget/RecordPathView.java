package com.n1njac.yiqipao.android.ui.widget;
/*    
 *    Created by N1njaC on 2017/9/27.
 *    email:aiai173cc@gmail.com 
 */

import android.animation.Animator;
import android.animation.TypeEvaluator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PathMeasure;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.AccelerateDecelerateInterpolator;

import com.n1njac.yiqipao.android.R;
import com.n1njac.yiqipao.android.utils.ParseUtil;
import com.n1njac.yiqipao.android.utils.RecordPathUtil;

import java.util.List;

public class RecordPathView extends View {

    private static final String TAG = RecordPathView.class.getSimpleName();

    private Paint mLinePaint, mIConPaint;
    private Bitmap mStartIcon, mEndIcon, mMidIcon;

    private float[] mStartPathPoint, mEndPathPoint, mDstPathPoint;
    private Path mDstPath;

    private boolean isDrawingPath = false;

    private float mAllPathLength;
    private long mDurationTime;
    private List<RecordPathUtil.RecordPathBean> mTotalPathList;
    private Path mWholePath;
    private PathMeasure mWholePathMeasure;

    private float mAnimValue;

    private int currentPathIndex = 0;

    private PathMeasure mDstPathMeasure;


    private onAnimCallback mCallback;


    public RecordPathView(Context context) {
        this(context, null);

    }

    public RecordPathView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);

    }

    public RecordPathView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    public void setAnimCallback(onAnimCallback callback) {
        this.mCallback = callback;
    }

    private void init(Context context) {
        mLinePaint = new Paint();
        mLinePaint.setAntiAlias(true);
        mLinePaint.setStyle(Paint.Style.STROKE);
        mLinePaint.setStrokeWidth(10);

        mIConPaint = new Paint();
        mIConPaint.setAntiAlias(true);

        mStartIcon = BitmapFactory.decodeResource(context.getResources(), R.drawable.start_point_icon);
        mEndIcon = BitmapFactory.decodeResource(context.getResources(), R.drawable.end_point_icon);
        mMidIcon = BitmapFactory.decodeResource(context.getResources(), R.drawable.his_anim_icon);

        mStartPathPoint = new float[2];
        mEndPathPoint = new float[2];
        mDstPathPoint = new float[2];

        mDstPath = new Path();
    }

    public void setPath(RecordPathUtil recordPathUtil) {
        if (recordPathUtil == null) return;

        if (!isDrawingPath) {
            isDrawingPath = true;
            mAllPathLength = recordPathUtil.getAllPathLength();
            Log.d(TAG, "mAllPathLength:" + mAllPathLength);
            mDurationTime = recordPathUtil.getTotalAnimationTime();
            mTotalPathList = recordPathUtil.getTotalPathList();
            mWholePath = recordPathUtil.getWholePath();
            mWholePathMeasure = new PathMeasure(mWholePath, false);
            //轨迹的起点坐标
            mWholePathMeasure.getPosTan(0, mStartPathPoint, null);
            //终点坐标
            mWholePathMeasure.getPosTan(mWholePathMeasure.getLength(), mEndPathPoint, null);
            if (mTotalPathList == null || mTotalPathList.size() == 0) return;
            startAnim();
        }
    }

    private void startAnim() {

        ValueAnimator animator = ValueAnimator.ofObject(new DstPathEvaluator(), 0, mWholePathMeasure.getLength());
        Log.d(TAG, "mWholePathMeasure.getLength():" + mWholePathMeasure.getLength());
        animator.setDuration(mDurationTime);
        animator.setInterpolator(new AccelerateDecelerateInterpolator());
        animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                mAnimValue = (float) animation.getAnimatedValue();
                Log.d(TAG, "value:" + mAnimValue);
                calculateAnimData();
                invalidate();
            }
        });

        animator.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {

            }

            @Override
            public void onAnimationEnd(Animator animation) {

                if (mCallback != null) {
                    mCallback.onAnimFinish();
                }

            }

            @Override
            public void onAnimationCancel(Animator animation) {

            }

            @Override
            public void onAnimationRepeat(Animator animation) {

            }
        });
        animator.start();

    }

    private void calculateAnimData() {
        float length = mAnimValue * mAllPathLength;
        Log.d(TAG, "length:" + length);
        //需要多少段的path的长度
        float needPathLength = 0;
        //当某段前面所有的长度刚好大于动画计算所需的长度，这一段肯定有剩余长度，表示为offsetLength
        float offsetLength = 0;
        for (int i = 0; i < mTotalPathList.size(); i++) {
            needPathLength += mTotalPathList.get(i).getPathLength();
            Log.d(TAG, "needPathLength:" + needPathLength);
            if (needPathLength > length) {
                //这一段path有剩余，记录下标
                currentPathIndex = i;
                offsetLength = needPathLength - length;
                break;
            }
        }
        if (mDstPath != null) {
            mDstPath.reset();
        }
        Log.d(TAG, "currentPathIndex:" + currentPathIndex);
        PathMeasure pathMeasure = new PathMeasure(mTotalPathList.get(currentPathIndex).getPath(), false);
        //将这一有剩余段的前部分（需要绘制的部分）提取出来放到mDstPath中
        pathMeasure.getSegment(0, mTotalPathList.get(currentPathIndex).getPathLength() - offsetLength, mDstPath, true);
        mDstPathMeasure = new PathMeasure(mDstPath, false);
        //将mDstPath的末尾的坐标放到mDstPathPoint中
        mDstPathMeasure.getPosTan(mDstPathMeasure.getLength(), mDstPathPoint, null);

    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if (mTotalPathList == null || mTotalPathList.size() == 0) return;
        if (currentPathIndex > 0) {

            for (int i = 0; i < currentPathIndex; i++) {
                RecordPathUtil.RecordPathBean recordPathBean = mTotalPathList.get(i);
                mLinePaint.setStrokeWidth(10);
                mLinePaint.setStyle(Paint.Style.STROKE);
                mLinePaint.setColor(recordPathBean.getEndColor());
                mLinePaint.setShader(recordPathBean.getShader());
                canvas.drawPath(recordPathBean.getPath(), mLinePaint);
                mLinePaint.setShader(null);
                mLinePaint.setStrokeWidth(1);
                mLinePaint.setStyle(Paint.Style.FILL_AND_STROKE);
                canvas.drawCircle(recordPathBean.getEndPoint().x, recordPathBean.getEndPoint().y, 5, mLinePaint);
            }

        }

        mLinePaint.setShader(mTotalPathList.get(currentPathIndex).getShader());
        canvas.drawPath(mDstPath, mLinePaint);
        canvas.drawBitmap(mStartIcon, mStartPathPoint[0] - mStartIcon.getWidth() / 2, mStartPathPoint[1] - mStartIcon.getHeight() / 2, mIConPaint);
        if (mAnimValue >= 1) {
            canvas.drawBitmap(mEndIcon, mEndPathPoint[0] - mEndIcon.getWidth() / 2, mEndPathPoint[1] - mEndIcon.getHeight() / 2, mIConPaint);
        } else {
            canvas.drawBitmap(mMidIcon, mDstPathPoint[0] - mMidIcon.getWidth() / 2, mDstPathPoint[1] - mMidIcon.getHeight() / 2, mIConPaint);
        }
    }

    private class DstPathEvaluator implements TypeEvaluator {

        @Override
        public Object evaluate(float fraction, Object startValue, Object endValue) {
            return fraction;
        }
    }

    public interface onAnimCallback {
        void onAnimFinish();
    }

    private float x, y;

    @Override
    public boolean onTouchEvent(MotionEvent event) {

        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:

                x = event.getX();
                y = event.getY();
                break;

            case MotionEvent.ACTION_UP:
                break;
            case MotionEvent.ACTION_CANCEL:

                if (Math.abs(x - event.getX()) > 0 || Math.abs(y - event.getY()) > 0) {
                    if (mCallback != null) {
                        mCallback.onAnimFinish();
                    }
                }

                break;

        }

        return true;
    }
}
