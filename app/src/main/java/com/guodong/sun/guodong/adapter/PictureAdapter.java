package com.guodong.sun.guodong.adapter;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.guodong.sun.guodong.entity.picture.PictureBean;

import java.util.List;

import me.drakeet.multitype.MultiTypeAdapter;
import me.drakeet.multitype.TypePool;

public class PictureAdapter extends MultiTypeAdapter {

    public PictureAdapter() {
        super();
    }

    @NonNull
    @Override
    public Class onFlattenClass(@NonNull Object item) {
        return ((PictureBean.DataBeanX.DataBean)item).getGroup().getClass();
    }
}
