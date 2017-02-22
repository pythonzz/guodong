package com.guodong.sun.guodong.presenter.presenterImpl;

import android.content.Context;

import com.google.gson.Gson;
import com.guodong.sun.guodong.Config;
import com.guodong.sun.guodong.api.ApiHelper;
import com.guodong.sun.guodong.api.DuanZiApi;
import com.guodong.sun.guodong.entity.duanzi.NeiHanDuanZi;
import com.guodong.sun.guodong.entity.picture.Picture;
import com.guodong.sun.guodong.presenter.IDuanziPresenter;
import com.guodong.sun.guodong.presenter.IPicturePresenter;
import com.guodong.sun.guodong.uitls.CacheUtil;
import com.guodong.sun.guodong.view.IDuanziView;
import com.guodong.sun.guodong.view.IPictureView;
import com.trello.rxlifecycle.LifecycleTransformer;

import java.util.ArrayList;

import rx.Observer;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by Administrator on 2016/10/10.
 */

public class PicturePreenterImpl extends BasePresenterImpl implements IPicturePresenter
{

    private IPictureView mPictureView;
    private CacheUtil mCacheUtil;
    private Gson gson = new Gson();
    private LifecycleTransformer bind;

    public PicturePreenterImpl(Context context, IPictureView mDuanziView, LifecycleTransformer bind)
    {
        this.mPictureView = mDuanziView;
        mCacheUtil = CacheUtil.get(context);
        this.bind = bind;
    }

    @Override
    public void getPictureData()
    {
        mPictureView.showProgressBar();
        Subscription subscription = ApiHelper
                .getInstance()
                .getApi(DuanZiApi.class, ApiHelper.DUANZI_BASE_URL)
                .getPicture()
                .compose(bind)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<Picture>()
                {
                    @Override
                    public void onCompleted()
                    {

                    }

                    @Override
                    public void onError(Throwable e)
                    {
                        mPictureView.hideProgressBar();
                        mPictureView.showError(e.getMessage());
                    }

                    @Override
                    public void onNext(Picture list)
                    {
                        mPictureView.hideProgressBar();
                        ArrayList<Picture.DataBeanX.DataBean> datas = new ArrayList<>();
                        for (Picture.DataBeanX.DataBean data : list.getData().getData())
                        {
                            if (data.getType() == 1)
                                datas.add(data);
                        }
                        mPictureView.updatePictureData(datas);
                    }
                });
        addSubscription(subscription);
    }
}