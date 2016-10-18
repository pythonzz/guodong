package com.guodong.sun.guodong.view;

import com.guodong.sun.guodong.base.IBaseView;
import com.guodong.sun.guodong.entity.duanzi.NeiHanVideo;

import java.util.ArrayList;

/**
 * Created by Administrator on 2016/10/17.
 */

public interface IVideoView extends IBaseView
{
    void updateVideoData(ArrayList<NeiHanVideo.DataBean> list);
}
