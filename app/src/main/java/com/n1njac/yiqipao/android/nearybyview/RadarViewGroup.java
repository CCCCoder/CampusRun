package com.n1njac.yiqipao.android.nearybyview;

import android.animation.ObjectAnimator;
import android.annotation.SuppressLint;
import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.util.SparseArray;
import android.view.View;
import android.view.ViewGroup;

import com.n1njac.yiqipao.android.R;

import java.util.List;

/**
 * Created by huanglei on 2017/3/22.
 */

//将雷达和小圆点结合
public class RadarViewGroup extends ViewGroup implements RadarView.IScanningListener {

    private int mWidth, mHeight;
    private List<PersonInfoBean> mBeanList;
    //    存放每个position的角度
    private SparseArray<Float> scanAngleArray = new SparseArray<>();
    private DotView currentShowChild;

    private IRadarClickListener iRadarClickListener;

    public void setRadarClickListener(IRadarClickListener iRadarClickListener) {
        this.iRadarClickListener = iRadarClickListener;
    }

    public RadarViewGroup(Context context) {
        this(context, null);
    }

    public RadarViewGroup(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public RadarViewGroup(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }


    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        setMeasuredDimension(measureSize(widthMeasureSpec), measureSize(heightMeasureSpec));
        mWidth = getMeasuredWidth();
        mHeight = getMeasuredHeight();
        mWidth = mHeight = Math.min(mWidth, mHeight);

//        测量每一个孩子
        measureChildren(widthMeasureSpec, heightMeasureSpec);
        for (int i = 0; i < getChildCount(); i++) {
            View child = getChildAt(i);
            if (child.getId() == R.id.radar_view) {
                RadarView radarView = (RadarView) child;
                radarView.setScanningListener(this);
//                只有当有数据的时候，才会扫描
                if (mBeanList != null && mBeanList.size() > 0) {
                    radarView.setMaxScanItemCount(mBeanList.size());
                    radarView.startScan();

                }
            }
        }
    }

    private int measureSize(int measureSpec) {
        int result = 0;
        int specMode = MeasureSpec.getMode(measureSpec);
        int specSize = MeasureSpec.getSize(measureSpec);
        if (specMode == MeasureSpec.EXACTLY) {
            result = specSize;
        } else {
            result = 300;
            if (specMode == MeasureSpec.AT_MOST) {
                result = Math.min(result, specSize);
            }
        }
        return result;
    }

    @SuppressLint("DrawAllocation")
    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
//        绘制雷达图
        View view = findViewById(R.id.radar_view);
        if (view != null) {
            view.layout(0, 0, view.getMeasuredWidth(), view.getMeasuredHeight());
        }

        for (int i = 0; i < getChildCount(); i++) {
            final int j = i;
            View child = getChildAt(i);
//            如果子view不是小圆点的话，就跳出，进行下一次for
            if (child.getId() == R.id.radar_view) continue;

            final DotView dotView = (DotView) child;
            dotView.setDisX((float) Math.cos(Math.toRadians(scanAngleArray.get(i - 1) - 5))
                    * (dotView.getProportion() * mWidth / 2));
            dotView.setDisY((float) Math.sin(Math.toRadians(scanAngleArray.get(i - 1) - 5))
                    * (dotView.getProportion() * mWidth / 2));

//            如果对应item的角度为0的话，说明还没有扫描到该item，不进行这个小圆点的layout
            Log.d("xyz", "scanAngleArray.get(i - 1):" + (i - 1) + ":" + scanAngleArray.get(i - 1));

            if (scanAngleArray.get(i - 1) == 0) continue;

            dotView.layout((int) dotView.getDisX() + mWidth / 2,
                    (int) dotView.getDisY() + mWidth / 2,
                    (int) dotView.getDisX() + mWidth / 2 + dotView.getMeasuredWidth(),
                    (int) dotView.getDisY() + mWidth / 2 + dotView.getMeasuredHeight());

            Log.d("tag", "dotView.getDisX():i:" + (i - 1) + ":" + dotView.getDisX());
            Log.d("tag", "dotView.getDisY():i:" + (i - 1) + ":" + dotView.getDisY());
            Log.d("tag", "dotView.getProportion():" + dotView.getProportion());


            dotView.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    resetAnim(currentShowChild);
                    currentShowChild = dotView;
                    startAnim(currentShowChild, j - 1);
                    iRadarClickListener.onRadarItemClick(j - 1);
                }
            });
        }
    }

    //    设置点击小圆点生成的效果
    private void startAnim(DotView view, int position) {
        if (view != null) {
            view.setCheckedIcon(mBeanList.get(position).getIconId());
            ObjectAnimator.ofFloat(view, "scaleX", 2f).setDuration(300).start();
            ObjectAnimator.ofFloat(view, "scaleY", 2f).setDuration(300).start();
        }
    }

    //    重置小圆点
    private void resetAnim(DotView view) {
        if (view != null) {
            view.clearIcon();
            ObjectAnimator.ofFloat(view, "scaleX", 1f).setDuration(300).start();
            ObjectAnimator.ofFloat(view, "scaleY", 1f).setDuration(300).start();
        }
    }


    //    设置数据
    public void setData(List<PersonInfoBean> personInfoBeenList) {
        this.mBeanList = personInfoBeenList;
        float max = Float.MIN_VALUE;
        for (int i = 0; i < personInfoBeenList.size(); i++) {
            PersonInfoBean infoBean = personInfoBeenList.get(i);
            if (infoBean.getDistance() > max) {
                max = infoBean.getDistance();
            }
//            设置数据的时候把所有的角度开始设置为0,扫描的时候才动态的更改数据
            scanAngleArray.put(i, 0f);

            DotView dotView = new DotView(getContext());
            if (personInfoBeenList.get(i).isSex()) {
                dotView.setPaintColor(getResources().getColor(R.color.bg_color_pink));
            } else {
                dotView.setPaintColor(getResources().getColor(R.color.bg_color_blue));
            }

            Log.d("aaa", "max:" + max);
            dotView.setProportion((personInfoBeenList.get(i).getDistance() / 9 + 0.6f) * 0.52f);

            addView(dotView);
        }

    }

    //    根据position，放大指定的CircleView小圆点
    public void setCurrentShowItem(int position) {
        DotView dotView = (DotView) getChildAt(position + 1);
        resetAnim(dotView);
        currentShowChild = dotView;
        startAnim(currentShowChild, position);
    }

    @Override
    public void onScanning(int position, float scanAngle) {
        if (scanAngle == 0) {
            scanAngleArray.put(position, 1f);
        } else {
            scanAngleArray.put(position, scanAngle);
        }
        requestLayout();
    }

    @Override
    public void onScanSuccess() {

    }

    public interface IRadarClickListener {
        void onRadarItemClick(int position);
    }
}
