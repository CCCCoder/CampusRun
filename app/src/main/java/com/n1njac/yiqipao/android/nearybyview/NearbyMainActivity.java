package com.n1njac.yiqipao.android.nearybyview;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.RequiresApi;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AccelerateInterpolator;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.n1njac.yiqipao.android.R;
import com.n1njac.yiqipao.android.bmobObject.PersonInfoBmob;
import com.n1njac.yiqipao.android.nearbychat.NearbyChatMainActivity;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import cn.bmob.v3.BmobInstallation;
import cn.bmob.v3.BmobPushManager;
import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;
import cn.bmob.v3.listener.PushListener;

public class NearbyMainActivity extends AppCompatActivity implements ViewPager.OnPageChangeListener, RadarViewGroup.IRadarClickListener {

    private CustomViewPager viewPager;
    private RelativeLayout relativeLayout;
    private int[] mIcon = {R.drawable.len};

    private int[] mIcon1 = {R.drawable.len, R.drawable.leo, R.drawable.lep,
            R.drawable.ler};
    private String[] names = {"len", "leo", "lep", "ler"};
    private List<PersonInfoBean> personInfoBeanList = new ArrayList<>();

    private List<PersonInfoBean> myDataList = new ArrayList<>();

    private PersonInfoBean infoBean;

    private FixedSpeedScroller mScroller;

    private RadarViewGroup radarViewGroup;

    private float[] distances = new float[]{2, 4, 6, 9};


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.nearby_main);
        initData();
        initView();
//        将装有这个viewpager的容器发生的事件交给这个viewpager处理
        relativeLayout.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                return viewPager.dispatchTouchEvent(event);
            }
        });

        ViewPagerAdapter mAdapter = new ViewPagerAdapter();
        viewPager.setAdapter(mAdapter);
//        设置预加载的个数
        viewPager.setOffscreenPageLimit(mIcon.length);
        viewPager.setPageMargin(getResources().getDimensionPixelOffset(R.dimen.view_pager_margin));
