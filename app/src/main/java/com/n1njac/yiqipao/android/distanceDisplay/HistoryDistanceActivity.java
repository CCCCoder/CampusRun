package com.n1njac.yiqipao.android.distanceDisplay;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.ProgressBar;

import com.mingle.widget.LoadingView;
import com.n1njac.yiqipao.android.BaseActivity;
import com.n1njac.yiqipao.android.R;
import com.n1njac.yiqipao.android.db.Distance;

import org.litepal.crud.DataSupport;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by huanglei on 2017/1/15.
 */

public class HistoryDistanceActivity extends BaseActivity {

    private ListView mListView;
    private List<com.n1njac.yiqipao.android.distanceDisplay.ItemBean> mItemBeanList;
    private BaseAdapter mAdapter;
    private Button returnBtn;

    public int count = 0;

    private ProgressBar mProgressBar;

    private LoadingView mLoadingView;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.history_main);
//        addData();
        initView();

    }

    private void initView() {

        mListView = (ListView) findViewById(R.id.history_lv);
        mLoadingView = (LoadingView) findViewById(R.id.loadView);
        mLoadingView.setLoadingText("拉取数据中...");
//        for (int i = 9; i < 18; i++) {
//            DataSupport.delete(Distance.class,i);
//        }
//        mProgressBar = (ProgressBar) findViewById(R.id.load_progress);
//        FoldingCube foldingCube = new FoldingCube();
//        foldingCube.setColor(R.color.orange);
//        mProgressBar.setIndeterminateDrawable(foldingCube);
//        mProgressBar.setVisibility(View.VISIBLE);
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Thread.sleep(4000);
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
//                            mProgressBar.setVisibility(View.GONE);
                            mLoadingView.setVisibility(View.GONE);
                            mListView.setVisibility(View.VISIBLE);
                        }
                    });
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }).start();

        initRunDistanceData();
        mAdapter = new HistoryAdapter(HistoryDistanceActivity.this, mItemBeanList);
        mListView.setAdapter(mAdapter);

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
        distance1.setTime("2017-4-10");
        distance1.setTotalDistance("2.23km");
        distance1.save();
        Distance distance2 = new Distance();
        distance2.setTime("2017-4-11");
        distance2.setTotalDistance("3.2km");
        distance2.save();
        Distance distance3 = new Distance();
        distance3.setTime("2017-4-12");
        distance3.setTotalDistance("1.15km");
        distance3.save();
        Distance distance4 = new Distance();
        distance4.setTime("2017-4-13");
        distance4.setTotalDistance("2.12km");
        distance4.save();
        Distance distance5 = new Distance();
        distance5.setTime("2017-4-14");
        distance5.setTotalDistance("1.89km");
        distance5.save();
        Distance distance6 = new Distance();
        distance6.setTime("2017-4-15");
        distance6.setTotalDistance("3.41km");
        distance6.save();
        Distance distance7 = new Distance();
        distance7.setTime("2017-4-16");
        distance7.setTotalDistance("1.21km");
        distance7.save();
        Distance distance8 = new Distance();
        distance8.setTime("2017-4-17");
        distance8.setTotalDistance("3.12km");
        distance8.save();
        Distance distance9 = new Distance();
        distance9.setTime("2017-4-18");
        distance9.setTotalDistance("0.34km");
        distance9.save();
    }


    private void initRunDistanceData() {

        mItemBeanList = new ArrayList<>();
//        从数据库中拿数据
        new Thread(new Runnable() {
            ItemBean itemBean;
            @Override
            public void run() {
                List<Distance> distanceList = DataSupport.findAll(Distance.class);
                for (int i = 0; i < distanceList.size(); i++) {
                    String time = distanceList.get(i).getTime();
                    String totalDistance = distanceList.get(i).getTotalDistance();
                    Log.d("xyz", "time:" + time + "totalDistance" + totalDistance);
                    itemBean = new ItemBean(time, totalDistance);
                    mItemBeanList.add(itemBean);
                }
            }
        }).start();

    }
}
