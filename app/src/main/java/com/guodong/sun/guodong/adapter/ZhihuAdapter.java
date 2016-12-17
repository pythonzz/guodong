package com.guodong.sun.guodong.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.guodong.sun.guodong.activity.MainActivity;
import com.guodong.sun.guodong.R;
import com.guodong.sun.guodong.activity.ZhiHuDetailActivity;
import com.guodong.sun.guodong.entity.zhihu.ZhihuDailyNews;
import com.guodong.sun.guodong.listener.OnLoadMoreLisener;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import jp.wasabeef.glide.transformations.RoundedCornersTransformation;

/**
 * Created by Administrator on 2016/10/12.
 */

public class ZhihuAdapter extends RecyclerView.Adapter<ZhihuAdapter.ZhihuViewHolder> implements OnLoadMoreLisener
{
    private Context mContext;
    private ArrayList<ZhihuDailyNews.Question> mLists = new ArrayList<>();
    private LayoutInflater mInflater;
    private boolean isLoading;

    public ZhihuAdapter(Context context)
    {
        this.mContext = context;
        mInflater = LayoutInflater.from(context);
    }

    @Override
    public ZhihuViewHolder onCreateViewHolder(ViewGroup parent, int viewType)
    {
        return new ZhihuViewHolder(mInflater.inflate(R.layout.fragment_zhihu_item, parent, false));
    }

    @Override
    public void onBindViewHolder(final ZhihuViewHolder holder, int position)
    {
        final ZhihuDailyNews.Question zhihu = mLists.get(position);
        holder.mTextView.setText(zhihu.getTitle());
        Glide.with(mContext)
                .load(zhihu.getImages().get(0))
                .bitmapTransform(new RoundedCornersTransformation(mContext, 10, 0))
                .placeholder(R.drawable.ic_default_image)
                .error(R.drawable.img_tips_error_load_error)
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .into(holder.mImageView);

        holder.mView.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                Intent intent = ZhiHuDetailActivity.newIntent(mContext, zhihu.getId());
                ActivityOptionsCompat optionsCompat = ActivityOptionsCompat
                        .makeSceneTransitionAnimation((MainActivity)mContext, holder.mImageView, ZhiHuDetailActivity.TRANSIT_PIC);
                try
                {
                    ActivityCompat.startActivity((MainActivity) mContext, intent, optionsCompat.toBundle());
                } catch (Exception e)
                {
                    e.printStackTrace();
                    mContext.startActivity(intent);
                }
            }
        });
    }

    @Override
    public int getItemCount()
    {
        return mLists.size();
    }

    public void addList(ArrayList<ZhihuDailyNews.Question> list)
    {
        if (mLists.size() != 0 && list.size() != 0)
        {
            if (mLists.get(0).getTitle().equals(list.get(0).getTitle()))
                return;
        }

        if (isLoading)
            mLists.addAll(list);
        else
            mLists.addAll(0, list);
        notifyDataSetChanged();
    }

    public void clearList()
    {
        mLists.clear();
    }

    @Override
    public void onLoadStart()
    {
        if (isLoading)
            return;
        isLoading = true;
        notifyItemInserted(getLoadingMoreItemPosition());
    }

    @Override
    public void onLoadFinish()
    {
        if (!isLoading) return;
        notifyItemRemoved(getLoadingMoreItemPosition());
        isLoading = false;
    }

    private int getLoadingMoreItemPosition()
    {
        return isLoading ? getItemCount() - 1 : RecyclerView.NO_POSITION;
    }

    class ZhihuViewHolder extends RecyclerView.ViewHolder
    {
        @BindView(R.id.zhihu_item_iv)
        ImageView mImageView;

        @BindView(R.id.zhihu_item_tv_title)
        TextView mTextView;

        View mView;

        public ZhihuViewHolder(View itemView)
        {
            super(itemView);
            mView = itemView;
            ButterKnife.bind(this, itemView);
        }
    }
}
