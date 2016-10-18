package com.guodong.sun.guodong.activity;

import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.WindowManager;
import android.widget.Toast;

import com.flyco.tablayout.SlidingTabLayout;
import com.guodong.sun.guodong.R;
import com.guodong.sun.guodong.adapter.HomePagerAdapter;
import com.guodong.sun.guodong.base.AbsBaseActivity;

import butterknife.BindView;
import fm.jiecao.jcvideoplayer_lib.JCVideoPlayer;

public class MainActivity extends AbsBaseActivity
{
    @BindView(R.id.nav_dl)
    DrawerLayout mDrawerLayout;

    @BindView(R.id.nav_toolbar)
    Toolbar mToolBar;

    @BindView(R.id.nav_view)
    NavigationView mNavView;

    @BindView(R.id.nav_tablayout)
    SlidingTabLayout mSlidingTab;

    @BindView(R.id.nav_viewpager)
    ViewPager mViewPager;

    private HomePagerAdapter mHomeAdapter;
    private long exitTime;

    @Override
    protected void initViews(Bundle savedInstanceState)
    {
        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.KITKAT)
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        setSupportActionBar(mToolBar);
        ActionBarDrawerToggle actionBarDrawerToggle = new ActionBarDrawerToggle(this, mDrawerLayout, mToolBar, R.string.open, R.string.close);
        actionBarDrawerToggle.syncState();
        mDrawerLayout.addDrawerListener(actionBarDrawerToggle);

        mNavView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener()
        {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item)
            {
                selectDrawerItem(item);
                return true;
            }
        });

        mHomeAdapter = new HomePagerAdapter(getSupportFragmentManager(), this);
        mViewPager.setOffscreenPageLimit(5);
        mViewPager.setAdapter(mHomeAdapter);
        mSlidingTab.setViewPager(mViewPager);
    }

    @Override
    protected int getLayoutId()
    {
        return R.layout.activity_main;
    }

    private void selectDrawerItem(MenuItem item)
    {
        Toast.makeText(this, item.getTitle(), Toast.LENGTH_SHORT).show();
        item.setChecked(true);
        //        mDrawerLayout.closeDrawers();
    }

    @Override
    protected void onPause()
    {
        super.onPause();
        JCVideoPlayer.releaseAllVideos();
    }

    @Override
    public void onBackPressed()
    {
        if (JCVideoPlayer.backPress())
            return;

        if (mDrawerLayout.isDrawerOpen(GravityCompat.START))
        {
            mDrawerLayout.closeDrawers();
            return;
        }
        else
        {
            if ((System.currentTimeMillis() - exitTime) > 2000)
            {
                Toast.makeText(MainActivity.this, "再点一次，退出", Toast.LENGTH_SHORT).show();
                exitTime = System.currentTimeMillis();
            }
            else
                super.onBackPressed();
        }
    }
}
