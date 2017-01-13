package com.guodong.sun.guodong.uitls;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.res.TypedArray;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;

import com.guodong.sun.guodong.activity.MyApplication;
import com.guodong.sun.guodong.R;

/**
 * App辅助类
 *
 * @author Sun
 */
public class AppUtil {
    // Suppress default constructor for noninstantiability
    private AppUtil() {
        throw new AssertionError();
    }

    /**
     * 全局获取Context
     *
     * @return
     */
    public static Context getContext() {
        return MyApplication.getInstance();
    }

    /**
     * 获取App名称
     *
     * @return 获取App名称
     */
    public static String getAppName() {
        Context context = getContext();
        return context.getString(context.getApplicationInfo().labelRes);
    }

    /**
     * get App versionCode
     *
     * @return
     */
    public static String getVersionCode() {
        PackageManager packageManager = getContext().getPackageManager();
        PackageInfo packageInfo;
        String versionCode = "";
        try {
            packageInfo = packageManager.getPackageInfo(getContext().getPackageName(), 0);
            versionCode = packageInfo.versionCode + "";
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return versionCode;
    }

    /**
     * get App versionName
     *
     * @return
     */
    public static String getVersionName() {
        PackageManager packageManager = getContext().getPackageManager();
        PackageInfo packageInfo;
        String versionName = "";
        try {
            packageInfo = packageManager.getPackageInfo(getContext().getPackageName(), 0);
            versionName = packageInfo.versionName;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return versionName;
    }

    /**
     * 获取Android系统版本
     */
    public static String getAndroidVersion() {
        return Build.VERSION.RELEASE;
    }

    /**
     * 获取Android SDK系统版本
     */
    public static String getAndroidSDKVersion() {
        return Build.VERSION.SDK_INT + "";
    }

    /**
     * 获取手机型号
     */
    public static String getPhoneModel() {
        return Build.MODEL;
    }

    /**
     * 获取网络状态信息
     *
     * @return NetworkInfo
     */
    public static NetworkInfo getNetworkInfo() {
        ConnectivityManager connectivityManager = (ConnectivityManager) getContext().getSystemService(Context.CONNECTIVITY_SERVICE);
        return connectivityManager.getActiveNetworkInfo();
    }

    /**
     * 判断是否离线
     *
     * @return true则有网络, 否则离线
     */
    public static boolean isNetworkConnected() {
        NetworkInfo activeNetworkInfo = getNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }

    /**
     * 判断是否Wifi连线
     *
     * @return true则wifi, 否则不是
     */
    public static boolean isWifi() {
        NetworkInfo activeNetworkInfo = getNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.getType() == ConnectivityManager.TYPE_WIFI;
    }

    public static int getToolbarHeight(Context context) {
        final TypedArray styledAttributes = context.getTheme().obtainStyledAttributes(
                new int[]{R.attr.actionBarSize});
        int toolbarHeight = (int) styledAttributes.getDimension(0, 0);
        styledAttributes.recycle();

        return toolbarHeight;
    }
}