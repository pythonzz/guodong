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
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.guodong.sun.guodong.R;
import com.guodong.sun.guodong.fragment.DuanziFragment;
import com.guodong.sun.guodong.fragment.MeiziFragment;
import com.guodong.sun.guodong.fragment.QiubaiFragment;
import com.guodong.sun.guodong.fragment.PictureFragment;
import com.guodong.sun.guodong.fragment.VideoFragment;
import com.guodong.sun.guodong.fragment.ZhihuFragment;

/**
 * Created by Administrator on 2016/10/9.
 */

public class HomePagerAdapter extends FragmentPagerAdapter
{
    private final String[] TITLES;
    private Fragment[] fragments;

    public HomePagerAdapter(FragmentManager fm, Context context)
    {
        super(fm);
        TITLES = context.getApplicationContext().getResources().getStringArray(R.array.titles);
        fragments = new Fragment[TITLES.length];
    }

    @Override
    public Fragment getItem(int position)
    {
        if (fragments[position] == null)
        {
            switch (position)
            {
                case 0:
                    fragments[position] = ZhihuFragment.newInstance();
                    break;
                case 1:
                    fragments[position] = PictureFragment.newInstance();
                    break;
                case 2:
                    fragments[position] = MeiziFragment.newInstance();
                    break;
                case 3:
                    fragments[position] = VideoFragment.newInstance();
                    break;
                case 4:
                    fragments[position] = QiubaiFragment.newInstance();
                    break;
                case 5:
                    fragments[position] = DuanziFragment.newInstance();
                    break;
                default:
                    break;
            }
        }
        return fragments[position];
    }

    @Override
    public int getCount()
    {
        return TITLES.length;
    }

    @Override
    public CharSequence getPageTitle(int position)
    {
        return TITLES[position];
    }
}
