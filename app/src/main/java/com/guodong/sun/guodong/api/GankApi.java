package com.guodong.sun.guodong.api;

import com.guodong.sun.guodong.entity.meizi.MeiziList;

import retrofit2.http.GET;
import retrofit2.http.Path;
import rx.Observable;

/**
 * Created by Administrator on 2016/10/9.
 */

public interface GankApi
{
    @GET("api/data/福利/20/{page}")
    Observable<MeiziList> getMeizhiData(@Path("page") int page);

//    @GET("api/data/休息视频/10/{page}")
//    Observable<VedioData> getVedioData(@Path("page") int page);
}
