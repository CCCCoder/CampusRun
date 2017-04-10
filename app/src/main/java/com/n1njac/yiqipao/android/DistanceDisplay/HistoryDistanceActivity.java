package com.n1njac.yiqipao.android.distanceDisplay;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.ProgressBar;

import com.github.ybq.android.spinkit.style.DoubleBounce;
import com.github.ybq.android.spinkit.style.FoldingCube;
import com.mingle.widget.LoadingView;
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

    private ProgressBar mProgressBar;

    private LoadingView mLoadingView;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.history_main);

        // TODO: 2017/4/10 加一个过渡动画

        initView();

    }

    private void initView() {

        mListView = (ListView) findViewById(R.id.history_lv);
        mLoadingView = (LoadingView) findViewById(R.id.loadView);
        mLoadingView.setLoadingText("拉取数据中...");
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
