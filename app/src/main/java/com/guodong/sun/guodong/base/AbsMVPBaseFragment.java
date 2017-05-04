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
 * Last modified 2017-05-04 15:02:14
 *
 * GitHub:   https://github.com/guodongAndroid
 * Website:  http://www.sunxiaoduo.com
 * Email:    sun33919135@gmail.com
 * QQ:       33919135
 */

package com.guodong.sun.guodong.base;

import android.app.Activity;

/**
 * Created by Administrator on 2016/10/9.
 */

public abstract class AbsMVPBaseFragment<V, T extends AbsBasePresenter<V>> extends AbsBaseFragment
{
    protected T mPresenter;

    @Override
    public void onAttach(Activity activity)
    {
        super.onAttach(activity);
        mPresenter = createPresenter();
        mPresenter.attachView((V) this);
    }

    @Override
    public void onDetach()
    {
        super.onDetach();
        mPresenter.detachView();
    }

    protected abstract T createPresenter();
}
