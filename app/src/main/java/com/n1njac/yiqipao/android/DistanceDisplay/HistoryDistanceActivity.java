package com.n1njac.yiqipao.android.distanceDisplay;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ListView;

import com.n1njac.yiqipao.android.R;
import com.n1njac.yiqipao.android.db.Distance;

import org.litepal.crud.DataSupport;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by huanglei on 2017/1/15.
 */

public class HistoryDistanceActivity extends AppCompatActivity {

    private ListView mListView;
    private List<ItemBean> mItemBeanList;
    private BaseAdapter mAdapter;
    private Button returnBtn;

    public int count = 0;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.history_main);
//        addData();
        initView();
    }

    private void initView() {
        mListView = (ListView) findViewById(R.id.history_lv);
        initRunDistanceData();
        mAdapter = new HistoryAdapter(this, mItemBeanList);
        mListView.setAdapter(mAdapter);
        //当listview为空的时候，加载这个布局
        View emptyView = View.inflate(this,R.layout.empty_listview,null);
        mListView.setEmptyView(emptyView);
        returnBtn = (Button) findViewById(R.id.return_history_btn);
        returnBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

//    模拟往数据库中添加数据

    public void addData() {
        Distance distance1 = new Distance();
        distance1.setTime("2013-1-1");
        distance1.setTotalDistance("2.23km");
        distance1.save();
        Distance distance2 = new Distance();
        distance2.setTime("2013-2-1");
        distance2.setTotalDistance("4.7km");
        distance2.save();
    }


    private void initRunDistanceData() {

        mItemBeanList = new ArrayList<>();

        //从数据库中拿数据
        new Thread(new Runnable() {
            ItemBean itemBean;

            @Override
            public void run() {
                List<Distance> distanceList = DataSupport.findAll(Distance.class);
                for (int i = 0; i < distanceList.size(); i++) {
                    String time = distanceList.get(i).getTime();
                    String totalDistance = distanceList.get(i).getTotalDistance();
                    itemBean = new ItemBean(time, totalDistance);
                    mItemBeanList.add(itemBean);
                }
            }
        }).start();

    }
}
