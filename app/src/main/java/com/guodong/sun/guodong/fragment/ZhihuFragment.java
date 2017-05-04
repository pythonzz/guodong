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

import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.guodong.sun.guodong.R;
import com.guodong.sun.guodong.adapter.ZhihuAdapter;
import com.guodong.sun.guodong.base.AbsBaseFragment;
import com.guodong.sun.guodong.entity.zhihu.ZhihuDailyNews;
import com.guodong.sun.guodong.listener.OnRcvScrollListener;
import com.guodong.sun.guodong.presenter.presenterImpl.ZhihuPresenterImpl;
import com.guodong.sun.guodong.uitls.AppUtil;
import com.guodong.sun.guodong.uitls.SnackbarUtil;
import com.guodong.sun.guodong.view.IZhihuView;
import com.guodong.sun.guodong.widget.CustomEmptyView;
import com.guodong.sun.guodong.widget.DividerItemDecoration;
import com.guodong.sun.guodong.widget.WrapContentLinearLayoutManager;
import com.melnykov.fab.FloatingActionButton;
import com.wdullaer.materialdatetimepicker.date.DatePickerDialog;

import java.util.ArrayList;
import java.util.Calendar;

import butterknife.BindView;

/**
 * Created by Administrator on 2016/10/12.
 */

public class ZhihuFragment extends AbsBaseFragment implements IZhihuView
{
    @BindView(R.id.zhihu_refresh)
    SwipeRefreshLayout mSwipeRefreshLayout;

    @BindView(R.id.zhihu_rv)
    RecyclerView mRecyclerView;

    @BindView(R.id.zhihu_fb)
    FloatingActionButton mFButton;

    @BindView(R.id.empty_layout)
    CustomEmptyView mCustomEmptyView;

    //RecycleView是否正在刷新
    private boolean isRefreshing = false;
    private boolean isLoading;

    private int mYear = Calendar.getInstance().get(Calendar.YEAR);
    private int mMonth = Calendar.getInstance().get(Calendar.MONTH);
    private int mDay = Calendar.getInstance().get(Calendar.DAY_OF_MONTH);

    private ZhihuPresenterImpl mZhihuPresenter;
    private ZhihuAdapter mAdapter;

    public static ZhihuFragment newInstance()
    {
        return new ZhihuFragment();
    }

    @Override
    protected int getLayoutResId()
    {
        return R.layout.fragment_zhihu;
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
        mZhihuPresenter.getZhihuData(Calendar.getInstance().getTimeInMillis());
    }

    private void initFButton()
    {
        mFButton.attachToRecyclerView(mRecyclerView);
        mFButton.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                Calendar now = Calendar.getInstance();
//                now.set(mYear, mMonth, mDay);
                DatePickerDialog dialog = DatePickerDialog.newInstance(new DatePickerDialog.OnDateSetListener()
                {
                    @Override
                    public void onDateSet(DatePickerDialog view, int year, int monthOfYear, int dayOfMonth)
                    {
                        mYear = year;
                        mMonth = monthOfYear;
                        mDay = dayOfMonth;
                        Calendar temp = Calendar.getInstance();
                        temp.clear();
                        temp.set(year, monthOfYear, dayOfMonth);
                        mZhihuPresenter.getZhihuData(temp.getTimeInMillis());
                    }
                }, now.get(Calendar.YEAR), now.get(Calendar.MONTH), now.get(Calendar.DAY_OF_MONTH));

                dialog.setMaxDate(Calendar.getInstance());
                Calendar minDate = Calendar.getInstance();
                // 2013.5.20是知乎日报api首次上线
                minDate.set(2013, 4, 20);
                dialog.setMinDate(minDate);
                dialog.vibrate(true);
                dialog.show(getActivity().getFragmentManager(), "DatePickerDialog");
            }
        });
    }

    private void initRecyclerView()
    {
        mAdapter = new ZhihuAdapter(getContext());
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setAdapter(mAdapter);
        mRecyclerView.setLayoutManager(new WrapContentLinearLayoutManager(getContext()));
        mRecyclerView.setItemAnimator(new DefaultItemAnimator());
        mRecyclerView.addItemDecoration(new DividerItemDecoration(getContext(), WrapContentLinearLayoutManager.VERTICAL));
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
        Calendar c = Calendar.getInstance();
        c.set(mYear, mMonth, --mDay);
        mZhihuPresenter.getZhihuData(c.getTimeInMillis());
    }

    @Override
    public void finishCreateView(Bundle state)
    {
        isPrepared = true;
        mZhihuPresenter = new ZhihuPresenterImpl(this, this.bindToLifecycle());
        lazyLoad();
    }

    @Override
    public void updateZhihuData(ArrayList<ZhihuDailyNews.Question> list)
    {
        if (mFButton.getVisibility() == View.GONE)
            mFButton.setVisibility(View.VISIBLE);
        hideEmptyView();
        // 注意addList() 和 onLoadFinish()的调用顺序
        mAdapter.addList(list);
        mAdapter.onLoadFinish();
        isLoading = false;
    }

    @Override
    public void showProgressBar()
    {
        mSwipeRefreshLayout.setColorSchemeResources(R.color.colorPrimary);
        mSwipeRefreshLayout.setRefreshing(true);
        mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener()
        {
            @Override
            public void onRefresh()
            {
                isRefreshing = true;
                // mAdapter.clearDuanziList();
                mZhihuPresenter.getZhihuData(Calendar.getInstance().getTimeInMillis());
            }
        });
    }

    @Override
    public void hideProgressBar()
    {
        if (mSwipeRefreshLayout != null)
            mSwipeRefreshLayout.setRefreshing(false);
        isLoading = false;
        isRefreshing = false;
    }

    @Override
    public void showError(String error)
    {
        initEmptyView();
        mFButton.setVisibility(View.GONE);
    }

    @Override
    public void onDetach()
    {
        super.onDetach();
        mZhihuPresenter.unsubcrible();
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
                    mZhihuPresenter.getZhihuData(Calendar.getInstance().getTimeInMillis());
                }
            });
        }
    }
}
