package com.guodong.sun.guodong.entity.picture.gifvideo;

import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.guodong.sun.guodong.R;
import com.guodong.sun.guodong.activity.MultiGifActivity;
import com.guodong.sun.guodong.activity.MyApplication;
import com.guodong.sun.guodong.entity.picture.ContentHolder;
import com.guodong.sun.guodong.entity.picture.PictureFrameProvider;
import com.guodong.sun.guodong.uitls.AlxGifHelper;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import pl.droidsonroids.gif.GifImageView;

public class GifProvider extends PictureFrameProvider<Gif, GifProvider.ViewHolder> {

    @Override
    protected ContentHolder onCreateContentViewHolder(@NonNull LayoutInflater inflater, @NonNull ViewGroup parent) {
        View view = inflater.inflate(R.layout.fragment_picture_gif_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    protected void onBindContentViewHolder(@NonNull final ViewHolder holder, @NonNull final Gif content) {

        RelativeLayout.LayoutParams lp = (RelativeLayout.LayoutParams) holder.mGifImageView.getLayoutParams();
        lp.height = MyApplication.ScreenWidth * content.getMiddle_image().getR_height() / content.getMiddle_image().getR_width();
        holder.mGifImageView.setLayoutParams(lp);
//        holder.mImageView.setLayoutParams(lp);

        Glide.with(holder.mGifImageView.getContext()).load(content.getMiddle_image().getUrl_list().get(0).getUrl())
                .diskCacheStrategy(DiskCacheStrategy.SOURCE)
                .into(holder.mGifImageView);

        // TODO: 2016/12/17
        AlxGifHelper.displayGif(content.getLarge_image().getUrl_list().get(0).getUrl(),
                holder.mGifImageView, holder.mProgressBar, holder.mTextView);

        final ArrayList<String> listurl = new ArrayList();
        listurl.add(content.getLarge_image().getUrl_list().get(0).getUrl());

        holder.mGifImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MultiGifActivity.startActivity(holder.mGifImageView.getContext(), 0, listurl,
                        content.getMiddle_image().getWidth(), content.getMiddle_image().getHeight());
            }
        });
    }

    static class ViewHolder extends ContentHolder {

        @BindView(R.id.fragment_picture_gifview)
        GifImageView mGifImageView;

        @BindView(R.id.fragment_picture_gifview_pb)
        ProgressBar mProgressBar;

        @BindView(R.id.fragment_picture_gifview_tv)
        TextView mTextView;

        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}
