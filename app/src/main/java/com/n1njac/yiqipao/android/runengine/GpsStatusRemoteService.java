package com.n1njac.yiqipao.android.runengine;

import android.Manifest;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.GpsSatellite;
import android.location.GpsStatus;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Binder;
import android.os.Bundle;
import android.os.IBinder;
import android.os.RemoteCallbackList;
import android.os.RemoteException;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.util.Log;

import com.n1njac.yiqipao.android.IGpsStatusCallback;
import com.n1njac.yiqipao.android.IGpsStatusService;

import java.util.Iterator;

/*
 *    Created by N1njaC on 2017/9/16.
 *    email:aiai173cc@gmail.com
 */

public class GpsStatusRemoteService extends Service {


    private static final String TAG = GpsStatusRemoteService.class.getSimpleName();
    public static final int SIGNAL_FULL = 0x01;
    public static final int SIGNAL_GOOD = 0x02;
    public static final int SIGNAL_BAD = 0x03;
    public static final int SIGNAL_NONE = 0x04;


    private LocationManager mLocationManager;
    private GpsStatus mGpsStatus;
    private GpsListener mGpsListener;
    private MyLocationListener mLocationListener;

    private static final int MAX_SATELLITE = 255;


    @Override
    public void onCreate() {
        super.onCreate();
        Log.d(TAG, "onCreate");
        mLocationManager = (LocationManager) this.getSystemService(Context.LOCATION_SERVICE);
        mGpsListener = new GpsListener();
        mLocationListener = new MyLocationListener();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        Log.d(TAG, "onStartCommand");
        initGpsStatus();
        return START_STICKY;
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        Log.d(TAG, "onBind");
//        initGpsStatus();
        return mBinder;
    }

    private RemoteCallbackList<IGpsStatusCallback> mCallback = new RemoteCallbackList<>();

    private IGpsStatusService.Stub mBinder = new IGpsStatusService.Stub() {
        @Override
        public void registerCallback(IGpsStatusCallback callback) throws RemoteException {

            mCallback.register(callback);
        }

        @Override
        public void unRegisterCallback(IGpsStatusCallback callback) throws RemoteException {
            mCallback.unregister(callback);
        }
    };

    private void broadcastData(int status) {
        int length = mCallback.beginBroadcast();
        for (int i = 0; i < length; i++) {
            try {
                mCallback.getBroadcastItem(i).gpsStatus(status);
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        }
        mCallback.finishBroadcast();
    }


    private void initGpsStatus() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        mGpsStatus = mLocationManager.getGpsStatus(null);
        mLocationManager.addGpsStatusListener(mGpsListener);
        mLocationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 60000, 1, mLocationListener);
    }

    private class GpsListener implements GpsStatus.Listener {

        @Override
        public void onGpsStatusChanged(int event) {
            switch (event) {
                case GpsStatus.GPS_EVENT_FIRST_FIX:
                    Log.i(TAG, "第一次定位");
                    break;

                case GpsStatus.GPS_EVENT_SATELLITE_STATUS:
                    Log.i(TAG, "卫星状态改变");

                    Iterator<GpsSatellite> iterator = mGpsStatus.getSatellites().iterator();
                    int count = 0;
                    while (iterator.hasNext() && count <= MAX_SATELLITE) {
                        GpsSatellite gpsSatellite = iterator.next();
                        if (gpsSatellite.getSnr() > 30) {
                            count++;
                            if (count > 8) {
                                //满格信号
                                broadcastData(SIGNAL_FULL);

                            } else if (count >= 4) {

                                //两格信号
                                broadcastData(SIGNAL_GOOD);

                            } else {
                                //一格信号

                                broadcastData(SIGNAL_BAD);

                            }
                        }
                    }
                    Log.d("xyz", "satellite num:" + count);
                    break;

                case GpsStatus.GPS_EVENT_STARTED:
                    Log.i(TAG, "gps打开");

                    Iterator<GpsSatellite> iterator2 = mGpsStatus.getSatellites().iterator();
                    int count2 = 0;
                    Log.d(TAG, "iterator2.hasNext():" + iterator2.hasNext());
                    while (iterator2.hasNext() && count2 <= MAX_SATELLITE) {
                        GpsSatellite gpsSatellite = iterator2.next();

                        //信噪比大于30，算作有效卫星
                        if (gpsSatellite.getSnr() > 30) {
                            count2++;
                        }
                        if (count2 > 8) {
                            //满格信号

                            broadcastData(SIGNAL_FULL);

                        } else if (count2 >= 4) {
                            //两格信号

                            broadcastData(SIGNAL_GOOD);

                        } else {
                            //一格信号

                            broadcastData(SIGNAL_BAD);

                        }

                    }
                    //测试用
//                    broadcastData(SIGNAL_BAD);
                    Log.d(TAG, "satellite num:" + count2);

                    break;

                case GpsStatus.GPS_EVENT_STOPPED:
                    Log.i(TAG, "gps关闭");
                    break;
            }
        }
    }

    private class MyLocationListener implements LocationListener {

        @Override
        public void onLocationChanged(Location location) {

        }

        @Override
        public void onStatusChanged(String provider, int status, Bundle extras) {

        }

        @Override
        public void onProviderEnabled(String provider) {

            Log.d(TAG, "onProviderEnabled");

        }

        @Override
        public void onProviderDisabled(String provider) {
            Log.d(TAG, "onProviderDisabled");
            //gps未开启

            broadcastData(SIGNAL_NONE);
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (mLocationManager != null) {
            mLocationManager.removeGpsStatusListener(mGpsListener);
            mLocationManager.removeUpdates(mLocationListener);
            mLocationManager = null;
        }
        Log.d(TAG, "onDestroy");
    }
}
