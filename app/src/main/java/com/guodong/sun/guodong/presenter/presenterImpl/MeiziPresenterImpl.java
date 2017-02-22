package com.guodong.sun.guodong.presenter.presenterImpl;

import android.content.Context;

import com.guodong.sun.guodong.api.ApiHelper;
import com.guodong.sun.guodong.api.GankApi;
import com.guodong.sun.guodong.entity.meizi.MeiziList;
import com.guodong.sun.guodong.presenter.IMeiziPresenter;
import com.guodong.sun.guodong.view.IMeiziView;
import com.trello.rxlifecycle.LifecycleTransformer;

import rx.Observer;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action0;
import rx.schedulers.Schedulers;


/**
 * Created by Administrator on 2016/10/9.
 */

public class MeiziPresenterImpl extends BasePresenterImpl implements IMeiziPresenter {

    private IMeiziView mMeiziView;
    private LifecycleTransformer bind;

    public MeiziPresenterImpl(Context context, IMeiziView mMeiziView, LifecycleTransformer bind) {
        this.mMeiziView = mMeiziView;
        this.bind = bind;
    }

    @Override
    public void getMeiziData(int page) {

        Subscription subscription = ApiHelper.getInstance().getApi(GankApi.class, ApiHelper.MEIZI_BASE_URL).getMeizhiData(page)
                .compose(bind)
                .subscribeOn(Schedulers.io())
                .doOnSubscribe(new Action0() {
                    @Override
                    public void call() {
                        mMeiziView.showProgressBar();
                    }
                })
                .subscribeOn(AndroidSchedulers.mainThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<MeiziList>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        mMeiziView.hideProgressBar();
                        mMeiziView.showError(e.getMessage());
                    }

                    @Override
                    public void onNext(MeiziList meiziList) {
                        mMeiziView.hideProgressBar();
                        mMeiziView.updateMeiziData(meiziList.getResults());
                    }
                });
        addSubscription(subscription);
    }
}
