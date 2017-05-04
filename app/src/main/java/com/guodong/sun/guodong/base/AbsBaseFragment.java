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
 * Last modified 2017-05-04 15:02:14
 *
 * GitHub:   https://github.com/guodongAndroid
 * Website:  http://www.sunxiaoduo.com
 * Email:    sun33919135@gmail.com
 * QQ:       33919135
 */

package com.guodong.sun.guodong.base;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.guodong.sun.guodong.activity.MyApplication;
import com.squareup.leakcanary.RefWatcher;
import com.trello.rxlifecycle.components.support.RxFragment;

import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * Created by Administrator on 2016/10/9.
 */

public abstract class AbsBaseFragment extends RxFragment
{
    private View parentView;

    private FragmentActivity activity;

    private LayoutInflater inflater;

    private Unbinder mUnbinder;

    // 标志位 标志已经初始化完成。
    protected boolean isPrepared;

    //标志位 fragment是否可见
    protected boolean isVisible;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle state)
    {
        this.inflater = inflater;
        parentView = inflater.inflate(getLayoutResId(), container, false);
        activity = getSupportActivity();
        mUnbinder = ButterKnife.bind(this, parentView);
        return parentView;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState)
    {
        super.onViewCreated(view, savedInstanceState);
        finishCreateView(savedInstanceState);
    }

    @Override
    public void onAttach(Activity activity)
    {
        super.onAttach(activity);
        this.activity = (FragmentActivity) activity;
    }

    @Override
    public void onDetach()
    {
        super.onDetach();
        this.activity = null;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        RefWatcher watcher = MyApplication.getRefWatcher(getActivity());
        watcher.watch(this);
        if (mUnbinder != null) {
            mUnbinder.unbind();
            mUnbinder = null;
        }
    }

    public FragmentActivity getSupportActivity()
    {
        return super.getActivity();
    }

    public android.app.ActionBar getSupportActionBar()
    {
        return getSupportActivity().getActionBar();
    }

    protected LayoutInflater getLayoutInflater()
    {
        return inflater;
    }

    protected View getParentView()
    {
        return parentView;
    }


    /**
     * Fragment数据的懒加载
     *
     * @param isVisibleToUser
     */
    @Override
    public void setUserVisibleHint(boolean isVisibleToUser)
    {

        super.setUserVisibleHint(isVisibleToUser);
        if (getUserVisibleHint())
        {
            isVisible = true;
            onVisible();
        }
        else
        {
            isVisible = false;
            onInvisible();
        }
    }

    protected void onVisible()
    {
        lazyLoad();
    }

    protected void onInvisible()
    {
    }

    protected abstract int getLayoutResId();
    protected abstract void lazyLoad();
    protected abstract void finishCreateView(Bundle state);
}
