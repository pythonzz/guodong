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
 * Last modified 2017-05-04 15:02:01
 *
 * GitHub:   https://github.com/guodongAndroid
 * Website:  http://www.sunxiaoduo.com
 * Email:    sun33919135@gmail.com
 * QQ:       33919135
 */

package com.guodong.sun.guodong.api;

import com.guodong.sun.guodong.entity.duanzi.NeiHanDuanZi;
import com.guodong.sun.guodong.entity.duanzi.VideoData;
import com.guodong.sun.guodong.entity.picture.PictureBean;

import retrofit2.http.GET;
import retrofit2.http.Query;
import rx.Observable;

/**
 * Created by Administrator on 2016/10/10.
 */

public interface DuanZiApi
{
    @GET("neihan/stream/mix/v1/?mpic=1&content_type=-102&message_cursor=-1&bd_Stringitude=113.369569&bd_latitude=23.149678&bd_city=广州市&am_Stringitude=113.367846&am_latitude=23.149878&am_city=广州市&am_loc_time=1465213692154&count=30&min_time=1465213700&screen_width=720&iid=4512422578&device_id=17215021497&ac=wifi&channel=NHSQH5AN&aid=7&app_name=joke_essay&version_code=431&device_platform=android&ssmix=a&device_type=6s+Plus&os_api=19&os_version=4.4.2&uuid=864394108025091&openudid=80FA5B208E050000&manifest_version_code=431")
    Observable<NeiHanDuanZi> getDuanZiData(@Query("essence") int page);

    @GET("neihan/stream/mix/v1/?mpic=1&content_type=-104&message_cursor=-1&bd_longitude=113.369569&bd_latitude=23.149678&bd_city=%E5%B9%BF%E5%B7%9E%E5%B8%82&am_longitude=113.367863&am_latitude=23.149878&am_city=%E5%B9%BF%E5%B7%9E%E5%B8%82&am_loc_time=1465227022051&count=30&min_time=1465213698&screen_width=720&iid=4512422578&device_id=17215021497&ac=wifi&channel=NHSQH5AN&aid=7&app_name=joke_essay&version_code=431&device_platform=android&ssmix=a&device_type=6s+Plus&os_api=19&os_version=4.4.2&uuid=864394108025091&openudid=80FA5B208E050000&manifest_version_code=431")
    Observable<VideoData> getVideoData(@Query("essence") int page);

    @GET("neihan/stream/mix/v1/?mpic=1&webp=1&essence=3&content_type=-103&message_cursor=90948784&am_longitude=120.165698&am_latitude=35.977649&am_city=%E9%9D%92%E5%B2%9B%E5%B8%82&am_loc_time=1481630676074&count=30&min_time=1481628791&screen_width=720&iid=6553950040&device_id=32953984286&ac=wifi&channel=meizu&aid=7&app_name=joke_essay&version_code=580&version_name=5.8.0&device_platform=android&ssmix=a&device_type=m3&device_brand=Meizu&os_api=22&os_version=5.1&uuid=861851033698766&openudid=69544b2781e55ea0&manifest_version_code=580&resolution=720*1280&dpi=320&update_version_code=5804")
    Observable<PictureBean> getPictureBean();
}
