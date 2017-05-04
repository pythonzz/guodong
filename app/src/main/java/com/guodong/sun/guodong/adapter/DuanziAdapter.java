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
 * Last modified 2017-05-04 15:01:33
 *
 * GitHub:   https://github.com/guodongAndroid
 * Website:  http://www.sunxiaoduo.com
 * Email:    sun33919135@gmail.com
 * QQ:       33919135
 */

package com.guodong.sun.guodong.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.guodong.sun.guodong.R;
import com.guodong.sun.guodong.activity.MainActivity;
import com.guodong.sun.guodong.entity.duanzi.NeiHanDuanZi;
import com.guodong.sun.guodong.glide.CircleImageTransform;
import com.guodong.sun.guodong.listener.CustomShareListener;
import com.guodong.sun.guodong.listener.OnLoadMoreLisener;
import com.guodong.sun.guodong.uitls.StringUtils;
import com.guodong.sun.guodong.widget.SpacingTextView;
import com.guodong.sun.guodong.widget.TextDrawable;
import com.umeng.socialize.ShareAction;
import com.umeng.socialize.ShareContent;
import com.umeng.socialize.bean.SHARE_MEDIA;
import com.umeng.socialize.shareboard.ShareBoardConfig;
import com.umeng.socialize.shareboard.SnsPlatform;
import com.umeng.socialize.utils.ShareBoardlistener;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Administrator on 2016/10/10.
 */

public class DuanziAdapter extends RecyclerView.Adapter<DuanziAdapter.DuanziViewHolder> implements OnLoadMoreLisener {

    private ArrayList<NeiHanDuanZi.Data> mDuanziLists = new ArrayList<>();
    private Context mContext;
    private LayoutInflater mInflater;
    private boolean isLoading;
    private RecyclerView mRecyclerView;

    public DuanziAdapter(Context context, RecyclerView recyclerView) {
        mContext = context;
        mRecyclerView = recyclerView;
        mInflater = LayoutInflater.from(context);
    }

