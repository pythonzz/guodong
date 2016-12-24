package com.guodong.sun.guodong.presenter.presenterImpl;

import android.content.Context;

import com.google.gson.Gson;
import com.guodong.sun.guodong.Config;
import com.guodong.sun.guodong.api.ApiHelper;
import com.guodong.sun.guodong.entity.qiubai.QiuShiBaiKe;
import com.guodong.sun.guodong.presenter.IQiubaiPresenter;
import com.guodong.sun.guodong.uitls.CacheUtil;
import com.guodong.sun.guodong.view.IQiubaiView;
import com.trello.rxlifecycle.LifecycleTransformer;

import java.util.ArrayList;

import rx.Observer;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by Administrator on 2016/10/11.
 */

public class QiubaiPersenterImpl extends BasePresenterImpl implements IQiubaiPresenter
{
    private IQiubaiView mQiubaiView;
    private CacheUtil mCacheUtil;
    private Gson gson = new Gson();
    private LifecycleTransformer bind;

    public QiubaiPersenterImpl(Context context, IQiubaiView mQiubaiView, LifecycleTransformer bind)
    {
        this.mQiubaiView = mQiubaiView;
        mCacheUtil = CacheUtil.get(context);
        this.bind = bind;
    }

    @Override
    public void getQiubaiData(int page)
    {
        mQiubaiView.showProgressBar();
        Subscription subscription = ApiHelper.getInstance().getQiuBaiApi().getQiuBaiData(page)
                .compose(bind)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<QiuShiBaiKe>()
                {
                    @Override
                    public void onCompleted()
                    {

                    }

                    @Override
                    public void onError(Throwable e)
                    {
                        mQiubaiView.hideProgressBar();
                        mQiubaiView.showError(e.getMessage());
                    }

                    @Override
                    public void onNext(QiuShiBaiKe qiuShiBaiKe)
                    {
                        mQiubaiView.hideProgressBar();
                        mCacheUtil.put(Config.QIUBAI, gson.toJson(qiuShiBaiKe));
                        ArrayList<QiuShiBaiKe.Item> list = new ArrayList<>();
                        for (QiuShiBaiKe.Item item : qiuShiBaiKe.getItems())
                        {
                            list.add(item);
                        }
                        mQiubaiView.updateQiubaiData(list);
                    }
                });
        addSubscription(subscription);
    }
}
