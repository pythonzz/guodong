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
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.guodong.sun.guodong.R;
import com.guodong.sun.guodong.activity.MainActivity;
import com.guodong.sun.guodong.entity.duanzi.NeiHanVideo;
import com.guodong.sun.guodong.glide.CircleImageTransform;
import com.guodong.sun.guodong.listener.CustomShareListener;
import com.guodong.sun.guodong.listener.OnLoadMoreLisener;
import com.guodong.sun.guodong.uitls.AnimatorUtils;
import com.guodong.sun.guodong.uitls.StringUtils;
import com.guodong.sun.guodong.widget.SpacingTextView;
import com.umeng.socialize.ShareAction;
import com.umeng.socialize.ShareContent;
import com.umeng.socialize.bean.SHARE_MEDIA;
import com.umeng.socialize.shareboard.ShareBoardConfig;
import com.umeng.socialize.shareboard.SnsPlatform;
import com.umeng.socialize.utils.ShareBoardlistener;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import fm.jiecao.jcvideoplayer_lib.JCVideoPlayer;
import fm.jiecao.jcvideoplayer_lib.JCVideoPlayerStandard;

/**
 * Created by Administrator on 2016/10/17.
 */

public class VideoAdapter extends RecyclerView.Adapter<VideoAdapter.VideoViewHolder> implements OnLoadMoreLisener
{
    private ArrayList<NeiHanVideo.DataBean> mLists = new ArrayList<>();
    private Context mContext;
    private LayoutInflater mInflater;
    private boolean isLoading;
    private static String coverurl="http://p2.pstatp.com/large/";
    private RecyclerView mRecyclerView;

    public VideoAdapter(Context context, RecyclerView recyclerView)
    {
        mContext = context;
        mRecyclerView = recyclerView;
        mInflater = LayoutInflater.from(context);
    }

    @Override
    public VideoViewHolder onCreateViewHolder(ViewGroup parent, int viewType)
    {
        return new VideoViewHolder(mInflater.inflate(R.layout.fragment_video_item, parent, false));
    }

    @Override
    public void onBindViewHolder(VideoViewHolder holder, int position)
    {

        AnimatorUtils.startScaleInAnimator(holder.mCardView);

        final NeiHanVideo.DataBean bean = mLists.get(position);
        final NeiHanVideo.DataBean.GroupBean groupBean = bean.getGroup();
        boolean isSetUp = false;
        if(bean.getGroup() != null)
            isSetUp = holder.mVideoPlayerStandard.setUp(groupBean.getMp4_url(),
                JCVideoPlayer.SCREEN_LAYOUT_LIST, "");

        if (isSetUp)
            Glide.with(mContext)
                    .load(coverurl + groupBean.getCover_image_uri())
                    .centerCrop()
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .into(holder.mVideoPlayerStandard.thumbImageView);

        holder.user_name.setText(groupBean.getUser().getName());
        if (TextUtils.isEmpty(groupBean.getContent()))
            holder.item_content.setVisibility(View.GONE);
        else
            holder.item_content.setText(groupBean.getContent());
        holder.category_name.setText(groupBean.getCategory_name());
        holder.item_digg.setText(StringUtils.getStr2W(groupBean.getDigg_count()));
        holder.item_bury.setText(StringUtils.getStr2W(groupBean.getBury_count()));
        holder.item_comment.setText(StringUtils.getStr2W(groupBean.getComment_count()));
        holder.item_share_count.setText(StringUtils.getStr2W(groupBean.getShare_count()));
        holder.item_share.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new ShareAction((MainActivity)mContext)
                        .setDisplayList(SHARE_MEDIA.WEIXIN, SHARE_MEDIA.WEIXIN_CIRCLE,
                                SHARE_MEDIA.QQ, SHARE_MEDIA.QZONE)
                        .setShareboardclickCallback(new ShareBoardlistener() {
                            @Override
                            public void onclick(SnsPlatform snsPlatform, SHARE_MEDIA share_media) {
                                if (bean != null) {
                                    ShareContent content = new ShareContent();
                                    content.mTitle = mContext.getResources().getString(R.string.app_name);
                                    content.mText = groupBean.getContent();
                                    content.mTargetUrl = groupBean.getShare_url();
                                    new ShareAction((MainActivity)mContext)
                                            .setShareContent(content)
                                            .setPlatform(share_media)
                                            .setCallback(new CustomShareListener((MainActivity)mContext))
                                            .share();
                                }
                            }
                        }).open(new ShareBoardConfig().setMenuItemBackgroundColor(ShareBoardConfig.BG_SHAPE_NONE));
            }
        });
        Glide.with(mContext)
                .load(groupBean.getUser().getAvatar_url())
                .bitmapTransform(new CircleImageTransform(mContext))
                .into(holder.user_avatar);
    }

    @Override
    public int getItemCount()
    {
        return mLists.size();
    }

    public void addLists(ArrayList<NeiHanVideo.DataBean> list)
    {
        if (mLists.size() != 0 && list.size() != 0)
        {
            if (mLists.get(0).getGroup().getMp4_url().equals(list.get(0).getGroup().getMp4_url()))
                return;
        }

        int size = mLists.size();
        if (isLoading)
        {
            mLists.addAll(list);
            notifyItemRangeInserted(size, list.size());
        }
        else
        {
            mLists.addAll(0, list);
            notifyItemRangeInserted(0, list.size());
            mRecyclerView.scrollToPosition(0);
        }
//        notifyDataSetChanged();
    }

    public void clearDuanziList()
    {
        mLists.clear();
    }

    @Override
    public void onLoadStart()
    {
        if (isLoading)
            return;
        isLoading = true;
//        notifyItemInserted(getLoadingMoreItemPosition());
    }

    @Override
    public void onLoadFinish()
    {
        if (!isLoading) return;
//        notifyItemRemoved(getLoadingMoreItemPosition());
        isLoading = false;
    }

    private int getLoadingMoreItemPosition()
    {
        return isLoading ? getItemCount() - 1 : RecyclerView.NO_POSITION;
    }

    static class VideoViewHolder extends RecyclerView.ViewHolder
    {

        @BindView(R.id.video_player)
        JCVideoPlayerStandard mVideoPlayerStandard;

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

        View mCardView;

        public VideoViewHolder(View itemView)
        {
            super(itemView);
            mCardView = itemView;
            ButterKnife.bind(this, itemView);
        }
    }
}
