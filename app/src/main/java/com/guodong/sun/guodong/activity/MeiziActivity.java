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
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.SimpleTarget;
import com.guodong.sun.guodong.R;
import com.guodong.sun.guodong.uitls.Once;
import com.guodong.sun.guodong.uitls.SnackbarUtil;
import com.guodong.sun.guodong.widget.ZoomImageView;
import com.squareup.leakcanary.RefWatcher;
import com.trello.rxlifecycle.components.support.RxAppCompatActivity;

import java.io.File;
import java.io.FileOutputStream;
import java.util.Date;


/**
 * Created by Administrator on 2016/10/11.
 */

public class MeiziActivity extends RxAppCompatActivity {
    private static final String TAG = "MeiziActivity";

    public static final String EXTRA_IMAGE_URL = "image";
    public static final String TRANSIT_LOCATIONX = "locationX";
    public static final String TRANSIT_LOCATIONY = "locationY";
    public static final String TRANSIT_WIDTH = "width";
    public static final String TRANSIT_HEIGHT = "hieght";

    private Bitmap mBitmap; // 保存图片时使用
    private String mImageUrl;
    private ZoomImageView mPicture;
    private int mLocationX;
    private int mLocationY;
    private int mWidth;
    private int mHeight;

    public static Intent newIntent(Context context, String url, int locationX, int locationY, int width, int height) {
        Intent intent = new Intent(context, MeiziActivity.class);
        intent.putExtra(EXTRA_IMAGE_URL, url);
        intent.putExtra(TRANSIT_LOCATIONX, locationX);
        intent.putExtra(TRANSIT_LOCATIONY, locationY);
        intent.putExtra(TRANSIT_WIDTH, width);
        intent.putExtra(TRANSIT_HEIGHT, height);
        return intent;
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mImageUrl = getIntent().getStringExtra(EXTRA_IMAGE_URL);
        mLocationX = getIntent().getIntExtra(TRANSIT_LOCATIONX, 0);
        mLocationY = getIntent().getIntExtra(TRANSIT_LOCATIONY, 0);
        mWidth = getIntent().getIntExtra(TRANSIT_WIDTH, 0);
        mHeight = getIntent().getIntExtra(TRANSIT_HEIGHT, 0);

        mPicture = new ZoomImageView(this);
        mPicture.setOriginalInfo(mWidth, mHeight, mLocationX, mLocationY);
        mPicture.transformIn();
        mPicture.setLayoutParams(new ViewGroup.LayoutParams(-1, -1));
        setContentView(mPicture);
        setupPhotoAttacher();
        Glide.with(this)
                .load(mImageUrl)
                .asBitmap()
                .fitCenter()
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .into(new SimpleTarget<Bitmap>() {
                    @Override
                    public void onResourceReady(Bitmap resource, GlideAnimation<? super Bitmap> glideAnimation) {
                        mBitmap = resource;
                        mPicture.setImageBitmap(resource);
                    }
                });
        new Once(this).show("提示", new Once.OnceCallback() {
            @Override
            public void onOnce() {
                SnackbarUtil.showMessage(mPicture, "单击图片返回, 双击图片放大, 长按图片保存");
            }
        });
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

    private void setupPhotoAttacher() {
        mPicture.setOnClickListener(new ZoomImageView.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
//                Toast.makeText(PictureActivity.this, "单击", Toast.LENGTH_SHORT).show();
            }
        });

        mPicture.setOnLongClickListener(new ZoomImageView.OnLongClickListener() {
            @Override
            public void onLongClick(View v) {
                createDialog();
            }
        });
    }

    private void createDialog() {
        new AlertDialog.Builder(MeiziActivity.this).setMessage("保存到手机?").setNegativeButton(android.R.string.cancel, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
            }
        }).setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                saveImage();
                dialogInterface.dismiss();
            }
        }).show();
    }

    private void saveImage() {
        File externalStorageDirectory = Environment.getExternalStorageDirectory();
        File directory = new File(externalStorageDirectory, getString(R.string.app_name));
        if (!directory.exists())
            directory.mkdir();
        try {
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
            SnackbarUtil.showMessage(mPicture, "已保存到" + file.getAbsolutePath());
        } catch (Exception e) {
            e.printStackTrace();
            Log.e(TAG, "saveImage: " + e.getMessage());
            SnackbarUtil.showMessage(mPicture, "啊偶, 出错了", "再试试", new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    saveImage();
                }
            });
        }
    }

    @Override
    public void onBackPressed() {
        mPicture.setOnTransformListener(new ZoomImageView.TransformListener() {
            @Override
            public void onTransformComplete(int mode) {
                if (mode == 2) {
                    finish();
                    overridePendingTransition(0, 0);
                }
            }
        });
        mPicture.transformOut();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        RefWatcher watcher = MyApplication.getRefWatcher(this);
        watcher.watch(this);
        Log.e(TAG, "onDestroy: 妹子退出了");

        if (mPicture != null) {
            mPicture = null;
        }

        if (mBitmap != null) {
            mBitmap = null;
        }
    }
}
