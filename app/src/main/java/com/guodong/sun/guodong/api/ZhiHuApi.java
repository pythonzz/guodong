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
 * Last modified 2017-05-04 15:02:01
 *
 * GitHub:   https://github.com/guodongAndroid
 * Website:  http://www.sunxiaoduo.com
 * Email:    sun33919135@gmail.com
 * QQ:       33919135
 */

package com.guodong.sun.guodong.api;

import com.guodong.sun.guodong.entity.zhihu.ZhihuDailyNews;
import com.guodong.sun.guodong.entity.zhihu.ZhihuDailyStory;

import java.util.ArrayList;

import retrofit2.http.GET;
import retrofit2.http.Path;
import rx.Observable;

/**
 * Created by Administrator on 2016/10/12.
 */

public interface ZhiHuApi
{
    @GET("api/4/news/before/{date}")
    Observable<ZhihuDailyNews> getZhiHuData(@Path("date") String date);

    @GET("api/4/news/{id}")
    Observable<ZhihuDailyStory> getZhiHuStory(@Path("id") int id);
}
