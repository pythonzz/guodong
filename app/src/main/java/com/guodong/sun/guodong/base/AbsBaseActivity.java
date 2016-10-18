package com.guodong.sun.guodong.base;

import android.os.Bundle;
import android.support.annotation.Nullable;

import com.trello.rxlifecycle.components.support.RxAppCompatActivity;

import butterknife.ButterKnife;

/**
 * Created by Administrator on 2016/10/9.
 */

public abstract class AbsBaseActivity extends RxAppCompatActivity
{
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(getLayoutId());
        ButterKnife.bind(this);
        //初始化控件
        initViews(savedInstanceState);
    }

    protected abstract void initViews(Bundle savedInstanceState);
    protected abstract int getLayoutId();
}
