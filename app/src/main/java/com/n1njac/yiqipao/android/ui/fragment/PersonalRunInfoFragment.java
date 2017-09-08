package com.n1njac.yiqipao.android.ui.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.n1njac.yiqipao.android.R;
import com.n1njac.yiqipao.android.ui.activity.ExecPlanActivity;
import com.n1njac.yiqipao.android.ui.activity.HistoryDistanceActivity;
import com.n1njac.yiqipao.android.ui.widget.DistanceDisplayArcView;

import static android.app.Activity.RESULT_OK;

/**
 * Created by huanglei on 2017/1/12.
 */

public class PersonalRunInfoFragment extends Fragment {

    private DistanceDisplayArcView distanceDisplayArcView;
    private TextView exec, history;
    private TextView remindText;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.personal_run_frag, container, false);
        exec = (TextView) view.findViewById(R.id.exec_text);
        history = (TextView) view.findViewById(R.id.history_tx);
        remindText = (TextView) view.findViewById(R.id.remind_tx);

        exec.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), ExecPlanActivity.class);
                startActivityForResult(intent,1);
            }
        });
        history.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), HistoryDistanceActivity.class);
                startActivity(intent);
            }
        });
        distanceDisplayArcView = (DistanceDisplayArcView) view.findViewById(R.id.arc_view);

        distanceDisplayArcView.setNowDistance(10,4.7);

        return view;
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == RESULT_OK){
            String content = data.getStringExtra("distance");
            double distance = Double.parseDouble(content);
            distanceDisplayArcView.setNowDistance(distance,4.7);
        }
    }
}
