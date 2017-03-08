package com.n1njac.yiqipao.android.run.trackshow;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.res.Resources;
import android.graphics.Color;
import android.text.InputType;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.baidu.mapapi.map.BaiduMap.OnMapClickListener;
import com.baidu.mapapi.map.BitmapDescriptor;
import com.baidu.mapapi.map.BitmapDescriptorFactory;
import com.baidu.mapapi.map.CircleOptions;
import com.baidu.mapapi.map.MapPoi;
import com.baidu.mapapi.map.MarkerOptions;
import com.baidu.mapapi.map.Overlay;
import com.baidu.mapapi.map.OverlayOptions;
import com.baidu.mapapi.map.PolygonOptions;
import com.baidu.mapapi.map.Stroke;
import com.baidu.mapapi.model.LatLng;
import com.baidu.trace.OnGeoFenceListener;
import com.baidu.trace.model.CreateLocalFenceResult;
import com.baidu.trace.model.DeleteLocalFenceResult;
import com.baidu.trace.model.LocalCircularFence;
import com.n1njac.yiqipao.android.R;
import com.n1njac.yiqipao.android.TrackApplication;
import com.n1njac.yiqipao.android.run.trackutils.DateUtils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * 地理围栏
 */
@SuppressLint("NewApi")
public class Geofence implements OnClickListener {

    private TrackApplication trackApp = null;

    private Context mContext = null;

    protected PopupWindow popupwindow = null;

    private Button btnCircularFence = null;
    private Button btnVertexesFence = null;
    private Button btnDeleteFence = null;
    private Button btnMonitoredStatus = null;
    private Button btnHistoryAlarm = null;

    private LayoutInflater mInflater = null;

    // 围栏圆心纬度
    private double latitude = 0;

    // 围栏圆心经度
    private double longitude = 0;

    // 围栏半径
    private static int radius = 100;

    // 顶点数量
    private static int vertexNumber = 4;

    private static int radiusTemp = radius;

    private List<LatLng> vertexs = new ArrayList<LatLng>();

    private List<LatLng> vertexsTemp = new ArrayList<LatLng>();

    // 围栏类型（0: 服务端围栏、1：本地围栏）
    private static int fenceType = 0;

    // 围栏形状（0：圆形、1：多边形）
    private static int fenceShape = 0;

    // 围栏编号
    private static int serverFenceId = 0;

    private static List<Long> localFenceIds = new ArrayList<Long>();

    // 地理围栏监听器
    private static OnGeoFenceListener geoFenceListener = null;

    private static Overlay fenceOverlay = null;

    private static Overlay fenceOverlayTemp = null;


    // 围栏覆盖物
    private static OverlayOptions fenceOverlayOption = null;

    private static OverlayOptions fenceOverlayOptionsTemp = null;

    private List<Overlay> overlays = new ArrayList<Overlay>();

    // 围栏去噪精度
    private static int precision = 30;

    private BitmapDescriptor pointBitmap = null;

    // 当前显示的围栏类型
    private static int showFenceType = 0;

    private OnMapClickListener mapClickListener = new OnMapClickListener() {

        public void onMapClick(LatLng arg0) {
            // TODO Auto-generated method stub

            switch (fenceShape) {
                case 0:
                    if (null != fenceOverlayOption) {
                        fenceOverlayOptionsTemp = fenceOverlayOption;
                    }
                    if (null != fenceOverlay) {
                        fenceOverlay.remove();
                    }

                    latitude = arg0.latitude;
                    longitude = arg0.longitude;

                    fenceOverlayOption = new CircleOptions().fillColor(0x000000FF).center(arg0)
                            .stroke(new Stroke(5, Color.rgb(0xff, 0x00, 0x33)))
                            .radius(radius);

                    addMarker();
                    createOrUpdateDialog();

                    break;

                case 1:
                    vertexs.add(arg0);
                    int resourceId;

                    Resources res = mContext.getResources();
                    if (vertexs.size() <= 10) {
                        resourceId =
                                res.getIdentifier("icon_mark" + vertexs.size(), "mipmap", trackApp.getPackageName());
                    } else {
                        resourceId = res.getIdentifier("icon_gcoding", "mipmap",
                                mContext.getPackageName());
                    }

                    pointBitmap = BitmapDescriptorFactory
                            .fromResource(resourceId);

                    OverlayOptions overlayOptions = new MarkerOptions().position(arg0)
                            .icon(pointBitmap).zIndex(9).draggable(true);
                    overlays.add(trackApp.getmBaiduMap().addOverlay(overlayOptions));

                    if (vertexs.size() == vertexNumber) {

                        if (null != fenceOverlayOption) {
                            fenceOverlayOptionsTemp = fenceOverlayOption;
                        }
                        if (null != fenceOverlay) {
                            fenceOverlay.remove();
                        }

                        fenceOverlayOption =
                                new PolygonOptions().points(vertexs).stroke(new Stroke(vertexNumber, 0xAAFF0000))
                                        .fillColor(0x30FFFFFF);
                        addMarker();
                        createOrUpdateDialog();
                    }

                    break;

                default:
                    break;
            }

        }

        public boolean onMapPoiClick(MapPoi arg0) {
            // TODO Auto-generated method stub
            return false;
        }
    };

