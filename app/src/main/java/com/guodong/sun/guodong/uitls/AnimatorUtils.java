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
 * Last modified 2017-05-04 15:05:52
 *
 * GitHub:   https://github.com/guodongAndroid
 * Website:  http://www.sunxiaoduo.com
 * Email:    sun33919135@gmail.com
 * QQ:       33919135
 */

package com.guodong.sun.guodong.uitls;

import android.view.View;

import com.nineoldandroids.animation.Animator;
import com.nineoldandroids.animation.AnimatorSet;
import com.nineoldandroids.animation.ObjectAnimator;

/**
 * Created by Administrator on 2016/11/18.
 */

public class AnimatorUtils
{

    private static final long DEFAULT_DURATION = 300;
    private static final float DEFAULT_SCALE_FROM = .5f;

    public static void startSlideInLeftAnimator(View view)
    {
        AnimatorSet set = new AnimatorSet();
        Animator translationX = ObjectAnimator // 平移
                .ofFloat(view, "translationX", -view.getRootView().getWidth(), 0);
//        Animator alpha = ObjectAnimator // 透明度
//                .ofFloat(view, "alpha", 0.4f, 1.0f);
        set.playTogether(translationX);
        set.setDuration(DEFAULT_DURATION).start();
    }

    public static void startSlideInRightAnimator(View view)
    {
        AnimatorSet set = new AnimatorSet();
        Animator translationX = ObjectAnimator // 平移
                .ofFloat(view, "translationX", view.getRootView().getWidth(), 0);
        //        Animator alpha = ObjectAnimator // 透明度
        //                .ofFloat(view, "alpha", 0.4f, 1.0f);
        set.playTogether(translationX);
        set.setDuration(DEFAULT_DURATION).start();
    }

    public static void startScaleInAnimator(View view)
    {
        AnimatorSet set = new AnimatorSet();
        Animator scaleX = ObjectAnimator // 缩放X
                .ofFloat(view, "scaleX", DEFAULT_SCALE_FROM, 1.0f);
        Animator scaleY = ObjectAnimator // 缩放Y
                .ofFloat(view, "scaleY", DEFAULT_SCALE_FROM, 1.0f);
        set.playTogether(scaleX, scaleY);
        set.setDuration(DEFAULT_DURATION).start();
    }
}
