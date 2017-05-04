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
 * Last modified 2017-05-04 19:03:36
 *
 * GitHub:   https://github.com/guodongAndroid
 * Website:  http://www.sunxiaoduo.com
 * Email:    sun33919135@gmail.com
 * QQ:       33919135
 */

package com.github.guodongandroid.recyclerview.adapter.typepool;

import android.support.annotation.NonNull;
import android.util.Log;

import com.github.guodongandroid.recyclerview.adapter.base.BaseViewProvider;

import java.util.ArrayList;
import java.util.List;

/**
 * 类型池
 */
public class MultiTypePool implements TypePool {
    private final String TAG = MultiTypePool.class.getSimpleName();

    private final List<Class<?>> mContents;
    private final List<BaseViewProvider> mProviders;

    public MultiTypePool() {
        mContents = new ArrayList<>();
        mProviders = new ArrayList<>();
    }

    /**
     * 将数据类型和 provider 关联起来
     *
     * @param clazz    数据类型
     * @param provider provider
     */
    @Override
    public void register(@NonNull Class<?> clazz, @NonNull BaseViewProvider provider) {
        if (!mContents.contains(clazz)) {
            mContents.add(clazz);
            mProviders.add(provider);
        } else {
            int index = mContents.indexOf(clazz);
            mProviders.set(index, provider);
            Log.w(TAG, "You have registered the " + clazz.getSimpleName() + " type. " +
                    "It will override the original binder.");
        }
    }

    @Override
    public int indexOf(@NonNull final Class<?> clazz) throws ProviderNotFoundException {
        int index = mContents.indexOf(clazz);
        if (index >= 0) {
            return index;
        }
        for (int i = 0; i < mContents.size(); i++) {
            if (mContents.get(i).isAssignableFrom(clazz)) {
                return i;
            }
        }
        throw new ProviderNotFoundException(clazz);
    }

    @Override
    public BaseViewProvider getProviderByIndex(int index) {
        return mProviders.get(index);
    }

    @Override
    public <T extends BaseViewProvider> T getProviderByClass(@NonNull final Class<?> clazz) {
        return (T) getProviderByIndex(indexOf(clazz));
    }

    public List<Class<?>> getContents() {
        return mContents;
    }

    public List<BaseViewProvider> getProviders() {
        return mProviders;
    }


    private class ProviderNotFoundException extends RuntimeException {

        ProviderNotFoundException(@NonNull Class<?> clazz) {
            super("Do you have registered the provider for {className}.class in the adapter/pool?"
                    .replace("{className}", clazz.getSimpleName()));
        }
    }
}