    /**
     * 创建本地围栏
     */
    private void createLocalCircularFence() {
        System.out.println("createLocalFence");
        com.baidu.trace.model.LatLng center = new com.baidu.trace.model.LatLng(latitude, longitude);
        // 本地围栏名称
        String fenceName = "local_fence_01";
        // 本地围栏去噪精度
        double precision = 200;
        LocalCircularFence circularFence =
                new LocalCircularFence(fenceName, center, radius, precision, com.baidu.trace.model.CoordType.BD);
        trackApp.getClient().createLocalFence(circularFence, geoFenceListener);
    }

    /**
     * 删除本地围栏
     */
    private void deleteLocalCircularFence() {
        // 传list，表示删除一个或多个本地围栏；传null表示删除所有本地围栏
        trackApp.getClient().deleteLocalFence(localFenceIds, geoFenceListener);
    }

    /**
     * 创建服务端圆形围栏（若创建围栏时，还未创建entity，请先调用addEntity(...)添加entity）
     */
    private void createServerCircularFence() {

        // 创建者（entity标识）
        String creator = trackApp.getEntityName();
        // 围栏名称
        String fenceName = trackApp.getEntityName() + "_fence";
        // 围栏描述
        String fenceDesc = "test";
        // 监控对象列表（多个entityName，以英文逗号"," 分割）
        String monitoredPersons = trackApp.getEntityName();
        // 观察者列表（多个entityName，以英文逗号"," 分割）
        String observers = trackApp.getEntityName();
        // 生效时间列表（表示几点几分 到 几点几分围栏生效，示例为08点00分 到 23点00分）
        String validTimes = "0800,2300";
        // 生效周期
        int validCycle = 4;
        // 围栏生效日期
        String validDate = "";
        // 生效日期列表
        String validDays = "";
        // 坐标类型 （1：GPS经纬度，2：国测局经纬度，3：百度经纬度）
        int coordType = 3;
        // 围栏圆心（圆心位置, 格式 : "经度,纬度"）
        String center = longitude + "," + latitude;
        // 围栏半径（单位 : 米）
        double radius = Geofence.radius;
        // 报警条件（1：进入时触发提醒，2：离开时触发提醒，3：进入离开均触发提醒）
        int alarmCondition = 3;

        trackApp.getClient().createCircularFence(trackApp.getServiceId(), creator, fenceName, fenceDesc,
                monitoredPersons, observers,
                validTimes, validCycle, validDate, validDays, coordType, center, radius, precision, alarmCondition,
                geoFenceListener);
    }

    /**
     * 创建服务端多边形围栏（若创建围栏时，还未创建entity，请先调用addEntity(...)添加entity）
     */
    private void createServerVertexesFence() {

        // 创建者（entity标识）
        String creator = trackApp.getEntityName();
        // 围栏名称
        String fenceName = trackApp.getEntityName() + "_fence";
        // 围栏描述
        String fenceDesc = "test";
        // 监控对象列表（多个entityName，以英文逗号"," 分割）
        String monitoredPersons = trackApp.getEntityName();
        // 观察者列表（多个entityName，以英文逗号"," 分割）
        String observers = trackApp.getEntityName();
        // 生效时间列表（表示几点几分 到 几点几分围栏生效，示例为08点00分 到 23点00分）
        String validTimes = "0800,2300";
        // 生效周期
        int validCycle = 4;
        // 围栏生效日期
        String validDate = "";
        // 生效日期列表
        String validDays = "";
        // 坐标类型 （1：GPS经纬度，2：国测局经纬度，3：百度经纬度）
        int coordType = 3;
        // 报警条件（1：进入时触发提醒，2：离开时触发提醒，3：进入离开均触发提醒）
        int alarmCondition = 3;
        // 顶点列表
        StringBuilder vertexsStr = new StringBuilder();
        for (LatLng ll : vertexs) {
            vertexsStr.append(ll.longitude).append(",").append(ll.latitude).append(";");
        }
        trackApp.getClient().createVertexesFence(trackApp.getServiceId(), creator, fenceName, fenceDesc,
                monitoredPersons, observers,
                validTimes, validCycle, validDate, validDays, coordType,
                vertexsStr.substring(0, vertexsStr.length() - 1), precision,
                alarmCondition,
                geoFenceListener);
    }

