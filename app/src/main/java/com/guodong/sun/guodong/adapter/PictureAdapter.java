package com.guodong.sun.guodong.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.SimpleTarget;
import com.guodong.sun.guodong.R;
import com.guodong.sun.guodong.activity.LongPictureActivity;
import com.guodong.sun.guodong.activity.MultiGifActivity;
import com.guodong.sun.guodong.activity.MultiPictureActivity;
import com.guodong.sun.guodong.activity.MyApplication;
import com.guodong.sun.guodong.entity.picture.Picture;
import com.guodong.sun.guodong.entity.picture.ThumbImageList;
import com.guodong.sun.guodong.glide.CircleImageTransform;
import com.guodong.sun.guodong.listener.OnLoadMoreLisener;
import com.guodong.sun.guodong.uitls.AlxGifHelper;
import com.guodong.sun.guodong.uitls.StringUtils;
import com.guodong.sun.guodong.widget.NineGridImageView;
import com.guodong.sun.guodong.widget.NineGridImageViewAdapter;
import com.guodong.sun.guodong.widget.SpacingTextView;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import pl.droidsonroids.gif.GifImageView;

/**
 * Created by Administrator on 2016/10/10.
 */

public class PictureAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> implements OnLoadMoreLisener {

    private static final int MULTI_IMAGE = 4;
    private static final int GIF_IMAGE = 2;
    private static final int ITEM_IMAGE = 1;
    private static final int LONG_IMAGE = 3;

    private ArrayList<Picture.DataBeanX.DataBean> mPictureLists = new ArrayList<>();
    private Context mContext;
    private LayoutInflater mInflater;
    private boolean isLoading;
    private RecyclerView mRecyclerView;

    public PictureAdapter(Context context, RecyclerView recyclerView) {
        mContext = context;
        mRecyclerView = recyclerView;
        mInflater = LayoutInflater.from(context);
    }

    @Override
    public int getItemViewType(int position) {
        Picture.DataBeanX.DataBean.GroupBean bean = mPictureLists.get(position).getGroup();
        if (bean.getMedia_type() == MULTI_IMAGE) {
            if (bean.getIs_multi_image() == 1)
                return MULTI_IMAGE;
        } else if (bean.getMedia_type() == GIF_IMAGE) {
            if (bean.getIs_gif() == 1)
                return GIF_IMAGE;
        } else if (bean.getMedia_type() == ITEM_IMAGE
                && bean.getMiddle_image().getR_height() < MyApplication.ScreenHeight) {
            return ITEM_IMAGE;
        } else if (bean.getMedia_type() == ITEM_IMAGE
                && bean.getMiddle_image().getR_height() > MyApplication.ScreenHeight) {
            return LONG_IMAGE;
        }
        return -1;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        RecyclerView.ViewHolder holder = null;
        switch (viewType) {
            case MULTI_IMAGE:
                holder = new MultiItemViewHolder(mInflater.inflate(R.layout.fragment_picture_multi_item, parent, false));
                break;

            case GIF_IMAGE:
                holder = new GifItemViewHolder(mInflater.inflate(R.layout.fragment_picture_gif_item, parent, false));
                break;

            case ITEM_IMAGE:
                holder = new ItemViewHolder(mInflater.inflate(R.layout.fragment_picture_item, parent, false));
                break;

            case LONG_IMAGE:
                holder = new LongItemViewHolder(mInflater.inflate(R.layout.fragment_picture_long_item, parent, false));
                break;
        }
        return holder;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        Picture.DataBeanX.DataBean.GroupBean bean = mPictureLists.get(position).getGroup();
        switch (holder.getItemViewType()) {
            case MULTI_IMAGE:
                bindMultiImageViewHolder((MultiItemViewHolder) holder, bean);
                break;

            case GIF_IMAGE:
                bindGifImageViewHolder((GifItemViewHolder) holder, bean);
                break;

            case ITEM_IMAGE:
                bindItemImageViewHolder((ItemViewHolder) holder, bean);
                break;

            case LONG_IMAGE:
                bindLongImageViewHolder((LongItemViewHolder) holder, bean);
                break;
        }
    }

    /**
     * 绑定长图布局
     *
     * @param holder viewholder
     * @param bean bean
     */
    private void bindLongImageViewHolder(final LongItemViewHolder holder, final Picture.DataBeanX.DataBean.GroupBean bean) {
        displayTopAndBottom(holder, bean);

        // ----------------------------------------------------------

        diaplayLongImage(holder, bean);
    }

