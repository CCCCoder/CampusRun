package com.n1njac.yiqipao.android.runengine;

import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;
import android.support.annotation.Nullable;

/*
 *    Created by N1njaC on 2017/9/16.
 *    email:aiai173cc@gmail.com
 */

public class GpsStatusService extends Service {


    private IGPSStatusCallback mCallback;


    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return new GpsBinder();
    }

    private class GpsBinder extends Binder {

        public GpsStatusService getGpsStatusService() {
            return GpsStatusService.this;
        }
    }
}
