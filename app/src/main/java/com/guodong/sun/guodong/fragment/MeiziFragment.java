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
import com.guodong.sun.guodong.adapter.MeiziAdapter;
import com.guodong.sun.guodong.base.AbsBaseFragment;
import com.guodong.sun.guodong.entity.meizi.Meizi;
import com.guodong.sun.guodong.listener.OnRcvScrollListener;
import com.guodong.sun.guodong.presenter.presenterImpl.MeiziPresenterImpl;
import com.guodong.sun.guodong.uitls.AppUtil;
import com.guodong.sun.guodong.uitls.SnackbarUtil;
import com.guodong.sun.guodong.view.IMeiziView;
import com.guodong.sun.guodong.widget.CustomEmptyView;
import com.guodong.sun.guodong.widget.WrapContentGridLayoutManager;
import com.melnykov.fab.FloatingActionButton;
import com.nineoldandroids.animation.ObjectAnimator;
import com.nineoldandroids.animation.ValueAnimator;

import java.util.ArrayList;

import butterknife.BindView;

/**
 * Created by Administrator on 2016/10/9.
 */

public class MeiziFragment extends AbsBaseFragment implements IMeiziView {
    @BindView(R.id.meizi_swipe_refresh_layout)
    SwipeRefreshLayout mSwipeRefreshLayout;

    @BindView(R.id.meizi_recycler)
    RecyclerView mRecyclerView;

    @BindView(R.id.meizi_fb)
    FloatingActionButton mFButton;

    @BindView(R.id.empty_layout)
    CustomEmptyView mCustomEmptyView;

    //RecycleView是否正在刷新
    private boolean isRefreshing = false;
    private boolean isLoading;
    private int page = 1;

    private MeiziAdapter mAdapter;
    private MeiziPresenterImpl mMeiziPresenter;
    private ObjectAnimator mAnimator;


    public static MeiziFragment newInstance() {
        return new MeiziFragment();
    }

    @Override
    protected int getLayoutResId() {
        return R.layout.fragment_meizi;
    }

    @Override
    protected void lazyLoad() {
        if (!isPrepared || !isVisible)
            return;
        initRecyclerView();
        initFButton();
        isPrepared = false;
        mMeiziPresenter.getMeiziData(1);
    }

    private void initFButton() {
        mFButton.attachToRecyclerView(mRecyclerView);
        mFButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
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
                mMeiziPresenter.getMeiziData(1);
            }
        });
    }

    private void initRecyclerView() {
        mAdapter = new MeiziAdapter(getContext());
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setAdapter(mAdapter);
        mRecyclerView.setLayoutManager(new WrapContentGridLayoutManager(getContext(), 3));
        mRecyclerView.setItemAnimator(new DefaultItemAnimator());
        mRecyclerView.addOnScrollListener(new OnRcvScrollListener() {
            @Override
            public void onBottom() {
                super.onBottom();
                if (!isLoading) {
                    isLoading = true;
                    page++;
                    loadMoreDate();
                }
            }
        });
    }

    private void loadMoreDate() {
        mAdapter.onLoadStart();
        mMeiziPresenter.getMeiziData(page);
    }

    @Override
    public void finishCreateView(Bundle state) {
        isPrepared = true;
        mMeiziPresenter = new MeiziPresenterImpl(getContext(), this, this.bindToLifecycle());
        mSwipeRefreshLayout.setColorSchemeResources(R.color.colorPrimary);
        lazyLoad();
    }

    @Override
    public void updateMeiziData(ArrayList<Meizi> list) {
        hideEmptyView();
        mAdapter.addLists(list);
        mAdapter.onLoadFinish();
        isLoading = false;
    }

    @Override
    public void showProgressBar() {
        isRefreshing = true;
        mSwipeRefreshLayout.setRefreshing(true);
        mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                isRefreshing = true;
                //                mAdapter.clearMeiziList();
                mMeiziPresenter.getMeiziData(1);
            }
        });
    }

    @Override
    public void hideProgressBar() {
        if (mAnimator != null)
            mAnimator.cancel();
        if (mSwipeRefreshLayout != null)
            mSwipeRefreshLayout.setRefreshing(false);
        isLoading = false;
        isRefreshing = false;
    }

    @Override
    public void showError(String error) {
        initEmptyView();
    }

    public void hideEmptyView() {
        mCustomEmptyView.setVisibility(View.GONE);
    }

    public void initEmptyView() {
        mSwipeRefreshLayout.setRefreshing(false);
        SnackbarUtil.showMessage(mRecyclerView, getString(R.string.noNetwork));
    }
}
