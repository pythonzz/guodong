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
 * Last modified 2017-05-04 15:04:47
 *
 * GitHub:   https://github.com/guodongAndroid
 * Website:  http://www.sunxiaoduo.com
 * Email:    sun33919135@gmail.com
 * QQ:       33919135
 */

package com.guodong.sun.guodong.fragment;

import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.LinearInterpolator;

import com.guodong.sun.guodong.R;
import com.guodong.sun.guodong.adapter.VideoAdapter;
import com.guodong.sun.guodong.base.AbsBaseFragment;
import com.guodong.sun.guodong.entity.duanzi.NeiHanVideo;
import com.guodong.sun.guodong.listener.OnRcvScrollListener;
import com.guodong.sun.guodong.presenter.presenterImpl.VideoPresenterImpl;
import com.guodong.sun.guodong.uitls.AppUtil;
import com.guodong.sun.guodong.uitls.SnackbarUtil;
import com.guodong.sun.guodong.view.IVideoView;
import com.guodong.sun.guodong.widget.CustomEmptyView;
import com.guodong.sun.guodong.widget.WrapContentLinearLayoutManager;
import com.melnykov.fab.FloatingActionButton;
import com.nineoldandroids.animation.ObjectAnimator;
import com.nineoldandroids.animation.ValueAnimator;

import java.util.ArrayList;

import butterknife.BindView;
import fm.jiecao.jcvideoplayer_lib.JCVideoPlayer;
import fm.jiecao.jcvideoplayer_lib.JCVideoPlayerManager;

/**
 * Created by Administrator on 2016/10/17.
 */

public class VideoFragment extends AbsBaseFragment implements IVideoView
{
    @BindView(R.id.video_recycler)
    RecyclerView mRecyclerView;
    @BindView(R.id.empty_layout)
    CustomEmptyView mCustomEmptyView;
    @BindView(R.id.video_swipe_refresh_layout)
    SwipeRefreshLayout mSwipeRefreshLayout;
    @BindView(R.id.video_fb)
    FloatingActionButton mFButton;

    //RecycleView是否正在刷新
    private boolean isRefreshing = false;
    private boolean isLoading;
    private int page = 1;

    private VideoAdapter mAdapter;
    private VideoPresenterImpl mVideoPresenter;
    private ObjectAnimator mAnimator;

    public static VideoFragment newInstance()
    {
        return new VideoFragment();
    }

    @Override
    protected int getLayoutResId()
    {
        return R.layout.fragment_video;
    }

    @Override
    protected void lazyLoad()
    {
        if (!isPrepared || !isVisible)
            return;
        showProgressBar();
        initRecyclerView();
        initFButton();
        isPrepared = false;
        mVideoPresenter.getVideoData(1);
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
                mVideoPresenter.getVideoData(1);
            }
        });
    }

    private void initRecyclerView()
    {
        mAdapter = new VideoAdapter(getContext(), mRecyclerView);
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setAdapter(mAdapter);
        mRecyclerView.setLayoutManager(new WrapContentLinearLayoutManager(getContext()));
        mRecyclerView.setItemAnimator(new DefaultItemAnimator());
        mRecyclerView.addOnChildAttachStateChangeListener(new RecyclerView.OnChildAttachStateChangeListener()
        {
            @Override
            public void onChildViewAttachedToWindow(View view)
            {
            }

            @Override
            public void onChildViewDetachedFromWindow(View view)
            {
                if (JCVideoPlayerManager.getFirst() != null)
                {
                    JCVideoPlayer videoPlayer = (JCVideoPlayer) JCVideoPlayerManager.getFirst();
                    if (((ViewGroup) view).indexOfChild(videoPlayer) != -1 && videoPlayer.currentState == JCVideoPlayer.CURRENT_STATE_PLAYING)
                    {
                        JCVideoPlayer.releaseAllVideos();
                    }
                }
            }
        });

        mRecyclerView.addOnScrollListener(new OnRcvScrollListener()
        {
            @Override
            public void onBottom()
            {
                super.onBottom();
                if (!isLoading)
                {
                    isLoading = true;
                    page++;
                    loadMoreDate();
                }
            }
        });
    }

    private void loadMoreDate()
    {
        mAdapter.onLoadStart();
        mVideoPresenter.getVideoData(page);
    }

    @Override
    protected void finishCreateView(Bundle state)
    {
        isPrepared = true;
        mVideoPresenter = new VideoPresenterImpl(this, this.bindToLifecycle());
        lazyLoad();
    }

    @Override
    protected void onInvisible()
    {
        JCVideoPlayer.releaseAllVideos();
    }

    @Override
    public void updateVideoData(ArrayList<NeiHanVideo.DataBean> list)
    {
        hideEmptyView();
        // 注意addList() 和 onLoadFinish()的调用顺序
        mAdapter.addLists(list);
        mAdapter.onLoadFinish();
        isLoading = false;
    }

    @Override
    public void showProgressBar()
    {
        isRefreshing = true;
        mSwipeRefreshLayout.setColorSchemeResources(R.color.colorPrimary);
        mSwipeRefreshLayout.setRefreshing(true);
        mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener()
        {
            @Override
            public void onRefresh()
            {
                isRefreshing = true;
                //                mAdapter.clearDuanziList();
                mVideoPresenter.getVideoData(1);
            }
        });
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
        initEmptyView();
    }

    public void hideEmptyView()
    {
        mCustomEmptyView.setVisibility(View.GONE);
    }

    public void initEmptyView()
    {
        if (!AppUtil.isNetworkConnected())
        {
            SnackbarUtil.showMessage(mRecyclerView, getString(R.string.noNetwork));
        }
        else
        {
            mSwipeRefreshLayout.setRefreshing(false);
            mCustomEmptyView.setVisibility(View.VISIBLE);
            mCustomEmptyView.setEmptyImage(R.drawable.img_tips_error_load_error);
            mCustomEmptyView.setEmptyText(getString(R.string.loaderror));
            SnackbarUtil.showMessage(mRecyclerView, getString(R.string.noNetwork));
            mCustomEmptyView.reload(new CustomEmptyView.ReloadOnClickListener()
            {
                @Override
                public void reloadClick()
                {
                    mVideoPresenter.getVideoData(1);
                }
            });
        }
    }
}
