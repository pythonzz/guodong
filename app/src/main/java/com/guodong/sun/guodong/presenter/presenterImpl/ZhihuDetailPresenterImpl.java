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
import com.guodong.sun.guodong.entity.zhihu.ZhihuDailyStory;
import com.guodong.sun.guodong.presenter.IZhihuDetailPresenter;
import com.guodong.sun.guodong.view.IZhihuDetailView;
import com.trello.rxlifecycle.LifecycleTransformer;

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
    private LifecycleTransformer bind;

    public ZhihuDetailPresenterImpl(IZhihuDetailView zhihuDetailView, LifecycleTransformer bind)
    {
        mZhihuDetailView = zhihuDetailView;
        mGson = new Gson();
        this.bind = bind;
    }

    @Override
    public void getZhihuDetailData(int id)
    {
        mZhihuDetailView.showProgressBar();
        Subscription subscription = ApiHelper
                .getInstance().getApi(ZhiHuApi.class, ApiHelper.ZHIHU_BASE_URL)
                .getZhiHuStory(id)
                .compose(bind)
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
                        mZhihuDetailView.hideProgressBar();
                        mZhihuDetailView.showError(e.getMessage());
                    }

                    @Override
                    public void onNext(ZhihuDailyStory story)
                    {
                        mZhihuDetailView.hideProgressBar();
                        mZhihuDetailView.updateZhihuDetailData(story);
                    }
                });
        addSubscription(subscription);
    }
}