    /**
     * 加载长图
     *
     * @param holder viewholder
     * @param bean bean
     */
    private void diaplayLongImage(final LongItemViewHolder holder, final Picture.DataBeanX.DataBean.GroupBean bean) {

        Glide.with(mContext)
                .load(bean.getMiddle_image().getUrl_list().get(0).getUrl())
                .asBitmap()
                .placeholder(R.drawable.ic_default_image)
                .diskCacheStrategy(DiskCacheStrategy.SOURCE)
                .into(new SimpleTarget<Bitmap>() {
                    @Override
                    public void onResourceReady(Bitmap resource, GlideAnimation<? super Bitmap> glideAnimation) {
                        holder.mImageView.setImageBitmap(Bitmap.createBitmap(resource, 0, 0, resource.getWidth(), 500));
                    }
                });

        holder.mImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LongPictureActivity.startActivity(mContext,
                        bean.getMiddle_image().getUrl_list().get(0).getUrl());
            }
        });

        holder.mLinearLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LongPictureActivity.startActivity(mContext,
                        bean.getMiddle_image().getUrl_list().get(0).getUrl());
            }
        });
    }

    /**
     * 绑定单张图片布局
     *
     * @param holder viewholder
     * @param bean bean
     */
    private void bindItemImageViewHolder(final ItemViewHolder holder, Picture.DataBeanX.DataBean.GroupBean bean) {
        displayTopAndBottom(holder, bean);

        // ----------------------------------------------------------

        LinearLayout.LayoutParams lp = (LinearLayout.LayoutParams) holder.mImageView.getLayoutParams();
        lp.height = MyApplication.ScreenWidth * bean.getMiddle_image().getR_height() / bean.getMiddle_image().getR_width();
        holder.mImageView.setLayoutParams(lp);

        final String url = bean.getMiddle_image().getUrl_list().get(0).getUrl();

        holder.mImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ArrayList<String> list = new ArrayList<>();
                list.add(url);
                MultiPictureActivity.startActivity(mContext, 0, list);
            }
        });

        Glide.with(mContext)
                .load(url)
                .placeholder(R.drawable.ic_default_image)
                .into(holder.mImageView);
    }

    /**
     * 绑定GIF布局
     *
     * @param holder viewholder
     * @param bean bean
     */
    private void bindGifImageViewHolder(GifItemViewHolder holder, Picture.DataBeanX.DataBean.GroupBean bean) {
        displayTopAndBottom(holder, bean);

        // ----------------------------------------------------------

        LinearLayout.LayoutParams lp = (LinearLayout.LayoutParams) holder.mImageView.getLayoutParams();
        lp.height = MyApplication.ScreenWidth * bean.getMiddle_image().getR_height() / bean.getMiddle_image().getR_width();
        holder.mImageView.setLayoutParams(lp);

        // TODO: 2016/12/17
        AlxGifHelper.displayImage(bean.getLarge_image().getUrl_list().get(0).getUrl(),
                holder.mImageView, holder.mProgressBar);
    }

    /**
     * 绑定多图布局
     *
     * @param holder viewholder
     * @param bean bean
     */
    private void bindMultiImageViewHolder(MultiItemViewHolder holder, Picture.DataBeanX.DataBean.GroupBean bean) {
        displayTopAndBottom(holder, bean);

        // ----------------------------------------------------------

        displayMultiImage(holder, bean);
    }

    private void displayTopAndBottom(PictureViewHolder holder, Picture.DataBeanX.DataBean.GroupBean bean) {
        holder.user_name.setText(bean.getUser().getName());
        if (TextUtils.isEmpty(bean.getContent()))
            setViewGone(holder.item_content);
        else
            holder.item_content.setText(bean.getContent());
        holder.category_name.setText(bean.getCategory_name());
        holder.item_digg.setText(StringUtils.getStr2W(bean.getDigg_count()));
        holder.item_bury.setText(StringUtils.getStr2W(bean.getBury_count()));
        holder.item_comment.setText(StringUtils.getStr2W(bean.getComment_count()));
        holder.item_share.setText(StringUtils.getStr2W(bean.getShare_count()));
        Glide.with(mContext)
                .load(bean.getUser().getAvatar_url())
                .bitmapTransform(new CircleImageTransform(mContext))
                .into(holder.user_avatar);
    }

    /**
     * NineGirdImageView
     *
     * @param holder viewholder
     * @param bean bean
     */
    private void displayMultiImage(MultiItemViewHolder holder, Picture.DataBeanX.DataBean.GroupBean bean) {
        holder.mNineGridImageView.setImagesData(bean.getLarge_image_list());
    }

    private void setViewGone(View view) {
        view.setVisibility(View.GONE);
    }

    private void displayImageView(ImageView v, String url) {
        Glide.with(mContext)
                .load(url)
                .diskCacheStrategy(DiskCacheStrategy.SOURCE)
                .placeholder(R.drawable.ic_default_image)
                .into(v);
    }


    @Override
    public int getItemCount() {
        return mPictureLists.size();
    }

    public void addLists(ArrayList<Picture.DataBeanX.DataBean> list) {
        if (mPictureLists.size() != 0 && list.size() != 0) {
            if (mPictureLists.get(0).getGroup().getText().equals(list.get(0).getGroup().getText()))
                return;
        }

        int size = mPictureLists.size();
        if (isLoading) {
            mPictureLists.addAll(list);
            notifyItemRangeInserted(size, list.size());
        } else {
            mPictureLists.addAll(0, list);
            notifyItemRangeInserted(0, list.size());
            mRecyclerView.scrollToPosition(0);
        }
//        notifyDataSetChanged();
    }

    public void clearDuanziList() {
        mPictureLists.clear();
    }

    @Override
    public void onLoadStart() {
        if (isLoading)
            return;
        isLoading = true;
//        notifyItemInserted(getLoadingMoreItemPosition());
    }

    @Override
    public void onLoadFinish() {
        if (!isLoading) return;
//        notifyItemRemoved(getLoadingMoreItemPosition());
        isLoading = false;
    }

    private int getLoadingMoreItemPosition() {
        return isLoading ? getItemCount() - 1 : RecyclerView.NO_POSITION;
    }

    class PictureViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.picture_item_user_avatar)
        ImageView user_avatar;

        @BindView(R.id.picture_item_user_name)
        TextView user_name;

        @BindView(R.id.picture_item_content)
        SpacingTextView item_content;

        @BindView(R.id.picture_item_category_name)
        TextView category_name;

        @BindView(R.id.picture_item_digg)
        TextView item_digg;

        @BindView(R.id.picture_item_share)
        TextView item_share;

        @BindView(R.id.picture_item_bury)
        TextView item_bury;

        @BindView(R.id.picture_item_comment)
        TextView item_comment;

        PictureViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }

    class ItemViewHolder extends PictureViewHolder {

        @BindView(R.id.fragment_picture_item_iv)
        ImageView mImageView;

        ItemViewHolder(View itemView) {
            super(itemView);
//            ButterKnife.bind(this, itemView);
        }
    }

    class GifItemViewHolder extends PictureViewHolder {

        @BindView(R.id.fragment_picture_gifview)
        GifImageView mImageView;

        @BindView(R.id.fragment_picture_gifview_pb)
        ProgressBar mProgressBar;

        GifItemViewHolder(View itemView) {
            super(itemView);
//            ButterKnife.bind(this, itemView);
        }
    }

    class LongItemViewHolder extends PictureViewHolder {

        @BindView(R.id.fragment_picture_item_iv)
        ImageView mImageView;

        @BindView(R.id.fragment_picture_item_ll)
        LinearLayout mLinearLayout;

        LongItemViewHolder(View itemView) {
            super(itemView);
//            ButterKnife.bind(this, itemView);
        }
    }

    class MultiItemViewHolder extends PictureViewHolder {

        @BindView(R.id.picture_multi_nine)
        NineGridImageView mNineGridImageView;

        private NineGridImageViewAdapter<ThumbImageList> mAdapter = new NineGridImageViewAdapter<ThumbImageList>() {
            @Override
            protected void onDisplayImage(Context context, ImageView imageView, ThumbImageList s) {
                displayImageView(imageView, s.getUrl());
            }

            @Override
            protected void onItemImageClick(Context context, int index, List<ThumbImageList> list) {
                ArrayList<String> listUrl = new ArrayList<>();
                for (ThumbImageList thumbImageList : list) {
                    listUrl.add(thumbImageList.getUrl());
                }

                if (list.get(index).is_gif()) {
                    MultiGifActivity.startActivity(context, index, listUrl,
                            list.get(index).getWidth(), list.get(index).getHeight());
                    return;
                }

                MultiPictureActivity.startActivity(context, index,  listUrl);
            }
        };

        MultiItemViewHolder(View itemView) {
            super(itemView);
//            ButterKnife.bind(this, itemView);
            mNineGridImageView.setAdapter(mAdapter);
        }
    }

}
