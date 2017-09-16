package com.n1njac.yiqipao.android.ui.activity;

import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.n1njac.yiqipao.android.R;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by N1njaC on 2017/9/16.
 */

public class UserRunRecordActivity extends BaseActivity {


    private static final int COUNT_DURATION = 1000;

    @BindView(R.id.count_1_iv)
    ImageView count1Iv;
    @BindView(R.id.count_2_iv)
    ImageView count2Iv;
    @BindView(R.id.count_3_iv)
    ImageView count3Iv;
    @BindView(R.id.count_4_iv)
    ImageView count4Iv;
    @BindView(R.id.count_down_relat)
    RelativeLayout countDownRelat;

    private ArrayList<ImageView> mImageViews;
    private AnimatorSet mAnimatorSet;

    private Handler mHandler = new Handler();
    private int count = 0;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.run_record_act);
        //放在ButterKnife前面
        if (Build.VERSION.SDK_INT >= 21) {
            View decorView = getWindow().getDecorView();
            int option = View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN | View.SYSTEM_UI_FLAG_LAYOUT_STABLE;
            decorView.setSystemUiVisibility(option);
            getWindow().setStatusBarColor(Color.TRANSPARENT);
        }
        ButterKnife.bind(this);

        mImageViews = new ArrayList<>();
        mAnimatorSet = new AnimatorSet();
        mImageViews.add(0, count3Iv);
        mImageViews.add(1, count2Iv);
        mImageViews.add(2, count1Iv);
        mImageViews.add(3, count4Iv);
        mHandler.post(new Runnable() {
            @Override
            public void run() {

                if (count < 4) {
                    Log.d("xyz", count + "");
                    startAnimator(count, mAnimatorSet);
                    ++count;
                    mHandler.postDelayed(this, COUNT_DURATION);
                } else {
                    mHandler.removeCallbacks(this);
                    Log.d("xyz", "finish");
                    countDownRelat.setVisibility(View.GONE);
                }
            }
        });
    }

    public void startAnimator(int count, AnimatorSet set) {

        if (count > 0) {
            ImageView view = mImageViews.get(count - 1);
            view.clearAnimation();
            view.setVisibility(View.GONE);
        }

        ImageView view = mImageViews.get(count);
        view.setVisibility(View.VISIBLE);
        ObjectAnimator objectAnimatorX = ObjectAnimator.ofFloat(view, "scaleX", 0f, 1.1f, 1f);
        ObjectAnimator objectAnimatorY = ObjectAnimator.ofFloat(view, "scaleY", 0, 1.1f, 1f);
        ObjectAnimator alpha = ObjectAnimator.ofFloat(view, "alpha", 0, 1);
        set.playTogether(objectAnimatorX, objectAnimatorY, alpha);
        set.setDuration(COUNT_DURATION);
        set.start();
    }
}
