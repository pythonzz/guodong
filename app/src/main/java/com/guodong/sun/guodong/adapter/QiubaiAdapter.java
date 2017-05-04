/*
 * Copyright (C) 2017 guodongAndroid
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 * Last modified 2017-05-04 15:01:33
 *
 * GitHub:   https://github.com/guodongAndroid
 * Website:  http://www.sunxiaoduo.com
 * Email:    sun33919135@gmail.com
 * QQ:       33919135
 */

package com.guodong.sun.guodong.adapter;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.guodong.sun.guodong.R;
import com.guodong.sun.guodong.activity.MainActivity;
import com.guodong.sun.guodong.entity.qiubai.QiuShiBaiKe;
import com.guodong.sun.guodong.listener.CustomShareListener;
import com.guodong.sun.guodong.listener.OnLoadMoreLisener;
import com.guodong.sun.guodong.uitls.AnimatorUtils;
import com.guodong.sun.guodong.uitls.DateTimeHelper;
import com.guodong.sun.guodong.uitls.SnackbarUtil;
import com.umeng.socialize.ShareAction;
import com.umeng.socialize.ShareContent;
import com.umeng.socialize.bean.SHARE_MEDIA;
import com.umeng.socialize.shareboard.ShareBoardConfig;
import com.umeng.socialize.shareboard.SnsPlatform;
import com.umeng.socialize.utils.ShareBoardlistener;

import java.util.ArrayList;
import java.util.Date;

import butterknife.BindView;
import butterknife.ButterKnife;

import static android.content.Context.CLIPBOARD_SERVICE;

/**
 * Created by Administrator on 2016/10/10.
 */

public class QiubaiAdapter extends RecyclerView.Adapter<QiubaiAdapter.DuanziViewHolder> implements OnLoadMoreLisener
{

    private ArrayList<QiuShiBaiKe.Item> mQiubaiLists = new ArrayList<>();
    private LayoutInflater mInflater;
    private Context mContext;
    private boolean isLoading;
    private RecyclerView mRecyclerView;

    public QiubaiAdapter(Context context, RecyclerView recyclerView)
    {
        this.mContext = context;
        this.mRecyclerView = recyclerView;
        mInflater = LayoutInflater.from(context);
    }

    @Override
    public DuanziViewHolder onCreateViewHolder(ViewGroup parent, int viewType)
    {
        return new DuanziViewHolder(mInflater.inflate(R.layout.fragment_qiubai_item, parent, false));
    }

    @Override
    public void onBindViewHolder(DuanziViewHolder holder, int position)
    {
        final QiuShiBaiKe.Item qiubai = mQiubaiLists.get(position);
        if (qiubai.getUser() == null)
            holder.tvAuthor.setText("匿名用户");
        else
            holder.tvAuthor.setText(qiubai.getUser().getLogin());
        holder.tvContent.setText(qiubai.getContent());
        holder.tvTime.setText(DateTimeHelper.getInterval(new Date((long) qiubai.getCreated_at() * 1000)));

        AnimatorUtils.startSlideInRightAnimator(holder.cardView);

        holder.cardView.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                share(qiubai);
            }
        });

        holder.cardView.setOnLongClickListener(new View.OnLongClickListener()
        {
            @Override
            public boolean onLongClick(View view)
            {
                copyToClipboard(view, qiubai);
                return true;
            }
        });
    }

    private void copyToClipboard(View view, QiuShiBaiKe.Item qiubai)
    {
        ClipboardManager manager = (ClipboardManager) mContext.getSystemService(CLIPBOARD_SERVICE);
        ClipData clipData = ClipData.newPlainText("text", qiubai.getContent());
        manager.setPrimaryClip(clipData);
        SnackbarUtil.showMessage(view, "内容已复制到剪贴板");
    }

    private void share(final QiuShiBaiKe.Item qiubai)
    {
        new ShareAction((MainActivity)mContext)
                .setDisplayList(SHARE_MEDIA.WEIXIN, SHARE_MEDIA.WEIXIN_CIRCLE,
                        SHARE_MEDIA.QQ, SHARE_MEDIA.QZONE)
                .setShareboardclickCallback(new ShareBoardlistener() {
                    @Override
                    public void onclick(SnsPlatform snsPlatform, SHARE_MEDIA share_media) {
                        if (qiubai != null) {
                            ShareContent content = new ShareContent();
                            content.mTitle = mContext.getResources().getString(R.string.app_name);
                            content.mText = qiubai.getContent();
                            content.mTargetUrl = "http://www.qiushibaike.com/share/" + qiubai.getId();
                            new ShareAction((MainActivity)mContext)
                                    .setShareContent(content)
                                    .setPlatform(share_media)
                                    .setCallback(new CustomShareListener((MainActivity)mContext))
                                    .share();
                        }
                    }
                }).open(new ShareBoardConfig().setMenuItemBackgroundColor(ShareBoardConfig.BG_SHAPE_NONE));
    }

    @Override
    public int getItemCount()
    {
        return mQiubaiLists.size();
    }

    public void addLists(ArrayList<QiuShiBaiKe.Item> list)
    {
        // 判断返回的数据是否已存在
        if (mQiubaiLists.size() != 0 && list.size() != 0)
        {
            if (mQiubaiLists.get(0).getContent().equals(list.get(0).getContent()))
                return;
        }

        int size = mQiubaiLists.size();
        if (isLoading)
        {
            mQiubaiLists.addAll(list);
            notifyItemRangeInserted(size, list.size());
        }
        else
        {
            mQiubaiLists.addAll(0, list);
            notifyItemRangeInserted(0, list.size());
            mRecyclerView.scrollToPosition(0);
        }
//        notifyDataSetChanged();
    }

    public void clearQiubaiList()
    {
        mQiubaiLists.clear();
    }

    @Override
    public void onLoadStart()
    {
        if (isLoading)
            return;
        isLoading = true;
//        notifyItemInserted(getLoadingMoreItemPosition());
    }

    @Override
    public void onLoadFinish()
    {
        if (!isLoading)
            return;
//        notifyItemRemoved(getLoadingMoreItemPosition());
        isLoading = false;
    }

    private int getLoadingMoreItemPosition()
    {
        return isLoading ? getItemCount() - 1 : RecyclerView.NO_POSITION;
    }

    static class DuanziViewHolder extends RecyclerView.ViewHolder
    {
        @BindView(R.id.duanzi_author)
        TextView tvAuthor;

        @BindView(R.id.duanzi_time)
        TextView tvTime;

        @BindView(R.id.duanzi_content)
        TextView tvContent;

        View cardView;

        public DuanziViewHolder(View itemView)
        {
            super(itemView);
            cardView = itemView;
            ButterKnife.bind(this, itemView);
        }
    }
}
