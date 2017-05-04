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

import com.guodong.sun.guodong.retrofit.RetrofitHelper;

/**
 * Created by Administrator on 2016/10/9.
 */

public class ApiHelper
{
    public static final String MEIZI_BASE_URL = "http://gank.io/";
    public static final String TOUTIAO_BASE_URL = "http://c.m.163.com/";
    public static final String ZHIHU_BASE_URL = "http://news-at.zhihu.com/";
    public static final String DUANZI_BASE_URL = "http://ic.snssdk.com/";
    public static final String QIUBAI_BASE_URL = "http://m2.qiushibaike.com/";

    private volatile static ApiHelper sInstance;

    private ApiHelper()
    {
    }

    public static ApiHelper getInstance()
    {
        if (sInstance == null)
        {
            synchronized (ApiHelper.class)
            {
                if (sInstance == null)
                {
                    sInstance = new ApiHelper();
                }
            }
        }
        return sInstance;
    }

    public <T> T getApi(Class<T> cls, String baseUrl) {
        return RetrofitHelper.getInstance().createService(cls, baseUrl);
    }
}
