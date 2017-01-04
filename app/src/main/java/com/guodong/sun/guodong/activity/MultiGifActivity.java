package com.guodong.sun.guodong.activity;

import android.content.Context;
import android.content.Intent;
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

import com.guodong.sun.guodong.R;
import com.guodong.sun.guodong.fragment.MultiGifFragment;
import com.guodong.sun.guodong.fragment.MultiPictureFragment;
import com.trello.rxlifecycle.components.support.RxAppCompatActivity;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Administrator on 2016/12/16.
 */

public class MultiGifActivity extends RxAppCompatActivity {

    private static final String MULTI_IMAGE_URL = "MULTI_IMAGE_URL";
    private static final String MULTI_IMAGE_POS = "MULTI_IMAGE_POS";
    private static final String MULTI_IMAGE_CURRENT_POS = "MULTI_IMAGE_CURRENT_POS";
    private static final String MULTI_IMAGE_WIDTH = "MULTI_IMAGE_WIDTH";
    private static final String MULTI_IMAGE_HEIGHT = "MULTI_IMAGE_HEIGHT";

    public static void startActivity(Context context, int pos, ArrayList<String> list, int width, int height) {
        Intent intent = new Intent(context, MultiGifActivity.class);
        intent.putExtra(MULTI_IMAGE_POS, pos);
        intent.putStringArrayListExtra(MULTI_IMAGE_URL, list);
        intent.putExtra(MULTI_IMAGE_WIDTH, width);
        intent.putExtra(MULTI_IMAGE_HEIGHT, height);
        context.startActivity(intent);
        ((MainActivity)context).overridePendingTransition(R.anim.zoom_in, R.anim.zoom_out);
    }

    private int currentPos;
    private int width;
    private int height;

    private List<String> mListUrl;

    @BindView(R.id.picture_multi_pager)
    ViewPager mViewPager;

    @BindView(R.id.picture_multi_pager_bottom)
    TextView mTextView;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (savedInstanceState != null) {
            currentPos = savedInstanceState.getInt(MULTI_IMAGE_CURRENT_POS);
            width = savedInstanceState.getInt(MULTI_IMAGE_WIDTH);
            height = savedInstanceState.getInt(MULTI_IMAGE_HEIGHT);
        } else {
            currentPos = getIntent().getIntExtra(MULTI_IMAGE_POS, 0);
            width = getIntent().getIntExtra(MULTI_IMAGE_WIDTH, 0);
            height = getIntent().getIntExtra(MULTI_IMAGE_HEIGHT, 0);
        }

        mListUrl = getIntent().getStringArrayListExtra(MULTI_IMAGE_URL);

        setContentView(R.layout.activity_multi_picture);
        ButterKnife.bind(this);

        mViewPager.setAdapter(new FragmentPagerAdapter(getSupportFragmentManager()) {
            @Override
            public Fragment getItem(int position) {
                return MultiGifFragment.newInstance(mListUrl.get(position), width, height);
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
