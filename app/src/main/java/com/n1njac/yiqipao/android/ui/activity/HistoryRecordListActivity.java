package com.n1njac.yiqipao.android.ui.activity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;

import com.n1njac.yiqipao.android.R;
import com.n1njac.yiqipao.android.adapter.HistoryDataAdapter;
import com.n1njac.yiqipao.android.bean.LocationBean;
import com.n1njac.yiqipao.android.bmobObject.RunDataBmob;
import com.n1njac.yiqipao.android.bmobObject.UserInfoBmob;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.BmobUser;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;

/**
 * Created by N1njaC on 2017/9/23.
 * 1.以后需处理数据量大的时候的情况，需做分页处理
 * 2.添加下拉刷新操作
 */

public class HistoryRecordListActivity extends BaseActivity {

    private static final String TAG = HistoryRecordListActivity.class.getSimpleName();
    @BindView(R.id.history_back_btn)
    Button historyBackBtn;
    @BindView(R.id.his_run_data_rv)
    RecyclerView mRecyclerView;

    private ProgressDialog mProgressDialog;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.history_record_layout);
        ButterKnife.bind(this);

        getDataFromServer();

    }

    private void getDataFromServer() {

        mProgressDialog = new ProgressDialog(this);
        mProgressDialog.setMessage("加载中...");
        mProgressDialog.show();

        String objectId = BmobUser.getCurrentUser(UserInfoBmob.class).getObjectId();
        Log.i(TAG, "object id:" + objectId);

        BmobQuery<RunDataBmob> query = new BmobQuery<>();
        query.addWhereEqualTo("pUserObjectId", objectId);
        query.setLimit(50);
        query.order("-createdAt");
        query.findObjects(new FindListener<RunDataBmob>() {
            @Override
            public void done(final List<RunDataBmob> list, BmobException e) {
                mProgressDialog.dismiss();
                if (e == null) {
                    LinearLayoutManager layoutManager = new LinearLayoutManager(getApplicationContext());
                    layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
                    mRecyclerView.setLayoutManager(layoutManager);
                    mRecyclerView.setHasFixedSize(true);
                    mRecyclerView.setAdapter(new HistoryDataAdapter(getApplicationContext(), list, new HistoryDataAdapter.OnItemClickListener() {

                        @Override
                        public void itemClick(View view, int position) {
                            Log.d(TAG, "position:" + position);
                            Intent intent = new Intent(HistoryRecordListActivity.this, HistoryRunRecordActivity.class);
                            Bundle bundle = new Bundle();
                            bundle.putSerializable("run_data", list.get(position));
                            intent.putExtras(bundle);
                            startActivity(intent);
                        }
                    }));
                } else {
                    Log.d(TAG, "error------>" + e.getErrorCode() + " " + e.getLocalizedMessage());
                }

            }
        });
    }

    @OnClick(R.id.history_back_btn)
    public void onViewClicked() {
        finish();
    }
}
