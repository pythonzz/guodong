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
