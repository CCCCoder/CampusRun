package com.n1njac.yiqipao.android.adapter;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.n1njac.yiqipao.android.R;
import com.n1njac.yiqipao.android.bean.PersonalItemBean;

import java.util.List;

/**
 * Created by huanglei on 2017/2/7.
 */

public class PersonalInfoAdapter extends BaseAdapter {

    private LayoutInflater mInflater;

    private Context mContext;
    private List<PersonalItemBean> mPersonalItemBeen;


    public PersonalInfoAdapter(Context context, List<PersonalItemBean> personalItemBeen) {
        this.mContext = context;
        this.mPersonalItemBeen = personalItemBeen;
        mInflater = LayoutInflater.from(mContext);

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
        if (convertView == null) {
            viewHolder = new ViewHolder();
            convertView = mInflater.inflate(R.layout.personal_info_item, null);
            viewHolder.description = (TextView) convertView.findViewById(R.id.description_tx);
            viewHolder.content = (TextView) convertView.findViewById(R.id.content_tx);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        PersonalItemBean itemBean = mPersonalItemBeen.get(position);
        viewHolder.description.setText(itemBean.getDescription());
        //默认值
        viewHolder.content.setText(itemBean.getContent());

        SharedPreferences sf = PreferenceManager.getDefaultSharedPreferences(mContext);
        switch (position){
            case 0:
                String content1 = sf.getString("user_nickname","user");
                viewHolder.content.setText(content1);
                break;
            case 1:
                String content2 = sf.getString("sex","男");
                viewHolder.content.setText(content2);
                break;
            case 2:
                String content3 = sf.getString("birth","1999年9月");
                viewHolder.content.setText(content3);
                break;
            case 3:
                String content4 = sf.getString("height","170厘米");
                viewHolder.content.setText(content4);
                break;
            case 4:
                String content5 = sf.getString("weight","60kg");
                viewHolder.content.setText(content5);
                break;
            case 5:

                break;


        }


        return convertView;
    }

    class ViewHolder {

        TextView description;
        TextView content;

    }
}
