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
