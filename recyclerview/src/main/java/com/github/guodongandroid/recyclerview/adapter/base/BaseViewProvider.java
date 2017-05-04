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
 * Last modified 2017-05-04 19:04:12
 *
 * GitHub:   https://github.com/guodongAndroid
 * Website:  http://www.sunxiaoduo.com
 * Email:    sun33919135@gmail.com
 * QQ:       33919135
 */

package com.github.guodongandroid.recyclerview.adapter.base;

import android.content.Context;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;


import com.github.guodongandroid.recyclerview.adapter.holder.RecyclerViewHolder;

import java.util.List;

/**
 * ItemView 的管理者
 */
public abstract class BaseViewProvider<T> {
    private LayoutInflater mInflater;
    private int mLayoutId;
    protected Context mContext;

    public BaseViewProvider(@NonNull Context context, @NonNull @LayoutRes int layout_id) {
        mInflater = LayoutInflater.from(context);
        mLayoutId = layout_id;
        mContext = context;
    }

    public RecyclerViewHolder onCreateViewHolder(ViewGroup parent) {
        View view = mInflater.inflate(mLayoutId, parent, false);
        RecyclerViewHolder viewHolder = new RecyclerViewHolder(view);
        onViewHolderIsCreated(viewHolder);
        return viewHolder;
    }

    /**
     * 当 ViewHolder 创建完成时调用
     *
     * @param holder ViewHolder
     */
    public void onViewHolderIsCreated(RecyclerViewHolder holder) {

    }

    /**
     * 在绑定数据时调用，需要用户自己处理
     *
     * @param holder   ViewHolder
     * @param position 坐标
     * @param bean     数据
     */
    public void onBindView(RecyclerViewHolder holder, int position, T bean) {

    }

    /**
     * 在绑定数据时调用，需要用户自己处理
     * <p>
     * 使用{@link android.support.v7.widget.RecyclerView.Adapter#notifyItemChanged(int, Object)}更新
     * 单个ItemView并且{@code payloads}不为empty
     * <p> 用法
     * http://blog.chengyunfeng.com/?p=1007
     *
     * @param holder   ViewHolder
     * @param position 坐标
     * @param bean     数据
     * @param payloads A non-null list of merged payloads. Can be empty list if requires full
     *                 update.
     */
    public void onBindView(RecyclerViewHolder holder, int position, T bean, List<Object> payloads) {

    }
}
