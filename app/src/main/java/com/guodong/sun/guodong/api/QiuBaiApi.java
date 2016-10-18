package com.guodong.sun.guodong.api;

import com.guodong.sun.guodong.entity.qiubai.QiuShiBaiKe;

import retrofit2.http.GET;
import retrofit2.http.Query;
import rx.Observable;

/**
 * Created by Administrator on 2016/10/11.
 */

public interface QiuBaiApi
{
    @GET("article/list/text")
    Observable<QiuShiBaiKe> getQiuBaiData(@Query("page") int page);
}
