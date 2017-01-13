package com.n1njac.yiqipao.android;

import android.support.design.widget.NavigationView;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.WindowManager;
import android.widget.Toast;

import com.n1njac.yiqipao.android.Fragment.NearbyPersonInfoFragment;
import com.n1njac.yiqipao.android.Fragment.PersonalInfoFragment;
import com.n1njac.yiqipao.android.Fragment.RunFragment;

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

        mToolbar = (Toolbar) findViewById(R.id.tool_bar);
        mToolbar.setTitle("校园跑");
        setSupportActionBar(mToolbar);

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



    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.toolbar_setting,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()){
            case R.id.setting:
                Toast.makeText(this,"click setting",Toast.LENGTH_SHORT).show();
                break;
            case R.id.about:
                Toast.makeText(this,"click about",Toast.LENGTH_SHORT).show();
                break;
        }
        return super.onOptionsItemSelected(item);
    }
}
