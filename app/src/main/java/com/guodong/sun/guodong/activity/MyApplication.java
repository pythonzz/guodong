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
 * Last modified 2017-05-04 15:01:49
 *
 * GitHub:   https://github.com/guodongAndroid
 * Website:  http://www.sunxiaoduo.com
 * Email:    sun33919135@gmail.com
 * QQ:       33919135
 */

package com.guodong.sun.guodong.activity;

import android.app.Application;
import android.content.Context;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.WindowManager;

import com.antfortune.freeline.FreelineCore;
import com.guodong.sun.guodong.CrashHandler;
import com.squareup.leakcanary.LeakCanary;
import com.squareup.leakcanary.RefWatcher;
import com.umeng.socialize.Config;
import com.umeng.socialize.PlatformConfig;
import com.umeng.socialize.UMShareAPI;

import java.io.BufferedInputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

/**
 * Created by Administrator on 2016/9/19.
 */
public class MyApplication extends Application {

    private static final String TAG = "MyApplication";

    private static MyApplication sInstance;
    public static int ScreenWidth;
    public static int ScreenHeight;

    private RefWatcher mRefWatcher;

    public static RefWatcher getRefWatcher(Context context) {
        MyApplication app = (MyApplication) context.getApplicationContext();
        return app.mRefWatcher;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        FreelineCore.init(this);
        mRefWatcher = LeakCanary.install(this);
        sInstance = this;
        CrashHandler crashHandler = CrashHandler.sInstance;
        crashHandler.init(this);
        crashHandler.setDebug(true);

        UmengConfig();
        WindowManager manager = (WindowManager) getSystemService(WINDOW_SERVICE);
        DisplayMetrics outMetrics = new DisplayMetrics();
        manager.getDefaultDisplay().getMetrics(outMetrics);
        ScreenWidth = outMetrics.widthPixels;
        ScreenHeight = outMetrics.heightPixels;
    }

    public static MyApplication getInstance() {
        return sInstance;
    }

    private void UmengConfig() {
        Properties pro = new Properties();
        String qq_id = null, qq_key = null, wx_id = null, wx_secret = null, umeng_key = null;
        InputStream is = null;

        try {
            is = sInstance.getAssets().open("umeng.properties");
            pro.load(is);
            umeng_key = pro.getProperty("umeng.key");
            qq_id = pro.getProperty("qq.id");
            qq_key = pro.getProperty("qq.key");
            wx_id = pro.getProperty("wx.id");
            wx_secret = pro.getProperty("wx.secret");
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if (is != null) {
                    is.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        Config.DEBUG = true;
        UMShareAPI.init(this, umeng_key); // 不需要修改
        PlatformConfig.setWeixin(wx_id, wx_secret); // 不需要修改
        PlatformConfig.setQQZone(qq_id, qq_key); // 不需要修改
    }
}
