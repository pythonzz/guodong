package com.guodong.sun.guodong.base;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;

import com.guodong.sun.guodong.activity.MyApplication;
import com.squareup.leakcanary.RefWatcher;
import com.trello.rxlifecycle.components.support.RxAppCompatActivity;

import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * Created by Administrator on 2016/10/9.
 */

public abstract class AbsBaseActivity extends RxAppCompatActivity
{
    private static final String TAG = "AbsBaseActivity";

    private Unbinder mUnbinder;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(getLayoutId());
        mUnbinder = ButterKnife.bind(this);
        //初始化控件
        initViews(savedInstanceState);
    }

    protected abstract void initViews(Bundle savedInstanceState);
    protected abstract int getLayoutId();

    @Override
    protected void onDestroy() {
        super.onDestroy();
        RefWatcher watcher = MyApplication.getRefWatcher(this);
        watcher.watch(this);
        if (mUnbinder != null) {
            mUnbinder.unbind();
            mUnbinder = null;
        }
        Log.e(TAG, "程序退出");
    }
}
