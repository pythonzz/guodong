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

package com.guodong.sun.guodong.entity.picture;

import com.google.gson.Gson;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.guodong.sun.guodong.activity.MyApplication;
import com.guodong.sun.guodong.entity.picture.gifvideo.Gif;
import com.guodong.sun.guodong.entity.picture.gifvideo.GifVideo;
import com.guodong.sun.guodong.entity.picture.gifvideo.GifVideoBean;
import com.guodong.sun.guodong.entity.picture.multiimage.MultiImage;
import com.guodong.sun.guodong.entity.picture.singleimage.LongImage;
import com.guodong.sun.guodong.entity.picture.singleimage.SingleImage;

import java.lang.reflect.Type;


/**
 * @author drakeet
 */
public class PictureContentDeserializer implements JsonDeserializer<PictureContent> {

    @Override
    public PictureContent deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context)
        throws JsonParseException {
        Gson gson = GsonProvider.gson;
        JsonObject jsonObject = (JsonObject) json;
        final int media_type = intOrEmpty(jsonObject.get("media_type"));
        final int is_multi_image = intOrEmpty(jsonObject.get("is_multi_image"));
        final int is_gif = intOrEmpty(jsonObject.get("is_gif"));
        final JsonObject middle_image = jsonObjectOrEmpty(jsonObject.get("middle_image"));
        final int height = intOrEmpty(middle_image != null ? middle_image.get("r_height"): null);
        final JsonObject gifvideo = jsonObjectOrEmpty(jsonObject.get("gifvideo"));
        PictureContent content = null;

        if (media_type == 2 ) {  // gif
            if (is_gif == 1 && gifvideo != null) {
                content = gson.fromJson(json, GifVideoBean.class);
            } else {
                content = gson.fromJson(json, Gif.class);
            }
        } else if (media_type == 1) { // 单张图片
            if (height > MyApplication.ScreenHeight) {
                content = gson.fromJson(json, LongImage.class);
            } else if (height < MyApplication.ScreenHeight) {
                content = gson.fromJson(json, SingleImage.class);
            }
        } else if (media_type == 4 && is_multi_image == 1) { // 多张图片
            content = gson.fromJson(json, MultiImage.class);
        }
        return content;
    }

    private JsonObject jsonObjectOrEmpty(JsonElement jsonElement) {
        return jsonElement == null ? null : jsonElement.isJsonNull() ? null : jsonElement.getAsJsonObject();
    }


    private int intOrEmpty(JsonElement jsonElement) {
        return jsonElement == null ? 0 : jsonElement.isJsonNull() ? 0 : jsonElement.getAsInt();
    }
}
