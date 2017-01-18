package com.n1njac.yiqipao.android;

import android.content.ContentResolver;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.design.widget.NavigationView;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.n1njac.yiqipao.android.Fragment.NearbyPersonInfoFragment;
import com.n1njac.yiqipao.android.Fragment.PersonalInfoFragment;
import com.n1njac.yiqipao.android.Fragment.RunFragment;

import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {


    private TabLayout mTabLayout;
    private ViewPager mViewPager;

    private IndexViewPager viewPager;

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
        initTabLayoutWithFragment();

        mNavigationView = (NavigationView) findViewById(R.id.nav_view);

        View view = mNavigationView.getHeaderView(0);
        mIcon = (ImageView) view.findViewById(R.id.icon_image);

        mIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showTakePhotoOrChooseFromAlbumDialog();
            }
        });

        mNavigationView.setCheckedItem(R.id.first_item);
        mNavigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(MenuItem item) {

                switch (item.getItemId()){
                    case R.id.first_item:

                        Toast.makeText(MainActivity.this,"click aim",Toast.LENGTH_SHORT).show();
                        break;
                    case R.id.second_item:
                        Toast.makeText(MainActivity.this,"click behance",Toast.LENGTH_SHORT).show();
                        break;
                    case R.id.third_item:
                        Toast.makeText(MainActivity.this,"click etsy",Toast.LENGTH_SHORT).show();
                        break;
                    case R.id.about_item:
                        Toast.makeText(MainActivity.this,"click about",Toast.LENGTH_SHORT).show();
                        break;
                }
                mDrawerLayout.closeDrawer(GravityCompat.START);
                return true;
            }
        });

//        mToolbar = (Toolbar) findViewById(R.id.tool_bar);
//        mToolbar.setTitle("校园跑");
//        setSupportActionBar(mToolbar);

        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this,mDrawerLayout,mToolbar,R.string.open,R.string.close);
        mDrawerLayout.addDrawerListener(toggle);
        toggle.syncState();

    }



    private void initTabLayoutWithFragment() {

        mList = new ArrayList<>();
        PersonalInfoFragment personalInfoFragment = new PersonalInfoFragment();
        RunFragment runFragment = new RunFragment();
        NearbyPersonInfoFragment nearbyPersonInfoFragment = new NearbyPersonInfoFragment();
        mList.add(personalInfoFragment);
        mList.add(runFragment);
        mList.add(nearbyPersonInfoFragment);

        mTabLayout = (TabLayout) findViewById(R.id.tab_layout);
        //mViewPager = (ViewPager) findViewById(R.id.view_pager);
        viewPager = (IndexViewPager) findViewById(R.id.view_pager);


        FragmentManager adapter = new FragmentManager(getSupportFragmentManager(),mList);
        //mViewPager.setAdapter(adapter);
        viewPager.setAdapter(adapter);

        mTabLayout.setupWithViewPager(viewPager);
        TabLayout.Tab one = mTabLayout.getTabAt(0);
        TabLayout.Tab two = mTabLayout.getTabAt(1);
        TabLayout.Tab three = mTabLayout.getTabAt(2);

        one.setIcon(R.mipmap.ic_launcher);

    }


    private void showTakePhotoOrChooseFromAlbumDialog() {
        CharSequence[] items = {"拍照","从相册选择"};
        new AlertDialog.Builder(this)
                .setTitle("更换头像")
                .setItems(items, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if (which == TAKE_PHOTO){

                            Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
                            intent.addCategory(Intent.CATEGORY_OPENABLE);
                            intent.setType("image/*");
                            startActivityForResult(intent,TAKE_PHOTO);
                        }else {
                            Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                            startActivityForResult(intent,CHOOSE_FROM_ALBUM);
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
        if (resultCode == RESULT_OK){
            Uri uri = data.getData();
                Glide.with(this).load(uri).into(mIcon);
        }
    }
}
