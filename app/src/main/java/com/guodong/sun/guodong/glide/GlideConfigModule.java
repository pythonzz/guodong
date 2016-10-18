package com.guodong.sun.guodong.glide;

import android.content.Context;

import com.bumptech.glide.Glide;
import com.bumptech.glide.GlideBuilder;
import com.bumptech.glide.load.DecodeFormat;
import com.bumptech.glide.module.GlideModule;

public class GlideConfigModule implements GlideModule
{

    @Override
    public void applyOptions(Context context, GlideBuilder builder) {
        // 优化图片显示质量
        builder.setDecodeFormat(DecodeFormat.PREFER_ARGB_8888);
    }

    @Override
    public void registerComponents(Context context, Glide glide) {
        //glide.clearDiskCache();
    }
}