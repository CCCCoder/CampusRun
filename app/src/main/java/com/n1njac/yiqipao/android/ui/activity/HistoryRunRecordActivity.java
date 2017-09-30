package com.n1njac.yiqipao.android.ui.activity;

import android.animation.Animator;
import android.animation.ObjectAnimator;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Point;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.util.TypedValue;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.amap.api.maps.AMap;
import com.amap.api.maps.CameraUpdateFactory;
import com.amap.api.maps.MapView;
import com.amap.api.maps.model.BitmapDescriptorFactory;
import com.amap.api.maps.model.GroundOverlayOptions;
import com.amap.api.maps.model.LatLng;
import com.amap.api.maps.model.MarkerOptions;
import com.amap.api.maps.model.PolylineOptions;
import com.n1njac.yiqipao.android.R;
import com.n1njac.yiqipao.android.bean.LocationBean;
import com.n1njac.yiqipao.android.bmobObject.RunDataBmob;
import com.n1njac.yiqipao.android.ui.widget.RecordPathView;
import com.n1njac.yiqipao.android.ui.widget.RunDataTextView;
import com.n1njac.yiqipao.android.utils.FontCacheUtil;
import com.n1njac.yiqipao.android.utils.ParseUtil;
import com.n1njac.yiqipao.android.utils.RecordPathUtil;
import com.n1njac.yiqipao.android.utils.SizeUtil;
import com.n1njac.yiqipao.android.utils.ToastUtil;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.UpdateListener;

/**
 * Created by N1njaC on 2017/9/25.
 */

public class HistoryRunRecordActivity extends BaseActivity implements View.OnTouchListener, GestureDetector.OnGestureListener, AMap.OnMapLoadedListener {
    @BindView(R.id.map_relat)
    RelativeLayout mapRelat;
    @BindView(R.id.his_detail_distance_tv)
    RunDataTextView hisDetailDistanceTv;
    @BindView(R.id.his_detail_duration_tv)
    RunDataTextView hisDetailDurationTv;
    @BindView(R.id.his_detail_avspeed_tv)
    RunDataTextView hisDetailAvspeedTv;
    @BindView(R.id.his_detail_calorie_tv)
    RunDataTextView hisDetailCalorieTv;
    @BindView(R.id.his_detail_slowest_speed_tv)
    RunDataTextView hisDetailSlowestSpeedTv;
    @BindView(R.id.his_detail_fast_speed_tv)
    RunDataTextView hisDetailFastSpeedTv;
    @BindView(R.id.his_detail_back_iv)
    ImageView hisDetailBackIv;
    @BindView(R.id.his_detail_delete_iv)
    ImageView hisDetailDeleteIv;
    @BindView(R.id.his_detail_share_iv)
    ImageView hisDetailShareIv;
    @BindView(R.id.big_his_detail_distance_tv)
    RunDataTextView bigHisDetailDistanceTv;
    @BindView(R.id.big_his_detail_duration_tv)
    RunDataTextView bigHisDetailDurationTv;
    @BindView(R.id.big_his_detail_avspeed_tv)
    RunDataTextView bigHisDetailAvspeedTv;
    @BindView(R.id.big_his_detail_calorie_tv)
    RunDataTextView bigHisDetailCalorieTv;
    @BindView(R.id.big_his_detail_slowest_speed_tv)
    RunDataTextView bigHisDetailSlowestSpeedTv;
    @BindView(R.id.big_his_detail_fast_speed_tv)
    RunDataTextView bigHisDetailFastSpeedTv;
    @BindView(R.id.big_his_prompt)
    TextView bigHisPrompt;
    @BindView(R.id.big_his_tool_bar)
    RelativeLayout bigHisToolBar;
    @BindView(R.id.big_his_root)
    RelativeLayout bigHisRoot;
    @BindView(R.id.his_content_root)
    RelativeLayout hisContentRoot;
    @BindView(R.id.his_tool_bar_title)
    TextView hisToolBarTitle;
    @BindView(R.id.his_detail_map)
    MapView mMapView;
    @BindView(R.id.map_record_path_view)
    RecordPathView mapRecordPathView;
    @BindView(R.id.map_back_iv)
    ImageView mapBackIv;
    @BindView(R.id.map_delete_iv)
    ImageView mapDeleteIv;
    @BindView(R.id.map_share_iv)
    ImageView mapShareIv;

    private static final String TAG = HistoryRunRecordActivity.class.getSimpleName();
    private static final int FLING_MIN_DISTANCE = 50;
    private static final int FLING_MIN_VELOCITY = 200;

