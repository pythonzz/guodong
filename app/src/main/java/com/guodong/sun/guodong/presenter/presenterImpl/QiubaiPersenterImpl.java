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

import com.google.gson.Gson;
import com.guodong.sun.guodong.Config;
import com.guodong.sun.guodong.api.ApiHelper;
import com.guodong.sun.guodong.api.QiuBaiApi;
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
        Subscription subscription = ApiHelper.getInstance().getApi(QiuBaiApi.class, ApiHelper.QIUBAI_BASE_URL).getQiuBaiData(page)
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
