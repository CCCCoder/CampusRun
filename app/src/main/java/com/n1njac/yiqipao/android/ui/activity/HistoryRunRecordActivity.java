package com.n1njac.yiqipao.android.ui.activity;

import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.annotation.Nullable;

import com.n1njac.yiqipao.android.R;
import com.n1njac.yiqipao.android.bmobObject.RunDataBmob;

/**
 * Created by N1njaC on 2017/9/25.
 */

public class HistoryRunRecordActivity extends BaseActivity {
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.his_run_record_act);
        RunDataBmob runData = (RunDataBmob) getIntent().getSerializableExtra("run_data");

        //利用一个和下滑布局一样的布局来实现。
    }
}
