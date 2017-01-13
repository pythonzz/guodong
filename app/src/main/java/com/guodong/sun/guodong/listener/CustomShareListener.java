package com.guodong.sun.guodong.listener;

import android.app.Activity;
import android.util.Log;
import android.widget.Toast;

import com.umeng.socialize.UMShareListener;
import com.umeng.socialize.bean.SHARE_MEDIA;

import java.lang.ref.WeakReference;

public class CustomShareListener implements UMShareListener {

    private WeakReference<Activity> mActivity;

    public CustomShareListener(Activity activity) {
        mActivity = new WeakReference(activity);
    }

    @Override
    public void onResult(SHARE_MEDIA platform) {
        switch (platform) {
            case QQ:
                Toast.makeText(mActivity.get(), "QQ 分享成功", Toast.LENGTH_SHORT).show();
                break;

            case QZONE:
                Toast.makeText(mActivity.get(), "QQ空间 分享成功", Toast.LENGTH_SHORT).show();
                break;

            case WEIXIN:
                Toast.makeText(mActivity.get(), "微信 分享成功", Toast.LENGTH_SHORT).show();
                break;

            case WEIXIN_CIRCLE:
                Toast.makeText(mActivity.get(), "微信朋友圈 分享成功", Toast.LENGTH_SHORT).show();
                break;

            default:
                break;
        }
    }

    @Override
    public void onError(SHARE_MEDIA platform, Throwable t) {
        switch (platform) {
            case QQ:
                Toast.makeText(mActivity.get(), "QQ 分享失败,请稍后重试", Toast.LENGTH_SHORT).show();
                break;

            case QZONE:
                Toast.makeText(mActivity.get(), "QQ空间 分享失败,请稍后重试", Toast.LENGTH_SHORT).show();
                break;

            case WEIXIN:
                Toast.makeText(mActivity.get(), "微信 分享失败,请稍后重试", Toast.LENGTH_SHORT).show();
                break;

            case WEIXIN_CIRCLE:
                Toast.makeText(mActivity.get(), "微信朋友圈 分享失败,请稍后重试", Toast.LENGTH_SHORT).show();
                break;

            default:
                Toast.makeText(mActivity.get(), "啊哦,分享失败了,请稍后重试", Toast.LENGTH_SHORT).show();
                break;
        }

        if (t != null) {
            Log.e("umeng", "throw:" + t.getMessage());
        }
}

    @Override
    public void onCancel(SHARE_MEDIA platform) {
        switch (platform) {
            case QQ:
                Toast.makeText(mActivity.get(), "您取消了 QQ 分享", Toast.LENGTH_SHORT).show();
                break;

            case QZONE:
                Toast.makeText(mActivity.get(), "您取消了 QQ空间 分享", Toast.LENGTH_SHORT).show();
                break;

            case WEIXIN:
                Toast.makeText(mActivity.get(), "您取消了 微信 分享", Toast.LENGTH_SHORT).show();
                break;

            case WEIXIN_CIRCLE:
                Toast.makeText(mActivity.get(), "您取消了 微信朋友圈 分享", Toast.LENGTH_SHORT).show();
                break;

            default:
                Toast.makeText(mActivity.get(), "您取消了分享", Toast.LENGTH_SHORT).show();
                break;
        }
    }
}