    /**
     * 删除服务端围栏
     */
    private void deleteServerFence(int fenceId) {
        trackApp.getClient().deleteFence(trackApp.getServiceId(), fenceId, geoFenceListener);
    }

    /**
     * 更新服务端圆形围栏
     */
    private void updateServerCircularFence() {
        // 围栏名称
        String fenceName = trackApp.getEntityName() + "_fence";
        // 围栏ID
        int fenceId = Geofence.serverFenceId;
        // 围栏描述
        String fenceDesc = "test fence";
        // 监控对象列表（多个entityName，以英文逗号"," 分割）
        String monitoredPersons = trackApp.getEntityName();
        // 观察者列表（多个entityName，以英文逗号"," 分割）
        String observers = trackApp.getEntityName();
        // 生效时间列表（表示几点几分 到 几点几分围栏生效，示例为08点00分 到 23点00分）
        String validTimes = "0800,2300";
        // 生效周期
        int validCycle = 4;
        // 围栏生效日期
        String validDate = "";
        // 生效日期列表
        String validDays = "";
        // 坐标类型 （1：GPS经纬度，2：国测局经纬度，3：百度经纬度）
        int coordType = 3;
        // 围栏圆心（圆心位置, 格式 : "经度,纬度"）
        String center = longitude + "," + latitude;
        // 围栏半径（单位 : 米）
        double radius = Geofence.radius;
        // 报警条件（1：进入时触发提醒，2：离开时触发提醒，3：进入离开均触发提醒）
        int alarmCondition = 3;

        trackApp.getClient().updateCircularFence(trackApp.getServiceId(), fenceName, fenceId, fenceDesc,
                monitoredPersons,
                observers, validTimes, validCycle, validDate, validDays, coordType, center, radius, precision,
                alarmCondition,
                geoFenceListener);
    }

    /**
     * 更新服务端多边形围栏
     */
    private void updateServerVertexesFence() {
        // 围栏名称
        String fenceName = trackApp.getEntityName() + "_fence";
        // 围栏ID
        int fenceId = Geofence.serverFenceId;
        // 围栏描述
        String fenceDesc = "test fence";
        // 监控对象列表（多个entityName，以英文逗号"," 分割）
        String monitoredPersons = trackApp.getEntityName();
        // 观察者列表（多个entityName，以英文逗号"," 分割）
        String observers = trackApp.getEntityName();
        // 生效时间列表（表示几点几分 到 几点几分围栏生效，示例为08点00分 到 23点00分）
        String validTimes = "0800,2300";
        // 生效周期
        int validCycle = 4;
        // 围栏生效日期
        String validDate = "";
        // 生效日期列表
        String validDays = "";
        // 坐标类型 （1：GPS经纬度，2：国测局经纬度，3：百度经纬度）
        int coordType = 3;
        // 顶点列表
        StringBuilder vertexsStr = new StringBuilder();
        for (LatLng ll : vertexs) {
            vertexsStr.append(ll.longitude).append(",").append(ll.latitude).append(";");
        }
        System.out.println("vertexs : " + vertexsStr.substring(0, vertexsStr.length() - 1));
        // 报警条件（1：进入时触发提醒，2：离开时触发提醒，3：进入离开均触发提醒）
        int alarmCondition = 3;
        trackApp.getClient().updateVertexesFence(trackApp.getServiceId(), fenceName, fenceId, fenceDesc,
                monitoredPersons, observers,
                validTimes, validCycle, validDate, validDays, coordType,
                vertexsStr.substring(0, vertexsStr.length() - 1), precision, alarmCondition,
                geoFenceListener);
    }

    /**
     * 查询服务端围栏列表
     */
    protected void queryServerFenceList() {
        // 创建者（entity标识）
        String creator = trackApp.getEntityName();
        // 围栏ID列表
        String fenceIds = "";
        trackApp.getClient().queryFenceList(trackApp.getServiceId(), creator, fenceIds, geoFenceListener);
    }

    /**
     * 监控状态（服务端围栏）
     */
    private void monitoredStatus() {
        // 围栏ID
        int fenceId = Geofence.serverFenceId;
        // 监控对象列表（多个entityName，以英文逗号"," 分割）
        String monitoredPersons = trackApp.getEntityName();
        trackApp.getClient().queryMonitoredStatus(trackApp.getServiceId(), fenceId, monitoredPersons,
                geoFenceListener);
    }

    /**
     * 指定位置的监控状态(服务端围栏）
     */
    @SuppressWarnings("unused")
    private void monitoredStatusByLocation() {
        // 围栏ID
        int fenceId = Geofence.serverFenceId;
        // 监控对象列表（多个entityName，以英文逗号"," 分割）
        String monitoredPersons = trackApp.getEntityName();
        trackApp.getClient().queryMonitoredStatusByLocation(trackApp.getServiceId(), fenceId,
                monitoredPersons, "116.31283995461331,40.0469717410504,3", geoFenceListener);

        trackApp.getClient().queryMonitoredStatusByLocation(trackApp.getServiceId(), fenceId,
                monitoredPersons, "117,41,3", geoFenceListener);
    }