    @Override
    public DuanziViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new DuanziViewHolder(mInflater.inflate(R.layout.fragment_duanzi_item, parent, false));
    }

    @Override
    public void onBindViewHolder(DuanziViewHolder holder, int position) {
        final NeiHanDuanZi.Data duanzi = mDuanziLists.get(position);
        final NeiHanDuanZi.Group groupBean = duanzi.getGroup();
        ArrayList<NeiHanDuanZi.Comment> comments = duanzi.getComments();
        displayTopAndBottom(holder, duanzi, groupBean);

        dispalyShenping(holder, duanzi, groupBean, comments);
    }

    private void dispalyShenping(DuanziViewHolder holder, NeiHanDuanZi.Data duanzi, NeiHanDuanZi.Group groupBean, ArrayList<NeiHanDuanZi.Comment> comments) {
        int size = comments.size();
        if (size> 0) {
            holder.item_shenping.setVisibility(View.VISIBLE);
            if (size == 1) {
                diaplayShenPingOne(holder, duanzi, groupBean, comments);
            } else if (size == 2) {
                diaplayShenPingAll(holder, duanzi, groupBean, comments);
            }
        }
    }

    private void diaplayShenPingAll(DuanziViewHolder holder, final NeiHanDuanZi.Data duanzi, final NeiHanDuanZi.Group groupBean, ArrayList<NeiHanDuanZi.Comment> comments) {
        diaplayShenPingOne(holder, duanzi, groupBean, comments);
        diaplayShenPingTwo(holder, duanzi, groupBean, comments);
    }

    private void diaplayShenPingOne(DuanziViewHolder holder, final NeiHanDuanZi.Data duanzi, final NeiHanDuanZi.Group groupBean, ArrayList<NeiHanDuanZi.Comment> comments) {
        NeiHanDuanZi.Comment comment = comments.get(0);
        holder.item_shenping_one.setVisibility(View.VISIBLE);
        holder.item_shenping_one_user_name.setText(comment.getUser_name());
        holder.item_shenping_one_digg.setText(StringUtils.getStr2W(comment.getDigg_count()));
        holder.item_shenping_one_content.setText(comment.getText());
        Glide.with(mContext).load(comment.getAvatar_url())
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .bitmapTransform(new CircleImageTransform(mContext))
                .into(holder.item_shenping_one_user_avatar);
        holder.item_shenping_one_share.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                share(duanzi, groupBean);
            }
        });
    }

    private void diaplayShenPingTwo(DuanziViewHolder holder, final NeiHanDuanZi.Data duanzi, final NeiHanDuanZi.Group groupBean, ArrayList<NeiHanDuanZi.Comment> comments) {
        NeiHanDuanZi.Comment comment = comments.get(1);
        holder.item_shenping_two.setVisibility(View.VISIBLE);
        holder.item_shenping_two_user_name.setText(comment.getUser_name());
        holder.item_shenping_two_digg.setText(StringUtils.getStr2W(comment.getDigg_count()));
        holder.item_shenping_two_content.setText(comment.getText());
        Glide.with(mContext).load(comment.getAvatar_url())
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .bitmapTransform(new CircleImageTransform(mContext))
                .into(holder.item_shenping_two_user_avatar);
        holder.item_shenping_two_share.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                share(duanzi, groupBean);
            }
        });
    }

    private void displayTopAndBottom(DuanziViewHolder holder, final NeiHanDuanZi.Data duanzi, final NeiHanDuanZi.Group groupBean) {
        if (groupBean.getUser() == null) {
            holder.user_name.setText("匿名用户");
            holder.user_avatar.setVisibility(View.GONE);
        } else {
            holder.user_name.setText(groupBean.getUser().getName());
            Glide.with(mContext)
                    .load(groupBean.getUser().getAvatar_url())
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .bitmapTransform(new CircleImageTransform(mContext))
                    .into(holder.user_avatar);
        }

        holder.item_content.setText(groupBean.getContent());
        holder.category_name.setText(groupBean.getCategory_name());
        holder.item_digg.setText(StringUtils.getStr2W(groupBean.getDigg_count()));
        holder.item_bury.setText(StringUtils.getStr2W(groupBean.getBury_count()));
        holder.item_comment.setText(StringUtils.getStr2W(groupBean.getComment_count()));
        holder.item_share_count.setText(StringUtils.getStr2W(groupBean.getShare_count()));
        holder.item_share.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                share(duanzi, groupBean);
            }
        });
    }

    private void share(final NeiHanDuanZi.Data duanzi, final NeiHanDuanZi.Group groupBean) {
        new ShareAction((MainActivity) mContext)
                .setDisplayList(SHARE_MEDIA.WEIXIN, SHARE_MEDIA.WEIXIN_CIRCLE,
                        SHARE_MEDIA.QQ, SHARE_MEDIA.QZONE)
                .setShareboardclickCallback(new ShareBoardlistener() {
                    @Override
                    public void onclick(SnsPlatform snsPlatform, SHARE_MEDIA share_media) {
                        if (duanzi != null) {
                            ShareContent content = new ShareContent();
                            content.mTitle = mContext.getResources().getString(R.string.app_name);
                            content.mText = groupBean.getContent();
                            content.mTargetUrl = groupBean.getShare_url();
                            new ShareAction((MainActivity) mContext)
                                    .setShareContent(content)
                                    .setPlatform(share_media)
                                    .setCallback(new CustomShareListener((MainActivity) mContext))
                                    .share();
                        }
                    }
                }).open(new ShareBoardConfig().setMenuItemBackgroundColor(ShareBoardConfig.BG_SHAPE_NONE));
    }

    @Override
    public int getItemCount() {
        return mDuanziLists.size();
    }

    public void addLists(ArrayList<NeiHanDuanZi.Data> list) {
        if (mDuanziLists.size() != 0 && list.size() != 0) {
            if (mDuanziLists.get(0).getGroup().getText().equals(list.get(0).getGroup().getText()))
                return;
        }

        int size = mDuanziLists.size();
        if (isLoading) {
            mDuanziLists.addAll(list);
            notifyItemRangeInserted(size, list.size());
        } else {
            mDuanziLists.addAll(0, list);
            notifyItemRangeInserted(0, list.size());
            mRecyclerView.scrollToPosition(0);
        }
//        notifyDataSetChanged();
    }

    public void clearDuanziList() {
        mDuanziLists.clear();
    }

    @Override
    public void onLoadStart() {
        if (isLoading)
            return;
        isLoading = true;
//        notifyItemInserted(getLoadingMoreItemPosition());
    }

    @Override
    public void onLoadFinish() {
        if (!isLoading) return;
//        notifyItemRemoved(getLoadingMoreItemPosition());
        isLoading = false;
    }

    private int getLoadingMoreItemPosition() {
        return isLoading ? getItemCount() - 1 : RecyclerView.NO_POSITION;
    }

    static class DuanziViewHolder extends RecyclerView.ViewHolder {
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

        @BindView(R.id.shenping_rl)
        RelativeLayout item_shenping;

        @BindView(R.id.shenping_one)
        RelativeLayout item_shenping_one;

        @BindView(R.id.shenping_one_user_avatar)
        ImageView item_shenping_one_user_avatar;

        @BindView(R.id.shenping_one_user_name)
        TextView item_shenping_one_user_name;

        @BindView(R.id.shenping_one_digg)
        TextDrawable item_shenping_one_digg;

        @BindView(R.id.shenping_one_content)
        TextView item_shenping_one_content;

        @BindView(R.id.shenping_one_share)
        ImageView item_shenping_one_share;

        @BindView(R.id.shenping_two)
        RelativeLayout item_shenping_two;

        @BindView(R.id.shenping_two_user_avatar)
        ImageView item_shenping_two_user_avatar;

        @BindView(R.id.shenping_two_user_name)
        TextView item_shenping_two_user_name;

        @BindView(R.id.shenping_two_digg)
        TextDrawable item_shenping_two_digg;

        @BindView(R.id.shenping_two_content)
        TextView item_shenping_two_content;

        @BindView(R.id.shenping_two_share)
        ImageView item_shenping_two_share;

        View view;

        public DuanziViewHolder(View itemView) {
            super(itemView);
            view = itemView;
            ButterKnife.bind(this, itemView);
        }
    }
}
