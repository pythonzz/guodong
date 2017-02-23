package com.guodong.sun.guodong.presenter.presenterImpl;

import android.content.Context;
import android.util.Log;

import com.guodong.sun.guodong.api.ApiHelper;
import com.guodong.sun.guodong.api.DuanZiApi;
import com.guodong.sun.guodong.entity.picture.GsonProvider;
import com.guodong.sun.guodong.entity.picture.PictureBean;
import com.guodong.sun.guodong.presenter.IPicturePresenter;
import com.guodong.sun.guodong.retrofit.RetrofitHelper;
import com.guodong.sun.guodong.view.IPictureView;
import com.trello.rxlifecycle.LifecycleTransformer;

import java.util.ArrayList;

import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;
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
    private LifecycleTransformer bind;
    private Retrofit mRetrofit;

    public PicturePreenterImpl(Context context, IPictureView mDuanziView, LifecycleTransformer bind)
    {
        this.mPictureView = mDuanziView;
        this.bind = bind;
        mRetrofit = new Retrofit.Builder()
                .baseUrl(ApiHelper.DUANZI_BASE_URL)
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                .addConverterFactory(GsonConverterFactory.create(GsonProvider.gson))
                .client(RetrofitHelper.createOkHttpClient())
                .build();
    }

    @Override
    public void getPictureData()
    {
        mPictureView.showProgressBar();
        Subscription subscription = mRetrofit.create(DuanZiApi.class).getPictureBean()
                .compose(bind)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<PictureBean>()
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
                        Log.e("Test", "onError: " + e.getMessage());
                    }

                    @Override
                    public void onNext(PictureBean list)
                    {
                        mPictureView.hideProgressBar();
                        ArrayList<PictureBean.DataBeanX.DataBean> datas = new ArrayList<>();
                        for (PictureBean.DataBeanX.DataBean data : list.getData().getData())
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