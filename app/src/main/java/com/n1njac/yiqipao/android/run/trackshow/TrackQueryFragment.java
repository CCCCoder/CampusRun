package com.n1njac.yiqipao.android.run.trackshow;

import android.annotation.SuppressLint;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.baidu.mapapi.map.BitmapDescriptor;
import com.baidu.mapapi.map.BitmapDescriptorFactory;
import com.baidu.mapapi.map.MapStatusUpdate;
import com.baidu.mapapi.map.MapStatusUpdateFactory;
import com.baidu.mapapi.map.MarkerOptions;
import com.baidu.mapapi.map.PolylineOptions;
import com.baidu.mapapi.model.LatLng;
import com.baidu.mapapi.model.LatLngBounds;
import com.baidu.trace.OnTrackListener;
import com.n1njac.yiqipao.android.R;
import com.n1njac.yiqipao.android.TrackApplication;
import com.n1njac.yiqipao.android.run.trackutils.DateDialog;
import com.n1njac.yiqipao.android.run.trackutils.GsonService;


import org.json.JSONException;
import org.json.JSONObject;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.n1njac.yiqipao.android.run.trackutils.DateUtils;
import com.n1njac.yiqipao.android.run.trackutils.HistoryTrackData;

/**
 * 轨迹查询
 */
@SuppressLint("NewApi")
public class TrackQueryFragment extends Fragment implements OnClickListener {

    private TrackApplication trackApp = null;

    private Button btnDate = null;

    private Button btnProcessed = null;

    private Button btnDistance = null;

    private int startTime = 0;
    private int endTime = 0;

    private int year = 0;
    private int month = 0;
    private int day = 0;

    // 起点图标
    private static BitmapDescriptor bmStart;
    // 终点图标
    private static BitmapDescriptor bmEnd;

    // 起点图标覆盖物
    private static MarkerOptions startMarker = null;
    // 终点图标覆盖物
    private static MarkerOptions endMarker = null;
    // 路线覆盖物
    public static PolylineOptions polyline = null;

    private static MarkerOptions markerOptions = null;

    /**
     * Track监听器
     */
    protected static OnTrackListener trackListener = null;

    private MapStatusUpdate msUpdate = null;

    private TextView tvDatetime = null;

