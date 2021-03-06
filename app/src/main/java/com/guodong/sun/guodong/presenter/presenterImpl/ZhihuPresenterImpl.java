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

import com.google.gson.Gson;
import com.guodong.sun.guodong.api.ApiHelper;
import com.guodong.sun.guodong.api.ZhiHuApi;
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
                .getApi(ZhiHuApi.class, ApiHelper.ZHIHU_BASE_URL)
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
