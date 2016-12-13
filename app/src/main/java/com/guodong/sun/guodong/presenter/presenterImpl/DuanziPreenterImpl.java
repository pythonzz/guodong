package com.guodong.sun.guodong.presenter.presenterImpl;

import android.content.Context;

import com.google.gson.Gson;
import com.guodong.sun.guodong.Config;
import com.guodong.sun.guodong.api.ApiHelper;
import com.guodong.sun.guodong.entity.duanzi.NeiHanDuanZi;
import com.guodong.sun.guodong.presenter.IDuanziPresenter;
import com.guodong.sun.guodong.uitls.CacheUtil;
import com.guodong.sun.guodong.view.IDuanziView;

import java.util.ArrayList;

import rx.Observer;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by Administrator on 2016/10/10.
 */

public class DuanziPreenterImpl extends BasePresenterImpl implements IDuanziPresenter
{

    private IDuanziView mDuanziView;
    private CacheUtil mCacheUtil;
    private Gson gson = new Gson();

    public DuanziPreenterImpl(Context context, IDuanziView mDuanziView)
    {
        this.mDuanziView = mDuanziView;
        mCacheUtil = CacheUtil.get(context);
    }

    @Override
    public void getDuanziData(int page)
    {
        mDuanziView.showProgressBar();
        Subscription subscription = ApiHelper
                .getInstance()
                .getDuanZiApi()
                .getDuanZiData(page)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<NeiHanDuanZi>()
                {
                    @Override
                    public void onCompleted()
                    {

                    }

                    @Override
                    public void onError(Throwable e)
                    {
                        mDuanziView.hideProgressBar();
                        mDuanziView.showError(e.getMessage());
                    }

                    @Override
                    public void onNext(NeiHanDuanZi list)
                    {
                        mDuanziView.hideProgressBar();
                        mCacheUtil.put(Config.DUANZI, gson.toJson(list));
                        ArrayList<NeiHanDuanZi.Data> datas = new ArrayList<>();
                        for (NeiHanDuanZi.Data data : list.getData().getData())
                        {
                            if (data.getAd() == null)
                                datas.add(data);
                        }
                        mDuanziView.updateDuanziData(datas);
                    }
                });
        addSubscription(subscription);
    }
}