    private static int isProcessed = 0;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_trackquery,
                container, false);

        init(view);

        // 初始化OnTrackListener
        initOnTrackListener();

        return view;
    }

    /**
     * 初始化
     */
    private void init(final View view) {

        btnDate = (Button) view.findViewById(R.id.btn_date);
        btnProcessed = (Button) view.findViewById(R.id.btn_isprocessed);
        btnDistance = (Button) view.findViewById(R.id.btn_distance);

        btnDate.setOnClickListener(this);
        btnProcessed.setOnClickListener(this);
        btnDistance.setOnClickListener(this);

        tvDatetime = (TextView) view.findViewById(R.id.tv_datetime);
        tvDatetime.setText(" 当前日期 : " + DateUtils.getCurrentDate() + " ");

    }

    /**
     * 查询历史轨迹
     */
    private void queryHistoryTrack(int processed, String processOption) {

        // entity标识
        String entityName = trackApp.getEntityName();
        // 是否返回精简的结果（0 : 否，1 : 是）
        int simpleReturn = 0;
        // 是否返回纠偏后轨迹（0 : 否，1 : 是）
        int isProcessed = processed;
        // 开始时间
        if (startTime == 0) {
            startTime = (int) (System.currentTimeMillis() / 1000 - 12 * 60 * 60);
        }
        if (endTime == 0) {
            endTime = (int) (System.currentTimeMillis() / 1000);
        }
        // 分页大小
        int pageSize = 1000;
        // 分页索引
        int pageIndex = 1;

        trackApp.getClient().queryHistoryTrack(trackApp.getServiceId(), entityName, simpleReturn,
                isProcessed, processOption,
                startTime, endTime,
                pageSize,
                pageIndex,
                trackListener);
    }

    // 查询里程
    private void queryDistance(int processed, String processOption) {

        // entity标识
        String entityName = trackApp.getEntityName();

        // 是否返回纠偏后轨迹（0 : 否，1 : 是）
        int isProcessed = processed;

        // 里程补充
        String supplementMode = "driving";

        // 开始时间
        if (startTime == 0) {
            startTime = (int) (System.currentTimeMillis() / 1000 - 12 * 60 * 60);
        }
        // 结束时间
        if (endTime == 0) {
            endTime = (int) (System.currentTimeMillis() / 1000);
        }

        trackApp.getClient().queryDistance(trackApp.getServiceId(), entityName, isProcessed, processOption,
                supplementMode, startTime, endTime, trackListener);
    }

    /**
     * 轨迹查询(先选择日期，再根据是否纠偏，发送请求)
     */
    private void queryTrack() {


        // 选择日期
        int[] date = null;
        DisplayMetrics dm = new DisplayMetrics();
        this.getActivity().getWindowManager().getDefaultDisplay().getMetrics(dm);
        int width = dm.widthPixels;
        int height = dm.heightPixels;


        if (year == 0 && month == 0 && day == 0) {
            String curDate = DateUtils.getCurrentDate();
            date = DateUtils.getYMDArray(curDate, "-");
        }

        if (date != null) {
            year = date[0];
            month = date[1];
            day = date[2];
        }


        DateDialog dateDiolog = new DateDialog(getContext(), new DateDialog.PriorityListener() {


            public void refreshPriorityUI(String sltYear, String sltMonth,
                                          String sltDay, DateDialog.CallBack back) {

                Log.d("TGA", sltYear + sltMonth + sltDay);

                year = Integer.parseInt(sltYear);
                month = Integer.parseInt(sltMonth);
                day = Integer.parseInt(sltDay);
                String st = year + "年" + month + "月" + day + "日0时0分0秒";
                String et = year + "年" + month + "月" + day + "日23时59分59秒";

                startTime = Integer.parseInt(DateUtils.getTimeToStamp(st));
                endTime = Integer.parseInt(DateUtils.getTimeToStamp(et));

                back.execute();
            }

        }, new DateDialog.CallBack() {

            public void execute() {

                tvDatetime.setText(" 当前日期 : " + year + "-" + month + "-" + day + " ");
                // 选择完日期，根据是否纠偏发送轨迹查询请求
                if (0 == isProcessed) {
                    Toast.makeText(getActivity(), "正在查询历史轨迹，请稍候", Toast.LENGTH_SHORT).show();
                    queryHistoryTrack(0, null);
                } else {
                    Toast.makeText(getActivity(), "正在查询纠偏后的历史轨迹，请稍候", Toast.LENGTH_SHORT).show();
                    queryHistoryTrack(1, "need_denoise=1,need_vacuate=1,need_mapmatch=1");
                }
            }
        }, year, month, day, width, height, "选择日期", 1);

        Window window = dateDiolog.getWindow();
        assert window != null;
        window.setGravity(Gravity.CENTER); // 此处可以设置dialog显示的位置
        dateDiolog.setCancelable(true);
        dateDiolog.show();


    }

    /**
     * 显示历史轨迹
     *
     * @param historyTrack
     */
    private void showHistoryTrack(String historyTrack) {

        HistoryTrackData historyTrackData = GsonService.parseJson(historyTrack,
                HistoryTrackData.class);

        List<LatLng> latLngList = new ArrayList<LatLng>();
        if (historyTrackData != null && historyTrackData.getStatus() == 0) {
            if (historyTrackData.getListPoints() != null) {
                latLngList.addAll(historyTrackData.getListPoints());
            }

            // 绘制历史轨迹
            drawHistoryTrack(latLngList, historyTrackData.distance);

        }

    }

    public void onClick(View v) {
        // TODO Auto-generated method stub
        switch (v.getId()) {
            case R.id.btn_date:
                // 查询轨迹
                queryTrack();
                break;

            case R.id.btn_isprocessed:
                isProcessed = isProcessed ^ 1;
                if (0 == isProcessed) {
                    btnProcessed.setBackgroundColor(Color.rgb(0xff, 0xff, 0xff));
                    btnProcessed.setTextColor(Color.rgb(0x00, 0x00, 0x00));
                    Toast.makeText(getActivity(), "正在查询历史轨迹，请稍候", Toast.LENGTH_SHORT).show();
                    queryHistoryTrack(0, null);
                } else {
                    btnProcessed.setBackgroundColor(Color.rgb(0x99, 0xcc, 0xff));
                    btnProcessed.setTextColor(Color.rgb(0x00, 0x00, 0xd8));
                    Toast.makeText(getActivity(), "正在查询纠偏后的历史轨迹，请稍候", Toast.LENGTH_SHORT).show();
                    queryHistoryTrack(1, "need_denoise=1,need_vacuate=1,need_mapmatch=1");
                }
                break;

            case R.id.btn_distance:
                queryDistance(0, null);
                break;

            default:
                break;
        }
    }

    /**
     * 初始化OnTrackListener
     */
    private void initOnTrackListener() {

        trackListener = new OnTrackListener() {

            // 请求失败回调接口
            @Override
            public void onRequestFailedCallback(String arg0) {
                // TODO Auto-generated method stub
                trackApp.getmHandler().obtainMessage(0, "track请求失败回调接口消息 : " + arg0).sendToTarget();
            }

            // 查询历史轨迹回调接口
            @Override
            public void onQueryHistoryTrackCallback(String arg0) {
                // TODO Auto-generated method stub
                super.onQueryHistoryTrackCallback(arg0);
                showHistoryTrack(arg0);
            }

            @Override
            public void onQueryDistanceCallback(String arg0) {
                // TODO Auto-generated method stub
                try {
                    JSONObject dataJson = new JSONObject(arg0);
                    if (null != dataJson && dataJson.has("status") && dataJson.getInt("status") == 0) {
                        double distance = dataJson.getDouble("distance");
                        DecimalFormat df = new DecimalFormat("#.0");
                        trackApp.getmHandler().obtainMessage(0, "里程 : " + df.format(distance) + "米").sendToTarget();
                    }
                } catch (JSONException e) {
                    // TODO Auto-generated catch block
                    trackApp.getmHandler().obtainMessage(0, "queryDistance回调消息 : " + arg0).sendToTarget();
                }
            }

            @Override
            public Map<String, String> onTrackAttrCallback() {
                // TODO Auto-generated method stub
                System.out.println("onTrackAttrCallback");
                return null;
            }

        };
    }

    /**
     * 绘制历史轨迹
     *
     * @param points
     */
    private void drawHistoryTrack(final List<LatLng> points, final double distance) {
        // 绘制新覆盖物前，清空之前的覆盖物
        trackApp.getmBaiduMap().clear();

        if (points.size() == 1) {
            points.add(points.get(0));
        }

        if (points == null || points.size() == 0) {
            trackApp.getmHandler().obtainMessage(0, "当前查询无轨迹点").sendToTarget();
            resetMarker();
        } else if (points.size() > 1) {

            LatLng llC = points.get(0);
            LatLng llD = points.get(points.size() - 1);
            LatLngBounds bounds = new LatLngBounds.Builder()
                    .include(llC).include(llD).build();

            msUpdate = MapStatusUpdateFactory.newLatLngBounds(bounds);

            bmStart = BitmapDescriptorFactory.fromResource(R.mipmap.icon_start);
            bmEnd = BitmapDescriptorFactory.fromResource(R.mipmap.icon_end);

            // 添加起点图标
            startMarker = new MarkerOptions()
                    .position(points.get(points.size() - 1)).icon(bmStart)
                    .zIndex(9).draggable(true);

            // 添加终点图标
            endMarker = new MarkerOptions().position(points.get(0))
                    .icon(bmEnd).zIndex(9).draggable(true);

            // 添加路线（轨迹）
            polyline = new PolylineOptions().width(10)
                    .color(Color.RED).points(points);

            markerOptions = new MarkerOptions();
            markerOptions.flat(true);
            markerOptions.anchor(0.5f, 0.5f);
            markerOptions.icon(BitmapDescriptorFactory
                    .fromResource(R.mipmap.icon_gcoding));
            markerOptions.position(points.get(points.size() - 1));

            addMarker();

            trackApp.getmHandler().obtainMessage(0, "当前轨迹里程为 : " + (int) distance + "米").sendToTarget();

        }

    }

    /**
     * 添加覆盖物
     */
    protected void addMarker() {

        if (null != msUpdate) {
            trackApp.getmBaiduMap().animateMapStatus(msUpdate, 2000);
        }

        if (null != startMarker) {
            trackApp.getmBaiduMap().addOverlay(startMarker);
        }

        if (null != endMarker) {
            trackApp.getmBaiduMap().addOverlay(endMarker);
        }

        if (null != polyline) {
            trackApp.getmBaiduMap().addOverlay(polyline);
        }

    }

    /**
     * 重置覆盖物
     */
    private void resetMarker() {
        startMarker = null;
        endMarker = null;
        polyline = null;
    }

    public static final TrackQueryFragment newInstance(TrackApplication trackApp) {
        TrackQueryFragment fragment = new TrackQueryFragment();
        fragment.trackApp = trackApp;
        return fragment;
    }

}
