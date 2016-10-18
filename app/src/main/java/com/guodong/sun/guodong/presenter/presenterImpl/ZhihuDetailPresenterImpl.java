package com.guodong.sun.guodong.presenter.presenterImpl;

import com.google.gson.Gson;
import com.guodong.sun.guodong.api.ApiHelper;
import com.guodong.sun.guodong.entity.zhihu.ZhihuDailyStory;
import com.guodong.sun.guodong.presenter.IZhihuDetailPresenter;
import com.guodong.sun.guodong.view.IZhihuDetailView;

import rx.Observer;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by Administrator on 2016/10/13.
 */

public class ZhihuDetailPresenterImpl extends BasePresenterImpl implements IZhihuDetailPresenter
{
    private IZhihuDetailView mZhihuDetailView;
    private Gson mGson;

    public ZhihuDetailPresenterImpl(IZhihuDetailView zhihuDetailView)
    {
        mZhihuDetailView = zhihuDetailView;
        mGson = new Gson();
    }

    @Override
    public void getZhihuDetailData(int id)
    {
        mZhihuDetailView.showProgressBar();
        Subscription subscription = ApiHelper
                .getInstance().getZhiHuApi()
                .getZhiHuStory(id)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<ZhihuDailyStory>()
                {
                    @Override
                    public void onCompleted()
                    {
                    }

                    @Override
                    public void onError(Throwable e)
                    {
                        mZhihuDetailView.hidProgressBar();
                        mZhihuDetailView.showError(e.getMessage());
                    }

                    @Override
                    public void onNext(ZhihuDailyStory story)
                    {
                        mZhihuDetailView.hidProgressBar();
                        mZhihuDetailView.updateZhihuDetailData(story);
                    }
                });
        addSubscription(subscription);
    }
}
