package com.guodong.sun.guodong.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.target.SizeReadyCallback;
import com.guodong.sun.guodong.activity.MainActivity;
import com.guodong.sun.guodong.activity.PictureActivity;
import com.guodong.sun.guodong.R;
import com.guodong.sun.guodong.entity.meizi.Meizi;
import com.guodong.sun.guodong.listener.OnLoadMoreLisener;
import com.guodong.sun.guodong.widget.BadgedFourThreeImageView;
import com.guodong.sun.guodong.widget.SquareCenterImageView;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Administrator on 2016/10/10.
 */

public class MeiziAdapter extends RecyclerView.Adapter<MeiziAdapter.MeiziViewHolder> implements OnLoadMoreLisener
{
    private ArrayList<Meizi> mMeiziLists = new ArrayList<>();
    private Context mContext;
    private LayoutInflater mInflater;

    private boolean isLoading;

    public MeiziAdapter(Context context)
    {
        mContext = context;
        mInflater = LayoutInflater.from(context);
    }

    @Override
    public MeiziViewHolder onCreateViewHolder(ViewGroup parent, int viewType)
    {
        return new MeiziViewHolder(mInflater.inflate(R.layout.fragment_meizi_item, parent, false));
    }

    @Override
    public void onBindViewHolder(final MeiziViewHolder holder, final int position)
    {
        Glide.with(mContext)
                .load(mMeiziLists.get(position).getUrl())
                .centerCrop().diskCacheStrategy(DiskCacheStrategy.ALL)
                .into(holder.imageView)
                .getSize(new SizeReadyCallback()
                {
                    @Override
                    public void onSizeReady(int width, int height)
                    {
                        if (!holder.card.isShown())
                            holder.card.setVisibility(View.VISIBLE);
                    }
                });

        holder.imageView.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                int[] location = new int[2];
                view.getLocationOnScreen(location);
                Intent intent = PictureActivity.newIntent(mContext, mMeiziLists.get(position).url,
                        location[0], location[1] + 50, view.getWidth(), view.getHeight());
                mContext.startActivity(intent);
                ((MainActivity)mContext).overridePendingTransition(0, 0);
            }
        });
    }

    public void addLists(ArrayList<Meizi> list)
    {
        if (list.size() != 0 && mMeiziLists.size() != 0)
        {
            if (list.get(0).getUrl().equals(mMeiziLists.get(0).getUrl()))
                return;
        }

        if (isLoading)
            mMeiziLists.addAll(list);
        else
            mMeiziLists.addAll(0, list);
        notifyDataSetChanged();
    }

    public void clearMeiziList()
    {
        mMeiziLists.clear();
    }

    @Override
    public int getItemCount()
    {
        return mMeiziLists.size();
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

    class MeiziViewHolder extends RecyclerView.ViewHolder
    {
        @BindView(R.id.meizi_iv)
        SquareCenterImageView imageView;

        View card;

        public MeiziViewHolder(View itemView)
        {
            super(itemView);
            card = itemView;
            ButterKnife.bind(this, itemView);
        }
    }
}