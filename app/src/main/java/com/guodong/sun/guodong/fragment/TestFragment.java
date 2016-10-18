package com.guodong.sun.guodong.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.guodong.sun.guodong.R;

/**
 * Created by Administrator on 2016/10/12.
 */

public class TestFragment extends Fragment
{
    private static String strText;
    private static int colorInt;

    public static TestFragment newInstance(String setText, int color)
    {
        strText = setText;
        colorInt = color;
        return new TestFragment();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState)
    {
        View view = inflater.inflate(R.layout.fragment_test, container, false);
        LinearLayout ll = (LinearLayout) view.findViewById(R.id.ll_test);
        ll.setBackgroundColor(colorInt);
        TextView textView = (TextView) view.findViewById(R.id.tv_test);
        textView.setText(strText);
        return view;
    }
}
