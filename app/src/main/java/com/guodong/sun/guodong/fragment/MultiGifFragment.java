package com.guodong.sun.guodong.fragment;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.SizeReadyCallback;
import com.bumptech.glide.request.target.Target;
import com.guodong.sun.guodong.R;
import com.guodong.sun.guodong.activity.MultiGifActivity;
import com.guodong.sun.guodong.activity.MultiPictureActivity;
import com.guodong.sun.guodong.activity.MyApplication;
import com.guodong.sun.guodong.glide.ProgressTarget;
import com.guodong.sun.guodong.uitls.AlxGifHelper;
import com.guodong.sun.guodong.uitls.Once;
import com.guodong.sun.guodong.uitls.SnackbarUtil;
import com.shizhefei.view.largeimage.LargeImageView;
import com.shizhefei.view.largeimage.factory.FileBitmapDecoderFactory;
import com.trello.rxlifecycle.components.support.RxFragment;

import java.io.File;
import java.io.FileOutputStream;
import java.util.Date;

import butterknife.BindView;
import butterknife.ButterKnife;
import pl.droidsonroids.gif.GifImageView;

/**
 * Created by Administrator on 2016/12/17.
 */

public class MultiGifFragment extends RxFragment {

    private static final String TAG = MultiGifFragment.class.getSimpleName();
    private String mImageUrl;
    private int width;
    private int height;

    @BindView(R.id.gif_photo_view)
    GifImageView mGifImageView;

    @BindView(R.id.tv_progress)
    TextView mTextView;

    @BindView(R.id.progress_wheel)
    ProgressBar mProgressBar;

    private Bitmap mBitmap;

    public static MultiGifFragment newInstance(String imageUrl, int width, int height) {
        MultiGifFragment f = new MultiGifFragment();
        Bundle args = new Bundle();
        args.putString("url", imageUrl);
        args.putInt("width", width);
        args.putInt("height", height);
        f.setArguments(args);
        return f;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mImageUrl = getArguments() != null ? getArguments().getString("url") : null;
        width = getArguments() != null ? getArguments().getInt("width", 0) : 0;
        height = getArguments() != null ? getArguments().getInt("height", 0) : 0;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_multi_gif, container, false);
        ButterKnife.bind(this, view);
        new Once(getContext()).show("提示", new Once.OnceCallback() {
            @Override
            public void onOnce() {
                SnackbarUtil.showMessage(mGifImageView, "单击图片返回, 双击放大, 长按图片保存");
            }
        });
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {

//        RelativeLayout.LayoutParams lp = (RelativeLayout.LayoutParams) mGifImageView.getLayoutParams();
//        lp.height = MyApplication.ScreenWidth * height / width;
//        mGifImageView.setLayoutParams(lp);

        final String path = AlxGifHelper.displayImage(mImageUrl, mGifImageView, mProgressBar, mTextView);

        mGifImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((MultiGifActivity) getActivity()).onBackPressed();
            }
        });

        mGifImageView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                if (path != null) {
                    mBitmap = BitmapFactory.decodeFile(path);
                    if (mBitmap != null)
                        createDialog();
                }
                return true;
            }
        });

    }

    private void createDialog() {
        new AlertDialog.Builder(getContext()).setMessage("保存到手机?").setNegativeButton(android.R.string.cancel, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
            }
        }).setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                saveImage();
                dialogInterface.dismiss();
            }
        }).show();
    }

    private void saveImage() {
        File externalStorageDirectory = Environment.getExternalStorageDirectory();
        File directory = new File(externalStorageDirectory, getString(R.string.app_name));
        if (!directory.exists())
            directory.mkdir();
        try {
            File file = new File(directory, new Date().getTime() + ".gif");
            FileOutputStream fos = new FileOutputStream(file);
            mBitmap.compress(Bitmap.CompressFormat.JPEG, 100, fos);
            fos.flush();
            fos.close();
            // 通知图库刷新
            Intent intent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
            Uri uri = Uri.fromFile(file);
            intent.setData(uri);
            getContext().sendBroadcast(intent);
            SnackbarUtil.showMessage(mGifImageView, "已保存到" + file.getAbsolutePath());
        } catch (Exception e) {
            e.printStackTrace();
            Log.e(TAG, "saveImage: " + e.getMessage());
            SnackbarUtil.showMessage(mGifImageView, "啊偶, 出错了", "再试试", new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    saveImage();
                }
            });
        }
    }
}
