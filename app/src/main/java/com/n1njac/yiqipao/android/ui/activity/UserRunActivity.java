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
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.View;
import android.view.ViewAnimationUtils;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.amap.api.maps.AMap;
import com.amap.api.maps.CameraUpdateFactory;
import com.amap.api.maps.MapView;
import com.amap.api.maps.model.LatLng;
import com.amap.api.maps.model.MyLocationStyle;
import com.amap.api.maps.model.PolylineOptions;
import com.amap.api.trace.LBSTraceClient;
import com.n1njac.yiqipao.android.IGpsStatusCallback;
import com.n1njac.yiqipao.android.IGpsStatusService;
import com.n1njac.yiqipao.android.IRunDataCallback;
import com.n1njac.yiqipao.android.IRunDataService;
import com.n1njac.yiqipao.android.R;
import com.n1njac.yiqipao.android.bean.LocationBean;
import com.n1njac.yiqipao.android.bmobObject.RunDataBmob;
import com.n1njac.yiqipao.android.bmobObject.UserInfoBmob;
import com.n1njac.yiqipao.android.runengine.GpsStatusRemoteService;
import com.n1njac.yiqipao.android.runengine.RunningCoreRemoteService;
import com.n1njac.yiqipao.android.ui.widget.RunDataTextView;
import com.n1njac.yiqipao.android.utils.CalculateUtil;
import com.n1njac.yiqipao.android.utils.FontCacheUtil;
import com.n1njac.yiqipao.android.utils.ParseUtil;
import com.n1njac.yiqipao.android.utils.SizeUtil;
import com.n1njac.yiqipao.android.utils.TimeUtil;
import com.n1njac.yiqipao.android.utils.ToastUtil;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.bmob.v3.BmobUser;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.SaveListener;

import static com.n1njac.yiqipao.android.runengine.GpsStatusRemoteService.SIGNAL_BAD;
import static com.n1njac.yiqipao.android.runengine.GpsStatusRemoteService.SIGNAL_FULL;
import static com.n1njac.yiqipao.android.runengine.GpsStatusRemoteService.SIGNAL_GOOD;
import static com.n1njac.yiqipao.android.runengine.GpsStatusRemoteService.SIGNAL_NONE;

/**
 * Created by N1njaC on 2017/9/16.
 */

public class UserRunActivity extends BaseActivity {


    private static final String TAG = UserRunActivity.class.getSimpleName();
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
    @BindView(R.id.map_gps_iv)
    ImageView mapGpsIv;
    @BindView(R.id.map_gps_prompt_tv)
    TextView mapGpsPromptTv;
    @BindView(R.id.back_run_data_iv)
    ImageView backRunDataIv;
    @BindView(R.id.run_map_time_tv)
    RunDataTextView runMapTimeTv;
    @BindView(R.id.run_map_distance_tv)
    RunDataTextView runMapDistanceTv;
    @BindView(R.id.run_map_speed_tv)
    RunDataTextView runMapSpeedTv;
    @BindView(R.id.run_map_stop_btn)
    Button runMapStopBtn;
    @BindView(R.id.run_map_pause_btn)
    Button runMapPauseBtn;
    @BindView(R.id.run_map_start_btn)
    Button runMapStartBtn;
    @BindView(R.id.run_map_mv)
    MapView mMapView;


    private ArrayList<ImageView> mImageViews;
    private AnimatorSet mAnimatorSet;
    private WindowManager mWindowManager;

    private int mWidth, mHeight;
    private float centerX, centerY;
    private float mRadius;
    private Animator mCircularReveal;

    private View rootRunMapLayout, rootRunDataLayout;

