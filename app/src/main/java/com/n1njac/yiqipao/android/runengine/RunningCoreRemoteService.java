package com.n1njac.yiqipao.android.runengine;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.os.RemoteCallbackList;
import android.os.RemoteException;
import android.support.annotation.IntDef;
import android.support.annotation.Nullable;
import android.util.Log;

import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationClient;
import com.amap.api.location.AMapLocationClientOption;
import com.amap.api.location.AMapLocationListener;
import com.amap.api.maps.model.LatLng;
import com.amap.api.trace.LBSTraceClient;
import com.amap.api.trace.TraceListener;
import com.amap.api.trace.TraceLocation;
import com.n1njac.yiqipao.android.IRunDataCallback;
import com.n1njac.yiqipao.android.IRunDataService;
import com.n1njac.yiqipao.android.bean.LocationBean;
import com.n1njac.yiqipao.android.utils.ParseUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by N1njaC on 2017/9/22.
 */

public class RunningCoreRemoteService extends Service implements AMapLocationListener, TraceListener {

    private static final String TAG = RunningCoreRemoteService.class.getSimpleName();

    private AMapLocationClient mAMapLocationClient;
    private List<TraceLocation> mTraceLocations;
    private LBSTraceClient mLBSTraceClient;

    private float mSpeed;

    private boolean isFirstCallback = true;

    private float avSpeed;


    @Override
    public void onCreate() {
        super.onCreate();
        Log.d(TAG, "onCreate");
        mAMapLocationClient = new AMapLocationClient(getApplicationContext());
        mAMapLocationClient.setLocationListener(this);
        AMapLocationClientOption locationClientOption = new AMapLocationClientOption();
        locationClientOption.setLocationMode(AMapLocationClientOption.AMapLocationMode.Hight_Accuracy);
        locationClientOption.setInterval(2000);
        locationClientOption.setOnceLocation(false);
        locationClientOption.setMockEnable(true);
        mAMapLocationClient.setLocationListener(this);
        mAMapLocationClient.setLocationOption(locationClientOption);
        mAMapLocationClient.startLocation();

        mLBSTraceClient = LBSTraceClient.getInstance(this);

        mTraceLocations = new ArrayList<>();
    }

    //定位结果回调
    @Override
    public void onLocationChanged(AMapLocation aMapLocation) {


        double latitude = aMapLocation.getLatitude();
        double longitude = aMapLocation.getLongitude();

        float bear = aMapLocation.getBearing();
        float speed = aMapLocation.getSpeed();
        long time = aMapLocation.getTime();
        float lastSpeed = 0;

        if (isFirstCallback) {
            lastSpeed = speed;
            isFirstCallback = false;
        } else {
            //平均配速
            avSpeed = (lastSpeed + speed) / 2;
            lastSpeed = avSpeed;
        }


        mSpeed = speed;
        int gpsStatus = aMapLocation.getGpsAccuracyStatus();
        int satellites = aMapLocation.getSatellites();

        Log.i(TAG, "onLocationChanged-->" + "latitude:" + latitude + " longitude:" + longitude);
        Log.i(TAG, "bear:" + bear + " speed:" + speed + " time:" + time);
        Log.i(TAG, "error info:" + aMapLocation.getErrorInfo() + " error code:" + aMapLocation.getErrorCode());
        Log.i(TAG, "error info:" + aMapLocation.getErrorInfo() + " error code:" + aMapLocation.getErrorCode());


        TraceLocation traceLocation = new TraceLocation(latitude, longitude, speed, bear, time);
        mTraceLocations.add(traceLocation);

        mLBSTraceClient.queryProcessedTrace(1, mTraceLocations, LBSTraceClient.TYPE_AMAP, this);

    }


    //轨迹纠偏回调
    @Override
    public void onRequestFailed(int lineID, String errorInfo) {

        Log.d(TAG, "onRequestFailed---->" + errorInfo);
    }

    @Override
    public void onTraceProcessing(int lineID, int index, List<LatLng> segments) {
        Log.d(TAG, "onTraceProcessing");
    }

    @Override
    public void onFinished(int lineID, List<LatLng> linePoints, int distance, int waitingTime) {

        Log.d(TAG, "onFinished------>" + " distance:" + distance + "linePoints size:" + linePoints.size());

        List<LocationBean> locationBeanList = ParseUtil.parseLatLng2Bean(linePoints);
        broadcastResult(locationBeanList, distance, mSpeed);
    }


    //分发纠偏后的结果（经纬度，路程，速度）
    private void broadcastResult(List<LocationBean> locationBeanList, int distance, float speed) {
        int length = mCallback.beginBroadcast();
        for (int i = 0; i < length; i++) {
            try {
                mCallback.getBroadcastItem(i).onLocationChange(locationBeanList);
                mCallback.getBroadcastItem(i).onDistanceChange(distance);
                mCallback.getBroadcastItem(i).onSpeedChange(speed);
                mCallback.getBroadcastItem(i).onAvSpeedChange(avSpeed);

            } catch (RemoteException e) {
                e.printStackTrace();
            }
        }
        mCallback.finishBroadcast();
    }


    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.d(TAG, "onStartCommand");
        return START_STICKY;
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        Log.d(TAG, "onBind");
        return mBinder;
    }

    private RemoteCallbackList<IRunDataCallback> mCallback = new RemoteCallbackList<>();

    private IRunDataService.Stub mBinder = new IRunDataService.Stub() {
        @Override
        public void registerRunDataCallback(IRunDataCallback callback) throws RemoteException {

            mCallback.register(callback);
        }

        @Override
        public void unRegisterRunDataCallback(IRunDataCallback callback) throws RemoteException {

            mCallback.unregister(callback);
        }

        @Override
        public void continueRun() throws RemoteException {

            if (mAMapLocationClient != null) {
                mAMapLocationClient.startLocation();
            }

        }

        @Override
        public void pauseRun() throws RemoteException {


            if (mAMapLocationClient != null) {
                mAMapLocationClient.stopLocation();
            }

        }

        @Override
        public void stopRun() throws RemoteException {


            if (mAMapLocationClient != null) {
                mAMapLocationClient.onDestroy();
            }
        }

        @Override
        public void startRun() throws RemoteException {

            if (mAMapLocationClient != null) {
                mAMapLocationClient.startLocation();
            }

        }
    };

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.d(TAG, "onDestroy");
        if (mAMapLocationClient != null) {
            mAMapLocationClient.onDestroy();
            mAMapLocationClient = null;
        }
    }
}
