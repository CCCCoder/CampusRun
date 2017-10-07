package com.n1njac.yiqipao.android.adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.n1njac.yiqipao.android.R;
import com.n1njac.yiqipao.android.bmobObject.RunDataBmob;
import com.n1njac.yiqipao.android.utils.FontCacheUtil;

import java.util.List;

/**
 * Created by N1njaC on 2017/9/25.
 */

public class HistoryDataAdapter extends RecyclerView.Adapter<HistoryDataAdapter.ViewHolder> {


    private List<RunDataBmob> mRunDataList;
    private Context mContext;

    private OnItemClickListener mListener;

    public HistoryDataAdapter(Context context, List<RunDataBmob> runDataList, OnItemClickListener listener) {
        this.mContext = context;
        this.mRunDataList = runDataList;
        this.mListener = listener;
    }


    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.recycler_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {

        holder.runStartTime.setText(mRunDataList.get(position).getRunStartTime());
        holder.runDuration.setText(mRunDataList.get(position).getRunDurationTime());
        holder.runDistance.setText(mRunDataList.get(position).getRunDistance());
        holder.runSpeed.setText(mRunDataList.get(position).getAvPace());

        holder.item.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                mListener.itemClick(v, holder.getAdapterPosition());
            }
        });
    }

    @Override
    public int getItemCount() {
        return mRunDataList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        private TextView runStartTime;
        private TextView runDistance;
        private TextView runDuration;
        private TextView runSpeed;
        private RelativeLayout item;

        public ViewHolder(View itemView) {
            super(itemView);
            runStartTime = (TextView) itemView.findViewById(R.id.history_run_time_tv);
            runDistance = (TextView) itemView.findViewById(R.id.history_run_distance_tv);
            runDuration = (TextView) itemView.findViewById(R.id.his_run_duration_tv);
            runSpeed = (TextView) itemView.findViewById(R.id.his_run_speed_tv);
            item = (RelativeLayout) itemView.findViewById(R.id.whole_item_relat);
            Typeface boldTf = FontCacheUtil.getFont(mContext, "fonts/Avenir_Next_Condensed_demi_bold.ttf");
            Typeface regularTf = FontCacheUtil.getFont(mContext, "fonts/Avenir_Next_Condensed_regular.ttf");
            runDistance.setTypeface(boldTf);
            runDuration.setTypeface(regularTf);
            runSpeed.setTypeface(regularTf);

        }
    }

    public interface OnItemClickListener {
        void itemClick(View view, int position);
    }

}