    private Typeface boldTf;

    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {

            int status = (int) msg.obj;
            switch (status) {
                case SIGNAL_FULL:

                    runDataGpsIv.setImageResource(R.drawable.gps_3);
                    mapGpsIv.setImageResource(R.drawable.gps_3);
                    runDataGpsPromptTv.setText("");
                    mapGpsPromptTv.setText("");

                    break;
                case SIGNAL_GOOD:

                    runDataGpsIv.setImageResource(R.drawable.gps_2);
                    mapGpsIv.setImageResource(R.drawable.gps_2);
                    runDataGpsPromptTv.setText("");
                    mapGpsPromptTv.setText("");
                    break;
                case SIGNAL_BAD:
                    runDataGpsIv.setImageResource(R.drawable.gps_1);
                    mapGpsIv.setImageResource(R.drawable.gps_1);
                    runDataGpsPromptTv.setText(R.string.gps_search_small);
                    mapGpsPromptTv.setText(R.string.gps_search_small);
                    break;
                case SIGNAL_NONE:
                    runDataGpsIv.setImageResource(R.drawable.gps_0);
                    mapGpsIv.setImageResource(R.drawable.gps_0);
                    runDataGpsPromptTv.setText(R.string.gps_def_prompt);
                    mapGpsPromptTv.setText(R.string.gps_def_prompt);
                    break;
                default:
                    break;
            }
        }
    };
    private int count = 0;
    private int currentStatus = 0;

    private AMap aMap;
    private LBSTraceClient mLBSTraceClient;
    private IGpsStatusService mIGpsStatusService;
    private IRunDataService mIRunDataService;

    //跑步路程
    private double mKM;
    //跑步轨迹点
    private List<LatLng> mPoints;
    //平均配速
    private float mAvSpeed;
    //开始跑步时间
    private long mStartRunTime;
    //跑步用时
    private String mDurationTime;

    //跑步计数器
    private Timer mTimer;
    private boolean isPause = false;
    private int timeCount = -4;

    private ServiceConnection gpsConn = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            Log.d(TAG, "gps onServiceConnected");
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


    private ServiceConnection runDataConn = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {

            Log.d(TAG, "run data onServiceConnected");
            mIRunDataService = IRunDataService.Stub.asInterface(service);
            try {
                mIRunDataService.registerRunDataCallback(iRunDataCallback);
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            mIRunDataService = null;
        }
    };

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d(TAG,"-------------------------onCreate-----------------------");
        setContentView(R.layout.run_record_act);
        //放在ButterKnife前面
        if (Build.VERSION.SDK_INT >= 21) {
            View decorView = getWindow().getDecorView();
            int option = View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN | View.SYSTEM_UI_FLAG_LAYOUT_STABLE;
            decorView.setSystemUiVisibility(option);
            getWindow().setStatusBarColor(Color.TRANSPARENT);
        }
        ButterKnife.bind(this);
        boldTf = FontCacheUtil.getFont(this, "fonts/Avenir_Next_Condensed_demi_bold.ttf");

        initRunDataLayout();
        initMapLayout(savedInstanceState);
        initCountDownAnimation();

        mStartRunTime = System.currentTimeMillis();

        mWindowManager = (WindowManager) getSystemService(WINDOW_SERVICE);

        //计时器
        mTimer = new Timer();
        mTimer.schedule(runCountTask, 1000, 1000);

        this.bindService(new Intent(UserRunActivity.this, GpsStatusRemoteService.class), gpsConn, Context.BIND_AUTO_CREATE);
        this.bindService(new Intent(UserRunActivity.this, RunningCoreRemoteService.class), runDataConn, Context.BIND_AUTO_CREATE);

    }

    @Override
    protected void onRestart() {
        super.onRestart();
        Log.d(TAG,"-------------------------onRestart-----------------------");
    }

    @Override
    protected void onStart() {
        super.onStart();
        Log.d(TAG,"-------------------------onStart-----------------------");
    }

    @Override
    protected void onStop() {
        super.onStop();
        Log.d(TAG,"-------------------------onStop-----------------------");
    }

    //计时器
    private TimerTask runCountTask = new TimerTask() {
        @Override
        public void run() {
            if (!isPause) {
                timeCount++;
                Log.d(TAG, "time count:" + timeCount);
                String hour = String.valueOf(timeCount / 3600);
                String minute = String.valueOf(timeCount / 60);
                String second = String.valueOf(timeCount % 60);

                String secondStr = second.length() == 1 ? "0" + second : second;
                String minuteStr = minute.length() == 1 ? "0" + minute : minute;
                String hourStr = hour.length() == 1 ? "0" + hour : hour;

                final String countTime = hourStr + ":" + minuteStr + ":" + secondStr;

                mDurationTime = countTime;
                Log.d(TAG, "count time:" + countTime);
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        runDataTimeTv.setText(countTime);
                        runMapTimeTv.setText(countTime);
                    }
                });

            }
        }
    };

    private void initRunDataLayout() {
        rootRunDataLayout = findViewById(R.id.include_root_run_data);
        rootRunDataLayout.setPadding(0, SizeUtil.getStatusBarHeight(this), 0, 0);
        runDataDistanceTv.setTypeface(boldTf);
        runDataTimeTv.setTypeface(boldTf);
        runDataSpeedTv.setTypeface(boldTf);
    }

    private void initMapLayout(Bundle savedInstanceState) {
        rootRunMapLayout = findViewById(R.id.include_root_run_map);
        rootRunMapLayout.setVisibility(View.GONE);
        runMapDistanceTv.setTypeface(boldTf);
        runMapSpeedTv.setTypeface(boldTf);
        runMapTimeTv.setTypeface(boldTf);
        mMapView.onCreate(savedInstanceState);
        initMap();
    }

    //地图相关属性
    private void initMap() {
        aMap = mMapView.getMap();
        mLBSTraceClient = LBSTraceClient.getInstance(this);
        //初始化定位蓝点样式类myLocationStyle.myLocationType(MyLocationStyle.LOCATION_TYPE_LOCATION_ROTATE);
        // 连续定位、且将视角移动到地图中心点，并且会跟随设备移动。
        MyLocationStyle locationStyle = new MyLocationStyle();
        locationStyle.myLocationType(MyLocationStyle.LOCATION_TYPE_FOLLOW);
        locationStyle.interval(3000);
        locationStyle.strokeColor(Color.argb(0, 0, 0, 0));
        locationStyle.radiusFillColor(Color.argb(0, 0, 0, 0));
        aMap.setMyLocationStyle(locationStyle);
        aMap.setMyLocationEnabled(true);
        aMap.moveCamera(CameraUpdateFactory.zoomTo(17));

    }


