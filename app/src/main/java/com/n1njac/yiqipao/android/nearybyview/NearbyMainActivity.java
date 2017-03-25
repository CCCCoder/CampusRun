package com.n1njac.yiqipao.android.nearybyview;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
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
import com.n1njac.yiqipao.android.nearbychat.NearbyChatMainActivity;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class NearbyMainActivity extends AppCompatActivity implements ViewPager.OnPageChangeListener, RadarViewGroup.IRadarClickListener {

    private CustomViewPager viewPager;
    private RelativeLayout relativeLayout;
    private int[] mIcon = {R.drawable.len, R.drawable.leo, R.drawable.lep,
            R.drawable.ler};
    private String[] names = {"len", "leo", "lep", "ler"};
    private List<PersonInfoBean> personInfoBeanList = new ArrayList<>();
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
        Random random = new Random(50);
        for (int i = 0; i < mIcon.length; i++) {
            float distance = random.nextFloat() * 10;
            Log.d("xyz", "distance:" + distance);
            boolean sex = i % 3 != 0;
            infoBean = new PersonInfoBean(mIcon[i], names[i], distances[i], sex);
            personInfoBeanList.add(infoBean);
        }
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
            distance.setText(infoBean.getDistance() + "km");

            icon.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Toast.makeText(NearbyMainActivity.this, "这是" + infoBean.getName() + "：）", Toast.LENGTH_SHORT).show();
                }
            });

            sayHello.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent i = new Intent(NearbyMainActivity.this, NearbyChatMainActivity.class);
                    startActivity(i);
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