    /**
     * 报警信息（服务端围栏）
     */
    private void historyAlarm() {
        // 围栏ID
        int fenceId = Geofence.serverFenceId;
        // 监控对象列表（多个entityName，以英文逗号"," 分割）
        String monitoredPersons = trackApp.getEntityName();
        // 开始时间（unix时间戳）
        int beginTime = (int) (System.currentTimeMillis() / 1000 - 12 * 60 * 60);
        // 结束时间（unix时间戳）
        int endTime = (int) (System.currentTimeMillis() / 1000);

        trackApp.getClient().queryFenceHistoryAlarmInfo(trackApp.getServiceId(), fenceId, monitoredPersons, beginTime,
                endTime,
                geoFenceListener);
    }

    /**
     * 点击事件
     */
    public void onClick(View v) {
        // TODO Auto-generated method stub
        switch (v.getId()) {

            // 设置圆形围栏
            case R.id.btn_circularfence:
                fenceShape = 0;
                fenceType = 0;
                inputDialog(mContext.getString(R.string.set_fence), mContext.getString(R.string.circular_fence_caption));
                trackApp.getmBaiduMap().setOnMapClickListener(mapClickListener);
                if (popupwindow != null && popupwindow.isShowing()) {
                    popupwindow.dismiss();
                }
                break;

            // 设置多边形围栏
            case R.id.btn_vertexesfence:
                fenceShape = 1;
                fenceType = 0;
                vertexsTemp.addAll(vertexs);
                vertexs.clear();
                inputDialog(mContext.getString(R.string.set_fence), mContext.getString(R.string.vertexes_fence_caption));
                trackApp.getmBaiduMap().setOnMapClickListener(mapClickListener);
                if (popupwindow != null && popupwindow.isShowing()) {
                    popupwindow.dismiss();
                }
                break;

            // 删除围栏
            case R.id.btn_deletefence:
                // 围栏形状为-1，代表删除围栏操作
                fenceShape = -1;
                fenceType = 0;
                inputDialog(mContext.getString(R.string.delete_fence), null);
                break;

            // 历史报警
            case R.id.btn_historyalarm:
                historyAlarm();
                if (popupwindow != null && popupwindow.isShowing()) {
                    popupwindow.dismiss();
                }
                break;

            // 监控对象状态
            case R.id.btn_monitoredstatus:
                monitoredStatus();
                if (popupwindow != null && popupwindow.isShowing()) {
                    popupwindow.dismiss();
                }
                break;

            default:
                break;
        }

    }

    @SuppressLint({"InflateParams", "ClickableViewAccessibility"})
    protected void initPopupWindowView() {

        // 获取自定义布局文件menu_geofence.xml的视图
        View customView = mInflater.inflate(R.layout.menu_geofence, null);
        popupwindow = new PopupWindow(customView, LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT, true);
        // 自定义view添加触摸事件
        customView.setOnTouchListener(new OnTouchListener() {

            public boolean onTouch(View v, MotionEvent event) {
                // TODO Auto-generated method stub
                if (popupwindow != null && popupwindow.isShowing()) {
                    popupwindow.dismiss();
                    popupwindow = null;
                }

                return false;
            }

        });

        btnCircularFence = (Button) customView.findViewById(R.id.btn_circularfence);
        btnVertexesFence = (Button) customView.findViewById(R.id.btn_vertexesfence);
        btnMonitoredStatus = (Button) customView.findViewById(R.id.btn_monitoredstatus);
        btnHistoryAlarm = (Button) customView.findViewById(R.id.btn_historyalarm);
        btnDeleteFence = (Button) customView.findViewById(R.id.btn_deletefence);

        btnCircularFence.setOnClickListener(this);
        btnVertexesFence.setOnClickListener(this);
        btnDeleteFence.setOnClickListener(this);
        btnMonitoredStatus.setOnClickListener(this);
        btnHistoryAlarm.setOnClickListener(this);

    }

