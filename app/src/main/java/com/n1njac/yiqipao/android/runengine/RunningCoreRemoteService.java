package com.n1njac.yiqipao.android.runengine;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.os.RemoteCallbackList;
import android.os.RemoteException;
import android.support.annotation.IntDef;
import android.support.annotation.Nullable;

import com.amap.api.location.AMapLocationClient;
import com.n1njac.yiqipao.android.IRunDataCallback;
import com.n1njac.yiqipao.android.IRunDataService;

/**
 * Created by N1njaC on 2017/9/22.
 */

public class RunningCoreRemoteService extends Service {


    private AMapLocationClient mAMapLocationClient;

    @Override
    public void onCreate() {
        super.onCreate();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        return START_STICKY;
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
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

        }

        @Override
        public void pauseRun() throws RemoteException {

        }

        @Override
        public void stopRun() throws RemoteException {

        }

        @Override
        public void startRun() throws RemoteException {

        }
    };


}
