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
 * Last modified 2017-05-04 19:02:47
 *
 * GitHub:   https://github.com/guodongAndroid
 * Website:  http://www.sunxiaoduo.com
 * Email:    sun33919135@gmail.com
 * QQ:       33919135
 */

package com.github.guodongandroid.recyclerview.adapter.singletype;

import android.content.Context;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;


import com.github.guodongandroid.recyclerview.adapter.holder.RecyclerViewHolder;

import java.util.ArrayList;
import java.util.List;

public abstract class SingleTypeAdapter<T> extends RecyclerView.Adapter<RecyclerViewHolder> {

    protected Context mContext;
    private LayoutInflater mInflater;
    private List<T> mDatas = new ArrayList<>();
    private int mLayoutId;

    public SingleTypeAdapter(@NonNull Context context, @LayoutRes int layoutId) {
        mInflater = LayoutInflater.from(context);
        mContext = context;
        mLayoutId = layoutId;
    }

    @Override
    public RecyclerViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View rootView = null;
        try {
            rootView = mInflater.inflate(mLayoutId, parent, false);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return new RecyclerViewHolder(rootView);
    }

    @Override
    public void onBindViewHolder(RecyclerViewHolder holder, int position) {
        convert(position, holder, mDatas.get(position));
    }

    @Override
    public void onBindViewHolder(RecyclerViewHolder holder, int position, List<Object> payloads) {
        if (payloads.isEmpty()) {
            onBindViewHolder(holder, position);
        } else {

        }
    }

    /**
     * 在此处处理数据
     *
     * @param position 坐标
     * @param holder   view holder
     * @param bean     数据
     */
    public abstract void convert(int position, RecyclerViewHolder holder, T bean);

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
    protected void convert( RecyclerViewHolder holder, int position, T bean, List<Object> payloads) {

    }

    @Override
    public int getItemCount() {
        return mDatas.size();
    }

    public void addDatas(List<T> datas) {
        this.mDatas.addAll(datas);
        notifyDataSetChanged();
    }

    public List<T> getDatas() {
        return mDatas;
    }

    public void clearDatas() {
        this.mDatas.clear();
        notifyDataSetChanged();
    }
}
