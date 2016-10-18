package com.guodong.sun.guodong.presenter.presenterImpl;

import com.guodong.sun.guodong.base.IBasePresenter;

import rx.Subscription;
import rx.subscriptions.CompositeSubscription;

/**
 * Created by Administrator on 2016/10/9.
 */
public class BasePresenterImpl implements IBasePresenter
{

    private CompositeSubscription mCompositeSubscription;

    protected void addSubscription(Subscription s)
    {
        if (this.mCompositeSubscription == null)
            this.mCompositeSubscription = new CompositeSubscription();
        this.mCompositeSubscription.add(s);
    }

    @Override
    public void unsubcrible()
    {
        if (this.mCompositeSubscription != null)
            this.mCompositeSubscription.unsubscribe();
    }
}
