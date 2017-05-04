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
import com.guodong.sun.guodong.api.DuanZiApi;
import com.guodong.sun.guodong.entity.duanzi.NeiHanVideo;
import com.guodong.sun.guodong.entity.duanzi.VideoData;
import com.guodong.sun.guodong.presenter.IVideoPresenter;
import com.guodong.sun.guodong.view.IVideoView;
import com.trello.rxlifecycle.LifecycleTransformer;


import java.util.ArrayList;

import rx.Observer;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by Administrator on 2016/10/17.
 */

public class VideoPresenterImpl extends BasePresenterImpl implements IVideoPresenter
{
    private IVideoView mVideoView;
    private Gson mGson;
    private LifecycleTransformer bind;

    public VideoPresenterImpl(IVideoView videoView, LifecycleTransformer bind)
    {
        mVideoView = videoView;
        mGson = new Gson();
        this.bind = bind;
    }

    @Override
    public void getVideoData(int page)
    {
        mVideoView.showProgressBar();
        Subscription subscription = ApiHelper.getInstance()
                .getApi(DuanZiApi.class, ApiHelper.DUANZI_BASE_URL).getVideoData(page)
                .compose(bind)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<VideoData>()
                {
                    @Override
                    public void onCompleted()
                    {
                    }

                    @Override
                    public void onError(Throwable e)
                    {
                        mVideoView.hideProgressBar();
                        mVideoView.showError(e.getMessage());
                    }

                    @Override
                    public void onNext(VideoData data)
                    {
                        mVideoView.hideProgressBar();
                        ArrayList<NeiHanVideo.DataBean> list = new ArrayList<>();
                        for (NeiHanVideo.DataBean bean : data.getData().getData()) {
                            if (bean.getType() == 1)
                                list.add(bean);
                        }
                        mVideoView.updateVideoData(list);
                    }
                });
        addSubscription(subscription);
    }
}
