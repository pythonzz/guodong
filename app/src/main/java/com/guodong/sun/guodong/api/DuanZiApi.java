package com.guodong.sun.guodong.api;

import com.guodong.sun.guodong.entity.duanzi.NeiHanDuanZi;
import com.guodong.sun.guodong.entity.duanzi.NeiHanVideo;
import com.guodong.sun.guodong.entity.duanzi.VideoData;

import retrofit2.http.GET;
import retrofit2.http.Path;
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
}
