package com.n1njac.yiqipao.android.ui.fragment;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.n1njac.yiqipao.android.R;
import com.n1njac.yiqipao.android.bmobObject.RunDataBmob;
import com.n1njac.yiqipao.android.bmobObject.UserInfoBmob;
import com.n1njac.yiqipao.android.ui.activity.ExecPlanActivity;
import com.n1njac.yiqipao.android.ui.activity.HistoryRecordListActivity;
import com.n1njac.yiqipao.android.ui.widget.DistanceDisplayArcView;
import com.n1njac.yiqipao.android.utils.SizeUtil;
import com.n1njac.yiqipao.android.utils.TimeUtil;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.BmobUser;
import cn.bmob.v3.datatype.BmobDate;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;

import static android.app.Activity.RESULT_OK;

/**
 * Created by huanglei on 2017/1/12.
 */

public class PersonalRunInfoFragment extends Fragment {

    private static final String TAG = PersonalRunInfoFragment.class.getSimpleName();
    private DistanceDisplayArcView distanceDisplayArcView;
    private TextView exec, history;
    private TextView remindText;
    private RelativeLayout root;
    private SharedPreferences mPrefs;

    private UpdateRunDataReceiver mReceiver;

    public static final String UPDATE_RUN_DATA_ACTION = "com.n1njac.yiqipao.android.update_run_data";


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.personal_run_frag, container, false);
        root = (RelativeLayout) view.findViewById(R.id.user_run_info_relat);
        root.setPadding(0, SizeUtil.getStatusBarHeight(getActivity()), 0, 0);
        exec = (TextView) view.findViewById(R.id.exec_text);
        history = (TextView) view.findViewById(R.id.history_tx);
        remindText = (TextView) view.findViewById(R.id.remind_tx);
        mPrefs = PreferenceManager.getDefaultSharedPreferences(getActivity());
        exec.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), ExecPlanActivity.class);
                startActivityForResult(intent, 1);
            }
        });
        history.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                Intent intent = new Intent(getActivity(), HistoryDistanceActivity.class);
                Intent intent = new Intent(getActivity(), HistoryRecordListActivity.class);
                startActivity(intent);
            }
        });
        distanceDisplayArcView = (DistanceDisplayArcView) view.findViewById(R.id.arc_view);
        queryTodayCurrentDistance();

        mReceiver = new UpdateRunDataReceiver();
        IntentFilter filter = new IntentFilter();
        filter.addAction(UPDATE_RUN_DATA_ACTION);
        getActivity().registerReceiver(mReceiver, filter);

        return view;
    }

    private void queryTodayCurrentDistance() {

        BmobQuery<RunDataBmob> query = new BmobQuery<>();
        List<BmobQuery<RunDataBmob>> queryList = new ArrayList<>();

        BmobQuery<RunDataBmob> startQ = new BmobQuery<>();
        BmobQuery<RunDataBmob> endQ = new BmobQuery<>();

        long systemTime = System.currentTimeMillis();
        StringBuilder timeYMD1 = TimeUtil.getCurrentTimeYMD(systemTime);
        StringBuilder timeYMD2 = TimeUtil.getCurrentTimeYMD(systemTime);
        //一天的完整时间
        String startTime = timeYMD1.append(" 00:00:00").toString();
        String endTime = timeYMD2.append(" 23:59:59").toString();
        Log.d(TAG, "start time:" + startTime);
        Log.d(TAG, "end time:" + endTime);

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date startDate = null;
        Date endDate = null;
        try {
            startDate = sdf.parse(startTime);
            endDate = sdf.parse(endTime);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        startQ.addWhereGreaterThanOrEqualTo("createdAt", new BmobDate(startDate));
        queryList.add(startQ);
        endQ.addWhereLessThanOrEqualTo("createdAt", new BmobDate(endDate));
        queryList.add(endQ);

        query.and(queryList);
        String objectId = BmobUser.getCurrentUser(UserInfoBmob.class).getObjectId();
        Log.i(TAG, "object id:" + objectId);
        query.addWhereEqualTo("pUserObjectId", objectId);

        query.findObjects(new FindListener<RunDataBmob>() {
            @Override
            public void done(List<RunDataBmob> list, BmobException e) {
                if (e == null) {
                    Log.d(TAG, "data size:" + list.size());
                    Log.d(TAG, "data:" + list.get(0).getRunDistance());

                    setDistance(list);

                } else {
                    Log.d(TAG, "error:" + e.getErrorCode() + " " + e.getLocalizedMessage());

//                    String distance = mPrefs.getString("distance", "10");
//                    distanceDisplayArcView.setNowDistance(Double.parseDouble(distance), 0.0);
                }

            }
        });

    }

    private void setDistance(List<RunDataBmob> list) {

        double runDistance;
        double mTodayTotalDistance = 0.0;
        for (RunDataBmob data : list) {
            if (!data.getRunDistance().equals("")) {
                runDistance = Double.parseDouble(data.getRunDistance().trim());
                mTodayTotalDistance += runDistance;
            }
        }

        Log.d(TAG, "total distance:" + mTodayTotalDistance);

        String planDistance = mPrefs.getString("distance", "10");
        distanceDisplayArcView.setNowDistance(Double.parseDouble(planDistance), mTodayTotalDistance);

    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == RESULT_OK) {
            queryTodayCurrentDistance();
        }
    }


    private class UpdateRunDataReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            Log.d(TAG, "------------>receive update");
            queryTodayCurrentDistance();
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        if (mReceiver != null) {
            getActivity().unregisterReceiver(mReceiver);
        }
    }
}
