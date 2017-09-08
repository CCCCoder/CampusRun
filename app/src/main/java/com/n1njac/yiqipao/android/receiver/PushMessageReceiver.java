package com.n1njac.yiqipao.android.receiver;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.util.Log;

import com.n1njac.yiqipao.android.R;
import com.n1njac.yiqipao.android.ui.activity.NearbyChatMainActivity;

import org.json.JSONException;
import org.json.JSONObject;

import cn.bmob.push.PushConstants;

/**
 * 这里规定：接收到的推送可能是拒绝推送，也可能是请求推送，这里拒绝推送
 * msg填：refuse，请求推送填：require。即pushMessage里面的内容
 */


public class PushMessageReceiver extends BroadcastReceiver {

    private String msg;

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    @Override
    public void onReceive(Context context, Intent intent) {
        if (intent.getAction().equals(PushConstants.ACTION_MESSAGE)) {
            Log.d("xyz", "客户端收到推送内容：" + intent.getStringExtra("msg"));

            try {
                JSONObject object = new JSONObject(intent.getStringExtra("msg"));
                msg = object.getString("alert");
                Log.d("xyz", "推送接收到的msg:" + msg);
            } catch (JSONException e) {
                e.printStackTrace();
            }


            if (msg.equals("refuse")) {
                NotificationManager mNotificationManager = (NotificationManager)
                        context.getSystemService(Context.NOTIFICATION_SERVICE);
                Notification.Builder mBuilder = new Notification.Builder(context);
                mBuilder.setContentTitle("校园跑")
                        .setContentText("对方拒绝了你的请求：）")
                        .setTicker("来自校园跑的消息")
                        .setWhen(System.currentTimeMillis())
                        .setAutoCancel(true)
                        .setSmallIcon(R.mipmap.ic_launcher);

                mNotificationManager.notify(1, mBuilder.build());
            } else if (msg.equals("require")) {
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
}
