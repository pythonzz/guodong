package com.guodong.sun.guodong.view;

import com.guodong.sun.guodong.base.IBaseView;
import com.guodong.sun.guodong.entity.picture.PictureBean;

import java.util.ArrayList;

/**
 * Created by Administrator on 2016/10/10.
 */

public interface IPictureView extends IBaseView
{
    void updatePictureData(ArrayList<PictureBean.DataBeanX.DataBean> list);
}
