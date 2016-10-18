package com.guodong.sun.guodong.base;

import android.os.Bundle;
import android.support.annotation.Nullable;

/**
 * Created by Administrator on 2016/10/9.
 */

public abstract class AbsMVPBaseActivity<V, T extends AbsBasePresenter<V>> extends AbsBaseActivity
{
    protected T mPresenter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        mPresenter = createPresenter();
        mPresenter.attachView((V) this);
    }

    @Override
    protected void onDestroy()
    {
        super.onDestroy();
        mPresenter.detachView();
    }

    protected abstract T createPresenter();
}
