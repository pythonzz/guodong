package com.guodong.sun.guodong.presenter.presenterImpl;

import com.google.gson.Gson;
import com.guodong.sun.guodong.api.ApiHelper;
import com.guodong.sun.guodong.entity.zhihu.ZhihuDailyNews;
import com.guodong.sun.guodong.presenter.IZhihuPresenter;
import com.guodong.sun.guodong.uitls.DateTimeHelper;
import com.guodong.sun.guodong.view.IZhihuView;
import com.trello.rxlifecycle.LifecycleTransformer;

import rx.Observer;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by Administrator on 2016/10/12.
 */

public class ZhihuPresenterImpl extends BasePresenterImpl implements IZhihuPresenter
{
    private IZhihuView mZhihuView;
    private Gson gson;
    private LifecycleTransformer bind;

    public ZhihuPresenterImpl(IZhihuView mZhihuView, LifecycleTransformer bind)
    {
        this.mZhihuView = mZhihuView;
        gson = new Gson();
        this.bind = bind;
    }

    @Override
    public void getZhihuData(long data)
    {
        mZhihuView.showProgressBar();
        Subscription subscription = ApiHelper
                .getInstance()
                .getZhiHuApi()
                .getZhiHuData(DateTimeHelper.formatZhihuDailyDate(data))
                .compose(bind)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<ZhihuDailyNews>()
                {
                    @Override
                    public void onCompleted()
                    {
                    }

                    @Override
                    public void onError(Throwable e)
                    {
                        mZhihuView.hideProgressBar();
                        mZhihuView.showError(e.getMessage());
                    }

                    @Override
                    public void onNext(ZhihuDailyNews data)
                    {
                        mZhihuView.hideProgressBar();
                        mZhihuView.updateZhihuData(data.getStories());
                    }
                });
        addSubscription(subscription);
    }
}
