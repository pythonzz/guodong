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
 * Last modified 2017-05-04 15:04:47
 *
 * GitHub:   https://github.com/guodongAndroid
 * Website:  http://www.sunxiaoduo.com
 * Email:    sun33919135@gmail.com
 * QQ:       33919135
 */

package com.guodong.sun.guodong.fragment;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.SizeReadyCallback;
import com.bumptech.glide.request.target.Target;
import com.guodong.sun.guodong.R;
import com.guodong.sun.guodong.activity.MultiGifActivity;
import com.guodong.sun.guodong.activity.MultiPictureActivity;
import com.guodong.sun.guodong.activity.MyApplication;
import com.guodong.sun.guodong.glide.ProgressTarget;
import com.guodong.sun.guodong.uitls.AlxGifHelper;
import com.guodong.sun.guodong.uitls.Once;
import com.guodong.sun.guodong.uitls.SnackbarUtil;
import com.shizhefei.view.largeimage.LargeImageView;
import com.shizhefei.view.largeimage.factory.FileBitmapDecoderFactory;
import com.squareup.leakcanary.RefWatcher;
import com.trello.rxlifecycle.components.support.RxFragment;

import java.io.File;
import java.io.FileOutputStream;
import java.io.ObjectOutputStream;
import java.util.Date;

import butterknife.BindView;
import butterknife.ButterKnife;
import pl.droidsonroids.gif.GifImageView;

/**
 * Created by Administrator on 2016/12/17.
 */

/**
 * 多图为GIF查看的Fragment
 */
public class MultiGifFragment extends RxFragment {

    private static final String TAG = MultiGifFragment.class.getSimpleName();
    private String mImageUrl;
    private int width;
    private int height;

    @BindView(R.id.gif_photo_view)
    GifImageView mGifImageView;

    @BindView(R.id.tv_progress)
    TextView mTextView;

    @BindView(R.id.progress_wheel)
    ProgressBar mProgressBar;

    public static MultiGifFragment newInstance(String imageUrl, int width, int height) {
        MultiGifFragment f = new MultiGifFragment();
        Bundle args = new Bundle();
        args.putString("url", imageUrl);
        args.putInt("width", width);
        args.putInt("height", height);
        f.setArguments(args);
        return f;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mImageUrl = getArguments() != null ? getArguments().getString("url") : null;
        width = getArguments() != null ? getArguments().getInt("width", 0) : 0;
        height = getArguments() != null ? getArguments().getInt("height", 0) : 0;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_multi_gif, container, false);
        ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {

        RelativeLayout.LayoutParams lp = (RelativeLayout.LayoutParams) mGifImageView.getLayoutParams();
        lp.height = MyApplication.ScreenWidth * height / width;
        mGifImageView.setLayoutParams(lp);

        final String path = AlxGifHelper.displayImage(mImageUrl, mGifImageView, mProgressBar, mTextView);

        mGifImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((MultiGifActivity) getActivity()).onBackPressed();
            }
        });

        mGifImageView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                createDialog();
                return true;
            }
        });

    }

    private void createDialog() {
        new AlertDialog.Builder(getContext()).setMessage("保存到手机?").setNegativeButton(android.R.string.cancel, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
            }
        }).setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                AlxGifHelper.saveGIF(mGifImageView, mImageUrl, mProgressBar);
                dialogInterface.dismiss();
            }
        }).show();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        RefWatcher watcher = MyApplication.getRefWatcher(getActivity());
        watcher.watch(this);
    }
}
