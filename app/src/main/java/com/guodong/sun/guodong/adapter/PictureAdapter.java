package com.guodong.sun.guodong.adapter;

import android.content.ActivityNotFoundException;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Environment;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.SimpleTarget;
import com.guodong.sun.guodong.R;
import com.guodong.sun.guodong.activity.MyApplication;
import com.guodong.sun.guodong.entity.duanzi.NeiHanDuanZi;
import com.guodong.sun.guodong.entity.picture.Picture;
import com.guodong.sun.guodong.entity.picture.ThumbImageList;
import com.guodong.sun.guodong.glide.CircleImageTransform;
import com.guodong.sun.guodong.listener.OnLoadMoreLisener;
import com.guodong.sun.guodong.uitls.AnimatorUtils;
import com.guodong.sun.guodong.uitls.DateTimeHelper;
import com.guodong.sun.guodong.uitls.SnackbarUtil;
import com.guodong.sun.guodong.uitls.StringUtils;
import com.guodong.sun.guodong.widget.SpacingTextView;
import com.shizhefei.view.largeimage.LargeImageView;
import com.shizhefei.view.largeimage.factory.FileBitmapDecoderFactory;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

import static android.content.Context.CLIPBOARD_SERVICE;

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
                binGifImageViewHolder((GifItemViewHolder) holder, bean);
                break;

            case ITEM_IMAGE:
                binItemImageViewHolder((ItemViewHolder) holder, bean);
                break;

            case LONG_IMAGE:
                binLongImageViewHolder((LongItemViewHolder) holder, bean);
                break;
        }
    }

    /**
     * 绑定长图布局
     * @param holder
     * @param bean
     */
    private void binLongImageViewHolder(final LongItemViewHolder holder, final Picture.DataBeanX.DataBean.GroupBean bean) {
        displayTopAndBottom(holder, bean);

        // ----------------------------------------------------------

        Glide.with(mContext)
                .load(bean.getMiddle_image().getUrl_list().get(0).getUrl())
                .asBitmap()
                .diskCacheStrategy(DiskCacheStrategy.SOURCE)
                .into(new SimpleTarget<Bitmap>() {
                    @Override
                    public void onResourceReady(Bitmap resource, GlideAnimation<? super Bitmap> glideAnimation) {
                        FileOutputStream fout = null;
                        try {
                            //保存图片
                            String fileName = mContext.getExternalCacheDir()
                                    + StringUtils.getUrlPicName(bean.getMiddle_image().getUrl_list().get(0).getUrl());
                            fout = new FileOutputStream(fileName);
                            resource.compress(Bitmap.CompressFormat.JPEG, 100, fout);
                            holder.mImageView.setImage(new FileBitmapDecoderFactory(fileName));
                        } catch (FileNotFoundException e) {
                            e.printStackTrace();
                        } finally {
                            try {
                                if (fout != null) fout.close();
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        }
                    }
                });

        holder.mImageView.setCriticalScaleValueHook(new LargeImageView.CriticalScaleValueHook() {
            @Override
            public float getMinScale(LargeImageView largeImageView, int imageWidth, int imageHeight, float suggestMinScale) {
                return 1;
            }

            @Override
            public float getMaxScale(LargeImageView largeImageView, int imageWidth, int imageHeight, float suggestMaxScale) {
                return 1;
            }
        });

        holder.mImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // TODO: 2016/12/15 长图的点击事件
            }
        });

        holder.mLinearLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // TODO: 2016/12/15 长图的点击事件
            }
        });
    }

    /**
     * 绑定单张图片布局
     * @param holder
     * @param bean
     */
    private void binItemImageViewHolder(ItemViewHolder holder, Picture.DataBeanX.DataBean.GroupBean bean) {
        displayTopAndBottom(holder, bean);

        // ----------------------------------------------------------

        LinearLayout.LayoutParams lp = (LinearLayout.LayoutParams) holder.mImageView.getLayoutParams();
        lp.height = MyApplication.ScreenWidth * bean.getMiddle_image().getR_height() / bean.getMiddle_image().getR_width();
        holder.mImageView.setLayoutParams(lp);

        Glide.with(mContext)
                .load(bean.getMiddle_image().getUrl_list().get(0).getUrl())
                .into(holder.mImageView);
    }

    /**
     * 绑定GIF布局
     * @param holder
     * @param bean
     */
    private void binGifImageViewHolder(GifItemViewHolder holder, Picture.DataBeanX.DataBean.GroupBean bean) {
        displayTopAndBottom(holder, bean);

        // ----------------------------------------------------------

        LinearLayout.LayoutParams lp = (LinearLayout.LayoutParams) holder.mImageView.getLayoutParams();
        lp.height = MyApplication.ScreenWidth * bean.getMiddle_image().getR_height() / bean.getMiddle_image().getR_width();
        holder.mImageView.setLayoutParams(lp);

        Glide.with(mContext)
                .load(bean.getLarge_image().getUrl_list().get(0).getUrl())
                .asGif()
                .diskCacheStrategy(DiskCacheStrategy.SOURCE)
                .into(holder.mImageView);
    }

    /**
     * 绑定多图布局
     * @param holder
     * @param bean
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
     * 后期重构 recyclerview
     * @param holder
     * @param bean
     */
    private void displayMultiImage(MultiItemViewHolder holder, Picture.DataBeanX.DataBean.GroupBean bean) {
        int size = bean.getLarge_image_list().size();
        List<ThumbImageList> list = bean.getLarge_image_list();
        if (size == 2) {
            setViewGone(holder.mLinearLayout2);
            setViewGone(holder.mLinearLayout3);
            setViewInVisible(holder.mImageView3);
            displayImageView(holder.mImageView1, list.get(0).getUrl());
            displayImageView(holder.mImageView2, list.get(1).getUrl());
        } else if (size == 3) {
            setViewGone(holder.mLinearLayout2);
            setViewGone(holder.mLinearLayout3);
            displayImageView(holder.mImageView1, list.get(0).getUrl());
            displayImageView(holder.mImageView2, list.get(1).getUrl());
            displayImageView(holder.mImageView3, list.get(2).getUrl());
        } else if (size == 4) {
            setViewGone(holder.mLinearLayout3);
            setViewInVisible(holder.mImageView3);
            setViewInVisible(holder.mImageView6);
            displayImageView(holder.mImageView1, list.get(0).getUrl());
            displayImageView(holder.mImageView2, list.get(1).getUrl());
            displayImageView(holder.mImageView4, list.get(2).getUrl());
            displayImageView(holder.mImageView5, list.get(3).getUrl());
        } else if (size == 5) {
            setViewGone(holder.mLinearLayout3);
            setViewInVisible(holder.mImageView6);
            displayImageView(holder.mImageView1, list.get(0).getUrl());
            displayImageView(holder.mImageView2, list.get(1).getUrl());
            displayImageView(holder.mImageView3, list.get(2).getUrl());
            displayImageView(holder.mImageView4, list.get(3).getUrl());
            displayImageView(holder.mImageView5, list.get(4).getUrl());
        } else if (size == 6) {
            setViewGone(holder.mLinearLayout3);
            displayImageView(holder.mImageView1, list.get(0).getUrl());
            displayImageView(holder.mImageView2, list.get(1).getUrl());
            displayImageView(holder.mImageView3, list.get(2).getUrl());
            displayImageView(holder.mImageView4, list.get(3).getUrl());
            displayImageView(holder.mImageView5, list.get(4).getUrl());
            displayImageView(holder.mImageView6, list.get(5).getUrl());
        } else if (size == 7) {
            setViewInVisible(holder.mImageView8);
            setViewInVisible(holder.mImageView9);
            displayImageView(holder.mImageView1, list.get(0).getUrl());
            displayImageView(holder.mImageView2, list.get(1).getUrl());
            displayImageView(holder.mImageView3, list.get(2).getUrl());
            displayImageView(holder.mImageView4, list.get(3).getUrl());
            displayImageView(holder.mImageView5, list.get(4).getUrl());
            displayImageView(holder.mImageView6, list.get(5).getUrl());
            displayImageView(holder.mImageView7, list.get(6).getUrl());
        } else if (size == 8) {
            setViewInVisible(holder.mImageView9);
            displayImageView(holder.mImageView1, list.get(0).getUrl());
            displayImageView(holder.mImageView2, list.get(1).getUrl());
            displayImageView(holder.mImageView3, list.get(2).getUrl());
            displayImageView(holder.mImageView4, list.get(3).getUrl());
            displayImageView(holder.mImageView5, list.get(4).getUrl());
            displayImageView(holder.mImageView6, list.get(5).getUrl());
            displayImageView(holder.mImageView7, list.get(6).getUrl());
            displayImageView(holder.mImageView8, list.get(7).getUrl());
        } else if (size == 9) {
            displayImageView(holder.mImageView1, list.get(0).getUrl());
            displayImageView(holder.mImageView2, list.get(1).getUrl());
            displayImageView(holder.mImageView3, list.get(2).getUrl());
            displayImageView(holder.mImageView4, list.get(3).getUrl());
            displayImageView(holder.mImageView5, list.get(4).getUrl());
            displayImageView(holder.mImageView6, list.get(5).getUrl());
            displayImageView(holder.mImageView7, list.get(6).getUrl());
            displayImageView(holder.mImageView8, list.get(7).getUrl());
            displayImageView(holder.mImageView9, list.get(8).getUrl());
        }
    }

    private void setViewGone(View view) {
        view.setVisibility(View.GONE);
    }

    private void setViewVisible(View view) {
        view.setVisibility(View.VISIBLE);
    }

    private void setViewInVisible(View view) {
        view.setVisibility(View.INVISIBLE);
    }

    private void displayImageView(ImageView v, String url) {
        Glide.with(mContext)
                .load(url)
                .diskCacheStrategy(DiskCacheStrategy.SOURCE)
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

        public PictureViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }

    class ItemViewHolder extends PictureViewHolder {

        @BindView(R.id.fragment_picture_item_iv)
        ImageView mImageView;

        public ItemViewHolder(View itemView) {
            super(itemView);
//            ButterKnife.bind(this, itemView);
        }
    }

    class GifItemViewHolder extends PictureViewHolder {

        @BindView(R.id.fragment_picture_gifview)
        ImageView mImageView;

        public GifItemViewHolder(View itemView) {
            super(itemView);
//            ButterKnife.bind(this, itemView);
        }
    }

    class LongItemViewHolder extends PictureViewHolder {

        @BindView(R.id.fragment_picture_item_iv)
        LargeImageView mImageView;

        @BindView(R.id.fragment_picture_item_ll)
        LinearLayout mLinearLayout;

        public LongItemViewHolder(View itemView) {
            super(itemView);
//            ButterKnife.bind(this, itemView);
        }
    }

    class MultiItemViewHolder extends PictureViewHolder {

        @BindView(R.id.picture_multi_item_iv1)
        ImageView mImageView1;

        @BindView(R.id.picture_multi_item_iv2)
        ImageView mImageView2;

        @BindView(R.id.picture_multi_item_iv3)
        ImageView mImageView3;

        @BindView(R.id.picture_multi_item_iv4)
        ImageView mImageView4;

        @BindView(R.id.picture_multi_item_iv5)
        ImageView mImageView5;

        @BindView(R.id.picture_multi_item_iv6)
        ImageView mImageView6;

        @BindView(R.id.picture_multi_item_iv7)
        ImageView mImageView7;

        @BindView(R.id.picture_multi_item_iv8)
        ImageView mImageView8;

        @BindView(R.id.picture_multi_item_iv9)
        ImageView mImageView9;

        @BindView(R.id.picture_multi_item_ll1)
        LinearLayout mLinearLayout1;

        @BindView(R.id.picture_multi_item_ll2)
        LinearLayout mLinearLayout2;

        @BindView(R.id.picture_multi_item_ll3)
        LinearLayout mLinearLayout3;

        public MultiItemViewHolder(View itemView) {
            super(itemView);
//            ButterKnife.bind(this, itemView);
        }
    }

}
