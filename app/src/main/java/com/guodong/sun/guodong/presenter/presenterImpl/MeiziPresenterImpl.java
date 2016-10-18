package com.guodong.sun.guodong.presenter.presenterImpl;

import android.content.Context;

import com.google.gson.Gson;
import com.guodong.sun.guodong.Config;
import com.guodong.sun.guodong.api.ApiHelper;
import com.guodong.sun.guodong.entity.meizi.MeiziList;
import com.guodong.sun.guodong.presenter.IMeiziPresenter;
import com.guodong.sun.guodong.uitls.CacheUtil;
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
    private CacheUtil mCacheUtil;
    private Gson gson = new Gson();

    public MeiziPresenterImpl(Context context, IMeiziView mMeiziView)
    {
        this.mMeiziView = mMeiziView;
        mCacheUtil = CacheUtil.get(context);
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
                        mMeiziView.hidProgressBar();
                        mMeiziView.showError(e.getMessage());
                    }

                    @Override
                    public void onNext(MeiziList meiziList)
                    {
                        mMeiziView.hidProgressBar();
                        mCacheUtil.put(Config.MEIZI, gson.toJson(meiziList));
                        mMeiziView.updateMeiziData(meiziList.getResults());
                    }
                });
        addSubscription(subscription);
    }
}
