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

    private Object mLock = new Object();
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

    public GankApi getGankApi()
    {
        return RetrofitHelper.getInstance().createService(GankApi.class, MEIZI_BASE_URL);
    }

    public DuanZiApi getDuanZiApi()
    {
        return RetrofitHelper.getInstance().createService(DuanZiApi.class, DUANZI_BASE_URL);
    }

    public QiuBaiApi getQiuBaiApi()
    {
        return RetrofitHelper.getInstance().createService(QiuBaiApi.class, QIUBAI_BASE_URL);
    }

    public ZhiHuApi getZhiHuApi()
    {
        return RetrofitHelper.getInstance().createService(ZhiHuApi.class, ZHIHU_BASE_URL);
    }
}
