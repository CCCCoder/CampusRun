package com.n1njac.yiqipao.android.Fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.n1njac.yiqipao.android.R;
import com.n1njac.yiqipao.android.personalInfo.ExecPlanActivity;
import com.n1njac.yiqipao.android.view.ArcView;

import static android.app.Activity.RESULT_OK;

/**
 * Created by huanglei on 2017/1/12.
 */

public class PersonalInfoFragment extends Fragment {

    private ArcView arcView;
    private TextView exec, history;

    private ExecPlanActivity activity;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.personlinfo_frag, container, false);
        Log.i("xyz","onCreateView");
        exec = (TextView) view.findViewById(R.id.exec_text);
        history = (TextView) view.findViewById(R.id.history_tx);
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

            }
        });
        arcView = (ArcView) view.findViewById(R.id.arc_view);

        return view;
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Log.i("xyz","onActivityResult");

        if (resultCode == RESULT_OK){
            String content = data.getStringExtra("distance");
            double distance = Double.parseDouble(content);
            Log.i("xyz","distance:"+distance);
            arcView.setNowDistance(distance,10);
        }
    }
}
