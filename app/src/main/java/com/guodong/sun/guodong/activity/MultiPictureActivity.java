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

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.SimpleTarget;
import com.guodong.sun.guodong.R;
import com.guodong.sun.guodong.base.AbsBaseActivity;
import com.guodong.sun.guodong.fragment.MultiPictureFragment;
import com.guodong.sun.guodong.uitls.Once;
import com.guodong.sun.guodong.uitls.SnackbarUtil;
import com.guodong.sun.guodong.uitls.StringUtils;
import com.shizhefei.view.largeimage.LargeImageView;
import com.shizhefei.view.largeimage.factory.FileBitmapDecoderFactory;
import com.trello.rxlifecycle.components.support.RxAppCompatActivity;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Administrator on 2016/12/16.
 */

public class MultiPictureActivity extends AbsBaseActivity {

    private static final String MULTI_IMAGE_URL = "MULTI_IMAGE_URL";
    private static final String MULTI_IMAGE_POS = "MULTI_IMAGE_POS";
    private static final String MULTI_IMAGE_CURRENT_POS = "MULTI_IMAGE_CURRENT_POS";

    public static void startActivity(Context context, int pos, ArrayList<String> list) {
        Intent intent = new Intent(context, MultiPictureActivity.class);
        intent.putExtra(MULTI_IMAGE_POS, pos);
        intent.putStringArrayListExtra(MULTI_IMAGE_URL, list);
        context.startActivity(intent);
        ((MainActivity)context).overridePendingTransition(R.anim.zoom_in, R.anim.zoom_out);
    }

    private int currentPos;
    private List<String> mListUrl;

    @BindView(R.id.picture_multi_pager)
    ViewPager mViewPager;

    @BindView(R.id.picture_multi_pager_bottom)
    TextView mTextView;

    @Override
    protected void initViews(Bundle savedInstanceState) {
        if (savedInstanceState != null) {
            currentPos = savedInstanceState.getInt(MULTI_IMAGE_CURRENT_POS);
        } else {
            currentPos = getIntent().getIntExtra(MULTI_IMAGE_POS, 0);
        }

        mListUrl = getIntent().getStringArrayListExtra(MULTI_IMAGE_URL);

        mViewPager.setAdapter(new FragmentPagerAdapter(getSupportFragmentManager()) {
            @Override
            public Fragment getItem(int position) {
                return MultiPictureFragment.newInstance(mListUrl.get(position));
            }

            @Override
            public int getCount() {
                return mListUrl.size();
            }
        });

        mViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            }

            @Override
            public void onPageSelected(int position) {
                mTextView.setText(getString(R.string.picture_conut, position + 1, mListUrl.size()));
            }

            @Override
            public void onPageScrollStateChanged(int state) {
            }
        });

        mViewPager.setCurrentItem(currentPos);
        mTextView.setText(getString(R.string.picture_conut, currentPos + 1, mListUrl.size()));
        new Once(this).show("提示", new Once.OnceCallback()
        {
            @Override
            public void onOnce()
            {
                SnackbarUtil.showMessage(mViewPager, "单击图片返回, 双击放大, 长按图片保存");
            }
        });
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_multi_picture;
    }

    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
        if (hasFocus && Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            View decorView = getWindow().getDecorView();
            int option = View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN  // 布局占据系统栏
                    | View.SYSTEM_UI_FLAG_LAYOUT_STABLE // 布局不会因系统栏改变而改变
                    | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION // 布局占据导航栏
                    | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION // 隐藏导航栏
                    | View.SYSTEM_UI_FLAG_FULLSCREEN // 全屏，隐藏系统栏
                    | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY; // 系统栏粘性
            decorView.setSystemUiVisibility(option);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                getWindow().setStatusBarColor(Color.TRANSPARENT);
                getWindow().setNavigationBarColor(Color.TRANSPARENT);
            }
        }
    }

    @Override
    public void onSaveInstanceState(Bundle outState, PersistableBundle outPersistentState) {
        super.onSaveInstanceState(outState, outPersistentState);
        outState.putInt(MULTI_IMAGE_CURRENT_POS, currentPos);
    }

    @Override
    public void onBackPressed() {
        finish();
        overridePendingTransition(R.anim.zoom_in, R.anim.zoom_out);
    }
}
