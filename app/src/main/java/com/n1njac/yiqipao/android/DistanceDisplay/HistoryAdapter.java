package com.n1njac.yiqipao.android.DistanceDisplay;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.n1njac.yiqipao.android.R;

import java.util.List;

/**
 * Created by huanglei on 2017/1/17.
 */

public class HistoryAdapter extends BaseAdapter {

    private LayoutInflater mInflater;

    private List<ItemBean> mItemBeen;


    public HistoryAdapter(Context context, List<ItemBean> itemBeen) {
        mInflater = LayoutInflater.from(context);
        this.mItemBeen = itemBeen;
    }

    @Override
    public int getCount() {
        return mItemBeen.size();
    }

    @Override
    public Object getItem(int position) {
        return mItemBeen.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder;
        if (convertView == null) {
            viewHolder = new ViewHolder();
            convertView = mInflater.inflate(R.layout.history_item, null);
            viewHolder.time = (TextView) convertView.findViewById(R.id.time_tx);
            viewHolder.distance = (TextView) convertView.findViewById(R.id.distance_tx);
        }else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        ItemBean itemBean = mItemBeen.get(position);

        viewHolder.time.setText(itemBean.runTime);
        viewHolder.distance.setText(itemBean.runDistance);

        return convertView;
    }

    class ViewHolder {
        TextView time;
        TextView distance;
    }
}
