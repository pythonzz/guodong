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

package com.guodong.sun.guodong.entity.picture;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.guodong.sun.guodong.R;
import com.guodong.sun.guodong.activity.MainActivity;
import com.guodong.sun.guodong.glide.CircleImageTransform;
import com.guodong.sun.guodong.listener.CustomShareListener;
import com.guodong.sun.guodong.uitls.StringUtils;
import com.guodong.sun.guodong.widget.SpacingTextView;
import com.umeng.socialize.ShareAction;
import com.umeng.socialize.ShareContent;
import com.umeng.socialize.bean.SHARE_MEDIA;
import com.umeng.socialize.shareboard.ShareBoardConfig;
import com.umeng.socialize.shareboard.SnsPlatform;
import com.umeng.socialize.utils.ShareBoardlistener;

import butterknife.BindView;
import butterknife.ButterKnife;
import me.drakeet.multitype.ItemViewProvider;

public abstract class PictureFrameProvider
        <Content extends PictureContent, SubViewHolder extends ContentHolder>
        extends ItemViewProvider<PictureBean.DataBeanX.DataBean, PictureFrameProvider.FrameHolder> {

    protected abstract ContentHolder onCreateContentViewHolder(
            @NonNull LayoutInflater inflater, @NonNull ViewGroup parent);

    protected abstract void onBindContentViewHolder(
            @NonNull SubViewHolder holder, @NonNull Content content);

    @NonNull @Override
    protected FrameHolder onCreateViewHolder(
            @NonNull LayoutInflater inflater, @NonNull ViewGroup parent) {
        View root = inflater.inflate(R.layout.fragment_picture_item, parent, false);
        ContentHolder subViewHolder = onCreateContentViewHolder(inflater, parent);
        return new FrameHolder(root, subViewHolder);
    }


    @SuppressWarnings("unchecked")
    @Override protected void onBindViewHolder(@NonNull final FrameHolder holder, @NonNull final PictureBean.DataBeanX.DataBean bean) {

        holder.user_name.setText(bean.getGroup().getUser().getName());
        if (TextUtils.isEmpty(bean.getGroup().getContent()))
            setViewGone(holder.item_content);
        else
            holder.item_content.setText(bean.getGroup().getContent());
        holder.category_name.setText(bean.getGroup().getCategory_name());
        holder.item_digg.setText(StringUtils.getStr2W(bean.getGroup().getDigg_count()));
        holder.item_bury.setText(StringUtils.getStr2W(bean.getGroup().getBury_count()));
        holder.item_comment.setText(StringUtils.getStr2W(bean.getGroup().getComment_count()));
        holder.item_share_count.setText(StringUtils.getStr2W(bean.getGroup().getShare_count()));
        holder.item_share.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new ShareAction((MainActivity)holder.itemView.getContext())
                        .setDisplayList(SHARE_MEDIA.WEIXIN, SHARE_MEDIA.WEIXIN_CIRCLE,
                                SHARE_MEDIA.QQ, SHARE_MEDIA.QZONE)
                        .setShareboardclickCallback(new ShareBoardlistener() {
                            @Override
                            public void onclick(SnsPlatform snsPlatform, SHARE_MEDIA share_media) {
                                if (bean != null) {
                                    ShareContent content = new ShareContent();
                                    content.mTitle = holder.itemView.getContext().getResources().getString(R.string.app_name);
                                    content.mText = bean.getGroup().getContent();
                                    content.mTargetUrl = bean.getGroup().getShare_url();
                                    new ShareAction((MainActivity)holder.itemView.getContext())
                                            .setShareContent(content)
                                            .setPlatform(share_media)
                                            .setCallback(new CustomShareListener((MainActivity)holder.itemView.getContext()))
                                            .share();
                                }
                            }
                        }).open(new ShareBoardConfig().setMenuItemBackgroundColor(ShareBoardConfig.BG_SHAPE_NONE));
            }
        });
        Glide.with(holder.itemView.getContext())
                .load(bean.getGroup().getUser().getAvatar_url())
                .bitmapTransform(new CircleImageTransform(holder.itemView.getContext()))
                .into(holder.user_avatar);

        final PictureContent pictureContent = bean.getGroup();
        onBindContentViewHolder((SubViewHolder) holder.subViewHolder, (Content) pictureContent);
    }

    private void setViewGone(View view) {
        view.setVisibility(View.GONE);
    }

    static class FrameHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.picture_item_user_avatar)
        ImageView user_avatar;

        @BindView(R.id.picture_item_user_name)
        TextView user_name;

        @BindView(R.id.picture_item_content)
        SpacingTextView item_content;

        @BindView(R.id.picture_item_category_name)
        TextView category_name;

        @BindView(R.id.picture_item_digg)
        TextView item_digg;

        @BindView(R.id.picture_item_share_count)
        TextView item_share_count;

        @BindView(R.id.picture_item_share)
        RelativeLayout item_share;

        @BindView(R.id.picture_item_bury)
        TextView item_bury;

        @BindView(R.id.picture_item_comment)
        TextView item_comment;

        @BindView(R.id.picture_container)
        FrameLayout container;

        private ContentHolder subViewHolder;

        public FrameHolder(View itemView, final ContentHolder subViewHolder) {
            super(itemView);
            ButterKnife.bind(this, itemView);
            container.addView(subViewHolder.itemView);
            this.subViewHolder = subViewHolder;
        }
    }

}
