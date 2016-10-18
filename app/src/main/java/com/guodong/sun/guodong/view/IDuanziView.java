package com.guodong.sun.guodong.view;

import com.guodong.sun.guodong.base.IBaseView;
import com.guodong.sun.guodong.entity.duanzi.NeiHanDuanZi;

import java.util.ArrayList;

/**
 * Created by Administrator on 2016/10/10.
 */

public interface IDuanziView extends IBaseView
{
    void updateDuanziData(ArrayList<NeiHanDuanZi.Data> list);
}
