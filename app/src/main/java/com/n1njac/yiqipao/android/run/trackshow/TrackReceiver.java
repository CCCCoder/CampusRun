package com.n1njac.yiqipao.android.run.trackshow;

import android.annotation.SuppressLint;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.widget.Toast;

public class TrackReceiver extends BroadcastReceiver {

    @SuppressLint("Wakelock")
    @Override
    public void onReceive(final Context context, final Intent intent) {
        final String action = intent.getAction();

        if (Intent.ACTION_SCREEN_OFF.equals(action)) {
            System.out.println("screen off,acquire wake lock!");
            if (null != TrackUploadFragment.wakeLock && !(TrackUploadFragment.wakeLock.isHeld())) {
                TrackUploadFragment.wakeLock.acquire();
            }
        } else if (Intent.ACTION_SCREEN_ON.equals(action)) {
            System.out.println("screen on,release wake lock!");
            if (null != TrackUploadFragment.wakeLock && TrackUploadFragment.wakeLock.isHeld()) {
                TrackUploadFragment.wakeLock.release();
            }
        } else if ("com.baidu.trace.action.GPS_STATUS".equals(action)) {
            int statusCode = intent.getIntExtra("statusCode", 0);
            System.out.println("GPS状态码 : " + statusCode);
            String statusMessage = intent.getStringExtra("statusMessage");
            Toast.makeText(context, statusMessage, Toast.LENGTH_SHORT).show();
        }
    }

}
