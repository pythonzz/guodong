package com.guodong.sun.guodong.entity.picture.singleimage;

import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.bumptech.glide.Glide;
import com.guodong.sun.guodong.R;
import com.guodong.sun.guodong.activity.MultiPictureActivity;
import com.guodong.sun.guodong.entity.picture.ContentHolder;
import com.guodong.sun.guodong.entity.picture.PictureFrameProvider;
import com.guodong.sun.guodong.widget.ResizableImageView;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * <p> 描述
 *
 * @author 孙国栋
 * @version 1.0
 * @date 2017-02-22 15:31
 * @see
 */
public class SingleImageViewProvider extends PictureFrameProvider<SingleImage, SingleImageViewProvider.ViewHolder> {

    @Override
    protected ContentHolder onCreateContentViewHolder(@NonNull LayoutInflater inflater, @NonNull ViewGroup parent) {
        View view = inflater.inflate(R.layout.fragment_picture_single_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    protected void onBindContentViewHolder(@NonNull final ViewHolder holder, @NonNull SingleImage content) {

        final String url = content.getMiddle_image().getUrl_list().get(0).getUrl();

        holder.mImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ArrayList<String> list = new ArrayList<>();
                list.add(url);
                MultiPictureActivity.startActivity(holder.mImageView.getContext(), 0, list);
            }
        });

        Glide.with(holder.mImageView.getContext())
                .load(url)
                .placeholder(R.drawable.ic_default_image)
                .into(holder.mImageView);
    }

    static class ViewHolder extends ContentHolder {

        @BindView(R.id.fragment_picture_item_iv)
        ResizableImageView mImageView;

        ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}
