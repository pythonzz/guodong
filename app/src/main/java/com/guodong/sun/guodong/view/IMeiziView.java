package com.guodong.sun.guodong.view;

import com.guodong.sun.guodong.base.IBaseView;
import com.guodong.sun.guodong.entity.meizi.Meizi;

import java.util.ArrayList;

/**
 * Created by Administrator on 2016/10/9.
 */

public interface IMeiziView extends IBaseView
{
    void updateMeiziData(ArrayList<Meizi> list);
}
