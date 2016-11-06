package com.guodong.sun.guodong.base;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.trello.rxlifecycle.components.support.RxFragment;

import butterknife.ButterKnife;

/**
 * Created by Administrator on 2016/10/9.
 */

public abstract class AbsBaseFragment extends RxFragment
{
    private View parentView;

    private FragmentActivity activity;

    private LayoutInflater inflater;

    // 标志位 标志已经初始化完成。
    protected boolean isPrepared;

    //标志位 fragment是否可见
    protected boolean isVisible;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle state)
    {
        this.inflater = inflater;
        parentView = inflater.inflate(getLayoutResId(), container, false);
        activity = getSupportActivity();
        ButterKnife.bind(this, parentView);
        return parentView;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState)
    {
        super.onViewCreated(view, savedInstanceState);
        finishCreateView(savedInstanceState);
    }

    @Override
    public void onAttach(Activity activity)
    {
        super.onAttach(activity);
        this.activity = (FragmentActivity) activity;
    }

    @Override
    public void onDetach()
    {
        super.onDetach();
        this.activity = null;
    }

    public FragmentActivity getSupportActivity()
    {
        return super.getActivity();
    }

    public android.app.ActionBar getSupportActionBar()
    {
        return getSupportActivity().getActionBar();
    }

    protected LayoutInflater getLayoutInflater()
    {
        return inflater;
    }

    protected View getParentView()
    {
        return parentView;
    }


    /**
     * Fragment数据的懒加载
     *
     * @param isVisibleToUser
     */
    @Override
    public void setUserVisibleHint(boolean isVisibleToUser)
    {

        super.setUserVisibleHint(isVisibleToUser);
        if (getUserVisibleHint())
        {
            isVisible = true;
            onVisible();
        }
        else
        {
            isVisible = false;
            onInvisible();
        }
    }

    protected void onVisible()
    {
        lazyLoad();
    }

    protected void onInvisible()
    {
    }

    protected abstract int getLayoutResId();
    protected abstract void lazyLoad();
    protected abstract void finishCreateView(Bundle state);
}