    /**
     * 初始化OnGeoFenceListener
     */
    private void initOnGeoFenceListener() {
        // 初始化geoFenceListener
        geoFenceListener = new OnGeoFenceListener() {

            // 请求失败回调接口
            @Override
            public void onRequestFailedCallback(String arg0) {
                // TODO Auto-generated method stub
                trackApp.getmBaiduMap().clear();
                if (null != fenceOverlayOptionsTemp) {
                    fenceOverlayOption = fenceOverlayOptionsTemp;
                }

                radius = radiusTemp;
                addMarker();
                trackApp.getmHandler().obtainMessage(0, "geoFence请求失败回调接口消息 : " + arg0).sendToTarget();
            }

            @Override
            public void onCreateLocalFenceCallback(CreateLocalFenceResult arg0) {
                // TODO Auto-generated method stub
                if (null != arg0 && arg0.getStatus() == 0) {
                    localFenceIds.add(arg0.getFenceId());
                    fenceOverlayOptionsTemp = null;
                    showFenceType = 1;
                    trackApp.getmHandler().obtainMessage(0, "本地圆形围栏创建成功，围栏编号 : " + arg0.getFenceId()).sendToTarget();
                } else {
                    trackApp.getmBaiduMap().clear();
                    if (null != fenceOverlayOptionsTemp) {
                        fenceOverlayOption = fenceOverlayOptionsTemp;
                    }
                    radius = radiusTemp;
                    addMarker();
                    trackApp.getmHandler().obtainMessage(0, "创建本地圆形围栏回调接口消息 : " + arg0.toString()).sendToTarget();
                }
            }

            @Override
            public void onDeleteLocalFenceCallback(DeleteLocalFenceResult arg0) {
                // TODO Auto-generated method stub
                if (0 == arg0.getStatus()) {
                    if (1 == showFenceType) {
                        fenceOverlay.remove();
                        fenceOverlayOption = null;
                    }
                    localFenceIds.clear();
                }
                trackApp.getmHandler().obtainMessage(0, "删除本地围栏回调接口消息 : " + arg0.toString()).sendToTarget();
            }

            // 创建服务端圆形围栏回调接口
            @Override
            public void onCreateCircularFenceCallback(String arg0) {
                // TODO Auto-generated method stub

                JSONObject dataJson;
                try {
                    dataJson = new JSONObject(arg0);
                    if (null != dataJson && dataJson.has("status") && 0 == dataJson.getInt("status")) {
                        serverFenceId = dataJson.getInt("fence_id");
                        fenceOverlayOptionsTemp = null;
                        showFenceType = 0;
                        trackApp.getmHandler().obtainMessage(0, "服务端圆形围栏创建成功，围栏编号 : " + serverFenceId).sendToTarget();
                    } else {
                        trackApp.getmBaiduMap().clear();
                        if (null != fenceOverlayOptionsTemp) {
                            fenceOverlayOption = fenceOverlayOptionsTemp;
                        }
                        radius = radiusTemp;
                        addMarker();
                        trackApp.getmHandler().obtainMessage(0, "创建服务端圆形围栏回调接口消息 : " + arg0).sendToTarget();
                    }
                } catch (JSONException e) {
                    // TODO Auto-generated catch block
                    trackApp.getmHandler().obtainMessage(0, "解析创建服务端圆形围栏回调消息失败").sendToTarget();
                }
            }

            // 更新服务端圆形围栏回调接口
            @Override
            public void onUpdateCircularFenceCallback(String arg0) {
                // TODO Auto-generated method stub
                trackApp.getmHandler().obtainMessage(0, "更新服务端圆形围栏回调接口消息 : " + arg0).sendToTarget();
            }

            // 创建服务端多边形围栏回调接口
            @Override
            public void onCreateVertexesFenceCallback(String arg0) {
                // TODO Auto-generated method stub
                JSONObject dataJson;
                try {
                    dataJson = new JSONObject(arg0);
                    if (null != dataJson && dataJson.has("status") && 0 == dataJson.getInt("status")) {
                        serverFenceId = dataJson.getInt("fence_id");
                        fenceOverlayOptionsTemp = null;
                        showFenceType = 0;
                        trackApp.getmHandler().obtainMessage(0, "服务端多边形围栏创建成功，围栏编号 : " + serverFenceId).sendToTarget();
                    } else {
                        trackApp.getmBaiduMap().clear();
                        if (null != fenceOverlayOptionsTemp) {
                            fenceOverlayOption = fenceOverlayOptionsTemp;
                        }
                        addMarker();
                        trackApp.getmHandler().obtainMessage(0, "创建服务端多边形围栏回调接口消息 : " + arg0).sendToTarget();
                    }
                } catch (JSONException e) {
                    // TODO Auto-generated catch block
                    trackApp.getmHandler().obtainMessage(0, "解析创建服务端多边形围栏回调消息失败").sendToTarget();
                }
            }

            // 更新服务端多边形围栏回调接口
            @Override
            public void onUpdateVertexesFenceCallback(String arg0) {
                // TODO Auto-generated method stub
                trackApp.getmHandler().obtainMessage(0, "更新服务端多边形围栏回调接口消息 : " + arg0).sendToTarget();
            }

            // 删除服务端围栏回调接口
            @Override
            public void onDeleteFenceCallback(String arg0) {
                // TODO Auto-generated method stub

                JSONObject dataJson;
                try {
                    dataJson = new JSONObject(arg0);
                    if (null != dataJson && dataJson.has("status") && 0 == dataJson.getInt("status")) {
                        for (Overlay overlay : overlays) {
                            overlay.remove();
                        }
                        if (!vertexsTemp.isEmpty()) {
                            vertexs.clear();
                            vertexs.addAll(vertexsTemp);
                        }
                        if (0 == showFenceType) {
                            fenceOverlay.remove();
                            fenceOverlayOption = null;
                        }

                        serverFenceId = 0;
                    }
                } catch (JSONException e) {
                    // TODO Auto-generated catch block
                    System.out.println("解析删除服务端围栏回调接口消息失败");
                }

                trackApp.getmHandler().obtainMessage(0, "删除服务端围栏回调接口消息 : " + arg0).sendToTarget();
            }

            // 查询服务端围栏列表回调接口
            @Override
            public void onQueryFenceListCallback(String arg0) {
                // TODO Auto-generated method stub

                System.out.println("fenceList : " + arg0);

                JSONObject dataJson;
                try {
                    dataJson = new JSONObject(arg0);
                    if (null != dataJson && dataJson.has("status") && 0 == dataJson.getInt("status")) {
                        if (dataJson.has("size")) {
                            JSONArray jsonArray = dataJson.getJSONArray("fences");
                            JSONObject jsonObj = jsonArray.getJSONObject(0);

                            int shape = jsonObj.getInt("shape");
                            if (1 == shape) {
                                JSONObject center = jsonObj.getJSONObject("center");
                                latitude = center.getDouble("latitude");
                                longitude = center.getDouble("longitude");
                                LatLng latLng = new LatLng(latitude, longitude);

                                if (0 == serverFenceId) {
                                    radius = (int) (jsonObj.getDouble("radius"));
                                    fenceOverlayOption = new CircleOptions().fillColor(0x000000FF).center(latLng)
                                            .stroke(new Stroke(5, Color.rgb(0xff, 0x00, 0x33)))
                                            .radius(radius);
                                }
                            } else if (2 == shape) {

                                JSONArray vertexArray = jsonObj.getJSONArray("vertexes");
                                for (int i = 0; i < vertexArray.length(); ++i) {
                                    JSONObject vertex = vertexArray.getJSONObject(i);
                                    longitude = vertex.getDouble("longitude");
                                    latitude = vertex.getDouble("latitude");
                                    LatLng latLng = new LatLng(latitude, longitude);
                                    if (0 == serverFenceId) {
                                        vertexs.add(latLng);
                                    }
                                }

                                if (0 == serverFenceId) {
                                    fenceOverlayOption =
                                            new PolygonOptions().points(vertexs)
                                                    .stroke(new Stroke(vertexArray.length(), 0xAAFF0000))
                                                    .fillColor(0x30FFFFFF);
                                }
                            }

                            if (0 == serverFenceId) {
                                addMarker();
                            }

                            serverFenceId = jsonObj.getInt("fence_id");
                        }
                    }
                } catch (JSONException e) {
                    // TODO Auto-generated catch block
                    System.out.println("解析围栏列表回调消息失败");
                }

            }

            // 查询历史报警回调接口（服务端围栏）
            @Override
            public void onQueryHistoryAlarmCallback(String arg0) {
                // TODO Auto-generated method stub
                StringBuffer historyAlarm = new StringBuffer();
                JSONObject dataJson;
                try {
                    dataJson = new JSONObject(arg0);
                    if (null != dataJson && dataJson.has("status") && 0 == dataJson.getInt("status")) {
                        int size = dataJson.getInt("size");
                        for (int i = 0; i < size; ++i) {
                            JSONArray jsonArray = dataJson.getJSONArray("monitored_person_alarms");
                            JSONObject jsonObj = jsonArray.getJSONObject(i);
                            String mPerson = jsonObj.getString("monitored_person");
                            int alarmSize = jsonObj.getInt("alarm_size");
                            JSONArray jsonAlarms = jsonObj.getJSONArray("alarms");
                            historyAlarm.append("监控对象[" + mPerson + "]报警信息\n");
                            for (int j = 0; j < alarmSize && j < jsonAlarms.length(); ++j) {
                                String action = jsonAlarms.getJSONObject(j).getInt("action") == 1 ? "进入" : "离开";
                                String date = DateUtils.getDate(jsonAlarms.getJSONObject(j).getInt("time"));
                                historyAlarm.append(date + " 【" + action + "】围栏\n");
                            }
                        }
                        if (TextUtils.isEmpty(historyAlarm)) {
                            trackApp.getmHandler().obtainMessage(0, "未查询到历史报警信息").sendToTarget();
                        } else {
                            trackApp.getmHandler().obtainMessage(0, historyAlarm.toString()).sendToTarget();
                        }
                    }

                } catch (JSONException e) {
                    // TODO Auto-generated catch block
                    trackApp.getmHandler().obtainMessage(0, "解析查询历史报警回调消息失败").sendToTarget();
                }

            }

            // 查询监控对象状态回调接口
            @Override
            public void onQueryMonitoredStatusCallback(String arg0) {
                // TODO Auto-generated method stub

                JSONObject dataJson;
                StringBuffer status = new StringBuffer();
                try {
                    dataJson = new JSONObject(arg0);
                    if (null != dataJson && dataJson.has("status") && 0 == dataJson.getInt("status")) {
                        int size = dataJson.getInt("size");
                        for (int i = 0; i < size; ++i) {
                            JSONArray jsonArray = dataJson.getJSONArray("monitored_person_statuses");
                            JSONObject jsonObj = jsonArray.getJSONObject(0);
                            String mPerson = jsonObj.getString("monitored_person");
                            int mStatus = jsonObj.getInt("monitored_status");
                            if (1 == mStatus) {
                                status.append("监控对象[ " + mPerson + " ]在围栏内\n");
                            } else if (2 == mStatus) {
                                status.append("监控对象[ " + mPerson + " ]在围栏外\n");
                            } else {
                                status.append("监控对象[ " + mPerson + " ]状态未知\n");
                            }
                        }
                        trackApp.getmHandler().obtainMessage(0, status.toString()).sendToTarget();
                    } else {
                        trackApp.getmHandler().obtainMessage(0, "查询监控对象状态回调消息 : " + arg0).sendToTarget();
                    }
                } catch (JSONException e) {
                    // TODO Auto-generated catch block
                    trackApp.getmHandler().obtainMessage(0, "解析查询监控对象状态回调消息失败").sendToTarget();
                }
            }

            @Override
            public void onQueryLocalFenceStatusCallback(String arg0) {
                // TODO Auto-generated method stub

                System.out.println("onQueryLocalFenceStatusCallback : " + arg0);
                JSONObject dataJson;
                StringBuffer status = new StringBuffer();
                try {
                    dataJson = new JSONObject(arg0);
                    if (null != dataJson && dataJson.has("status") && 0 == dataJson.getInt("status")) {
                        int size = dataJson.getInt("size");
                        for (int i = 0; i < size; ++i) {
                            JSONArray jsonArray = dataJson.getJSONArray("monitored_person_statuses");
                            JSONObject jsonObj = jsonArray.getJSONObject(0);
                            int mStatus = jsonObj.getInt("monitored_status");
                            String fenceName = jsonObj.getString("fence");
                            if (1 == mStatus) {
                                status.append("在围栏[" + fenceName + "]内\n");
                            } else if (2 == mStatus) {
                                status.append("在围栏[" + fenceName + "]外\n");
                            } else {
                                status.append("围栏[" + fenceName + "]状态未知\n");
                            }
                        }
                        trackApp.getmHandler().obtainMessage(0, status.toString()).sendToTarget();
                    } else {
                        trackApp.getmHandler().obtainMessage(0, "查询指定位置上本地围栏状态回调消息 : " + arg0).sendToTarget();
                    }
                } catch (JSONException e) {
                    // TODO Auto-generated catch block
                    trackApp.getmHandler().obtainMessage(0, "解析查询指定位置上本地围栏状态回调消息失败").sendToTarget();
                }
            }

        };
    }

