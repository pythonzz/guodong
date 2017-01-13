package com.guodong.sun.guodong.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.widget.TextView;

import com.guodong.sun.guodong.R;
import com.guodong.sun.guodong.base.AbsBaseActivity;
import com.guodong.sun.guodong.uitls.AppUtil;

import butterknife.BindView;

/**
 * Created by Administrator on 2016/12/18.
 */

public class AboutMeActivity extends AbsBaseActivity {

    @BindView(R.id.aboutme_toolbar)
    Toolbar mToolbar;

    @BindView(R.id.version)
    TextView mVersion;

    public static void startActivity(Context context) {
        Intent intent = new Intent(context, AboutMeActivity.class);
        context.startActivity(intent);
    }

    @Override
    protected void initViews(Bundle savedInstanceState) {
        setSupportActionBar(mToolbar);
        getSupportActionBar().setTitle("关于我");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        mVersion.setText(getString(R.string.version, AppUtil.getVersionName()));
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_about_me;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home)
            onBackPressed();
        return true;
    }
}
