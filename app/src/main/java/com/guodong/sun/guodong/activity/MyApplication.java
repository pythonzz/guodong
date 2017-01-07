package com.guodong.sun.guodong.activity;

import android.app.Application;
import android.util.DisplayMetrics;
import android.view.WindowManager;

import com.antfortune.freeline.FreelineCore;
import com.guodong.sun.guodong.CrashHandler;

/**
 * Created by Administrator on 2016/9/19.
 */
public class MyApplication extends Application
{
    private static MyApplication sInstance;
    public static int ScreenWidth;
    public static int ScreenHeight;

    @Override
    public void onCreate()
    {
        super.onCreate();
        FreelineCore.init(this);
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

    public static MyApplication getInstance()
    {
        return sInstance;
    }
}
