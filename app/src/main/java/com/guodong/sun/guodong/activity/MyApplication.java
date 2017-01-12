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
        Config.DEBUG = true;
        UMShareAPI.init(this, "582076f69f06fd4a220009f0");
        FreelineCore.init(this);
        mRefWatcher = LeakCanary.install(this);
        sInstance = this;
        CrashHandler crashHandler = CrashHandler.sInstance;
        crashHandler.init(this);
        crashHandler.setDebug(true);

        WindowManager manager = (WindowManager) getSystemService(WINDOW_SERVICE);
        DisplayMetrics outMetrics = new DisplayMetrics();
        manager.getDefaultDisplay().getMetrics(outMetrics);
        ScreenWidth = outMetrics.widthPixels;
        ScreenHeight = outMetrics.heightPixels;
    }

    public static MyApplication getInstance() {
        return sInstance;
    }

    {
        PlatformConfig.setWeixin("wxad830cfdd75835c0", "c09c9464d46312c00b3672bb3f16daa5"); // 不需要修改
        PlatformConfig.setQQZone("1105733003", "9FqrDa2AbrdessRY"); // 不需要修改
    }
}
