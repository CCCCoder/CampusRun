package com.n1njac.yiqipao.android.personalinfo;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

/**
 * Created by huanglei on 2017/2/7.
 */

public class PersonalInfoAdapter extends BaseAdapter {

    private LayoutInflater mInflater;

    private List<PersonalItemBean> mPersonalItemBeen;

    public PersonalInfoAdapter(Context context, List<PersonalItemBean> personalItemBeen) {
        this.mPersonalItemBeen = personalItemBeen;
        mInflater = LayoutInflater.from(context);

    }

    @Override
    public int getCount() {
        return mPersonalItemBeen.size();
    }

    @Override
    public Object getItem(int position) {
        return mPersonalItemBeen.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder = null;
        if (convertView == null){
            
        }
        return convertView;
    }


    class ViewHolder{
        TextView description;
        TextView content;
        ImageView icon;
    }
}
