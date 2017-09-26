package com.n1njac.yiqipao.android.ui.activity;

import android.animation.Animator;
import android.animation.ObjectAnimator;
import android.content.Context;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.util.TypedValue;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.n1njac.yiqipao.android.R;
import com.n1njac.yiqipao.android.bmobObject.RunDataBmob;
import com.n1njac.yiqipao.android.ui.widget.RunDataTextView;
import com.n1njac.yiqipao.android.utils.SizeUtil;

import butterknife.ButterKnife;
import butterknife.BindView;
import butterknife.OnClick;

/**
 * Created by N1njaC on 2017/9/25.
 */

public class HistoryRunRecordActivity extends BaseActivity implements View.OnTouchListener, GestureDetector.OnGestureListener {
    @BindView(R.id.map_relat)
    RelativeLayout mapRelat;
    @BindView(R.id.his_detail_distance_tv)
    RunDataTextView hisDetailDistanceTv;
    @BindView(R.id.his_detail_duration_tv)
    RunDataTextView hisDetailDurationTv;
    @BindView(R.id.his_detail_avspeed_tv)
    RunDataTextView hisDetailAvspeedTv;
    @BindView(R.id.his_detail_calorie_tv)
    RunDataTextView hisDetailCalorieTv;
    @BindView(R.id.his_detail_slowest_speed_tv)
    RunDataTextView hisDetailSlowestSpeedTv;
    @BindView(R.id.his_detail_fast_speed_tv)
    RunDataTextView hisDetailFastSpeedTv;
    @BindView(R.id.his_detail_back_iv)
    ImageView hisDetailBackIv;
    @BindView(R.id.his_detail_delete_iv)
    ImageView hisDetailDeleteIv;
    @BindView(R.id.his_detail_share_iv)
    ImageView hisDetailShareIv;
    @BindView(R.id.big_his_detail_distance_tv)
    RunDataTextView bigHisDetailDistanceTv;
    @BindView(R.id.big_his_detail_duration_tv)
    RunDataTextView bigHisDetailDurationTv;
    @BindView(R.id.big_his_detail_avspeed_tv)
    RunDataTextView bigHisDetailAvspeedTv;
    @BindView(R.id.big_his_detail_calorie_tv)
    RunDataTextView bigHisDetailCalorieTv;
    @BindView(R.id.big_his_detail_slowest_speed_tv)
    RunDataTextView bigHisDetailSlowestSpeedTv;
    @BindView(R.id.big_his_detail_fast_speed_tv)
    RunDataTextView bigHisDetailFastSpeedTv;
    @BindView(R.id.big_his_prompt)
    TextView bigHisPrompt;
    @BindView(R.id.big_his_tool_bar)
    RelativeLayout bigHisToolBar;
    @BindView(R.id.big_his_root)
    RelativeLayout bigHisRoot;
    @BindView(R.id.his_content_root)
    RelativeLayout hisContentRoot;
    @BindView(R.id.his_tool_bar_title)
    TextView hisToolBarTitle;

    private static final String TAG = HistoryRunRecordActivity.class.getSimpleName();
    private static final int FLING_MIN_DISTANCE = 50;
    private static final int FLING_MIN_VELOCITY = 200;

    //屏幕高度
    private int mHeight;
    //内容高度
    private int mContentHeight;
    //action bar高度
    private int mActionBarHeight;
    //状态栏高度
    private int mStatusBarHeight;

    private GestureDetector mDetector;

    private boolean isBigContent = false;

