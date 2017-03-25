package com.n1njac.yiqipao.android.nearybyview;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.Shader;
import android.graphics.SweepGradient;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;

import com.n1njac.yiqipao.android.R;

/**
 * Created by huanglei on 2017/3/22.
 */

public class RadarView extends View {


    //   画圆的画笔
    private Paint mPaintCircle;
    //    画扫描的画笔
    private Paint mPaintScan;
    //    画圆线的画笔
    private Paint mPaintLine;

    //    宽高
    private int mWidth, mHeight;
    //    需要旋转的矩形
    private Matrix matrix = new Matrix();
    //    扫描的角度
    private int scanAngle;
    //    扫描渲染的shader
    private Shader scanShader;
    //    最中间的icon
    private Bitmap centerIcon;
    //    当前扫描的次数
    private int currentScanningCount;
    //    最大扫描次数
    private int maxScanItemCount;
    //    当前扫描的item个数
    private int currentScanningItemCount;

    //    扫描的速度
    private int scanSpeed = 5;
    //    每个圆圈的比例
    private float[] circleProportion = {1 / 13f, 2 / 13f, 3 / 13f, 4 / 13f, 5 / 13f, 6 / 13f};

    //    只有设置了数据后才会开始扫描
    private boolean startScan = false;
    private IScanningListener iScanningListener;


    //     扫描时监听的回调接口
    public void setScanningListener(IScanningListener iScanningListener) {
        this.iScanningListener = iScanningListener;
    }

    //    设置中心icon(默认)
    public int centerIconId = R.drawable.circle_photo;

    public void setCenterIconId(int centerIconId) {
        this.centerIconId = centerIconId;
    }

    public RadarView(Context context) {
        this(context, null);
    }


    public RadarView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public RadarView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
        post(run);
    }

    //    paint初始化
    private void init() {
        mPaintLine = new Paint();
        mPaintLine.setColor(getResources().getColor(R.color.bg_color_blue));
        mPaintLine.setStyle(Paint.Style.STROKE);
        mPaintLine.setStrokeWidth(1);
        mPaintLine.setAntiAlias(true);

        mPaintCircle = new Paint();
        mPaintCircle.setColor(Color.WHITE);
        mPaintCircle.setAntiAlias(true);

        mPaintScan = new Paint();
        mPaintScan.setStyle(Paint.Style.FILL_AND_STROKE);
    }

    //    用来每次更新旋转的矩形
    private Runnable run = new Runnable() {
        @Override
        public void run() {
            scanAngle = (scanAngle + scanSpeed) % 360;
//            每次旋转的角度及中心
            matrix.postRotate(scanSpeed, mWidth / 2, mHeight / 2);
//            通知view重绘
            invalidate();
//            通知自己，重复绘制
            postDelayed(run, 130);
            if (startScan && currentScanningCount <= (360 / scanSpeed)) {
                if (iScanningListener != null && currentScanningCount % scanSpeed == 0
                        && currentScanningItemCount < maxScanItemCount) {
                    iScanningListener.onScanning(currentScanningItemCount, scanAngle);
                    currentScanningItemCount++;
                    Log.d("xyz", "scanAngle:" + scanAngle);
                    Log.d("xyz", "currentScanningItemCount:" + currentScanningItemCount);
                } else if (iScanningListener != null && currentScanningItemCount == maxScanItemCount) {
                    iScanningListener.onScanSuccess();
                }
                currentScanningCount++;

            }
        }
    };


    @SuppressLint("DrawAllocation")
    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        mWidth = getMeasuredWidth();
        mHeight = getMeasuredHeight();
        mWidth = mHeight = Math.min(mWidth, mHeight);
//        设置中心icon
        centerIcon = BitmapFactory.decodeResource(getResources(), centerIconId);
        //设置扫描渲染的shader
        scanShader = new SweepGradient(mWidth / 2, mHeight / 2,
                new int[]{Color.TRANSPARENT, Color.parseColor("#84B5CA")}, null);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        drawCircle(canvas);
        drawScan(canvas);
        drawCenterIcon(canvas);

    }

    //    绘制圆线圈
    private void drawCircle(Canvas canvas) {
        for (int i = 1; i < circleProportion.length; i++) {
            canvas.drawCircle(mWidth / 2, mHeight / 2, mWidth * circleProportion[i], mPaintLine);
        }
    }

    //    绘制扫描
    private void drawScan(Canvas canvas) {
        // 画布的旋转变换 需要调用save() 和 restore()
        canvas.save();
        mPaintScan.setShader(scanShader);
//        用指定的矩阵预先计算当前矩阵。
        canvas.concat(matrix);
        canvas.drawCircle(mWidth / 2, mHeight / 2, mWidth * circleProportion[4], mPaintScan);
        canvas.restore();
    }

    //    绘制最中间的图标
    private void drawCenterIcon(Canvas canvas) {
        canvas.drawBitmap(centerIcon, null,
                new Rect((int) (mWidth / 2 - mWidth * circleProportion[0]), (int) (mHeight / 2 - mWidth * circleProportion[0]),
                        (int) (mWidth / 2 + mWidth * circleProportion[0]), (int) (mHeight / 2 + mWidth * circleProportion[0])), mPaintCircle);
    }

    public interface IScanningListener {
        //        正在扫描的时候回调
        void onScanning(int position, float scanAngle);

        //        扫描成功回调
        void onScanSuccess();
    }

    public void setMaxScanItemCount(int maxScanItemCount) {
        this.maxScanItemCount = maxScanItemCount;
    }

    public void startScan() {
        this.startScan = true;
    }
}
