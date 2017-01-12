package com.guodong.sun.guodong.fragment;

import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.LinearInterpolator;

import com.guodong.sun.guodong.R;
import com.guodong.sun.guodong.adapter.PictureAdapter;
import com.guodong.sun.guodong.base.AbsBaseFragment;
import com.guodong.sun.guodong.entity.picture.Picture;
import com.guodong.sun.guodong.listener.OnRcvScrollListener;
import com.guodong.sun.guodong.presenter.presenterImpl.PicturePreenterImpl;
import com.guodong.sun.guodong.uitls.AppUtil;
import com.guodong.sun.guodong.uitls.SnackbarUtil;
import com.guodong.sun.guodong.view.IPictureView;
import com.guodong.sun.guodong.widget.SunVideoPlayer;
import com.guodong.sun.guodong.widget.WrapContentLinearLayoutManager;
import com.melnykov.fab.FloatingActionButton;
import com.nineoldandroids.animation.ObjectAnimator;
import com.nineoldandroids.animation.ValueAnimator;

import java.util.ArrayList;

import butterknife.BindView;
import fm.jiecao.jcvideoplayer_lib.JCVideoPlayer;
import fm.jiecao.jcvideoplayer_lib.JCVideoPlayerManager;

/**
 * Created by Administrator on 2016/10/10.
 */

public class PictureFragment extends AbsBaseFragment implements IPictureView
{
    @BindView(R.id.picture_swipe_refresh_layout)
    SwipeRefreshLayout mSwipeRefreshLayout;

    @BindView(R.id.picture_recycler)
    RecyclerView mRecyclerView;

    @BindView(R.id.picture_fb)
    FloatingActionButton mFButton;

    //RecycleView是否正在刷新
    private boolean isRefreshing = false;
    private boolean isLoading;

    private PicturePreenterImpl mPicturePreenter;
    private PictureAdapter mAdapter;
    private ObjectAnimator mAnimator;

    public static PictureFragment newInstance()
    {
        return new PictureFragment();
    }

    @Override
    protected int getLayoutResId()
    {
        return R.layout.fragment_picture;
    }

    @Override
    protected void lazyLoad()
    {
        if (!isPrepared || !isVisible)
            return;
        mSwipeRefreshLayout.setColorSchemeResources(R.color.colorPrimary);
        mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener()
        {
            @Override
            public void onRefresh()
            {
                isRefreshing = true;
//                mAdapter.clearDuanziList();
                mPicturePreenter.getPictureData();
            }
        });
        initRecyclerView();
        initFButton();
        isPrepared = false;
        mPicturePreenter.getPictureData();
    }

    private void initFButton()
    {
        mFButton.attachToRecyclerView(mRecyclerView);
        mFButton.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                if (isRefreshing || isLoading)
                    return;
                mAnimator = ObjectAnimator.ofFloat(v, "rotation", 0F, 360F);
                mAnimator.setDuration(500);
                mAnimator.setInterpolator(new LinearInterpolator());
                mAnimator.setRepeatCount(ValueAnimator.INFINITE);
                mAnimator.setRepeatMode(ValueAnimator.RESTART);
                mAnimator.start();
                mRecyclerView.scrollToPosition(0);
                isRefreshing = true;
                mPicturePreenter.getPictureData();
            }
        });
    }

    private void initRecyclerView()
    {
        mAdapter = new PictureAdapter(getContext(), mRecyclerView);
        mRecyclerView.setAdapter(mAdapter);
        mRecyclerView.setLayoutManager(new WrapContentLinearLayoutManager(getContext()));
        mRecyclerView.setItemAnimator(new DefaultItemAnimator());
        mRecyclerView.addOnScrollListener(new OnRcvScrollListener()
        {
            @Override
            public void onBottom()
            {
                super.onBottom();
                if (!isLoading)
                {
                    isLoading = true;
                    loadMoreDate();
                }
            }
        });
    }

    private void loadMoreDate()
    {
        mAdapter.onLoadStart();
        mPicturePreenter.getPictureData();
    }

    @Override
    public void finishCreateView(Bundle state)
    {
        isPrepared = true;
        mPicturePreenter = new PicturePreenterImpl(getContext(), this, this.bindToLifecycle());
        lazyLoad();
    }

    @Override
    public void onDetach()
    {
        super.onDetach();
        mPicturePreenter.unsubcrible();
    }

    @Override
    public void onPause() {
        super.onPause();
        hideProgressBar();
    }

    @Override
    public void updatePictureData(ArrayList<Picture.DataBeanX.DataBean> list)
    {
        // 注意addList() 和 onLoadFinish()的调用顺序
        mAdapter.addLists(list);
        mAdapter.onLoadFinish();
        isLoading = false;
    }

    @Override
    public void showProgressBar()
    {
        isRefreshing = true;
        mSwipeRefreshLayout.setRefreshing(true);
    }

    @Override
    public void hideProgressBar()
    {
        if (mAnimator != null)
            mAnimator.cancel();
        if (mSwipeRefreshLayout != null)
            mSwipeRefreshLayout.setRefreshing(false);
        isLoading = false;
        isRefreshing = false;
    }

    @Override
    public void showError(String error)
    {
        initEmptyView(error);
    }

    public void initEmptyView(String error)
    {
        if (!AppUtil.isNetworkConnected())
        {
            SnackbarUtil.showMessage(mRecyclerView, getString(R.string.noNetwork));
        }
        else
        {
            mSwipeRefreshLayout.setRefreshing(false);
            SnackbarUtil.showMessage(mFButton, error);
        }
    }
}