    private int count = 0;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.his_run_record_act);
        ButterKnife.bind(this);
        RunDataBmob runData = (RunDataBmob) getIntent().getSerializableExtra("run_data");
        getWindow().setStatusBarColor(getResources().getColor(R.color.colorPrimary));
        mDetector = new GestureDetector(this, this);
        hisContentRoot.setLongClickable(true);
        hisContentRoot.setOnTouchListener(this);

    }

    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
        TypedValue tv = new TypedValue();
        int actionBarHeight = 50;
        if (getTheme().resolveAttribute(android.R.attr.actionBarSize, tv, true)) {
            actionBarHeight = TypedValue.complexToDimensionPixelSize(tv.data, getResources().getDisplayMetrics());

            Log.d(TAG, "action bar height:" + actionBarHeight);
            Log.d(TAG, "status bar height:" + SizeUtil.getStatusBarHeight(this));
        }

        WindowManager wm = (WindowManager) this.getSystemService(Context.WINDOW_SERVICE);
        int height = wm.getDefaultDisplay().getHeight();
        int height2 = SizeUtil.dp2px(this, 223);
        Log.d(TAG, "height:" + height);
        Log.d(TAG, "height2:" + height2);

        mActionBarHeight = actionBarHeight;
        mStatusBarHeight = SizeUtil.getStatusBarHeight(this);
        mContentHeight = SizeUtil.dp2px(this, 223);
        mHeight = height;
    }

    @OnClick({R.id.his_detail_back_iv, R.id.his_detail_delete_iv, R.id.his_detail_share_iv})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.his_detail_back_iv:
                break;
            case R.id.his_detail_delete_iv:
                break;
            case R.id.his_detail_share_iv:
                break;
        }
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        return mDetector.onTouchEvent(event);
    }

    @Override
    public boolean onDown(MotionEvent e) {
        return false;
    }

    @Override
    public void onShowPress(MotionEvent e) {

    }

    @Override
    public boolean onSingleTapUp(MotionEvent e) {
        return false;
    }

    @Override
    public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {
        return false;
    }

    @Override
    public void onLongPress(MotionEvent e) {

    }

    @Override
    public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {

        if (e1.getY() - e2.getY() > FLING_MIN_DISTANCE && Math.abs(velocityY) > FLING_MIN_VELOCITY) {
            Log.d(TAG, "上滑");

            isBigContent = true;

            bigHisRoot.setVisibility(View.VISIBLE);
//            ObjectAnimator animator = ObjectAnimator.ofFloat(bigHisRoot, "translationY", 834 - 50 - 112, 0);
            ObjectAnimator animator = ObjectAnimator.ofFloat(bigHisRoot, "translationY", mHeight - mContentHeight - mStatusBarHeight - mActionBarHeight, 0);
            animator.setDuration(1000);
            animator.addListener(new Animator.AnimatorListener() {
                @Override
                public void onAnimationStart(Animator animation) {

                }

                @Override
                public void onAnimationEnd(Animator animation) {
                    getWindow().setStatusBarColor(getResources().getColor(R.color.colorPrimary));

                    visibleToolBar();

                }

                @Override
                public void onAnimationCancel(Animator animation) {

                }

                @Override
                public void onAnimationRepeat(Animator animation) {

                }
            });
            animator.start();


        } else if (e2.getY() - e1.getY() > FLING_MIN_DISTANCE && Math.abs(velocityY) > FLING_MIN_VELOCITY) {

            Log.d(TAG, "下滑");

        }

        return true;
    }


    private void setToolBarTransparent() {

        bigHisToolBar.setBackgroundResource(R.color.bg_transparent);
        hisDetailBackIv.setVisibility(View.GONE);
        hisDetailDeleteIv.setVisibility(View.GONE);
        hisDetailShareIv.setVisibility(View.GONE);
        hisToolBarTitle.setVisibility(View.GONE);

    }

    private void visibleToolBar() {
        bigHisToolBar.setBackgroundResource(R.color.colorPrimary);
        hisDetailBackIv.setVisibility(View.VISIBLE);
        hisDetailDeleteIv.setVisibility(View.VISIBLE);
        hisDetailShareIv.setVisibility(View.VISIBLE);
        hisToolBarTitle.setVisibility(View.VISIBLE);
    }

    @Override
    public void onBackPressed() {
        count++;
        if (isBigContent) {
            isBigContent = false;
            setToolBarTransparent();
            Log.d(TAG, "count:" + count);
            ObjectAnimator animator = ObjectAnimator.ofFloat(bigHisRoot, "translationY", 0, mHeight - mContentHeight - mStatusBarHeight - mActionBarHeight);

            animator.setDuration(1000);
            animator.addListener(new Animator.AnimatorListener() {
                @Override
                public void onAnimationStart(Animator animation) {

                }

                @Override
                public void onAnimationEnd(Animator animation) {
                    bigHisRoot.setVisibility(View.GONE);

                }

                @Override
                public void onAnimationCancel(Animator animation) {

                }

                @Override
                public void onAnimationRepeat(Animator animation) {

                }
            });
            animator.start();
        } else {
            finish();
        }
    }
}
