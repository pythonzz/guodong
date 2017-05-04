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
 * Last modified 2017-05-04 15:08:08
 *
 * GitHub:   https://github.com/guodongAndroid
 * Website:  http://www.sunxiaoduo.com
 * Email:    sun33919135@gmail.com
 * QQ:       33919135
 */

package com.guodong.sun.guodong;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Environment;
import android.os.Process;
import android.util.Log;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.PrintWriter;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

/**
 * Created by Sun on 2016/9/19.
 */
public enum CrashHandler implements Thread.UncaughtExceptionHandler
{
    sInstance;

    private static final String TAG = "CrashHandler";
    private static boolean DEBUG = false;

    private static String PATH = Environment.getExternalStorageDirectory().getPath() + "/Crash/log/";
    private static final String FILE_NAME = "CrashLog";
    private static final String FILE_NAME_SUFFIX = ".trace";
    private File file;
    private static String URL;
    private static String[] PARAMS;

    private Thread.UncaughtExceptionHandler mDefaultCrashHandler;
    private Context mContext;

    public void init(Context context)
    {
        mDefaultCrashHandler = Thread.getDefaultUncaughtExceptionHandler();
        Thread.setDefaultUncaughtExceptionHandler(this);
        mContext = context.getApplicationContext();
    }

    @Override
    public void uncaughtException(Thread thread, Throwable throwable)
    {
        try
        {
            if (DEBUG)
            {
                // 导出异常信息到SDCard中
                dumpExceptionToSDCard(throwable);
                // 这里可以上传异常信息到服务器，便于开发人员分析日志从而解决bug
                uploadExceptionToServer();
            }
        } catch (Exception e)
        {
            e.printStackTrace();
        }
        throwable.printStackTrace();

        if (mDefaultCrashHandler != null)
            mDefaultCrashHandler.uncaughtException(thread, throwable);
        else
            Process.killProcess(Process.myPid());
    }

    private void uploadExceptionToServer()
    {
        // TODO
    }

    private void dumpExceptionToSDCard(Throwable throwable)
    {
        if (!Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED))
        {
            Log.e(TAG, "dump to SDCard crash info failed");
            return;
        }

        try
        {
            File dir = new File(PATH);
            if (!dir.exists())
            {
                dir.mkdirs();
            }

            long currentTime = System.currentTimeMillis();
            String time = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.US).format(new Date(currentTime));
            file = new File(PATH + FILE_NAME + time + FILE_NAME_SUFFIX);

            PrintWriter pw = new PrintWriter(new BufferedWriter(new FileWriter(file)));
            pw.println(time);
            dumpPhoneInfo(pw);
            pw.println();
            throwable.printStackTrace(pw);
            pw.close();
        } catch (Exception e)
        {
            e.printStackTrace();
            Log.e(TAG, "dump to SDCard crash info failed");
        }
    }

    private void dumpPhoneInfo(PrintWriter pw) throws PackageManager.NameNotFoundException
    {
        PackageManager pm = mContext.getPackageManager();
        PackageInfo pi = pm.getPackageInfo(mContext.getPackageName(), PackageManager.GET_ACTIVITIES);
        pw.print("APP 版本: ");
        pw.print(pi.versionName);
        pw.print("_");
        pw.println(pi.versionCode);

        // Android版本号
        pw.print("OS 版本: ");
        pw.print(Build.VERSION.RELEASE);
        pw.print("_");
        pw.println(Build.VERSION.SDK_INT);

        // 手机制造商
        pw.print("手机制造商: ");
        pw.println(Build.MANUFACTURER);

        // 手机型号
        pw.print("手机型号: ");
        pw.println(Build.MODEL);

        // CPU架构
        pw.print("CPU架构: ");
        pw.println(Build.CPU_ABI);
    }

    public static void setDebug(boolean isDebug)
    {
        DEBUG = isDebug;
    }

    public static CrashHandler setPath(String path)
    {
        PATH = path;
        return sInstance;
    }

    public static String getPath()
    {
        return PATH;
    }

    public static CrashHandler setURL(String url, String... params)
    {
        URL = url;
        PARAMS = params;
        return sInstance;
    }

}
