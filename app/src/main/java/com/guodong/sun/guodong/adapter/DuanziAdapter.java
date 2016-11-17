package com.guodong.sun.guodong.adapter;

import android.content.ActivityNotFoundException;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import com.guodong.sun.guodong.R;
import com.guodong.sun.guodong.entity.duanzi.NeiHanDuanZi;
import com.guodong.sun.guodong.listener.OnLoadMoreLisener;
import com.guodong.sun.guodong.uitls.AnimatorUtils;
import com.guodong.sun.guodong.uitls.DateTimeHelper;
import com.guodong.sun.guodong.uitls.SnackbarUtil;

import java.util.ArrayList;
import java.util.Date;

import butterknife.BindView;
import butterknife.ButterKnife;

import static android.content.Context.CLIPBOARD_SERVICE;

/**
 * Created by Administrator on 2016/10/10.
 */

public class DuanziAdapter extends RecyclerView.Adapter<DuanziAdapter.DuanziViewHolder> implements OnLoadMoreLisener
{

    private ArrayList<NeiHanDuanZi.Data> mDuanziLists = new ArrayList<>();
    private Context mContext;
    private LayoutInflater mInflater;
    private boolean isLoading;
    private RecyclerView mRecyclerView;

    public DuanziAdapter(Context context, RecyclerView recyclerView)
    {
        mContext = context;
        mRecyclerView = recyclerView;
        mInflater = LayoutInflater.from(context);
    }

    @Override
    public DuanziViewHolder onCreateViewHolder(ViewGroup parent, int viewType)
    {
        return new DuanziViewHolder(mInflater.inflate(R.layout.fragment_duanzi_item, parent, false));
    }

    @Override
    public void onBindViewHolder(DuanziViewHolder holder, int position)
    {
        final NeiHanDuanZi.Data duanzi = mDuanziLists.get(position);
        if (duanzi.getGroup().getUser() == null)
            holder.tvAuthor.setText("匿名用户");
        else
            holder.tvAuthor.setText(duanzi.getGroup().getUser().getName());
        holder.tvContent.setText(duanzi.getGroup().getText());
        holder.tvTime.setText(DateTimeHelper.getInterval(new Date((long) duanzi.getDisplay_time() * 1000)));

        AnimatorUtils.startSlideInLeftAnimator(holder.view);

        holder.view.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                share(view, duanzi);
            }
        });

        holder.view.setOnLongClickListener(new View.OnLongClickListener()
        {
            @Override
            public boolean onLongClick(View view)
            {
                copyToClipboard(view, duanzi);
                return true;
            }
        });
    }

    private void copyToClipboard(View view, NeiHanDuanZi.Data duanzi)
    {
        ClipboardManager manager = (ClipboardManager) mContext.getSystemService(CLIPBOARD_SERVICE);
        ClipData clipData = ClipData.newPlainText("text", duanzi.getGroup().getText());
        manager.setPrimaryClip(clipData);
        SnackbarUtil.showMessage(view, "内容已复制到剪贴板");
    }

    private void share(View view, NeiHanDuanZi.Data duanzi)
    {
        try
        {
            Intent i = new Intent().setAction(Intent.ACTION_SEND).setType("text/plain");
            String text = duanzi.getGroup().getText();
            i.putExtra(Intent.EXTRA_TEXT, text);
            mContext.startActivity(Intent.createChooser(i, "分享至"));
        } catch (ActivityNotFoundException ex)
        {
            SnackbarUtil.showMessage(view, "没有可分享的App");
        }
    }

    @Override
    public int getItemCount()
    {
        return mDuanziLists.size();
    }

    public void addLists(ArrayList<NeiHanDuanZi.Data> list)
    {
        if (mDuanziLists.size() != 0 && list.size() != 0)
        {
            if (mDuanziLists.get(0).getGroup().getText().equals(list.get(0).getGroup().getText()))
                return;
        }

        int size = mDuanziLists.size();
        if (isLoading)
        {
            mDuanziLists.addAll(list);
            notifyItemRangeInserted(size, list.size());
        }
        else
        {
            mDuanziLists.addAll(0, list);
            notifyItemRangeInserted(0, list.size());
            mRecyclerView.scrollToPosition(0);
        }
//        notifyDataSetChanged();
    }

    public void clearDuanziList()
    {
        mDuanziLists.clear();
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
        if (!isLoading) return;
//        notifyItemRemoved(getLoadingMoreItemPosition());
        isLoading = false;
    }

    private int getLoadingMoreItemPosition()
    {
        return isLoading ? getItemCount() - 1 : RecyclerView.NO_POSITION;
    }

    class DuanziViewHolder extends RecyclerView.ViewHolder
    {
        @BindView(R.id.duanzi_author)
        TextView tvAuthor;

        @BindView(R.id.duanzi_time)
        TextView tvTime;

        @BindView(R.id.duanzi_content)
        TextView tvContent;

        View view;

        public DuanziViewHolder(View itemView)
        {
            super(itemView);
            view = itemView;
            ButterKnife.bind(this, itemView);
        }
    }
}