//
//
//    //定位信息回调
//    // 三秒钟轨迹纠正一次，回传结果绘制轨迹和计算路程。暂停的话 轨迹不绘制，定位照常。(调整到服务中实现)
//    @Override
//    public void onMyLocationChange(Location location) {
//
//        double latitude = location.getLatitude();
//        double longitude = location.getLongitude();
//
//        float bear = location.getBearing();
//        float speed = location.getSpeed();
//        long time = location.getTime();
//
//        LatLng latLng = new LatLng(latitude, longitude);
//        latLngs.add(latLng);
//
//        //用一个list将traceLocation保存，每隔3s请求一次轨迹纠偏以获得已跑的路程。
//        TraceLocation traceLocation = new TraceLocation(latitude, longitude, speed, bear, time);
//        traces.add(traceLocation);
//        Log.i(TAG, "latitude:" + latitude + " longitude:" + longitude);
//        Log.i(TAG, "bear:" + bear + " speed:" + speed + " time:" + time);
//        //轨迹纠正,开始的时候调用此方法开始绘制并计算轨迹，暂停则
//        mLBSTraceClient.queryProcessedTrace(1, traces, LBSTraceClient.TYPE_AMAP, this);
//
//    }

//    //TraceListener回调
//    @Override
//    public void onRequestFailed(int lineID, String errorInfo) {
//
//        Log.d(TAG, "onRequestFailed------->轨迹纠偏error:" + errorInfo);
//    }
//
//    @Override
//    public void onTraceProcessing(int lineID, int index, List<LatLng> segments) {
//        Log.d(TAG, "onTraceProcessing");
//    }
//
//    @Override
//    public void onFinished(int lineID, List<LatLng> linePoints, int distance, int waitingTime) {
//        Log.d(TAG, "onFinished" + " distance:" + distance + "linePoints size:" + linePoints.size());
//        aMap.addPolyline(new PolylineOptions().addAll(linePoints).width(10).color(Color.argb(255, 255, 20, 147)));
//    }

    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
        mWidth = mWindowManager.getDefaultDisplay().getWidth();
        mHeight = mWindowManager.getDefaultDisplay().getHeight();
        mRadius = (float) Math.sqrt(Math.pow(mWidth, 2) + Math.pow(mHeight, 2));
        //减去地图图标margin right 的距离和地图图标的半径
        centerX = mWidth - SizeUtil.dp2px(this, 15) - 37;
        //状态栏高度加actionbar的高度的一半。
        centerY = SizeUtil.getStatusBarHeight(this) + 96 / 2;


    }

    //远程跑步数据回调
    private IRunDataCallback iRunDataCallback = new IRunDataCallback.Stub() {

        @Override
        public void onDistanceChange(int distance) throws RemoteException {

            //单位：米(转换为公里 1公里 = 1000米)
            Log.d(TAG, "onDistanceChange---->distance" + distance);

            mKM = CalculateUtil.div(distance, 1000, 2);

            Log.d(TAG, "onDistanceChange---->mKm:" + mKM);

            final String kmStr = String.valueOf(mKM);
            Log.d(TAG, "onDistanceChange---->kmStr:" + kmStr);
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    runDataDistanceTv.setText(kmStr);
                    runMapDistanceTv.setText(kmStr);
                }
            });
        }

        @Override
        public void onSpeedChange(float speed) throws RemoteException {

            //单位：米\秒
            Log.d(TAG, "onSpeedChange--->" + speed);
            final String speedStr = String.valueOf(speed);
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    runDataSpeedTv.setText(speedStr);
                    runMapSpeedTv.setText(speedStr);
                }
            });
        }

        @Override
        public void onLocationChange(List<LocationBean> locations) throws RemoteException {

            Log.d(TAG, "onLocationChange--->size:" + locations.size());

            List<LatLng> linePoints = ParseUtil.parseBean2LatLng(locations);
            mPoints = linePoints;
            aMap.addPolyline(new PolylineOptions().addAll(linePoints).width(10).color(Color.argb(255, 255, 20, 147)));
        }

        @Override
        public void onAvSpeedChange(float avSpeed) throws RemoteException {
            mAvSpeed = avSpeed;
        }
    };


    //远程GPS回调
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
                    Log.d(TAG, count + "");
                    startAnimator(count, mAnimatorSet);
                    ++count;
                    mHandler.postDelayed(this, COUNT_DURATION);
                } else {
                    mHandler.removeCallbacks(this);
                    Log.d(TAG, "finish");
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


    @OnClick({R.id.go_to_map_iv, R.id.run_data_stop_btn, R.id.run_data_pause_btn, R.id.run_data_start_btn, R.id.back_run_data_iv, R.id.run_map_stop_btn, R.id.run_map_pause_btn, R.id.run_map_start_btn})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.go_to_map_iv:

                Log.d(TAG, "go to map");
                Log.i(TAG, "centerX:" + centerX + " centerY:" + centerY);


                mCircularReveal = ViewAnimationUtils.createCircularReveal(rootRunMapLayout, (int) centerX, (int) centerY, 0, mRadius);
                mCircularReveal.setDuration(1000).start();

                rootRunMapLayout.setVisibility(View.VISIBLE);

                mCircularReveal.addListener(new Animator.AnimatorListener() {
                    @Override
                    public void onAnimationStart(Animator animation) {

                    }

                    @Override
                    public void onAnimationEnd(Animator animation) {
                        rootRunDataLayout.setVisibility(View.INVISIBLE);
                    }

                    @Override
                    public void onAnimationCancel(Animator animation) {

                    }

                    @Override
                    public void onAnimationRepeat(Animator animation) {

                    }
                });
                break;
            case R.id.run_data_stop_btn:

                handleRunData(mKM, mPoints, mAvSpeed);

                break;
            case R.id.run_data_pause_btn:

                showStartAndStopBtnAnimation(runDataStartBtn, runDataStopBtn, runDataPauseBtn);
                showStartAndStopBtnAnimation(runMapStartBtn, runMapStopBtn, runMapPauseBtn);

                //计时器暂停
                isPause = true;

                if (mIRunDataService != null) {
                    try {
                        mIRunDataService.stopRun();
                    } catch (RemoteException e) {
                        e.printStackTrace();
                    }
                }


                break;
            case R.id.run_data_start_btn:

                showPauseBtnAnimation(runDataStartBtn, runDataStopBtn, runDataPauseBtn);
                showPauseBtnAnimation(runMapStartBtn, runMapStopBtn, runMapPauseBtn);

                //计时器继续
                isPause = false;

                if (mIRunDataService != null) {
                    try {
                        mIRunDataService.startRun();
                    } catch (RemoteException e) {
                        e.printStackTrace();
                    }

                }

                break;

            case R.id.run_map_stop_btn:

                handleRunData(mKM, mPoints, mAvSpeed);

                break;
            case R.id.run_map_pause_btn:

                showStartAndStopBtnAnimation(runMapStartBtn, runMapStopBtn, runMapPauseBtn);
                showStartAndStopBtnAnimation(runDataStartBtn, runDataStopBtn, runDataPauseBtn);

                //计时器暂停
                isPause = true;

                if (mIRunDataService != null) {
                    try {
                        mIRunDataService.stopRun();
                    } catch (RemoteException e) {
                        e.printStackTrace();
                    }
                }


                break;
            case R.id.run_map_start_btn:
                showPauseBtnAnimation(runMapStartBtn, runMapStopBtn, runMapPauseBtn);
                showPauseBtnAnimation(runDataStartBtn, runDataStopBtn, runDataPauseBtn);

                //计时器继续
                isPause = false;

                if (mIRunDataService != null) {
                    try {
                        mIRunDataService.startRun();
                    } catch (RemoteException e) {
                        e.printStackTrace();
                    }
                    break;
                }


            case R.id.back_run_data_iv:
                Log.d(TAG, "back to data");
                mCircularReveal = ViewAnimationUtils.createCircularReveal(rootRunMapLayout, (int) centerX, (int) centerY, mRadius, 0);
                mCircularReveal.setDuration(1000).start();
//                rootRunMapLayout.setVisibility(View.GONE);
                rootRunDataLayout.setVisibility(View.VISIBLE);
                mCircularReveal.addListener(new Animator.AnimatorListener() {
                    @Override
                    public void onAnimationStart(Animator animation) {

                    }

                    @Override
                    public void onAnimationEnd(Animator animation) {
                        rootRunMapLayout.setVisibility(View.INVISIBLE);
                    }

                    @Override
                    public void onAnimationCancel(Animator animation) {

                    }

                    @Override
                    public void onAnimationRepeat(Animator animation) {

                    }
                });
                break;


        }
    }

    //隐藏继续和结束按钮，开始计时跑步
    private void showPauseBtnAnimation(final View startBtn, final View stopBtn, final View pauseBtn) {

        AnimatorSet set = new AnimatorSet();
        ObjectAnimator startBtnAnimation = ObjectAnimator.ofFloat(startBtn, "translationX", 0, -SizeUtil.dp2px(getApplication(), 85));
        ObjectAnimator stopBtnAnimation = ObjectAnimator.ofFloat(stopBtn, "translationX", 0, SizeUtil.dp2px(getApplication(), 85));
        set.setDuration(500);
        set.playTogether(startBtnAnimation, stopBtnAnimation);
        set.start();
        set.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {

            }

            @Override
            public void onAnimationEnd(Animator animation) {
                startBtn.setVisibility(View.INVISIBLE);
                stopBtn.setVisibility(View.INVISIBLE);
                pauseBtn.setVisibility(View.VISIBLE);
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
    private void showStartAndStopBtnAnimation(View startBtn, View stopBtn, View pauseBtn) {
        pauseBtn.setVisibility(View.INVISIBLE);
        startBtn.setVisibility(View.VISIBLE);
        stopBtn.setVisibility(View.VISIBLE);
        AnimatorSet set = new AnimatorSet();
        ObjectAnimator startBtnAnimation = ObjectAnimator.ofFloat(startBtn, "translationX", -SizeUtil.dp2px(getApplication(), 85), 0);
        ObjectAnimator stopBtnAnimation = ObjectAnimator.ofFloat(stopBtn, "translationX", SizeUtil.dp2px(getApplication(), 85), 0);
        set.playTogether(startBtnAnimation, stopBtnAnimation);
        set.setDuration(500);
        set.start();
    }

    private void handleRunData(final double distance, final List<LatLng> points, final float avSpeed) {

        Log.d(TAG, "equal distance:" + distance);

        if (distance < 0.01) {
            new AlertDialog.Builder(this)
                    .setCancelable(false)
                    .setTitle("温馨提示")
                    .setMessage("此次运动距离过短,无法保存记录")
                    .setNegativeButton("取消", null)
                    .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {

                            if (mIRunDataService != null) {
                                try {
                                    mIRunDataService.stopRun();
                                } catch (RemoteException e) {
                                    e.printStackTrace();
                                }
                            }

                            finish();
                        }
                    })
                    .show();

            //test
//            List<LocationBean> locationBeanList = new ArrayList<>();
//            UserInfoBmob user = BmobUser.getCurrentUser(UserInfoBmob.class);
//            String objectId = user.getObjectId();
//            Log.d(TAG, "object id:" + objectId);
//
//
//            RunDataBmob runDataBmob = new RunDataBmob();
//            runDataBmob.setRunStartTime("2017年09月\n22日08:20");
//            runDataBmob.setRunDistance("3.21");
//            runDataBmob.setAvSpeed("10'48''");
//            runDataBmob.setPoints(locationBeanList);
//            runDataBmob.setRunDurationTime("00:14:23");
//            runDataBmob.setpUserObjectId("f9fc6b9a2a");
//
//            runDataBmob.save(new SaveListener<String>() {
//                @Override
//                public void done(String s, BmobException e) {
//                    Log.d(TAG, s);
//                    if (e != null) {
//                        ToastUtil.shortToast(getApplicationContext(), e.getMessage());
//                        Log.d(TAG, "上传数据库------->error code:" + e.getErrorCode() + " error:" + e.getMessage());
//                    }
//                }
//            });


        } else {

            new AlertDialog.Builder(this)
                    .setTitle("温馨提示")
                    .setMessage("是否保存此次跑步信息？")
                    .setCancelable(false)
                    .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {

                            new AlertDialog.Builder(getApplicationContext())
                                    .setTitle("提示")
                                    .setMessage("取消意味着此次跑步信息将全部丢失，您确定吗？")
                                    .setNegativeButton("点错了，我要保存", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {

                                            uploadRunData(distance, points, avSpeed);
                                        }
                                    })
                                    .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            finish();
                                        }
                                    }).show();

                        }
                    })
                    .setPositiveButton("保存", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {

                            // TODO: 2017/9/19 记录跑步数据，然后退出。上传服务器
                            uploadRunData(distance, points, avSpeed);

                        }
                    })
                    .show();

        }
    }


    private void uploadRunData(final double distance, final List<LatLng> points, final float avSpeed) {
        UserInfoBmob user = BmobUser.getCurrentUser(UserInfoBmob.class);
        String objectId = user.getObjectId();

        //1.精确时间 2.公里数 3.点坐标 4.平均配速 5.跑步用时
        String runTime = TimeUtil.parseTimeFormat(mStartRunTime);
        String km = String.valueOf(distance);
        String avSpeedStr = String.valueOf(avSpeed);
        List<LocationBean> pointsBean = ParseUtil.parseLatLng2Bean(points);
        String duration = mDurationTime;

        if (objectId != null) {
            RunDataBmob runDataBmob = new RunDataBmob();
            runDataBmob.setRunStartTime(runTime);
            runDataBmob.setRunDistance(km);
            runDataBmob.setAvSpeed(avSpeedStr);
            runDataBmob.setPoints(pointsBean);
            runDataBmob.setRunDurationTime(duration);
            runDataBmob.setpUserObjectId(objectId);

            runDataBmob.save(new SaveListener<String>() {
                @Override
                public void done(String s, BmobException e) {
                    if (e != null) {
                        ToastUtil.shortToast(getApplicationContext(), e.getMessage());
                        Log.d(TAG, "上传数据库------->error code:" + e.getErrorCode() + " error:" + e.getMessage());
                    }
                }
            });
        } else {
            ToastUtil.shortToast(getApplicationContext(), "Access token is null.Try login again");
        }
    }

    @Override
    public void onBackPressed() {
        Toast.makeText(this, "请先结束运动", Toast.LENGTH_SHORT).show();

    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.d(TAG,"-------------------------onResume-----------------------");
        mMapView.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
        Log.d(TAG,"-------------------------onPause-----------------------");
        mMapView.onPause();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.d(TAG,"-------------------------onDestroy-----------------------");
        this.unbindService(gpsConn);
        this.unbindService(runDataConn);

        if (mIGpsStatusService != null) {
            try {
                mIGpsStatusService.unRegisterCallback(iGpsStatusCallback);
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        }

        if (mIRunDataService != null) {
            try {
                mIRunDataService.unRegisterRunDataCallback(iRunDataCallback);
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        }

        mMapView.onDestroy();
    }

}
