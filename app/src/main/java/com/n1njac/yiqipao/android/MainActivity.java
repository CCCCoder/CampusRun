package com.n1njac.yiqipao.android;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.preference.PreferenceManager;
import android.provider.MediaStore;
import android.support.design.widget.NavigationView;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;

import com.ashokvarma.bottomnavigation.BottomNavigationBar;
import com.ashokvarma.bottomnavigation.BottomNavigationItem;
import com.bumptech.glide.Glide;
import com.n1njac.yiqipao.android.distanceDisplay.ExecPlanActivity;
import com.n1njac.yiqipao.android.Fragment.NearbyPersonInfoFragment;
import com.n1njac.yiqipao.android.Fragment.PersonalInfoFragment;
import com.n1njac.yiqipao.android.Fragment.PersonalRunInfoFragment;
import com.n1njac.yiqipao.android.Fragment.RunFragment;
import com.n1njac.yiqipao.android.distanceDisplay.HistoryDistanceActivity;
import com.n1njac.yiqipao.android.login.NewLoginActivity;

import java.util.ArrayList;
import java.util.List;

import cn.bmob.push.BmobPush;
import cn.bmob.v3.Bmob;
import cn.bmob.v3.BmobInstallation;

public class MainActivity extends AppCompatActivity implements BottomNavigationBar.OnTabSelectedListener {


    private TabLayout mTabLayout;
    private ViewPager mViewPager;
    private IndexViewPager viewPager;

    private BottomNavigationBar mNavigationBar;

    private Toolbar mToolbar;
    private DrawerLayout mDrawerLayout;
    private NavigationView mNavigationView;

    List<Fragment> mList;

    private Bitmap bitmap;

    private ImageView mIcon;

    private static final int TAKE_PHOTO = 1;
    private static final int CHOOSE_FROM_ALBUM = 2;

    private int selectItem = 0;

    private static final int CONSTANT_EXE_PLAN = 1001;
    private static final int CONSTANT_HIS_DISTANCE = 1002;
    private static final int CONSTANT_ABOUT = 1003;
    private static final int CONSTANT_SWITCH = 1004;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //透明状态栏
//        getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        //透明导航栏
//        getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);

        getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = getWindow();
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS
                    | WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
            window.getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                    | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                    | View.SYSTEM_UI_FLAG_LAYOUT_STABLE);
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(Color.TRANSPARENT);
            window.setNavigationBarColor(Color.TRANSPARENT);
        }

        setContentView(R.layout.activity_main);

//        初始化bmob
        Bmob.initialize(this, "130b520015c4c581392956ec14bc86c4");

//        初始化bmob推送
        BmobInstallation.getCurrentInstallation().save();
        BmobPush.startWork(this);


