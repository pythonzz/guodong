package com.guodong.sun.guodong.fragment;

import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.LinearInterpolator;

import com.guodong.sun.guodong.R;
import com.guodong.sun.guodong.adapter.VideoAdapter;
import com.guodong.sun.guodong.base.AbsBaseFragment;
import com.guodong.sun.guodong.entity.duanzi.NeiHanVideo;
import com.guodong.sun.guodong.presenter.presenterImpl.VideoPresenterImpl;
import com.guodong.sun.guodong.uitls.AppUtil;
import com.guodong.sun.guodong.uitls.SnackbarUtil;
import com.guodong.sun.guodong.view.IVideoView;
import com.guodong.sun.guodong.widget.CustomEmptyView;
import com.guodong.sun.guodong.widget.WrapContentLinearLayoutManager;
import com.melnykov.fab.FloatingActionButton;
import com.nineoldandroids.animation.ObjectAnimator;
import com.nineoldandroids.animation.ValueAnimator;

import java.util.ArrayList;

import butterknife.BindView;
import fm.jiecao.jcvideoplayer_lib.JCVideoPlayer;
import fm.jiecao.jcvideoplayer_lib.JCVideoPlayerManager;

/**
 * Created by Administrator on 2016/10/17.
 */

public class VideoFragment extends AbsBaseFragment implements IVideoView
{
    @BindView(R.id.video_recycler)
    RecyclerView mRecyclerView;
    @BindView(R.id.empty_layout)
    CustomEmptyView mCustomEmptyView;
    @BindView(R.id.video_swipe_refresh_layout)
    SwipeRefreshLayout mSwipeRefreshLayout;
    @BindView(R.id.video_fb)
    FloatingActionButton mFButton;

    //RecycleView是否正在刷新
    private boolean isRefreshing = false;
    private boolean isLoading;
    private int page = 1;

    private VideoAdapter mAdapter;
    private VideoPresenterImpl mVideoPresenter;
    private ObjectAnimator mAnimator;

    public static VideoFragment newInstance()
    {
        return new VideoFragment();
    }

    @Override
    protected int getLayoutResId()
    {
        return R.layout.fragment_video;
    }

    @Override
    protected void lazyLoad()
    {
        if (!isPrepared || !isVisible)
            return;
        showProgressBar();
        initRecyclerView();
        initFButton();
        isPrepared = false;
        mVideoPresenter.getVideoData(1);
    }

    private void initFButton()
    {
        mFButton.attachToRecyclerView(mRecyclerView);
        mFButton.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                if (isRefreshing || isLoading)
                    return;
                mAnimator = ObjectAnimator.ofFloat(v, "rotation", 0F, 360F);
                mAnimator.setDuration(500);
                mAnimator.setInterpolator(new LinearInterpolator());
                mAnimator.setRepeatCount(ValueAnimator.INFINITE);
                mAnimator.setRepeatMode(ValueAnimator.RESTART);
                mAnimator.start();
                mRecyclerView.smoothScrollToPosition(0);
                isRefreshing = true;
                mVideoPresenter.getVideoData(1);
            }
        });
    }

    private void initRecyclerView()
    {
        mAdapter = new VideoAdapter(getContext());
        mRecyclerView.setAdapter(mAdapter);
        mRecyclerView.setLayoutManager(new WrapContentLinearLayoutManager(getContext()));
        mRecyclerView.setItemAnimator(new DefaultItemAnimator());
        mRecyclerView.addOnChildAttachStateChangeListener(new RecyclerView.OnChildAttachStateChangeListener()
        {
            @Override
            public void onChildViewAttachedToWindow(View view)
            {
            }

            @Override
            public void onChildViewDetachedFromWindow(View view)
            {
                if (JCVideoPlayerManager.listener() != null)
                {
                    JCVideoPlayer videoPlayer = (JCVideoPlayer) JCVideoPlayerManager.listener();
                    if (((ViewGroup) view).indexOfChild(videoPlayer) != -1 && videoPlayer.currentState == JCVideoPlayer.CURRENT_STATE_PLAYING)
                    {
                        JCVideoPlayer.releaseAllVideos();
                    }
                }
            }
        });

        mRecyclerView.addOnScrollListener(new RecyclerView.OnScrollListener()
        {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState)
            {
                super.onScrollStateChanged(recyclerView, newState);
            }

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy)
            {
                super.onScrolled(recyclerView, dx, dy);
                if (dy > 0) //向下滚动
                {
                    WrapContentLinearLayoutManager llm = (WrapContentLinearLayoutManager) recyclerView.getLayoutManager();
                    int visibleItemCount = llm.getChildCount();
                    int totalItemCount = llm.getItemCount();
                    int firstVisiblesItemPos = llm.findFirstVisibleItemPosition();

                    if (!isLoading && (visibleItemCount + firstVisiblesItemPos) >= totalItemCount)
                    {
                        isLoading = true;
                        page++;
                        loadMoreDate();
                    }
                }
            }
        });
    }

    private void loadMoreDate()
    {
        mAdapter.onLoadStart();
        mVideoPresenter.getVideoData(page);
    }

    @Override
    public void finishCreateView(Bundle state)
    {
        isPrepared = true;
        mVideoPresenter = new VideoPresenterImpl(this);
        lazyLoad();
    }


    @Override
    public void updateVideoData(ArrayList<NeiHanVideo.DataBean> list)
    {
        hideEmptyView();
        // 注意addList() 和 onLoadFinish()的调用顺序
        mAdapter.addLists(list);
        mAdapter.onLoadFinish();
        isLoading = false;
    }

    @Override
    public void showProgressBar()
    {
        isRefreshing = true;
        mSwipeRefreshLayout.setColorSchemeResources(R.color.colorPrimary);
        mSwipeRefreshLayout.setRefreshing(true);
        mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener()
        {
            @Override
            public void onRefresh()
            {
                isRefreshing = true;
                //                mAdapter.clearDuanziList();
                mVideoPresenter.getVideoData(1);
            }
        });
    }

    @Override
    public void hidProgressBar()
    {
        if (mAnimator != null)
            mAnimator.cancel();
        mSwipeRefreshLayout.setRefreshing(false);
        isRefreshing = false;
    }

    @Override
    public void showError(String error)
    {
        initEmptyView();
    }

    public void hideEmptyView()
    {
        mCustomEmptyView.setVisibility(View.GONE);
    }

    public void initEmptyView()
    {
        if (!AppUtil.isNetworkConnected())
        {
            SnackbarUtil.showMessage(mRecyclerView, getString(R.string.noNetwork));
        }
        else
        {
            mSwipeRefreshLayout.setRefreshing(false);
            mCustomEmptyView.setVisibility(View.VISIBLE);
            mCustomEmptyView.setEmptyImage(R.drawable.img_tips_error_load_error);
            mCustomEmptyView.setEmptyText(getString(R.string.loaderror));
            SnackbarUtil.showMessage(mRecyclerView, getString(R.string.noNetwork));
            mCustomEmptyView.reload(new CustomEmptyView.ReloadOnClickListener()
            {
                @Override
                public void reloadClick()
                {
                    mVideoPresenter.getVideoData(1);
                }
            });
        }
    }
}
