package com.guodong.sun.guodong.view;

import com.guodong.sun.guodong.base.IBaseView;
import com.guodong.sun.guodong.entity.zhihu.ZhihuDailyStory;

/**
 * Created by Administrator on 2016/10/13.
 */

public interface IZhihuDetailView extends IBaseView
{
    void updateZhihuDetailData(ZhihuDailyStory story);
}
