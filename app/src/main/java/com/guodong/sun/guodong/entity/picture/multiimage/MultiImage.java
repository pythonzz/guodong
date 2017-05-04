/*
 * Copyright (C) 2017 guodongAndroid
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 * Last modified 2017-05-04 15:03:36
 *
 * GitHub:   https://github.com/guodongAndroid
 * Website:  http://www.sunxiaoduo.com
 * Email:    sun33919135@gmail.com
 * QQ:       33919135
 */

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
