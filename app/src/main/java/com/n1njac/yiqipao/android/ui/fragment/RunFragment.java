package com.n1njac.yiqipao.android.ui.fragment;

import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.os.RemoteException;
import android.provider.Settings;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.n1njac.yiqipao.android.IGpsStatusCallback;
import com.n1njac.yiqipao.android.IGpsStatusService;
import com.n1njac.yiqipao.android.R;
import com.n1njac.yiqipao.android.runengine.GpsStatusRemoteService;
import com.n1njac.yiqipao.android.ui.activity.UserRunActivity;
import com.n1njac.yiqipao.android.utils.SizeUtil;

import at.markushi.ui.CircleButton;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

import static com.n1njac.yiqipao.android.runengine.GpsStatusRemoteService.SIGNAL_BAD;
import static com.n1njac.yiqipao.android.runengine.GpsStatusRemoteService.SIGNAL_FULL;
import static com.n1njac.yiqipao.android.runengine.GpsStatusRemoteService.SIGNAL_GOOD;
import static com.n1njac.yiqipao.android.runengine.GpsStatusRemoteService.SIGNAL_NONE;

/**
 * Created by huanglei on 2017/1/12.
 */

public class RunFragment extends Fragment {

    private static final String TAG = RunFragment.class.getSimpleName();

    @BindView(R.id.bg_run_frag)
    ImageView bgRunFrag;
    @BindView(R.id.gps_iv)
    ImageView mGpsBg;
    @BindView(R.id.gps_prompt_tv)
    TextView mGpsPrompt;
    @BindView(R.id.circle_btn)
    CircleButton circleBtn;
    @BindView(R.id.run_frag_relative)
    RelativeLayout runRoot;
    Unbinder unbinder;


    private int currentStatus = 0;


    //可能造成内存泄漏。待处理
    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {

            int status = (int) msg.obj;
            switch (status) {
                case SIGNAL_FULL:
                    //src
                    mGpsBg.setImageResource(R.drawable.gps_3);
                    mGpsPrompt.setText(R.string.gps_search_strong);
                    break;
                case SIGNAL_GOOD:
                    mGpsBg.setImageResource(R.drawable.gps_2);
                    mGpsPrompt.setText(R.string.gps_search_middle);
                    break;
                case SIGNAL_BAD:
                    mGpsBg.setImageResource(R.drawable.gps_1);
                    mGpsPrompt.setText(R.string.gps_search_small);
                    break;
                case SIGNAL_NONE:
                    mGpsBg.setImageResource(R.drawable.gps_0);
                    mGpsPrompt.setText(R.string.gps_def_prompt);
                    break;
                default:
                    break;
            }
        }
    };

    private IGpsStatusService mGpsStatusService;
    private Intent mIntent;

    private ServiceConnection conn = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, final IBinder service) {

            mGpsStatusService = IGpsStatusService.Stub.asInterface(service);
            try {
                mGpsStatusService.registerCallback(iGpsStatusCallback);
            } catch (RemoteException e) {
                e.printStackTrace();
            }

        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            mGpsStatusService = null;
        }
    };


    private IGpsStatusCallback iGpsStatusCallback = new IGpsStatusCallback.Stub() {

        @Override
        public void gpsStatus(int status) throws RemoteException {

            Log.d(TAG, "STATUS:" + status + " Thread:" + Thread.currentThread());

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


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d(TAG, "onCreate");
        //binder service
        mIntent = new Intent(getActivity(), GpsStatusRemoteService.class);
        mIntent.setComponent(new ComponentName("com.n1njac.yiqipao.android", "com.n1njac.yiqipao.android.runengine.GpsStatusRemoteService"));
        getActivity().startService(mIntent);
        getActivity().bindService(new Intent(getActivity(), GpsStatusRemoteService.class), conn, Context.BIND_AUTO_CREATE);

    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        Log.d(TAG, "onCreateView");
        View view = inflater.inflate(R.layout.run_frag, container, false);
        unbinder = ButterKnife.bind(this, view);

        runRoot.setPadding(0, SizeUtil.getStatusBarHeight(getActivity()), 0, 0);
        Glide.with(getActivity()).load(R.drawable.run_bg).centerCrop().into(bgRunFrag);

        return view;
    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    @OnClick(R.id.circle_btn)
    public void onViewClicked() {


        if (currentStatus == SIGNAL_BAD) {

            new AlertDialog.Builder(getActivity())
                    .setMessage("GPS信号较弱，记录结果可能不准确，确定要开始跑步吗？")
                    .setNegativeButton("再等等", null)
                    .setTitle("温馨提示")
                    .setPositiveButton("毅然开跑", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            Intent intent = new Intent(getActivity(), UserRunActivity.class);
                            startActivity(intent);
                        }
                    })
                    .setCancelable(true)
                    .show();

        } else if (currentStatus == SIGNAL_NONE) {

            new AlertDialog.Builder(getActivity())
                    .setMessage("GPS未开启，无法记录轨迹")
                    .setNegativeButton("取消", null)
                    .setTitle("温馨提示")
                    .setPositiveButton("前往设置", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            startActivityForResult(new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS), 0);
                        }
                    })
                    .setCancelable(true)
                    .show();

        } else {
            Intent intent = new Intent(getActivity(), UserRunActivity.class);
            startActivity(intent);
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.d(TAG, "onDestroy");
        getActivity().unbindService(conn);
        getActivity().stopService(mIntent);
        if (mGpsStatusService != null) {
            try {
                mGpsStatusService.unRegisterCallback(iGpsStatusCallback);
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        }
    }
}
