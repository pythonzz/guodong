/*
 * Copyright (C) 2017 guodongAndroid
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 * Last modified 2017-05-04 15:01:49
 *
 * GitHub:   https://github.com/guodongAndroid
 * Website:  http://www.sunxiaoduo.com
 * Email:    sun33919135@gmail.com
 * QQ:       33919135
 */

package com.guodong.sun.guodong.activity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.WindowManager;
import android.widget.Toast;

import com.flyco.tablayout.SlidingTabLayout;
import com.guodong.sun.guodong.R;
import com.guodong.sun.guodong.adapter.HomePagerAdapter;
import com.guodong.sun.guodong.base.AbsBaseActivity;
import com.guodong.sun.guodong.glide.GlideCacheUtil;
import com.umeng.socialize.UMShareAPI;

import butterknife.BindView;
import fm.jiecao.jcvideoplayer_lib.JCVideoPlayer;

public class MainActivity extends AbsBaseActivity {
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
    protected void initViews(Bundle savedInstanceState) {
        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.KITKAT)
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        setSupportActionBar(mToolBar);
        ActionBarDrawerToggle actionBarDrawerToggle = new ActionBarDrawerToggle(this, mDrawerLayout, mToolBar, R.string.open, R.string.close);
        actionBarDrawerToggle.syncState();
        mDrawerLayout.addDrawerListener(actionBarDrawerToggle);

        mNavView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                selectDrawerItem(item);
                return true;
            }
        });

        mHomeAdapter = new HomePagerAdapter(getSupportFragmentManager(), this);
        mViewPager.setOffscreenPageLimit(5);
        mViewPager.setAdapter(mHomeAdapter);
        mSlidingTab.setViewPager(mViewPager);

        mViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            }

            @Override
            public void onPageSelected(int position) {
//                if (position == 1) {
//                    new AlertDialog.Builder(MainActivity.this)
//                            .setCancelable(false)
//                            .setTitle("提示")
//                            .setMessage("图片里的GIF很消耗流量,请尽量在WIFI下浏览")
//                            .setNegativeButton("去看段子", new DialogInterface.OnClickListener() {
//                                @Override
//                                public void onClick(DialogInterface dialog, int which) {
//                                    setViewPagerCurrent(5);
//                                }
//                            }).setPositiveButton("土豪随意", new DialogInterface.OnClickListener() {
//                        @Override
//                        public void onClick(DialogInterface dialog, int which) {
//                            dialog.dismiss();
//                        }
//                    }).show();
//                }
            }
            @Override
            public void onPageScrollStateChanged(int state) {
            }
        });
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_main;
    }

    private void selectDrawerItem(MenuItem item) {
//        Toast.makeText(this, item.getTitle(), Toast.LENGTH_SHORT).show();
        item.setChecked(true);

        // ----------------------------------

        switch (item.getItemId()) {
            case R.id.nav_clean:
                cleanCache();
                break;

            case R.id.nav_setting:
                AboutMeActivity.startActivity(this);
                mDrawerLayout.closeDrawers();
                break;
        }


        // ----------------------------------

        //        mDrawerLayout.closeDrawers();
    }

    /**
     * 清除缓存
     */
    private void cleanCache() {
        String size = GlideCacheUtil.getInstance().getCacheSize(this);
        new AlertDialog.Builder(this)
                .setTitle("缓存大小为" + size)
                .setMessage("是否清除缓存")
                .setCancelable(false)
                .setNegativeButton(android.R.string.cancel, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                    }
                }).setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Toast.makeText(MainActivity.this, "正在清理...", Toast.LENGTH_SHORT).show();
                if (GlideCacheUtil.getInstance().clearImageAllCache(MainActivity.this))
                    Toast.makeText(MainActivity.this, "清理完成", Toast.LENGTH_SHORT).show();
            }
        }).show();
    }

    public void setViewPagerCurrent(@NonNull int pos) {
        mViewPager.setCurrentItem(pos);
    }


    @Override
    protected void onPause() {
        super.onPause();
        JCVideoPlayer.releaseAllVideos();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        UMShareAPI.get(this).onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public void onBackPressed() {
        if (JCVideoPlayer.backPress())
            return;

        if (mDrawerLayout.isDrawerOpen(GravityCompat.START)) {
            mDrawerLayout.closeDrawers();
        } else {
            Intent launcherIntent = new Intent(Intent.ACTION_MAIN);
            launcherIntent.addCategory(Intent.CATEGORY_HOME);
            startActivity(launcherIntent);
//            if ((System.currentTimeMillis() - exitTime) > 2000) {
//                Toast.makeText(MainActivity.this, "再点一次，退出", Toast.LENGTH_SHORT).show();
//                exitTime = System.currentTimeMillis();
//            } else {
//                String size = GlideCacheUtil.getInstance().getCacheSize(MainActivity.this);
//                if (Double.valueOf(size.substring(0, size.length() - 2)) >= 50)
//                    GlideCacheUtil.getInstance().clearImageAllCache(MainActivity.this);
//                super.onBackPressed();
//            }
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mHomeAdapter != null) {
            mHomeAdapter = null;
        }
    }
}
