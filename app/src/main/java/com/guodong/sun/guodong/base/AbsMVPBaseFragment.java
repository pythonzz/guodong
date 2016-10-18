package com.guodong.sun.guodong.base;

import android.app.Activity;

/**
 * Created by Administrator on 2016/10/9.
 */

public abstract class AbsMVPBaseFragment<V, T extends AbsBasePresenter<V>> extends AbsBaseFragment
{
    protected T mPresenter;

    @Override
    public void onAttach(Activity activity)
    {
        super.onAttach(activity);
        mPresenter = createPresenter();
        mPresenter.attachView((V) this);
    }

    @Override
    public void onDetach()
    {
        super.onDetach();
        mPresenter.detachView();
    }

    protected abstract T createPresenter();
}
