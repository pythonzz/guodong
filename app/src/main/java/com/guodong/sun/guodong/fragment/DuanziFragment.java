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
import android.view.animation.LinearInterpolator;

import com.guodong.sun.guodong.R;
import com.guodong.sun.guodong.adapter.DuanziAdapter;
import com.guodong.sun.guodong.base.AbsBaseFragment;
import com.guodong.sun.guodong.entity.duanzi.NeiHanDuanZi;
import com.guodong.sun.guodong.listener.OnRcvScrollListener;
import com.guodong.sun.guodong.presenter.presenterImpl.DuanziPreenterImpl;
import com.guodong.sun.guodong.uitls.AppUtil;
import com.guodong.sun.guodong.uitls.SnackbarUtil;
import com.guodong.sun.guodong.view.IDuanziView;
import com.guodong.sun.guodong.widget.CustomEmptyView;
import com.guodong.sun.guodong.widget.WrapContentLinearLayoutManager;
import com.melnykov.fab.FloatingActionButton;
import com.nineoldandroids.animation.ObjectAnimator;
import com.nineoldandroids.animation.ValueAnimator;

import java.util.ArrayList;

import butterknife.BindView;

/**
 * Created by Administrator on 2016/10/10.
 */

public class DuanziFragment extends AbsBaseFragment implements IDuanziView
{
    @BindView(R.id.duanzi_swipe_refresh_layout)
    SwipeRefreshLayout mSwipeRefreshLayout;

    @BindView(R.id.duanzi_recycler)
    RecyclerView mRecyclerView;

    @BindView(R.id.duanzi_fb)
    FloatingActionButton mFButton;

    @BindView(R.id.empty_layout)
    CustomEmptyView mCustomEmptyView;

    //RecycleView是否正在刷新
    private boolean isRefreshing = false;
    private boolean isLoading;
    private int page = 1;

    private DuanziPreenterImpl mDuanziPresenter;
    private DuanziAdapter mAdapter;
    private ObjectAnimator mAnimator;

    public static DuanziFragment newInstance()
    {
        return new DuanziFragment();
    }

    @Override
    protected int getLayoutResId()
    {
        return R.layout.fragment_duanzi;
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
        mDuanziPresenter.getDuanziData(1);
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
                mDuanziPresenter.getDuanziData(1);
            }
        });
    }

    private void initRecyclerView()
    {
        mAdapter = new DuanziAdapter(getContext(), mRecyclerView);
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
                    page++;
                    loadMoreDate();
                }
            }
        });
    }

    private void loadMoreDate()
    {
        mAdapter.onLoadStart();
        mDuanziPresenter.getDuanziData(page);
    }

    @Override
    public void finishCreateView(Bundle state)
    {
        isPrepared = true;
        mDuanziPresenter = new DuanziPreenterImpl(getContext(), this, this.bindToLifecycle());
        lazyLoad();
    }

    @Override
    public void onDetach()
    {
        super.onDetach();
        mDuanziPresenter.unsubcrible();
    }

    @Override
    public void updateDuanziData(ArrayList<NeiHanDuanZi.Data> list)
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
                mDuanziPresenter.getDuanziData(1);
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
                    mDuanziPresenter.getDuanziData(1);
                }
            });
        }
    }
}
