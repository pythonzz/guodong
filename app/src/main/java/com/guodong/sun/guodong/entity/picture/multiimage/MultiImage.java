package com.guodong.sun.guodong.entity.picture.multiimage;

import com.guodong.sun.guodong.entity.picture.PictureContent;
import com.guodong.sun.guodong.entity.picture.ThumbImageList;

import java.util.List;

public class MultiImage extends PictureContent {

    private List<ThumbImageList> thumb_image_list;
    private List<ThumbImageList> large_image_list;

    public List<ThumbImageList> getThumb_image_list() {
        return thumb_image_list;
    }

    public void setThumb_image_list(List<ThumbImageList> thumb_image_list) {
        this.thumb_image_list = thumb_image_list;
    }

    public List<ThumbImageList> getLarge_image_list() {
        return large_image_list;
    }

    public void setLarge_image_list(List<ThumbImageList> large_image_list) {
        this.large_image_list = large_image_list;
    }
}
