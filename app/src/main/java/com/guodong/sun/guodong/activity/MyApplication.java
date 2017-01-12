package com.guodong.sun.guodong.activity;

import android.app.Application;
import android.content.Context;
import android.util.DisplayMetrics;
import android.view.WindowManager;

import com.antfortune.freeline.FreelineCore;
import com.guodong.sun.guodong.CrashHandler;
import com.squareup.leakcanary.LeakCanary;
import com.squareup.leakcanary.RefWatcher;
import com.umeng.socialize.PlatformConfig;
import com.umeng.socialize.UMShareAPI;

/**
 * Created by Administrator on 2016/9/19.
 */
public class MyApplication extends Application {
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
//        PlatformConfig.setWeixin("wx967daebe835fbeac", "5bb696d9ccd75a38c8a0bfe0675559b3"); // 需要修改
//        PlatformConfig.setQQZone("1105733003", "9FqrDa2AbrdessRY"); // 不需要修改
//        UMShareAPI.get(this);
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
}