    //屏幕高度
    private int mHeight;
    //内容高度
    private int mContentHeight;
    //action bar高度
    private int mActionBarHeight;
    //状态栏高度
    private int mStatusBarHeight;

    private GestureDetector mDetector;

    private boolean isBigContent = false;

    private AMap mAMap;

    private List<LatLng> mPoints;

    private RecordPathUtil mRecordPathUtil;

    private List<Integer> mColorList;

    private Bitmap mStartIcon, mEndIcon;

    private RunDataBmob runData;


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.his_run_record_act);
        ButterKnife.bind(this);
        runData = (RunDataBmob) getIntent().getSerializableExtra("run_data");
        Typeface boldTf = FontCacheUtil.getFont(this, "fonts/Avenir_Next_Condensed_demi_bold.ttf");
        initLayout(runData, boldTf);
        initMap(savedInstanceState, runData);

        getWindow().setStatusBarColor(getResources().getColor(R.color.colorPrimary));
        mDetector = new GestureDetector(this, this);
        hisContentRoot.setLongClickable(true);
        hisContentRoot.setOnTouchListener(this);

        mColorList = new ArrayList<>();
        mColorList.add(Color.rgb(0, 255, 127));
        mColorList.add(Color.rgb(255, 255, 0));

        mStartIcon = BitmapFactory.decodeResource(getResources(), R.drawable.start_point_icon);
        mEndIcon = BitmapFactory.decodeResource(getResources(), R.drawable.end_point_icon);

    }

    private void initMap(Bundle savedInstanceState, RunDataBmob runData) {
        mMapView.onCreate(savedInstanceState);
        if (mAMap == null) mAMap = mMapView.getMap();
        List<LocationBean> pointsBean = runData.getPoints();
        mPoints = ParseUtil.parseBean2LatLng(pointsBean);

        Log.d(TAG, "points size:" + mPoints.size());
//        for (LatLng l :
//                points) {
//            Log.d(TAG, "lat:" + l.latitude + " lo:" + l.longitude);
//        }

        if (mPoints != null && mPoints.size() > 0) {
            LatLng startPoint = new LatLng(mPoints.get(0).latitude, mPoints.get(0).longitude);
            setMapHalfTransparentBg(startPoint, R.drawable.his_map_bg);
            mAMap.moveCamera(CameraUpdateFactory.newLatLngZoom(startPoint, 17));
        } else {
            ToastUtil.shortToast(this, "点集合为空!");
        }

        //去掉高德地图右下角隐藏的缩放按钮
        mAMap.getUiSettings().setZoomControlsEnabled(false);
        mAMap.setOnMapLoadedListener(this);

    }

    //设置半透明的背景
    private void setMapHalfTransparentBg(LatLng latLng, int bg) {
        mAMap.addGroundOverlay(new GroundOverlayOptions()
                .position(latLng, 2 * 1000 * 1000, 2 * 1000 * 2000)
                .image(BitmapDescriptorFactory.fromBitmap(BitmapFactory.decodeResource(getResources(), bg))));
    }


    private void initLayout(RunDataBmob runData, Typeface typeface) {


        String avSpeed = runData.getAvSpeed();
        String distance = runData.getRunDistance();
        String duration = runData.getRunDurationTime();

        String avPace = runData.getAvPace();
        String maxPace = runData.getMaxPace();
        String minPace = runData.getMinPace();

        //平均配速
        hisDetailAvspeedTv.setTypeface(typeface);
        hisDetailAvspeedTv.setText(avPace);
        bigHisDetailAvspeedTv.setTypeface(typeface);
        bigHisDetailAvspeedTv.setText(avPace);

        hisDetailDistanceTv.setTypeface(typeface);
        hisDetailDistanceTv.setText(distance);
        bigHisDetailDistanceTv.setTypeface(typeface);
        bigHisDetailDistanceTv.setText(distance);

        hisDetailDurationTv.setTypeface(typeface);
        hisDetailDurationTv.setText(duration);
        bigHisDetailDurationTv.setTypeface(typeface);
        bigHisDetailDurationTv.setText(duration);

        //最满\快配速
        hisDetailSlowestSpeedTv.setText(minPace);
        hisDetailSlowestSpeedTv.setTypeface(typeface);
        hisDetailFastSpeedTv.setTypeface(typeface);
        hisDetailFastSpeedTv.setText(maxPace);

        bigHisDetailSlowestSpeedTv.setText(minPace);
        bigHisDetailFastSpeedTv.setText(maxPace);
        bigHisDetailSlowestSpeedTv.setTypeface(typeface);
        bigHisDetailFastSpeedTv.setTypeface(typeface);

        hisDetailCalorieTv.setTypeface(typeface);
        bigHisDetailCalorieTv.setTypeface(typeface);

    }

    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
        TypedValue tv = new TypedValue();
        int actionBarHeight = 50;
        if (getTheme().resolveAttribute(android.R.attr.actionBarSize, tv, true)) {
            actionBarHeight = TypedValue.complexToDimensionPixelSize(tv.data, getResources().getDisplayMetrics());

            Log.d(TAG, "action bar height:" + actionBarHeight);
            Log.d(TAG, "status bar height:" + SizeUtil.getStatusBarHeight(this));
        }

        WindowManager wm = (WindowManager) this.getSystemService(Context.WINDOW_SERVICE);
        int height = wm.getDefaultDisplay().getHeight();
        int height2 = SizeUtil.dp2px(this, 223);
        Log.d(TAG, "height:" + height);
        Log.d(TAG, "height2:" + height2);

        mActionBarHeight = actionBarHeight;
        mStatusBarHeight = SizeUtil.getStatusBarHeight(this);
        mContentHeight = SizeUtil.dp2px(this, 223);
        mHeight = height;
    }


    //地图加载完成回调
    @Override
    public void onMapLoaded() {
        Log.d(TAG, "onMapLoaded");
        mRecordPathUtil = new RecordPathUtil();
        addPoints2path();
        mapRecordPathView.setPath(mRecordPathUtil);
        mapRecordPathView.setAnimCallback(new RecordPathView.onAnimCallback() {
            @Override
            public void onAnimFinish() {
                //当轨迹绘制完成之后或者在绘制过程中用户移动了地图，替换为高德api绘制的轨迹

                mapRecordPathView.setVisibility(View.GONE);
                mAMap.addPolyline(new PolylineOptions().addAll(mPoints).width(10).useGradient(true).colorValues(mColorList));
                LatLng start = new LatLng(mPoints.get(0).latitude, mPoints.get(0).longitude);
                LatLng end = new LatLng(mPoints.get(mPoints.size() - 1).latitude, mPoints.get(mPoints.size() - 1).longitude);
                addMarker(start, mStartIcon);
                addMarker(end, mEndIcon);
            }
        });

    }

    private void addPoints2path() {
        Log.d(TAG, "points size" + mPoints.size());
        for (int i = 0; i < mPoints.size() - 1; i++) {

            LatLng startLatLng = mPoints.get(i);
            LatLng endLatLng = mPoints.get(i + 1);
            Point startPoint = mAMap.getProjection().toScreenLocation(startLatLng);
            Point endPoint = mAMap.getProjection().toScreenLocation(endLatLng);

            Log.d(TAG, "start point x:" + startPoint.x + "  y:" + startPoint.y);
            Log.d(TAG, "end point x:" + endPoint.x + "  y:" + endPoint.y);
            Log.d(TAG, "---------------------------------------------------");

            mRecordPathUtil.addPath(startPoint, endPoint, Color.rgb(0, 255, 127), Color.rgb(255, 255, 0));
        }
    }

    private void addMarker(LatLng latLng, Bitmap bitmap) {
        MarkerOptions markerOptions = new MarkerOptions();
        markerOptions.position(latLng);
        markerOptions.icon(BitmapDescriptorFactory.fromBitmap(bitmap));
        markerOptions.setFlat(true);

        //设置Marker覆盖物的锚点比例。锚点是marker 图标接触地图平面的点。
        float x = (float) 0.5;
        float y = (float) 0.5;
        markerOptions.anchor(x, y);
        mAMap.addMarker(markerOptions);
    }

    @OnClick({R.id.his_detail_back_iv, R.id.his_detail_delete_iv, R.id.his_detail_share_iv,
            R.id.map_back_iv, R.id.map_delete_iv, R.id.map_share_iv})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.his_detail_back_iv:
                finish();
                break;
            case R.id.his_detail_delete_iv:
                deleteRunRecord();
                break;
            case R.id.his_detail_share_iv:
                break;
            case R.id.map_back_iv:
                finish();
                break;
            case R.id.map_delete_iv:
                deleteRunRecord();
                break;
            case R.id.map_share_iv:
                break;
        }
    }

    //删除当前跑步记录
    private void deleteRunRecord() {
        new AlertDialog.Builder(this)
                .setCancelable(true)
                .setMessage("确定要删除记录么？")
                .setTitle("温馨提示")
                .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        deleteDataFromServer();
                    }
                })
                .setNegativeButton("取消", null)
                .show();
    }

    //先根据当前用户的object id 查询当前数据的object id ，然后执行删除
    private void deleteDataFromServer() {
        if (runData != null) {
            String objectId = runData.getObjectId();
            Log.d(TAG, "object id:" + objectId);
            RunDataBmob runDataBmob = new RunDataBmob();
            runDataBmob.delete(objectId, new UpdateListener() {
                @Override
                public void done(BmobException e) {
                    if (e == null) {
                        ToastUtil.shortToast(HistoryRunRecordActivity.this, "删除成功！");
                        finish();
                    } else {
                        ToastUtil.shortToast(HistoryRunRecordActivity.this, "删除失败：）");
                        Log.d(TAG, "error code:" + e.getErrorCode() + " error msg:" + e.getLocalizedMessage());
                    }
                }
            });
        }
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        return mDetector.onTouchEvent(event);
    }

    @Override
    public boolean onDown(MotionEvent e) {
        return false;
    }

    @Override
    public void onShowPress(MotionEvent e) {

    }

    @Override
    public boolean onSingleTapUp(MotionEvent e) {
        return false;
    }

    @Override
    public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {
        return false;
    }

    @Override
    public void onLongPress(MotionEvent e) {

    }

    @Override
    public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {

        if (!isBigContent && e1.getY() - e2.getY() > FLING_MIN_DISTANCE && Math.abs(velocityY) > FLING_MIN_VELOCITY) {
            Log.d(TAG, "上滑");

            //当布局被覆盖的时候 不允许滑动

            isBigContent = true;

            bigHisRoot.setVisibility(View.VISIBLE);
//            ObjectAnimator animator = ObjectAnimator.ofFloat(bigHisRoot, "translationY", 834 - 50 - 112, 0);
            ObjectAnimator animator = ObjectAnimator.ofFloat(bigHisRoot, "translationY", mHeight - mContentHeight - mStatusBarHeight - mActionBarHeight, 0);
            animator.setDuration(1000);
            animator.addListener(new Animator.AnimatorListener() {
                @Override
                public void onAnimationStart(Animator animation) {

                }

                @Override
                public void onAnimationEnd(Animator animation) {
                    getWindow().setStatusBarColor(getResources().getColor(R.color.colorPrimary));

                    visibleToolBar();

                }

                @Override
                public void onAnimationCancel(Animator animation) {

                }

                @Override
                public void onAnimationRepeat(Animator animation) {

                }
            });
            animator.start();


        } else if (e2.getY() - e1.getY() > FLING_MIN_DISTANCE && Math.abs(velocityY) > FLING_MIN_VELOCITY) {

            Log.d(TAG, "下滑");

        }

        return true;
    }


    private void setToolBarTransparent() {

        bigHisToolBar.setBackgroundResource(R.color.bg_transparent);
        hisDetailBackIv.setVisibility(View.GONE);
        hisDetailDeleteIv.setVisibility(View.GONE);
        hisDetailShareIv.setVisibility(View.GONE);
        hisToolBarTitle.setVisibility(View.GONE);

    }

    private void visibleToolBar() {
        bigHisToolBar.setBackgroundResource(R.color.colorPrimary);
        hisDetailBackIv.setVisibility(View.VISIBLE);
        hisDetailDeleteIv.setVisibility(View.VISIBLE);
        hisDetailShareIv.setVisibility(View.VISIBLE);
        hisToolBarTitle.setVisibility(View.VISIBLE);
    }

    @Override
    public void onBackPressed() {
        if (isBigContent) {
            isBigContent = false;
            setToolBarTransparent();

            ObjectAnimator animator = ObjectAnimator.ofFloat(bigHisRoot, "translationY", 0, mHeight - mContentHeight - mStatusBarHeight - mActionBarHeight);

            animator.setDuration(1000);
            animator.addListener(new Animator.AnimatorListener() {
                @Override
                public void onAnimationStart(Animator animation) {

                }

                @Override
                public void onAnimationEnd(Animator animation) {
                    bigHisRoot.setVisibility(View.GONE);

                }

                @Override
                public void onAnimationCancel(Animator animation) {

                }

                @Override
                public void onAnimationRepeat(Animator animation) {

                }
            });
            animator.start();
        } else {
            mRecordPathUtil = null;
            finish();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        mMapView.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
        mMapView.onPause();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mMapView.onDestroy();
    }


}
