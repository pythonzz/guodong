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
 * Last modified 2017-05-04 15:03:36
 *
 * GitHub:   https://github.com/guodongAndroid
 * Website:  http://www.sunxiaoduo.com
 * Email:    sun33919135@gmail.com
 * QQ:       33919135
 */

package com.guodong.sun.guodong.entity.picture.multiimage;

import android.content.Context;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.guodong.sun.guodong.R;
import com.guodong.sun.guodong.activity.MultiGifActivity;
import com.guodong.sun.guodong.activity.MultiPictureActivity;
import com.guodong.sun.guodong.entity.picture.ContentHolder;
import com.guodong.sun.guodong.entity.picture.PictureFrameProvider;
import com.guodong.sun.guodong.entity.picture.ThumbImageList;
import com.guodong.sun.guodong.widget.NineGridImageView;
import com.guodong.sun.guodong.widget.NineGridImageViewAdapter;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MultiImageProvider extends PictureFrameProvider<MultiImage, MultiImageProvider.ViewHolder> {

    @Override
    protected ContentHolder onCreateContentViewHolder(@NonNull LayoutInflater inflater, @NonNull ViewGroup parent) {
        View view = inflater.inflate(R.layout.fragment_picture_multi_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    protected void onBindContentViewHolder(@NonNull ViewHolder holder, @NonNull MultiImage content) {

        NineGridImageViewAdapter<ThumbImageList> mAdapter = new NineGridImageViewAdapter<ThumbImageList>() {
            @Override
            protected void onDisplayImage(Context context, ImageView imageView, ThumbImageList s) {
                displayImageView(imageView, s.getUrl());
            }

            @Override
            protected ImageView generateImageView(Context context) {
                return super.generateImageView(context);
            }

            @Override
            protected void onItemImageClick(Context context, int index, List<ThumbImageList> list) {
                ArrayList<String> listUrl = new ArrayList<>();
                for (ThumbImageList thumbImageList : list) {
                    listUrl.add(thumbImageList.getUrl());
                }

                if (list.get(index).is_gif()) {
                    MultiGifActivity.startActivity(context, index, listUrl,
                            list.get(index).getWidth(), list.get(index).getHeight());
                    return;
                }

                MultiPictureActivity.startActivity(context, index,  listUrl);
            }
        };
        holder.mNineGridImageView.setAdapter(mAdapter);
        holder.mNineGridImageView.setImagesData(content.getThumb_image_list(), content.getLarge_image_list());
    }

    private void displayImageView(ImageView v, String url) {
        Glide.with(v.getContext())
                .load(url)
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .placeholder(R.drawable.ic_default_image)
                .into(v);
    }

    static class ViewHolder extends ContentHolder {

        @BindView(R.id.picture_multi_nine)
        NineGridImageView mNineGridImageView;

        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}
