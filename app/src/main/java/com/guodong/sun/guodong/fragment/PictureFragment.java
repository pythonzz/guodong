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
import com.guodong.sun.guodong.adapter.PictureAdapter;
import com.guodong.sun.guodong.base.AbsBaseFragment;
import com.guodong.sun.guodong.entity.picture.PictureBean;
import com.guodong.sun.guodong.entity.picture.gifvideo.Gif;
import com.guodong.sun.guodong.entity.picture.gifvideo.GifProvider;
import com.guodong.sun.guodong.entity.picture.gifvideo.GifVideoBean;
import com.guodong.sun.guodong.entity.picture.gifvideo.GifVideoProvider;
import com.guodong.sun.guodong.entity.picture.multiimage.MultiImage;
import com.guodong.sun.guodong.entity.picture.multiimage.MultiImageProvider;
import com.guodong.sun.guodong.entity.picture.singleimage.LongImage;
import com.guodong.sun.guodong.entity.picture.singleimage.LongImageViewProvider;
import com.guodong.sun.guodong.entity.picture.singleimage.SingleImage;
import com.guodong.sun.guodong.entity.picture.singleimage.SingleImageViewProvider;
import com.guodong.sun.guodong.listener.OnRcvScrollListener;
import com.guodong.sun.guodong.presenter.presenterImpl.PicturePreenterImpl;
import com.guodong.sun.guodong.uitls.AppUtil;
import com.guodong.sun.guodong.uitls.SnackbarUtil;
import com.guodong.sun.guodong.view.IPictureView;
import com.guodong.sun.guodong.widget.WrapContentLinearLayoutManager;
import com.melnykov.fab.FloatingActionButton;
import com.nineoldandroids.animation.ObjectAnimator;
import com.nineoldandroids.animation.ValueAnimator;

import java.util.ArrayList;

import butterknife.BindView;
import me.drakeet.multitype.Items;

import static me.drakeet.multitype.MultiTypeAsserts.assertAllRegistered;

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
    private boolean isLoadingMore;

    private PicturePreenterImpl mPicturePreenter;
    private PictureAdapter mAdapter;
    private Items mItems;
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
                if (isRefreshing || isLoadingMore)
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
        mItems = new Items();
        mAdapter = new PictureAdapter();
        mAdapter.register(Gif.class, new GifProvider());
        mAdapter.register(GifVideoBean.class, new GifVideoProvider());
        mAdapter.register(MultiImage.class, new MultiImageProvider());
        mAdapter.register(LongImage.class, new LongImageViewProvider());
        mAdapter.register(SingleImage.class, new SingleImageViewProvider());

        mRecyclerView.setAdapter(mAdapter);

        mRecyclerView.setLayoutManager(new WrapContentLinearLayoutManager(getContext()));
        mRecyclerView.setItemAnimator(new DefaultItemAnimator());
        mRecyclerView.addOnScrollListener(new OnRcvScrollListener()
        {
            @Override
            public void onBottom()
            {
                super.onBottom();
                if (!isLoadingMore)
                {
                    isLoadingMore = true;
                    loadMoreDate();
                }
            }
        });
    }

    private void loadMoreDate()
    {
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
    public void updatePictureData(ArrayList<PictureBean.DataBeanX.DataBean> list)
    {
        int size = mAdapter.getItemCount();
        if (isLoadingMore) {
            mItems.addAll(list);
            mAdapter.setItems(mItems);
            mAdapter.notifyItemRangeInserted(size, list.size());
        } else {
            mItems.addAll(0, list);
            mAdapter.setItems(mItems);
            mAdapter.notifyItemRangeInserted(0, list.size());
            mRecyclerView.scrollToPosition(0);
        }
        assertAllRegistered(mAdapter, mItems);
        isLoadingMore = false;
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