    // 围栏信息对话框
    @SuppressLint("InflateParams")
    public void inputDialog(final String title, final String caption) {

        LayoutInflater layoutInflater = LayoutInflater.from(mContext);
        View dialogView = layoutInflater.inflate(R.layout.dialog_fence, null);

        final LinearLayout inputLayout = (LinearLayout) dialogView.findViewById(R.id.input_layout);
        final LinearLayout fenceTypeLayout = (LinearLayout) dialogView.findViewById(R.id.fenceType_layout);

        // 获取布局中的控件
        final TextView fenceKey = (TextView) dialogView.findViewById(R.id.fence_key);
        final EditText fenceValue = (EditText) dialogView.findViewById(R.id.fence_value);
        final EditText fencePrecision = (EditText) dialogView.findViewById(R.id.fence_precision);
        final RadioGroup fenceTypeGroup = (RadioGroup) dialogView.findViewById(R.id.fence_type);

        if (fenceShape == 1) {
            fenceTypeLayout.setVisibility(View.GONE);
        }

        if (fenceShape == -1) {
            inputLayout.setVisibility(View.GONE);
        }

        fenceTypeGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int checkedId) {
                switch (checkedId) {
                    case R.id.server_fence:
                        fenceType = 0;
                        break;

                    case R.id.local_fence:
                        fenceType = 1;
                        break;

                    default:
                        break;
                }
            }
        });

        fenceValue.setInputType(InputType.TYPE_CLASS_NUMBER);
        fencePrecision.setInputType(InputType.TYPE_CLASS_NUMBER);

        if (0 == fenceShape) {
            fenceKey.setText(mContext.getString(R.string.fence_radius));
            fenceValue.setText(String.valueOf(radius));
        } else if (1 == fenceShape) {
            fenceKey.setText(mContext.getString(R.string.vertex_number));
            fenceValue.setText(String.valueOf(vertexNumber));
        }

        fencePrecision.setText(String.valueOf(precision));

        AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
        builder.setTitle(title);
        builder.setView(dialogView);
        builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {

            public void onClick(DialogInterface dialog, int which) {
                // TODO Auto-generated method stub
                trackApp.getmBaiduMap().setOnMapClickListener(null);
            }

        });

        builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {

            public void onClick(DialogInterface dialog, int which) {
                if (TextUtils.isEmpty(caption)) {
                    if (0 == fenceType) {
                        deleteServerFence(serverFenceId);
                    } else if (1 == fenceType) {
                        deleteLocalCircularFence();
                    }
                } else {
                    if (TextUtils.isDigitsOnly(fenceValue.getText())) {
                        if (0 == fenceShape) {
                            radiusTemp = radius;
                            radius =
                                    Integer.parseInt(fenceValue.getText().toString()) > 0 ? Integer.parseInt(fenceValue
                                            .getText().toString()) : radius;
                        } else if (1 == fenceShape) {
                            vertexNumber = Integer.parseInt(fenceValue.getText().toString()) > 0 ? Integer
                                    .parseInt(fenceValue.getText().toString()) : vertexNumber;
                        }
                    }
                    if (TextUtils.isDigitsOnly(fencePrecision.getText())) {
                        precision = Integer.parseInt(fencePrecision.getText().toString()) >= 0 ? Integer
                                .parseInt(fencePrecision.getText().toString()) : precision;
                    }
                    Toast.makeText(mContext, caption, Toast.LENGTH_LONG).show();
                }
            }
        });
        builder.show();
    }

    /**
     * 设置围栏对话框
     */
    private void createOrUpdateDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
        builder.setTitle("确定设置围栏?");
        builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {

            public void onClick(DialogInterface dialog, int which) {

                trackApp.getmBaiduMap().clear();

                // 添加覆盖物
                if (null != fenceOverlayTemp) {
                    fenceOverlay = fenceOverlayTemp;
                }

                if (null != fenceOverlayOptionsTemp) {
                    fenceOverlayOption = fenceOverlayOptionsTemp;
                }

                switch (fenceShape) {
                    case 0:
                        radius = radiusTemp;
                        break;

                    case 1:
                        for (Overlay overlay : overlays) {
                            overlay.remove();
                        }
                        if (!vertexsTemp.isEmpty()) {
                            vertexs.clear();
                            vertexs.addAll(vertexsTemp);
                        }
                        break;

                    default:
                        break;

                }

                addMarker();
                trackApp.getmBaiduMap().setOnMapClickListener(null);
            }
        });

        builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {

            public void onClick(DialogInterface dialog, int which) {

                for (Overlay overlay : overlays) {
                    overlay.remove();
                }

                vertexsTemp.clear();
                vertexsTemp.addAll(vertexs);

                switch (fenceType) {

                    // 服务端围栏
                    case 0:
                        if (0 == serverFenceId) {
                            if (0 == fenceShape) {
                                createServerCircularFence();
                            } else if (1 == fenceShape) {
                                createServerVertexesFence();
                            }
                        } else {
                            if (0 == fenceShape) {
                                updateServerCircularFence();
                            } else if (1 == fenceShape) {
                                updateServerVertexesFence();
                            }
                        }
                        break;

                    // 客户端围栏
                    case 1:
                        createLocalCircularFence();
                        break;

                    default:
                        break;
                }

                trackApp.getmBaiduMap().setOnMapClickListener(null);
            }
        });

        builder.create();
        builder.setCancelable(false);
        builder.show();

    }

    /**
     * 添加地图覆盖物
     */
    protected void addMarker() {
        try {
            // 围栏覆盖物
            if (null != fenceOverlayOption) {
                fenceOverlay = trackApp.getmBaiduMap().addOverlay(fenceOverlayOption);
            }

        } catch (Exception e) {
            // TODO: handle exception
            e.printStackTrace();
        }
    }

    public Geofence(Context context, TrackApplication trackApp, LayoutInflater inflater) {
        initOnGeoFenceListener();
        this.trackApp = trackApp;
        this.mContext = context;
        mInflater = inflater;
        if (null == fenceOverlayOption) {
            queryServerFenceList();
        }
    }

}
