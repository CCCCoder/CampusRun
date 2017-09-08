package com.n1njac.yiqipao.android.ui.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.n1njac.yiqipao.android.R;

/**
 * Created by huanglei on 2017/3/25.
 */

public class AboutActivity extends BaseActivity {

    private CollapsingToolbarLayout ctl;
    private ImageView bg;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.about_main);

        bg = (ImageView) findViewById(R.id.bg_image);
        Glide.with(this).load(R.drawable.bg_run).into(bg);
        Toolbar toolbar = (Toolbar) findViewById(R.id.tool_bar);
        setSupportActionBar(toolbar);

        ctl = (CollapsingToolbarLayout) findViewById(R.id.collapsing_toolbar);
        ctl.setExpandedTitleColor(getResources().getColor(R.color.colorPrimary));

        ActionBar ab = getSupportActionBar();
        ab.setDisplayHomeAsUpEnabled(true);
        ab.setHomeAsUpIndicator(R.drawable.return_r);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        if (item.getItemId() == android.R.id.home){
            finish();
        }
        return true;
    }
}
