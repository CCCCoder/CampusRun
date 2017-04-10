package com.n1njac.yiqipao.android;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.v4.app.NotificationManagerCompat;
import android.util.Log;
import android.widget.Toast;

import com.n1njac.yiqipao.android.nearbychat.NearbyChatMainActivity;

import cn.bmob.push.PushConstants;

/**
 * Created by huanglei on 2017/4/8.
 */

public class MyPushMessageReceiver extends BroadcastReceiver {
    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    @Override
    public void onReceive(Context context, Intent intent) {
        if (intent.getAction().equals(PushConstants.ACTION_MESSAGE)) {

            NotificationManager mNotificationManager = (NotificationManager)
                    context.getSystemService(Context.NOTIFICATION_SERVICE);
            Intent intent1 = new Intent(context, NearbyChatMainActivity.class);
            PendingIntent pi = PendingIntent.getActivity(context, 0, intent1, 0);
            Notification.Builder mBuilder = new Notification.Builder(context);
            mBuilder.setContentTitle("校园跑")
                    .setContentText("有人邀请你一起跑步啦!")
                    .setTicker("来自校园跑的消息")
                    .setWhen(System.currentTimeMillis())
                    .setContentIntent(pi)
                    .setAutoCancel(true)
                    .setSmallIcon(R.mipmap.ic_launcher);

            mNotificationManager.notify(1, mBuilder.build());
        }
    }
}