//        设置切换动画
        viewPager.setPageTransformer(false, new ZoomOutPageTransformer());
        viewPager.addOnPageChangeListener(this);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            setViewPagerSpeed(250);
        }

        radarViewGroup.setData(personInfoBeanList);

        radarViewGroup.setRadarClickListener(this);
    }

    /*
    设置viewpager的切换速度,利用反射机制
     */
    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    private void setViewPagerSpeed(int duration) {
        try {
            Field mField = ViewPager.class.getDeclaredField("mScroller");
            mField.setAccessible(true);
            mScroller = new FixedSpeedScroller(NearbyMainActivity.this, new AccelerateInterpolator());
            mField.set(viewPager, mScroller);
            mScroller.setmDuration(duration);
        } catch (NoSuchFieldException | IllegalAccessException e) {
            e.printStackTrace();
        }
    }

    private void initData() {
        // TODO: 2017/4/8 installationId需要添加
        String installationId = "";
        Log.d("xyz", "获取到的installationId：" + installationId);
        Random random = new Random(50);
        for (int i = 0; i < mIcon.length; i++) {
            float distance = random.nextFloat() * 10;
            Log.d("xyz", "distance:" + distance);
            boolean sex = i % 3 != 0;
            infoBean = new PersonInfoBean(mIcon[i], names[i], distances[i], sex, installationId);
            personInfoBeanList.add(infoBean);


        }

//        getDataFromDatabase();
    }

    // TODO: 2017/4/9 从服务器数据库拿附近的人的数据
    public void getDataFromDatabase() {
        new Thread(new Runnable() {
            @Override
            public void run() {

                SharedPreferences sf = PreferenceManager.getDefaultSharedPreferences(getApplication());

                String objectId = sf.getString("bmobObjectId", null);
                Log.d("xyz", "查询数据库时id情况：" + objectId);

                BmobQuery<PersonInfoBmob> query = new BmobQuery<PersonInfoBmob>();
//                查询除了自己以外人的信息
                query.addWhereNotEqualTo("objectId", objectId);
                query.findObjects(new FindListener<PersonInfoBmob>() {
                    @Override
                    public void done(List<PersonInfoBmob> list, BmobException e) {

                        if (e == null) {
                            for (PersonInfoBmob personInfoBmob : list) {
                                double distance = personInfoBmob.getDistance();
                                String name = personInfoBmob.getNickName();
                                String sex = personInfoBmob.getSex();
                                boolean sexBoo = false;
                                if (sex.equals("男")) {
                                    sexBoo = true;
                                }
                                String id = personInfoBmob.getInstallationId();
                                PersonInfoBean personInfoBean = new PersonInfoBean(mIcon[0], name, (float) distance, sexBoo, id);
                                myDataList.add(personInfoBean);
                            }
                            Log.d("xyz", "查询成功:" + myDataList.size());
                        } else {
                            Log.i("xyz", "查询数据库失败：" + e.getMessage() + "," + e.getErrorCode());
                        }
                    }
                });
            }
        }).start();
    }


    private void initView() {
        viewPager = (CustomViewPager) findViewById(R.id.view_pager);
        relativeLayout = (RelativeLayout) findViewById(R.id.rl_vp);
        radarViewGroup = (RadarViewGroup) findViewById(R.id.radar_view_group);
    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

    }

    @Override
    public void onPageSelected(int position) {
        if (viewPager.getSpeed() < -3000) {
            viewPager.setCurrentItem(position);

            viewPager.setmSpeed(0);
        } else if (viewPager.getSpeed() > 3000 && position > 0) {
            viewPager.setCurrentItem(position);
            viewPager.setmSpeed(0);

        }
    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }

    @Override
    public void onRadarItemClick(int position) {
        viewPager.setCurrentItem(position);
    }

    class ViewPagerAdapter extends PagerAdapter {

        @Override
        public Object instantiateItem(ViewGroup container, int position) {


            final PersonInfoBean infoBean = personInfoBeanList.get(position);
            View view = LayoutInflater.from(NearbyMainActivity.this).inflate(R.layout.nearby_viewpager, null);
            ImageView icon = (ImageView) view.findViewById(R.id.person_icon_iv);
            TextView personName = (TextView) view.findViewById(R.id.person_name_tv);
            ImageView sexIcon = (ImageView) view.findViewById(R.id.sex_iv);
            TextView distance = (TextView) view.findViewById(R.id.distance_tx);
            TextView sayHello = (TextView) view.findViewById(R.id.say_hello);

            icon.setImageResource(infoBean.getIconId());
            personName.setText(infoBean.getName());
            if (infoBean.isSex()) {
                sexIcon.setImageResource(R.drawable.girl);
            } else {
                sexIcon.setImageResource(R.drawable.boy);
            }
//            distance.setText(infoBean.getDistance() + "km");
            distance.setText("0.01km");

            icon.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Toast.makeText(NearbyMainActivity.this, "这是" + infoBean.getName() + "：）", Toast.LENGTH_SHORT).show();
                }
            });

            sayHello.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    // TODO: 2017/4/8 怎么获取对方的installtionId

//                    18119635150
                    String meizuId = "1C04DA1CD835A7837A70E608036A9AA2";
//                    15505514373
                    String moniqiId = "8AC0A4524336DA27E2861D9071B03244";
                    String installationId = null;
                    SharedPreferences sf = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());

                    String phone = sf.getString("phone", null);
                    Log.d("xyz", "phone--nearby:" + phone);

                    // TODO: 2017/4/10 号码修改
                    String phone1 = "15505514373";
                    String phone2 = "18119635150";
                    if (phone.equals(phone1)) {
                        installationId = meizuId;
                    } else {
                        installationId = moniqiId;
                    }

                    Log.d("xyz", "installationId" + installationId);
                    BmobPushManager bmobPush = new BmobPushManager();
                    BmobQuery<BmobInstallation> query = BmobInstallation.getQuery();
                    query.addWhereEqualTo("installationId", installationId);
                    bmobPush.setQuery(query);
                    bmobPush.pushMessage("require", new PushListener() {
                        @Override
                        public void done(BmobException e) {
                            if (e == null) {
                                Toast.makeText(NearbyMainActivity.this, "已向对方发送跑步邀请", Toast.LENGTH_SHORT).show();
                            } else {
                                Toast.makeText(NearbyMainActivity.this, "发送失败", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });


//                    bmobPush.pushMessageAll("hello,bmob", new PushListener() {
//                        @Override
//                        public void done(BmobException e) {
//                            if (e == null){
//                                Toast.makeText(NearbyMainActivity.this, "已向对方发送跑步邀请", Toast.LENGTH_SHORT).show();
//                            }else {
//                                Toast.makeText(NearbyMainActivity.this, "发送失败", Toast.LENGTH_SHORT).show();
//                            }
//                        }
//                    });


                }
            });

            container.addView(view);
            return view;
        }

        @Override
        public int getCount() {
            return mIcon.length;
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view == object;
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            View view = (View) object;
            container.removeView(view);

        }
    }
}
