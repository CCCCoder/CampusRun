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
import android.location.Location;
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
import com.amap.api.trace.TraceListener;
import com.amap.api.trace.TraceLocation;
import com.amap.api.trace.TraceStatusListener;
import com.n1njac.yiqipao.android.IGpsStatusCallback;
import com.n1njac.yiqipao.android.IGpsStatusService;
import com.n1njac.yiqipao.android.R;
import com.n1njac.yiqipao.android.runengine.GpsStatusRemoteService;
import com.n1njac.yiqipao.android.ui.widget.RunDataTextView;
import com.n1njac.yiqipao.android.utils.FontCacheUtil;
import com.n1njac.yiqipao.android.utils.SizeUtil;

import java.util.ArrayList;
import java.util.List;

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

public class UserRunRecordActivity extends BaseActivity implements AMap.OnMyLocationChangeListener, TraceListener {


    private static final String TAG = UserRunRecordActivity.class.getSimpleName();
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

    private AMap aMap;
    private LBSTraceClient mLBSTraceClient;


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
        boldTf = FontCacheUtil.getFont(this, "fonts/Avenir_Next_Condensed_demi_bold.ttf");

        initRunDataLayout();
        initMapLayout(savedInstanceState);
        initCountDownAnimation();

        mWindowManager = (WindowManager) getSystemService(WINDOW_SERVICE);

        this.bindService(new Intent(UserRunRecordActivity.this, GpsStatusRemoteService.class), gpsConn, Context.BIND_AUTO_CREATE);


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

    private void initRunDataLayout() {
        rootRunDataLayout = findViewById(R.id.include_root_run_data);
        rootRunDataLayout.setPadding(0, SizeUtil.getStatusBarHeight(this), 0, 0);
        runDataDistanceTv.setTypeface(boldTf);
        runDataTimeTv.setTypeface(boldTf);
        runDataSpeedTv.setTypeface(boldTf);
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

        aMap.setOnMyLocationChangeListener(this);
        aMap.moveCamera(CameraUpdateFactory.zoomTo(17));

    }

    private List<LatLng> latLngs = new ArrayList<>();
    private List<TraceLocation> traces = new ArrayList<>();


    //定位信息回调
    // 三秒钟轨迹纠正一次，回传结果绘制轨迹和计算路程。暂停的话 轨迹不绘制，定位照常。
    @Override
    public void onMyLocationChange(Location location) {

        double latitude = location.getLatitude();
        double longitude = location.getLongitude();

        float bear = location.getBearing();
        float speed = location.getSpeed();
        long time = location.getTime();

        LatLng latLng = new LatLng(latitude, longitude);
        latLngs.add(latLng);

        //用一个list将traceLocation保存，每隔3s请求一次轨迹纠偏以获得已跑的路程。
        TraceLocation traceLocation = new TraceLocation(latitude, longitude, speed, bear, time);
        traces.add(traceLocation);
        Log.i(TAG, "latitude:" + latitude + " longitude:" + longitude);
        Log.i(TAG, "bear:" + bear + " speed:" + speed + " time:" + time);
        //轨迹纠正,开始的时候调用此方法开始绘制并计算轨迹，暂停则
        mLBSTraceClient.queryProcessedTrace(1, traces, LBSTraceClient.TYPE_AMAP, this);


//

    }

    //TraceListener回调
    @Override
    public void onRequestFailed(int lineID, String errorInfo) {

        Log.d(TAG, "onRequestFailed------->轨迹纠偏error:" + errorInfo);
    }

    @Override
    public void onTraceProcessing(int lineID, int index, List<LatLng> segments) {
        Log.d(TAG, "onTraceProcessing");
    }

    @Override
    public void onFinished(int lineID, List<LatLng> linePoints, int distance, int waitingTime) {
        Log.d(TAG, "onFinished" + " distance:" + distance + "linePoints size:" + linePoints.size());
        aMap.addPolyline(new PolylineOptions().addAll(linePoints).width(10).color(Color.argb(255, 255, 20, 147)));
    }

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

                handleRunData(runDataDistanceTv);

                break;
            case R.id.run_data_pause_btn:

                showStartAndStopBtnAnimation(runDataStartBtn, runDataStopBtn, runDataPauseBtn);
                showStartAndStopBtnAnimation(runMapStartBtn, runMapStopBtn, runMapPauseBtn);
                break;
            case R.id.run_data_start_btn:

                showPauseBtnAnimation(runDataStartBtn, runDataStopBtn, runDataPauseBtn);
                showPauseBtnAnimation(runMapStartBtn, runMapStopBtn, runMapPauseBtn);
                break;


            case R.id.run_map_stop_btn:
                handleRunData(runMapDistanceTv);

                break;
            case R.id.run_map_pause_btn:
                mLBSTraceClient.destroy();
                showStartAndStopBtnAnimation(runMapStartBtn, runMapStopBtn, runMapPauseBtn);
                showStartAndStopBtnAnimation(runDataStartBtn, runDataStopBtn, runDataPauseBtn);
                break;
            case R.id.run_map_start_btn:
                showPauseBtnAnimation(runMapStartBtn, runMapStopBtn, runMapPauseBtn);
                showPauseBtnAnimation(runDataStartBtn, runDataStopBtn, runDataPauseBtn);
                break;

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

    private void handleRunData(TextView distanceTv) {
        String distance = distanceTv.getText().toString();
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

    @Override
    protected void onResume() {
        super.onResume();
        mMapView.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
        mMapView.onPause();
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
        mMapView.onDestroy();
    }


}
