package com.n1njac.yiqipao.android.ui.activity;

import android.animation.Animator;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.ServiceConnection;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.os.RemoteException;
import android.support.annotation.Nullable;
import android.support.annotation.Size;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TabHost;
import android.widget.TextView;
import android.widget.Toast;

import com.n1njac.yiqipao.android.IGpsStatusCallback;
import com.n1njac.yiqipao.android.IGpsStatusService;
import com.n1njac.yiqipao.android.R;
import com.n1njac.yiqipao.android.runengine.GpsStatusRemoteService;
import com.n1njac.yiqipao.android.ui.widget.RunDataTextView;
import com.n1njac.yiqipao.android.utils.FontCacheUtil;
import com.n1njac.yiqipao.android.utils.SizeUtil;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

import static com.n1njac.yiqipao.android.runengine.GpsStatusRemoteService.SIGNAL_BAD;
import static com.n1njac.yiqipao.android.runengine.GpsStatusRemoteService.SIGNAL_FULL;
import static com.n1njac.yiqipao.android.runengine.GpsStatusRemoteService.SIGNAL_GOOD;
import static com.n1njac.yiqipao.android.runengine.GpsStatusRemoteService.SIGNAL_NONE;

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
    @BindView(R.id.run_data_distance_tv)
    RunDataTextView runDataDistanceTv;
    @BindView(R.id.run_data_time_tv)
    RunDataTextView runDataTimeTv;
    @BindView(R.id.run_data_speed_tv)
    RunDataTextView runDataSpeedTv;
    @BindView(R.id.run_data_gps_iv)
    ImageView runDataGpsIv;
    @BindView(R.id.run_data_gps_prompt_tv)
    TextView runDataGpsPromptTv;
    @BindView(R.id.go_to_map_iv)
    ImageView goToMapIv;
    @BindView(R.id.run_data_stop_btn)
    Button runDataStopBtn;
    @BindView(R.id.run_data_pause_btn)
    Button runDataPauseBtn;
    @BindView(R.id.run_data_start_btn)
    Button runDataStartBtn;


    private ArrayList<ImageView> mImageViews;
    private AnimatorSet mAnimatorSet;

    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {

            int status = (int) msg.obj;
            switch (status) {
                case SIGNAL_FULL:


                    break;
                case SIGNAL_GOOD:

                    break;
                case SIGNAL_BAD:

                    break;
                case SIGNAL_NONE:

                    break;
                default:
                    break;
            }
        }
    };
    private int count = 0;
    private int currentStatus = 0;


    private IGpsStatusService mIGpsStatusService;

    private ServiceConnection gpsConn = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {

            mIGpsStatusService = IGpsStatusService.Stub.asInterface(service);
            try {
                mIGpsStatusService.registerCallback(iGpsStatusCallback);
            } catch (RemoteException e) {
                e.printStackTrace();
            }

        }

        @Override
        public void onServiceDisconnected(ComponentName name) {

            mIGpsStatusService = null;
        }
    };

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

        Typeface boldTf = FontCacheUtil.getFont(this, "fonts/Avenir_Next_Condensed_demi_bold.ttf");
        runDataDistanceTv.setTypeface(boldTf);
        runDataTimeTv.setTypeface(boldTf);
        runDataSpeedTv.setTypeface(boldTf);

        View rootRunDataLayout = findViewById(R.id.include_root_run_data);
        rootRunDataLayout.setPadding(0, SizeUtil.getStatusBarHeight(this), 0, 0);

        initCountDownAnimation();

        //绑定gps状态服务
        this.bindService(new Intent(UserRunRecordActivity.this, GpsStatusRemoteService.class), gpsConn, Context.BIND_AUTO_CREATE);


    }


    private IGpsStatusCallback iGpsStatusCallback = new IGpsStatusCallback.Stub() {
        @Override
        public void gpsStatus(int status) throws RemoteException {

            switch (status) {
                case SIGNAL_FULL:

                    currentStatus = SIGNAL_FULL;

                    Message msg = Message.obtain();
                    msg.obj = SIGNAL_FULL;
                    mHandler.sendMessage(msg);

                    break;
                case SIGNAL_GOOD:

                    currentStatus = SIGNAL_GOOD;

                    Message msg2 = Message.obtain();
                    msg2.obj = SIGNAL_GOOD;
                    mHandler.sendMessage(msg2);

                    break;
                case SIGNAL_BAD:

                    currentStatus = SIGNAL_BAD;

                    Message msg3 = Message.obtain();
                    msg3.obj = SIGNAL_BAD;
                    mHandler.sendMessage(msg3);

                    break;
                case SIGNAL_NONE:

                    currentStatus = SIGNAL_NONE;

                    Message msg4 = Message.obtain();
                    msg4.obj = SIGNAL_NONE;
                    mHandler.sendMessage(msg4);

                    break;
            }


        }
    };

    private void initCountDownAnimation() {
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

    @Override
    protected void onDestroy() {
        super.onDestroy();
        this.unbindService(gpsConn);
        if (mIGpsStatusService != null) {
            try {
                mIGpsStatusService.unRegisterCallback(iGpsStatusCallback);
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        }
    }

    @OnClick({R.id.go_to_map_iv, R.id.run_data_stop_btn, R.id.run_data_pause_btn, R.id.run_data_start_btn})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.go_to_map_iv:
                break;
            case R.id.run_data_stop_btn:

                handleRunData();

                break;
            case R.id.run_data_pause_btn:

                showStartAndStopBtnAnimation();

                break;
            case R.id.run_data_start_btn:

                showPauseBtnAnimation();

                break;
        }
    }

    //隐藏继续和结束按钮，开始计时跑步
    private void showPauseBtnAnimation() {

        AnimatorSet set = new AnimatorSet();
        ObjectAnimator startBtnAnimation = ObjectAnimator.ofFloat(runDataStartBtn, "translationX", 0, -SizeUtil.dp2px(getApplication(), 85));
        ObjectAnimator stopBtnAnimation = ObjectAnimator.ofFloat(runDataStopBtn, "translationX", 0, SizeUtil.dp2px(getApplication(), 85));
        set.setDuration(500);
        set.playTogether(startBtnAnimation, stopBtnAnimation);
        set.start();
        set.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {

            }

            @Override
            public void onAnimationEnd(Animator animation) {
                runDataStartBtn.setVisibility(View.INVISIBLE);
                runDataStopBtn.setVisibility(View.INVISIBLE);
                runDataPauseBtn.setVisibility(View.VISIBLE);
            }

            @Override
            public void onAnimationCancel(Animator animation) {

            }

            @Override
            public void onAnimationRepeat(Animator animation) {

            }
        });

    }

    //隐藏暂停按钮，暂停跑步
    private void showStartAndStopBtnAnimation() {
        runDataPauseBtn.setVisibility(View.INVISIBLE);
        runDataStartBtn.setVisibility(View.VISIBLE);
        runDataStopBtn.setVisibility(View.VISIBLE);
        AnimatorSet set = new AnimatorSet();
        ObjectAnimator startBtnAnimation = ObjectAnimator.ofFloat(runDataStartBtn, "translationX", -SizeUtil.dp2px(getApplication(), 85), 0);
        ObjectAnimator stopBtnAnimation = ObjectAnimator.ofFloat(runDataStopBtn, "translationX", SizeUtil.dp2px(getApplication(), 85), 0);
        set.playTogether(startBtnAnimation, stopBtnAnimation);
        set.setDuration(500);
        set.start();
    }

    private void handleRunData() {
        String distance = runDataDistanceTv.getText().toString();
        float totalDistance = Float.parseFloat(distance);

        if (totalDistance < 0.1) {
            new AlertDialog.Builder(this)
                    .setTitle("温馨提示")
                    .setMessage("此次运动距离过短,无法保存记录")
                    .setNegativeButton("取消", null)
                    .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            finish();
                        }
                    })
                    .show();
        } else {
            // TODO: 2017/9/19 记录跑步数据，然后退出。
        }
    }

    @Override
    public void onBackPressed() {
        Toast.makeText(this, "请先结束运动", Toast.LENGTH_SHORT).show();

    }
}
