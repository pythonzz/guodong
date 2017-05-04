/*
 * Copyright (C) 2017 guodongAndroid
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 * Last modified 2017-05-04 15:05:15
 *
 * GitHub:   https://github.com/guodongAndroid
 * Website:  http://www.sunxiaoduo.com
 * Email:    sun33919135@gmail.com
 * QQ:       33919135
 */

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