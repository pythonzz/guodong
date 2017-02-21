package com.guodong.sun.guodong.base;

import java.lang.ref.Reference;
import java.lang.ref.WeakReference;

import rx.Subscription;
import rx.subscriptions.CompositeSubscription;

/**
 * Created by Administrator on 2016/10/9.
 */

public abstract class AbsBasePresenter<T>
{
    protected Reference<T> mViewRef;
    protected CompositeSubscription mCompositeSubscription;

    public void attachView(T view)
    {
        mViewRef = new WeakReference<>(view);
    }

    public T getView()
    {
        return mViewRef.get();
    }

    public boolean isViewAttached()
    {
        return mViewRef != null && mViewRef.get() != null;
    }

    public void detachView()
    {
        if (mViewRef != null)
        {
            mViewRef.clear();
            mViewRef = null;
        }
    }

    protected void addSubscription(Subscription s)
    {
        if (this.mCompositeSubscription == null)
            this.mCompositeSubscription = new CompositeSubscription();
        this.mCompositeSubscription.add(s);
    }

    public void unsubcrible()
    {
        if (this.mCompositeSubscription != null)
            this.mCompositeSubscription.unsubscribe();
    }
}
