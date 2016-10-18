package com.guodong.sun.guodong.view;

import com.guodong.sun.guodong.base.IBaseView;
import com.guodong.sun.guodong.entity.qiubai.QiuShiBaiKe;

import java.util.ArrayList;

/**
 * Created by Administrator on 2016/10/11.
 */

public interface IQiubaiView extends IBaseView
{
    void updateQiubaiData(ArrayList<QiuShiBaiKe.Item> list);
}
