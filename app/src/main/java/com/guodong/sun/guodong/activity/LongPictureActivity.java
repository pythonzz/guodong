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

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.request.target.SizeReadyCallback;
import com.bumptech.glide.request.target.Target;
import com.guodong.sun.guodong.R;
import com.guodong.sun.guodong.base.AbsBaseActivity;
import com.guodong.sun.guodong.glide.ProgressTarget;
import com.guodong.sun.guodong.uitls.SnackbarUtil;
import com.guodong.sun.guodong.uitls.StringUtils;
import com.shizhefei.view.largeimage.LargeImageView;
import com.shizhefei.view.largeimage.factory.FileBitmapDecoderFactory;
import com.trello.rxlifecycle.components.support.RxAppCompatActivity;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Date;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Administrator on 2016/12/16.
 */

public class LongPictureActivity extends AbsBaseActivity {

    private static final String LONG_IMAGE_URL = "LONG_IMAGE_URL";
    private static final String TAG = LongPictureActivity.class.getSimpleName();

    public static void startActivity(Context context, String url) {
        Intent intent = new Intent(context, LongPictureActivity.class);
        intent.putExtra(LONG_IMAGE_URL, url);
        context.startActivity(intent);
        ((MainActivity)context).overridePendingTransition(R.anim.zoom_in, R.anim.zoom_out);
    }

    private String long_image_url;
    private Bitmap mBitmap;

    @BindView(R.id.picture_pager)
    LargeImageView mImageView;

    @Override
    protected void initViews(Bundle savedInstanceState) {
        long_image_url = getIntent().getStringExtra(LONG_IMAGE_URL);

        mImageView.setEnabled(true);
        mImageView.setCriticalScaleValueHook(new LargeImageView.CriticalScaleValueHook() {
            @Override
            public float getMinScale(LargeImageView largeImageView, int imageWidth, int imageHeight, float suggestMinScale) {
                return 1;
            }

            @Override
            public float getMaxScale(LargeImageView largeImageView, int imageWidth, int imageHeight, float suggestMaxScale) {
                return 2;
            }
        });

        mImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        mImageView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                createDialog();
                return true;
            }
        });

        Glide.with(this)
                .load(long_image_url)
                .downloadOnly(new ProgressTarget<String, File>(long_image_url, null) {

                    @Override
                    public void onProgress(int progress) {
                    }

                    @Override
                    public void onResourceReady(File resource, GlideAnimation<? super File> animation) {
                        super.onResourceReady(resource, animation);
                        mImageView.setImage(new FileBitmapDecoderFactory(resource));
                        mBitmap = BitmapFactory.decodeFile(resource.getPath());
                    }

                    @Override
                    public void getSize(SizeReadyCallback cb) {
                        cb.onSizeReady(Target.SIZE_ORIGINAL, Target.SIZE_ORIGINAL);
                    }
                });
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_picture;
    }

    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
        if (hasFocus && Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            View decorView = getWindow().getDecorView();
            int option = View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN  // 布局占据系统栏
                    | View.SYSTEM_UI_FLAG_LAYOUT_STABLE // 布局不会因系统栏改变而改变
                    | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION // 布局占据导航栏
                    | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION // 隐藏导航栏
                    | View.SYSTEM_UI_FLAG_FULLSCREEN // 全屏，隐藏系统栏
                    | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY; // 系统栏粘性
            decorView.setSystemUiVisibility(option);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                getWindow().setStatusBarColor(Color.TRANSPARENT);
                getWindow().setNavigationBarColor(Color.TRANSPARENT);
            }
        }
    }

    private void createDialog()
    {
        new AlertDialog.Builder(this).setMessage("保存到手机?").setNegativeButton(android.R.string.cancel, new DialogInterface.OnClickListener()
        {
            @Override
            public void onClick(DialogInterface dialogInterface, int i)
            {
                dialogInterface.dismiss();
            }
        }).setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener()
        {
            @Override
            public void onClick(DialogInterface dialogInterface, int i)
            {
                saveImage();
                dialogInterface.dismiss();
            }
        }).show();
    }

    private void saveImage()
    {
        File externalStorageDirectory = Environment.getExternalStorageDirectory();
        File directory = new File(externalStorageDirectory, getString(R.string.app_name));
        if (!directory.exists())
            directory.mkdir();
        try
        {
            File file = new File(directory, new Date().getTime() + ".jpg");
            FileOutputStream fos = new FileOutputStream(file);
            mBitmap.compress(Bitmap.CompressFormat.JPEG, 100, fos);
            fos.flush();
            fos.close();
            // 通知图库刷新
            Intent intent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
            Uri uri = Uri.fromFile(file);
            intent.setData(uri);
            sendBroadcast(intent);
            SnackbarUtil.showMessage(mImageView, "已保存到" + file.getAbsolutePath());
        } catch (Exception e)
        {
            e.printStackTrace();
            Log.e(TAG, "saveImage: " + e.getMessage());
            SnackbarUtil.showMessage(mImageView, "啊偶, 出错了", "再试试", new View.OnClickListener()
            {
                @Override
                public void onClick(View view)
                {
                    saveImage();
                }
            });
        }
    }

    @Override
    public void onBackPressed() {
        finish();
        overridePendingTransition(R.anim.zoom_in, R.anim.zoom_out);
    }
}
