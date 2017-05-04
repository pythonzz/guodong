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
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.guodong.sun.guodong.R;
import com.guodong.sun.guodong.activity.MainActivity;
import com.guodong.sun.guodong.activity.MeiziActivity;
import com.guodong.sun.guodong.entity.meizi.Meizi;
import com.guodong.sun.guodong.listener.OnLoadMoreLisener;
import com.guodong.sun.guodong.widget.SquareCenterImageView;
import com.nineoldandroids.animation.AnimatorInflater;
import com.nineoldandroids.animation.AnimatorSet;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Administrator on 2016/10/10.
 */

public class MeiziAdapter extends RecyclerView.Adapter<MeiziAdapter.MeiziViewHolder> implements OnLoadMoreLisener
{
    private ArrayList<Meizi> mMeiziLists = new ArrayList<>();
    private Context mContext;
    private LayoutInflater mInflater;

    private boolean isLoading;

    public MeiziAdapter(Context context)
    {
        mContext = context;
        mInflater = LayoutInflater.from(context);
    }

    @Override
    public MeiziViewHolder onCreateViewHolder(ViewGroup parent, int viewType)
    {
        return new MeiziViewHolder(mInflater.inflate(R.layout.fragment_meizi_item, parent, false));
    }

    @Override
    public void onBindViewHolder(final MeiziViewHolder holder, final int position)
    {
        holder.imageView.setPivotX(0.0f);
        holder.imageView.setPivotY(0.0f);

        Glide.with(mContext)
                .load(mMeiziLists.get(position).url)
                .dontAnimate()
                .centerCrop()
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .placeholder(R.drawable.ic_default_image)
                .into(holder.imageView);

        AnimatorSet set = (AnimatorSet) AnimatorInflater.loadAnimator(mContext, R.animator.zoom_in);
        set.setTarget(holder.imageView);
        set.start();

        holder.imageView.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                int[] location = new int[2];
                view.getLocationOnScreen(location);
                Intent intent = MeiziActivity.newIntent(mContext, mMeiziLists.get(position).url,
                        location[0], location[1] + 50, view.getWidth(), view.getHeight());
                mContext.startActivity(intent);
                ((MainActivity)mContext).overridePendingTransition(0, 0);
            }
        });
    }

    public void addLists(ArrayList<Meizi> list)
    {
        if (list.size() != 0 && mMeiziLists.size() != 0)
        {
            if (list.get(0).url.equals(mMeiziLists.get(0).url))
                return;
        }

        int size = mMeiziLists.size();
        if (isLoading)
        {
            mMeiziLists.addAll(list);
            notifyItemRangeInserted(size, list.size());
        }
        else
        {
            mMeiziLists.addAll(0, list);
            notifyItemRangeInserted(0, list.size());
        }
//        notifyDataSetChanged();
    }

    public void clearMeiziList()
    {
        mMeiziLists.clear();
    }

    @Override
    public int getItemCount()
    {
        return mMeiziLists.size();
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

    static class MeiziViewHolder extends RecyclerView.ViewHolder
    {
        @BindView(R.id.meizi_iv)
        SquareCenterImageView imageView;

        View card;

        public MeiziViewHolder(View itemView)
        {
            super(itemView);
            card = itemView;
            ButterKnife.bind(this, itemView);
        }
    }
}