//        initTabLayoutWithFragment();
        setBottomNavigationBar();

        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);

        mDrawerLayout.addDrawerListener(new DrawerLayout.DrawerListener() {
            @Override
            public void onDrawerSlide(View drawerView, float slideOffset) {

            }

            @Override
            public void onDrawerOpened(View drawerView) {

            }

            @Override
            public void onDrawerClosed(View drawerView) {

                switch (selectItem){
                    case CONSTANT_EXE_PLAN:
                        startActivity(new Intent(MainActivity.this, ExecPlanActivity.class));
                        break;
                    case CONSTANT_HIS_DISTANCE:
                        startActivity(new Intent(MainActivity.this, HistoryDistanceActivity.class));
                        break;
                    case CONSTANT_ABOUT:
                        startActivity(new Intent(MainActivity.this, AboutActivity.class));
                        break;
                    case CONSTANT_SWITCH:
                        startActivity(new Intent(MainActivity.this, NewLoginActivity.class));
                        break;
                }

            }

            @Override
            public void onDrawerStateChanged(int newState) {

            }
        });

        mNavigationView = (NavigationView) findViewById(R.id.nav_view);

        View view = mNavigationView.getHeaderView(0);
        mIcon = (ImageView) view.findViewById(R.id.icon_image);

        //判断是否有头像uri，有的话直接从缓存里面拿。没有设置即默认。
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
        if (prefs.getString("iconUri", null) != null) {
            Uri uri = Uri.parse(prefs.getString("iconUri", null));
            Glide.with(this).load(uri).centerCrop().into(mIcon);
        }

        mIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showTakePhotoOrChooseFromAlbumDialog();
            }
        });

        mNavigationView.setCheckedItem(R.id.second_item);
        mNavigationView.setItemIconTintList(null);
        mNavigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(MenuItem item) {

                switch (item.getItemId()) {

                    case R.id.second_item:
                        selectItem = CONSTANT_EXE_PLAN;

                        break;
                    case R.id.third_item:
                        selectItem = CONSTANT_HIS_DISTANCE;

                        break;
                    case R.id.about_item:
                        selectItem = CONSTANT_ABOUT;

                        break;
                    case R.id.switch_item:
                        selectItem = CONSTANT_SWITCH;

                        break;
                }
                mDrawerLayout.closeDrawer(GravityCompat.START);
                return true;
            }
        });

    }


    //    设置底部的bar
    public void setBottomNavigationBar() {

        mNavigationBar = (BottomNavigationBar) findViewById(R.id.bottom_bar);
        mNavigationBar.setMode(BottomNavigationBar.MODE_SHIFTING);
        mNavigationBar.setBackgroundStyle(BottomNavigationBar.BACKGROUND_STYLE_STATIC);
        mNavigationBar.addItem(new BottomNavigationItem(R.mipmap.distance_64dp, "路程").setActiveColor("#00BFFF"))
                .addItem(new BottomNavigationItem(R.mipmap.run_64dp, "一起跑").setActiveColor("#00BFFF"))
                .addItem(new BottomNavigationItem(R.mipmap.friend_64dp, "附近").setActiveColor("#00BFFF"))
                .addItem(new BottomNavigationItem(R.mipmap.me_64dp, "我").setActiveColor("#00BFFF"))
                .setFirstSelectedPosition(0)
                .initialise();


//        把fragment加入容器
        mList = new ArrayList<>();
        PersonalRunInfoFragment personalRunInfoFragment = new PersonalRunInfoFragment();
        RunFragment runFragment = new RunFragment();
        NearbyPersonInfoFragment nearbyPersonInfoFragment = new NearbyPersonInfoFragment();
        PersonalInfoFragment personalInfoFragment = new PersonalInfoFragment();
        mList.add(personalRunInfoFragment);
        mList.add(runFragment);
        mList.add(nearbyPersonInfoFragment);
        mList.add(personalInfoFragment);

        android.support.v4.app.FragmentManager fm = getSupportFragmentManager();
        FragmentTransaction ft = fm.beginTransaction();
        ft.replace(R.id.frag_layout, personalRunInfoFragment);
        ft.commit();

        mNavigationBar.setTabSelectedListener(this);

    }


    @Override
    public void onTabSelected(int position) {
        if (mList != null) {
            if (position < mList.size()) {
                android.support.v4.app.FragmentManager fm = getSupportFragmentManager();
                FragmentTransaction ft = fm.beginTransaction();
                Fragment fragment = mList.get(position);
                if (fragment.isAdded()) {
                    ft.show(fragment);
                } else {
                    ft.add(R.id.frag_layout, fragment);
                }
                ft.commitAllowingStateLoss();
            }
        }
    }

    @Override
    public void onTabUnselected(int position) {

        if (mList != null) {
            if (position < mList.size()) {
                android.support.v4.app.FragmentManager fm = getSupportFragmentManager();
                FragmentTransaction ft = fm.beginTransaction();
                Fragment fragment = mList.get(position);
                ft.hide(fragment);
                ft.commitAllowingStateLoss();
            }
        }

    }

    @Override
    public void onTabReselected(int position) {

    }


    private void showTakePhotoOrChooseFromAlbumDialog() {
        CharSequence[] items = {"拍照", "从相册选择"};
        new AlertDialog.Builder(this)
                .setTitle("更换头像")
                .setItems(items, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if (which == TAKE_PHOTO) {

                            Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
                            intent.addCategory(Intent.CATEGORY_OPENABLE);
                            intent.setType("image/*");
                            startActivityForResult(intent, TAKE_PHOTO);
                        } else {
                            Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                            startActivityForResult(intent, CHOOSE_FROM_ALBUM);
                        }
                    }
                })
                .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                })
                .create()
                .show();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            if (requestCode == TAKE_PHOTO) {
                Uri uri = data.getData();
                Glide.with(this).load(uri).centerCrop().into(mIcon);
                SharedPreferences.Editor editor = PreferenceManager.getDefaultSharedPreferences(this).edit();
                editor.putString("iconUri", uri.toString());
                editor.apply();
            }
        }

//        这里做runMianActiity返回数据的处理。

    }

    @Override
    public void onBackPressed() {
        Log.d("xyz", "MainActivity---onBackPressed");
        finish();
    }
}
