package com.n1njac.yiqipao.android;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.net.Uri;
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
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.Toast;

import com.ashokvarma.bottomnavigation.BottomNavigationBar;
import com.ashokvarma.bottomnavigation.BottomNavigationItem;
import com.bumptech.glide.Glide;
import com.n1njac.yiqipao.android.Fragment.NearbyPersonInfoFragment;
import com.n1njac.yiqipao.android.Fragment.PersonalInfoFragment;
import com.n1njac.yiqipao.android.Fragment.PersonalRunInfoFragment;
import com.n1njac.yiqipao.android.Fragment.RunFragment;

import java.util.ArrayList;
import java.util.List;

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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //透明状态栏
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        //透明导航栏
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);

        setContentView(R.layout.activity_main);
//        initTabLayoutWithFragment();
        setBottomNavigationBar();

        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);

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

        mNavigationView.setCheckedItem(R.id.first_item);
        mNavigationView.setItemIconTintList(null);
        mNavigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(MenuItem item) {

                switch (item.getItemId()) {
                    case R.id.first_item:

                        Toast.makeText(MainActivity.this, "click aim", Toast.LENGTH_SHORT).show();
                        break;
                    case R.id.second_item:
                        Toast.makeText(MainActivity.this, "click behance", Toast.LENGTH_SHORT).show();
                        break;
                    case R.id.third_item:
                        Toast.makeText(MainActivity.this, "click etsy", Toast.LENGTH_SHORT).show();
                        break;
                    case R.id.about_item:
                        Toast.makeText(MainActivity.this, "click about", Toast.LENGTH_SHORT).show();
                        break;
                }
                mDrawerLayout.closeDrawer(GravityCompat.START);
                return true;
            }
        });

//        mToolbar = (Toolbar) findViewById(R.id.tool_bar);
//        mToolbar.setTitle("校园跑");
//        setSupportActionBar(mToolbar);


//        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, mDrawerLayout, mToolbar, R.string.open, R.string.close);
//        mDrawerLayout.addDrawerListener(toggle);
//        toggle.syncState();

    }


//    private void initTabLayoutWithFragment() {
//
//        mList = new ArrayList<>();
//        PersonalRunInfoFragment personalRunInfoFragment = new PersonalRunInfoFragment();
//        RunFragment runFragment = new RunFragment();
//        NearbyPersonInfoFragment nearbyPersonInfoFragment = new NearbyPersonInfoFragment();
//        PersonalInfoFragment personalInfoFragment = new PersonalInfoFragment();
//        mList.add(personalRunInfoFragment);
//        mList.add(runFragment);
//        mList.add(nearbyPersonInfoFragment);
//        mList.add(personalInfoFragment);
//
//        mTabLayout = (TabLayout) findViewById(R.id.tab_layout);
//        //mViewPager = (ViewPager) findViewById(R.id.view_pager);
//        viewPager = (IndexViewPager) findViewById(R.id.view_pager);
//
//
//        FragmentManager adapter = new FragmentManager(getSupportFragmentManager(), mList);
//        //mViewPager.setAdapter(adapter);
//        viewPager.setAdapter(adapter);
//
//        mTabLayout.setupWithViewPager(viewPager);
//        TabLayout.Tab one = mTabLayout.getTabAt(0);
//
//        TabLayout.Tab two = mTabLayout.getTabAt(1);
//        TabLayout.Tab three = mTabLayout.getTabAt(2);
//        one.setIcon(R.mipmap.ic_launcher);
//
//    }


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
                if (fragment.isAdded()){
                    ft.show(fragment);
                }else {
                    ft.add(R.id.frag_layout,fragment);
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
    }


}
