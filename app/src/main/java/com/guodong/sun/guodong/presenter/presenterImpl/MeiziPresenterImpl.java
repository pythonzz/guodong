package com.guodong.sun.guodong.presenter.presenterImpl;

import android.content.Context;

import com.guodong.sun.guodong.api.ApiHelper;
import com.guodong.sun.guodong.entity.meizi.MeiziList;
import com.guodong.sun.guodong.presenter.IMeiziPresenter;
import com.guodong.sun.guodong.view.IMeiziView;

import rx.Observer;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by Administrator on 2016/10/9.
 */

public class MeiziPresenterImpl extends BasePresenterImpl implements IMeiziPresenter
{

    private IMeiziView mMeiziView;

    public MeiziPresenterImpl(Context context, IMeiziView mMeiziView)
    {
        this.mMeiziView = mMeiziView;
    }

    @Override
    public void getMeiziData(int page)
    {
        mMeiziView.showProgressBar();
        Subscription subscription = ApiHelper.getInstance().getGankApi().getMeizhiData(page)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<MeiziList>()
                {
                    @Override
                    public void onCompleted()
                    {

                    }

                    @Override
                    public void onError(Throwable e)
                    {
                        mMeiziView.hideProgressBar();
                        mMeiziView.showError(e.getMessage());
                    }

                    @Override
                    public void onNext(MeiziList meiziList)
                    {
                        mMeiziView.hideProgressBar();
                        mMeiziView.updateMeiziData(meiziList.getResults());
                    }
                });
        addSubscription(subscription);
    }
}
