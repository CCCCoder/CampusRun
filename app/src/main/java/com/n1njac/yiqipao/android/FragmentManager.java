package com.n1njac.yiqipao.android;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentPagerAdapter;

import java.util.List;

/**
 * Created by huanglei on 2017/1/12.
 */

public class FragmentManager extends FragmentPagerAdapter {

    List<Fragment> list;

    String [] titles = new String[]{"路程","校园跑","附近校友","我"};

    public FragmentManager(android.support.v4.app.FragmentManager fm,List<Fragment> list) {
        super(fm);
        this.list = list;
    }

    @Override
    public Fragment getItem(int position) {
        return list.get(position);
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return titles[position];
    }
}
