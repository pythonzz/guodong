package com.guodong.sun.guodong.view;

import com.guodong.sun.guodong.base.IBaseView;
import com.guodong.sun.guodong.entity.zhihu.ZhihuDailyNews;

import java.util.ArrayList;

/**
 * Created by Administrator on 2016/10/12.
 */

public interface IZhihuView extends IBaseView
{
    void updateZhihuData(ArrayList<ZhihuDailyNews.Question> list);
}
