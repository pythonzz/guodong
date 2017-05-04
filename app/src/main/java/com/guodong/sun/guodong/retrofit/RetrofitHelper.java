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
 * Last modified 2017-05-04 15:05:32
 *
 * GitHub:   https://github.com/guodongAndroid
 * Website:  http://www.sunxiaoduo.com
 * Email:    sun33919135@gmail.com
 * QQ:       33919135
 */

package com.guodong.sun.guodong.retrofit;

import com.guodong.sun.guodong.activity.MyApplication;
import com.guodong.sun.guodong.uitls.AppUtil;

import java.io.File;
import java.io.IOException;
import java.util.concurrent.TimeUnit;

import okhttp3.Cache;
import okhttp3.CacheControl;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Retrofit2 帮助类
 * Created by Administrator on 2016/9/20.
 */
public class RetrofitHelper
{
    private static final Long CONNECTTIMEOUT = 15L;
    private static final Long READTIMEOUT = 20L;
    private static final Long WRITETIMEOUT = 20L;

    /**
     * 根据BaseUrL获取Retrofit.Builder
     *
     * @param baseUrl baseurl
     * @return
     */
    private Retrofit getRetrofit(String baseUrl)
    {
        return new Retrofit.Builder()
                .baseUrl(baseUrl)
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                .addConverterFactory(GsonConverterFactory.create())
                .client(createOkHttpClient())
                .build();
    }

    public static OkHttpClient createOkHttpClient()
    {
        HttpLoggingInterceptor loggingInterceptor = new HttpLoggingInterceptor();
        loggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
        return new OkHttpClient.Builder()
                .addInterceptor(loggingInterceptor)
                .addNetworkInterceptor(createCacheInterceptor())    // 无网缓存
                .addInterceptor(createCacheInterceptor())           // 无网缓存 两个都需要拦截
                .cache(getCache())
                .retryOnConnectionFailure(true)
                .connectTimeout(CONNECTTIMEOUT, TimeUnit.SECONDS)
                .readTimeout(READTIMEOUT, TimeUnit.SECONDS)
                .writeTimeout(WRITETIMEOUT, TimeUnit.SECONDS)
                .build();
    }

    private static Cache getCache()
    {
        // 获取缓存目标
        File cacheFile = new File(MyApplication.getInstance().getCacheDir(), AppUtil.getAppName());
        // 创建缓存对象,最大缓存50m
        return new Cache(cacheFile, 1024 * 1024 * 50);
    }

    private static Interceptor createCacheInterceptor()
    {
        return new Interceptor()
        {
            @Override
            public Response intercept(Chain chain) throws IOException
            {
                Request request = chain.request();
                if (!AppUtil.isNetworkConnected())
                {
                    request = request
                            .newBuilder()
                            .cacheControl(CacheControl.FORCE_CACHE)
                            .build();
                }

                Response response = chain.proceed(request);
                if (AppUtil.isNetworkConnected())
                {
                    int maxAge = 60 * 60 * 24 * 7;
                    response.newBuilder()
                            .header("Cache-Control", "Cache-Control: public, max-age=" + maxAge)
                            .removeHeader("Pragma")
                            .build();
                }
                else
                {
                    int maxStale = 60 * 60 * 24 * 7;
                    response.newBuilder()
                            .header("Cache-Control", "public, only-if-cached, max-stale=" + maxStale)
                            .removeHeader("Pragma")
                            .build();
                }
                return response;
            }
        };
    }

    public  <T> T createService(Class<T> cls, String baseUrl)
    {
        return getRetrofit(baseUrl).create(cls);
    }

    private volatile static RetrofitHelper sInstance;

    public static RetrofitHelper getInstance()
    {
        if (sInstance == null)
        {
            synchronized (RetrofitHelper.class)
            {
                if (sInstance == null)
                {
                    sInstance = new RetrofitHelper();
                }
            }
        }
        return sInstance;
    }

    private RetrofitHelper()
    {
    }
}
