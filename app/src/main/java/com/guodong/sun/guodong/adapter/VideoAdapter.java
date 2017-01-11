package com.guodong.sun.guodong.adapter;

import android.content.Context;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.guodong.sun.guodong.R;
import com.guodong.sun.guodong.entity.duanzi.NeiHanVideo;
import com.guodong.sun.guodong.listener.OnLoadMoreLisener;
import com.guodong.sun.guodong.uitls.AnimatorUtils;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import fm.jiecao.jcvideoplayer_lib.JCVideoPlayer;
import fm.jiecao.jcvideoplayer_lib.JCVideoPlayerStandard;

/**
 * Created by Administrator on 2016/10/17.
 */

public class VideoAdapter extends RecyclerView.Adapter<VideoAdapter.VideoViewHolder> implements OnLoadMoreLisener
{
    private ArrayList<NeiHanVideo.DataBean> mLists = new ArrayList<>();
    private Context mContext;
    private LayoutInflater mInflater;
    private boolean isLoading;
    private static String coverurl="http://p2.pstatp.com/large/";
    private RecyclerView mRecyclerView;

    public VideoAdapter(Context context, RecyclerView recyclerView)
    {
        mContext = context;
        mRecyclerView = recyclerView;
        mInflater = LayoutInflater.from(context);
    }

    @Override
    public VideoViewHolder onCreateViewHolder(ViewGroup parent, int viewType)
    {
        return new VideoViewHolder(mInflater.inflate(R.layout.fragment_video_item, parent, false));
    }

    @Override
    public void onBindViewHolder(VideoViewHolder holder, int position)
    {

        AnimatorUtils.startScaleInAnimator(holder.mCardView);

        NeiHanVideo.DataBean bean = mLists.get(position);
        boolean isSetUp = false;
        if(bean.getGroup() != null)
            isSetUp = holder.mVideoPlayerStandard.setUp(bean.getGroup().getMp4_url(),
                JCVideoPlayer.SCREEN_LAYOUT_LIST, bean.getGroup().getText());

        if (isSetUp)
            Glide.with(mContext)
                    .load(coverurl + bean.getGroup().getCover_image_uri())
                    .centerCrop()
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .into(holder.mVideoPlayerStandard.thumbImageView);
    }

    @Override
    public int getItemCount()
    {
        return mLists.size();
    }

    public void addLists(ArrayList<NeiHanVideo.DataBean> list)
    {
        if (mLists.size() != 0 && list.size() != 0)
        {
            if (mLists.get(0).getGroup().getMp4_url().equals(list.get(0).getGroup().getMp4_url()))
                return;
        }

        int size = mLists.size();
        if (isLoading)
        {
            mLists.addAll(list);
            notifyItemRangeInserted(size, list.size());
        }
        else
        {
            mLists.addAll(0, list);
            notifyItemRangeInserted(0, list.size());
            mRecyclerView.scrollToPosition(0);
        }
//        notifyDataSetChanged();
    }

    public void clearDuanziList()
    {
        mLists.clear();
    }

    @Override
    public void onLoadStart()
    {
        if (isLoading)
            return;
        isLoading = true;
//        notifyItemInserted(getLoadingMoreItemPosition());
    }

    @Override
    public void onLoadFinish()
    {
        if (!isLoading) return;
//        notifyItemRemoved(getLoadingMoreItemPosition());
        isLoading = false;
    }

    private int getLoadingMoreItemPosition()
    {
        return isLoading ? getItemCount() - 1 : RecyclerView.NO_POSITION;
    }

    static class VideoViewHolder extends RecyclerView.ViewHolder
    {

        @BindView(R.id.video_player)
        JCVideoPlayerStandard mVideoPlayerStandard;

        CardView mCardView;

        public VideoViewHolder(View itemView)
        {
            super(itemView);
            mCardView = (CardView) itemView;
            ButterKnife.bind(this, itemView);
        }
    }
}
