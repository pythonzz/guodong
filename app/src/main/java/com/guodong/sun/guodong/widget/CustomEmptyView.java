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
 * Last modified 2017-05-04 15:07:09
 *
 * GitHub:   https://github.com/guodongAndroid
 * Website:  http://www.sunxiaoduo.com
 * Email:    sun33919135@gmail.com
 * QQ:       33919135
 */

package com.guodong.sun.guodong.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.guodong.sun.guodong.R;


/**
 * 自定义EmptyView
 */
public class CustomEmptyView extends FrameLayout
{

    private ImageView mEmptyImg;

    private TextView mEmptyText;

    private Button mReloadBtn;

    private ReloadOnClickListener OnReloadOnClickListener;

    public CustomEmptyView(Context context, AttributeSet attrs, int defStyleAttr)
    {

        super(context, attrs, defStyleAttr);
        init();
    }

    public CustomEmptyView(Context context)
    {

        this(context, null);
    }

    public CustomEmptyView(Context context, AttributeSet attrs)
    {

        this(context, attrs, 0);
    }


    public void init()
    {

        View view = LayoutInflater.from(getContext()).inflate(R.layout.layout_empty, this);
        mEmptyImg = (ImageView) view.findViewById(R.id.empty_img);
        mEmptyText = (TextView) view.findViewById(R.id.empty_text);
        mReloadBtn = (Button) view.findViewById(R.id.btn_reload);
    }

    public void setEmptyImage(int imgRes)
    {

        mEmptyImg.setImageResource(imgRes);
    }

    public void setEmptyText(String text)
    {

        mEmptyText.setText(text);
    }

    public void hideReloadButton()
    {

        mReloadBtn.setVisibility(GONE);
    }


    public void reload(ReloadOnClickListener onReloadOnClickListener)
    {

        this.OnReloadOnClickListener = onReloadOnClickListener;

        mReloadBtn.setOnClickListener(new OnClickListener()
        {

            @Override
            public void onClick(View v)
            {

                if (OnReloadOnClickListener != null)
                {
                    OnReloadOnClickListener.reloadClick();
                }
            }
        });
    }


    public interface ReloadOnClickListener
    {

        void reloadClick();
    }
}
