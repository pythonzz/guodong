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
import android.content.res.Configuration;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.view.ViewCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.ImageView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.guodong.sun.guodong.R;
import com.guodong.sun.guodong.base.AbsBaseActivity;
import com.guodong.sun.guodong.entity.zhihu.ZhihuDailyStory;
import com.guodong.sun.guodong.listener.CustomShareListener;
import com.guodong.sun.guodong.presenter.presenterImpl.ZhihuDetailPresenterImpl;
import com.guodong.sun.guodong.uitls.SnackbarUtil;
import com.guodong.sun.guodong.uitls.WebUtils;
import com.guodong.sun.guodong.view.IZhihuDetailView;
import com.umeng.socialize.ShareAction;
import com.umeng.socialize.ShareContent;
import com.umeng.socialize.UMShareAPI;
import com.umeng.socialize.UMShareListener;
import com.umeng.socialize.bean.SHARE_MEDIA;
import com.umeng.socialize.shareboard.ShareBoardConfig;
import com.umeng.socialize.shareboard.SnsPlatform;
import com.umeng.socialize.utils.Log;
import com.umeng.socialize.utils.ShareBoardlistener;

import java.lang.ref.WeakReference;

import butterknife.BindView;

/**
 * Created by Administrator on 2016/10/13.
 */

public class ZhiHuDetailActivity extends AbsBaseActivity implements IZhihuDetailView {
    public static final String EXTRA_ID = "id";
    public static final String TRANSIT_PIC = "picture";
    private int mId;

    @BindView(R.id.zhihu_detail_CToolbarLayout)
    CollapsingToolbarLayout mToolbarLayout;

    @BindView(R.id.zhihu_detail_fab)
    FloatingActionButton mFButton;

    @BindView(R.id.zhihu_detail_iv)
    ImageView mImageView;

    @BindView(R.id.zhihu_detail_toolbar)
    Toolbar mToolbar;

    @BindView(R.id.zhihu_detail_webview)
    WebView mWebView;

    private AlertDialog mAlertDialog;
    private ZhihuDetailPresenterImpl mZhihuDetailPresenter;
    private ZhihuDailyStory mStory;
    private ShareAction mShareAction;
    private UMShareListener mShareListener;

    public static Intent newIntent(Context context, int id) {
        Intent intent = new Intent(context, ZhiHuDetailActivity.class);
        intent.putExtra(EXTRA_ID, id);
        return intent;
    }

    @Override
    protected void initViews(Bundle savedInstanceState) {

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            View decorView = getWindow().getDecorView();
            int option = View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN | View.SYSTEM_UI_FLAG_LAYOUT_STABLE;
            decorView.setSystemUiVisibility(option);
            getWindow().setStatusBarColor(Color.TRANSPARENT);
        }

//        mToolbar.setNavigationIcon(R.drawable.ic_arrow_back);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        ViewCompat.setTransitionName(mImageView, TRANSIT_PIC);

        initWebView();
        initShare();

//        mAlertDialog = new AlertDialog.Builder(this).create();
//        mAlertDialog.setView(getLayoutInflater().inflate(R.layout.loading_layout, null));

        mZhihuDetailPresenter = new ZhihuDetailPresenterImpl(this, this.bindToLifecycle());
        mId = getIntent().getIntExtra(EXTRA_ID, 0);
        mZhihuDetailPresenter.getZhihuDetailData(mId);

        mFButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ShareBoardConfig config = new ShareBoardConfig();
                config.setMenuItemBackgroundShape(ShareBoardConfig.BG_SHAPE_NONE);
                mShareAction.open(config);
            }
        });
    }

    private void initShare() {
        mShareListener = new CustomShareListener(this);
        mShareAction = new ShareAction(ZhiHuDetailActivity.this).setDisplayList(
                SHARE_MEDIA.WEIXIN, SHARE_MEDIA.WEIXIN_CIRCLE,
                SHARE_MEDIA.QQ, SHARE_MEDIA.QZONE)
                .setShareboardclickCallback(new ShareBoardlistener() {
                    @Override
                    public void onclick(SnsPlatform snsPlatform, SHARE_MEDIA share_media) {
                        if (mStory != null) {
                            ShareContent content = new ShareContent();
                            content.mTitle = getResources().getString(R.string.app_name);
                            content.mText = mStory.getTitle();
                            content.mTargetUrl = mStory.getShare_url();
                            new ShareAction(ZhiHuDetailActivity.this)
                                    .setShareContent(content)
                                    .setPlatform(share_media)
                                    .setCallback(mShareListener)
                                    .share();
                        }
                    }
                });
    }

    private void initWebView() {
        mWebView.setScrollbarFadingEnabled(true);
        //能够和js交互
        mWebView.getSettings().setJavaScriptEnabled(true);
        //缩放,设置为不能缩放可以防止页面上出现放大和缩小的图标
        mWebView.getSettings().setBuiltInZoomControls(false);
        //缓存
        mWebView.getSettings().setCacheMode(WebSettings.LOAD_CACHE_ELSE_NETWORK);
        //开启DOM storage API功能
        mWebView.getSettings().setDomStorageEnabled(true);
        //开启application Cache功能
        mWebView.getSettings().setAppCacheEnabled(true);
        mWebView.getSettings().setAppCachePath(getCacheDir().getAbsolutePath() + "/webViewCache");
        mWebView.getSettings().setBlockNetworkImage(true);
        mWebView.getSettings().setLoadWithOverviewMode(true);
        mWebView.getSettings().setLayoutAlgorithm(WebSettings.LayoutAlgorithm.SINGLE_COLUMN);
        mWebView.setWebChromeClient(new WebChromeClient());
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_zhihu_detail;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_refresh, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_refresh:
                mZhihuDetailPresenter.getZhihuDetailData(mId);
                break;

            case android.R.id.home:
                onBackPressed();
                break;
        }
        return true;
    }

    @Override
    public void onBackPressed() {
//        super.onBackPressed();
        finishAfterTransition();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mZhihuDetailPresenter.unsubcrible();
//        dimiss();
    }

    private void dimiss() {
        if (mAlertDialog != null) {
            mAlertDialog.dismiss();
            mAlertDialog = null;
        }
    }

    @Override
    public void updateZhihuDetailData(ZhihuDailyStory story) {
        if (story != null) {
            mStory = story;
            mWebView.loadDataWithBaseURL("x-data://base", WebUtils.convertResult(story.getBody()), "text/html", "utf-8", null);
            mWebView.getSettings().setBlockNetworkImage(false);
            Glide.with(this).load(story.getImage())
                    .crossFade().centerCrop()
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .placeholder(R.color.mid_grey)
                    .error(R.color.mid_grey)
                    .into(mImageView);
            mToolbarLayout.setTitle(story.getTitle());
        } else {
            mWebView.loadUrl(story.getShare_url());
            Glide.with(this).load(R.drawable.img_tips_error_load_error).into(mImageView);
        }
    }

    @Override
    public void showProgressBar() {
        //        if (mAlertDialog != null && !mAlertDialog.isShowing())
        //            mAlertDialog.show();
    }

    @Override
    public void hideProgressBar() {
//        dimiss();
    }

    @Override
    public void showError(String error) {
        SnackbarUtil.showMessage(mWebView, "加载失败", "重试", new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mZhihuDetailPresenter.getZhihuDetailData(mId);
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        /** attention to this below ,must add this**/
        UMShareAPI.get(this).onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        mShareAction.close();
    }
}
