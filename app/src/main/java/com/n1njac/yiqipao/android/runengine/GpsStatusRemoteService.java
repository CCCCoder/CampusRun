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
import android.os.RemoteException;
import android.support.annotation.IntDef;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.util.Log;

import com.n1njac.yiqipao.android.IGpsStatusCallback;

import java.util.Iterator;

/*
 *    Created by N1njaC on 2017/9/16.
 *    email:aiai173cc@gmail.com
 */

public class GpsStatusRemoteService extends Service {


    private static final String TAG = GpsStatusRemoteService.class.getSimpleName();
    public static final int SIGNAL_FULL = 0x10;
    public static final int SIGNAL_GOOD = 0x11;
    public static final int SIGNAL_BAD = 0x12;
    public static final int SIGNAL_NONE = 0x13;



    private IGpsStatusCallback mGpsStatusCallback;

    private LocationManager mLocationManager;
    private GpsStatus mGpsStatus;

    private static final int MAX_SATELLITE = 255;

    @Override
    public void onCreate() {
        super.onCreate();
        mLocationManager = (LocationManager) this.getSystemService(Context.LOCATION_SERVICE);

    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        initGpsStatus();
        return START_STICKY;
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return new GpsBinder();
    }

    private class GpsBinder extends Binder {

        public GpsStatusRemoteService getGpsStatusService() {
            return GpsStatusRemoteService.this;
        }
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
        mLocationManager.addGpsStatusListener(new GpsListener());
        mLocationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 60000, 1, new MyLocationManager());
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
                                try {
                                    mGpsStatusCallback.gpsStatus(SIGNAL_FULL);
                                } catch (RemoteException e) {
                                    e.printStackTrace();
                                }

                            } else if (count >= 4) {
                                try {
                                    mGpsStatusCallback.gpsStatus(SIGNAL_GOOD);
                                } catch (RemoteException e) {
                                    e.printStackTrace();
                                }
                            } else {
                                //一格信号
                                try {
                                    mGpsStatusCallback.gpsStatus(SIGNAL_BAD);
                                } catch (RemoteException e) {
                                    e.printStackTrace();
                                }
                            }
                        }
                    }
                    Log.d("xyz", "satellite num:" + count);
                    break;

                case GpsStatus.GPS_EVENT_STARTED:
                    Log.i(TAG, "gps打开");

                    Iterator<GpsSatellite> iterator2 = mGpsStatus.getSatellites().iterator();
                    int count2 = 0;
                    while (iterator2.hasNext() && count2 <= MAX_SATELLITE) {
                        GpsSatellite gpsSatellite = iterator2.next();

                        //信噪比大于30，算作有效卫星
                        if (gpsSatellite.getSnr() > 30) {
                            count2++;
                        }
                        if (count2 > 8) {
                            //满格信号
                            try {
                                mGpsStatusCallback.gpsStatus(SIGNAL_FULL);
                            } catch (RemoteException e) {
                                e.printStackTrace();
                            }
                        } else if (count2 >= 4) {
                            //两格信号
                            try {
                                mGpsStatusCallback.gpsStatus(SIGNAL_GOOD);
                            } catch (RemoteException e) {
                                e.printStackTrace();
                            }
                        } else {
                            //一格信号
                            try {
                                mGpsStatusCallback.gpsStatus(SIGNAL_BAD);
                            } catch (RemoteException e) {
                                e.printStackTrace();
                            }
                        }

                    }
                    Log.d("xyz", "satellite num:" + count2);

                    break;

                case GpsStatus.GPS_EVENT_STOPPED:
                    Log.i(TAG, "gps关闭");
                    break;
            }
        }
    }

    private class MyLocationManager implements LocationListener {

        @Override
        public void onLocationChanged(Location location) {

        }

        @Override
        public void onStatusChanged(String provider, int status, Bundle extras) {

        }

        @Override
        public void onProviderEnabled(String provider) {


        }

        @Override
        public void onProviderDisabled(String provider) {

            //gps未开启

            try {
                mGpsStatusCallback.gpsStatus(SIGNAL_NONE);
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        }
    }

    public void setGpsStatusCallback(IGpsStatusCallback gpsStatusCallback) {
        this.mGpsStatusCallback = gpsStatusCallback;
    }
}
