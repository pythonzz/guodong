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

import com.guodong.sun.guodong.api.ApiHelper;
import com.guodong.sun.guodong.api.GankApi;
import com.guodong.sun.guodong.entity.meizi.MeiziList;
import com.guodong.sun.guodong.presenter.IMeiziPresenter;
import com.guodong.sun.guodong.view.IMeiziView;
import com.trello.rxlifecycle.LifecycleTransformer;

import rx.Observer;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action0;
import rx.schedulers.Schedulers;


/**
 * Created by Administrator on 2016/10/9.
 */

public class MeiziPresenterImpl extends BasePresenterImpl implements IMeiziPresenter {

    private IMeiziView mMeiziView;
    private LifecycleTransformer bind;

    public MeiziPresenterImpl(Context context, IMeiziView mMeiziView, LifecycleTransformer bind) {
        this.mMeiziView = mMeiziView;
        this.bind = bind;
    }

    @Override
    public void getMeiziData(int page) {

        Subscription subscription = ApiHelper.getInstance().getApi(GankApi.class, ApiHelper.MEIZI_BASE_URL).getMeizhiData(page)
                .compose(bind)
                .subscribeOn(Schedulers.io())
                .doOnSubscribe(new Action0() {
                    @Override
                    public void call() {
                        mMeiziView.showProgressBar();
                    }
                })
                .subscribeOn(AndroidSchedulers.mainThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<MeiziList>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        mMeiziView.hideProgressBar();
                        mMeiziView.showError(e.getMessage());
                    }

                    @Override
                    public void onNext(MeiziList meiziList) {
                        mMeiziView.hideProgressBar();
                        mMeiziView.updateMeiziData(meiziList.getResults());
                    }
                });
        addSubscription(subscription);
    }
}
