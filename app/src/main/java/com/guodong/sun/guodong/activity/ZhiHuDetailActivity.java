package com.guodong.sun.guodong.activity;

import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
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
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.guodong.sun.guodong.R;
import com.guodong.sun.guodong.base.AbsBaseActivity;
import com.guodong.sun.guodong.entity.zhihu.ZhihuDailyStory;
import com.guodong.sun.guodong.presenter.presenterImpl.ZhihuDetailPresenterImpl;
import com.guodong.sun.guodong.uitls.SnackbarUtil;
import com.guodong.sun.guodong.uitls.WebUtils;
import com.guodong.sun.guodong.view.IZhihuDetailView;

import butterknife.BindView;

/**
 * Created by Administrator on 2016/10/13.
 */

public class ZhiHuDetailActivity extends AbsBaseActivity implements IZhihuDetailView
{
    public static final String EXTRA_ID = "id";
    public static final String TRANSIT_PIC = "picture";
    private int mId;

    @BindView(R.id.zhihu_detail_CToolbarLayout)
    CollapsingToolbarLayout mToolbarLayout;

    @BindView(R.id.zhihu_detail_fab)
    FloatingActionButton mFButton;

    @BindView(R.id.zhihu_detail_iv)
    ImageView mImageView;

    @BindView(R.id.zhihu_detail_tv)
    TextView mTextView;

    @BindView(R.id.zhihu_detail_toolbar)
    Toolbar mToolbar;

    @BindView(R.id.zhihu_detail_webview)
    WebView mWebView;

    private AlertDialog mAlertDialog;
    private ZhihuDetailPresenterImpl mZhihuDetailPresenter;
    private ZhihuDailyStory mStory;

    public static Intent newIntent(Context context, int id)
    {
        Intent intent = new Intent(context, ZhiHuDetailActivity.class);
        intent.putExtra(EXTRA_ID, id);
        return intent;
    }

    @Override
    protected void initViews(Bundle savedInstanceState)
    {

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP)
        {
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

//        mAlertDialog = new AlertDialog.Builder(this).create();
//        mAlertDialog.setView(getLayoutInflater().inflate(R.layout.loading_layout, null));

        mZhihuDetailPresenter = new ZhihuDetailPresenterImpl(this);
        mId = getIntent().getIntExtra(EXTRA_ID, 0);
        mZhihuDetailPresenter.getZhihuDetailData(mId);

        mFButton.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                try
                {
                    Intent shareIntent = new Intent().setAction(Intent.ACTION_SEND).setType("text/plain");
                    String shareText = mStory.getTitle() + " " + mStory.getShare_url() + "\t\t\t分享自 " + getString(R.string.app_name);
                    shareIntent.putExtra(Intent.EXTRA_TEXT, shareText);
                    startActivity(Intent.createChooser(shareIntent, "分享至"));
                } catch (ActivityNotFoundException ex)
                {
                    SnackbarUtil.showMessage(v, "分享失败");
                }
            }
        });
    }

    private void initWebView()
    {
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
    protected int getLayoutId()
    {
        return R.layout.activity_zhihu_detail;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
        getMenuInflater().inflate(R.menu.menu_refresh, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
        switch (item.getItemId())
        {
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
    protected void onDestroy()
    {
        super.onDestroy();
        mZhihuDetailPresenter.unsubcrible();
//        dimiss();
    }

    private void dimiss()
    {
        if (mAlertDialog != null)
        {
            mAlertDialog.dismiss();
            mAlertDialog = null;
        }
    }

    @Override
    public void updateZhihuDetailData(ZhihuDailyStory story)
    {
        if (story != null)
        {
            mStory = story;
            mWebView.loadDataWithBaseURL("x-data://base", WebUtils.convertResult(story.getBody()), "text/html", "utf-8", null);
            mWebView.getSettings().setBlockNetworkImage(false);
            Glide.with(this).load(story.getImage()).crossFade().centerCrop().into(mImageView);
            mToolbarLayout.setTitle(story.getTitle());
        }
        else
        {
            mWebView.loadUrl(story.getShare_url());
            Glide.with(this).load(R.drawable.img_tips_error_load_error).into(mImageView);
        }
    }

    @Override
    public void showProgressBar()
    {
        //        if (mAlertDialog != null && !mAlertDialog.isShowing())
        //            mAlertDialog.show();
    }

    @Override
    public void hidProgressBar()
    {
//        dimiss();
    }

    @Override
    public void showError(String error)
    {
        SnackbarUtil.showMessage(mWebView, "加载失败", "重试", new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                mZhihuDetailPresenter.getZhihuDetailData(mId);
            }
        });
    }
}
