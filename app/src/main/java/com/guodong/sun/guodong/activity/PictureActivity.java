package com.guodong.sun.guodong.activity;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.support.v4.view.ViewCompat;
import android.support.v7.app.AlertDialog;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.guodong.sun.guodong.R;
import com.guodong.sun.guodong.base.AbsBaseActivity;
import com.guodong.sun.guodong.uitls.Once;
import com.guodong.sun.guodong.uitls.SnackbarUtil;

import java.io.File;
import java.io.FileOutputStream;
import java.util.Date;

import butterknife.BindView;
import uk.co.senab.photoview.PhotoViewAttacher;

/**
 * Created by Administrator on 2016/10/11.
 */

public class PictureActivity extends AbsBaseActivity
{
    public static final String EXTRA_IMAGE_URL = "image";
    public static final String TRANSIT_PIC = "picture";

    String mImageUrl;
    PhotoViewAttacher mPhotoViewAttacher;

    @BindView(R.id.picture_layout)
    RelativeLayout mLayout;

    @BindView(R.id.picture_iv)
    ImageView mPicture;

    public static Intent newIntent(Context context, String url)
    {
        Intent intent = new Intent(context, PictureActivity.class);
        intent.putExtra(EXTRA_IMAGE_URL, url);
        return intent;
    }

    @Override
    protected void initViews(Bundle savedInstanceState)
    {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP)
        {
            View decorView = getWindow().getDecorView();
            int option = View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN | View.SYSTEM_UI_FLAG_LAYOUT_STABLE;
            decorView.setSystemUiVisibility(option);
            getWindow().setStatusBarColor(Color.TRANSPARENT);
        }
        ViewCompat.setTransitionName(mPicture, TRANSIT_PIC);
        mImageUrl = getIntent().getStringExtra(EXTRA_IMAGE_URL);
        Glide.with(this)
                .load(mImageUrl)
                .crossFade(2000)
                .centerCrop()
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .into(mPicture);
        setupPhotoAttacher();
        new Once(this).show("提示", new Once.OnceCallback()
        {
            @Override
            public void onOnce()
            {
                SnackbarUtil.showMessage(mPicture, "单击图片返回, 双击图片放大, 长按图片保存");
            }
        });
    }

    @Override
    protected int getLayoutId()
    {
        return R.layout.activity_picture;
    }

    private void setupPhotoAttacher() {
        mPhotoViewAttacher = new PhotoViewAttacher(mPicture);
        mPhotoViewAttacher.setOnViewTapListener(new PhotoViewAttacher.OnViewTapListener()
        {
            @Override
            public void onViewTap(View view, float x, float y)
            {
                onBackPressed();
            }
        });

        mPhotoViewAttacher.setOnLongClickListener(new View.OnLongClickListener()
        {
            @Override
            public boolean onLongClick(View view)
            {
                createDialog();
                return true;
            }
        });
    }

    private void createDialog()
    {
        new AlertDialog.Builder(PictureActivity.this)
                .setMessage("保存到手机?")
                .setNegativeButton(android.R.string.cancel, new DialogInterface.OnClickListener()
                {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i)
                    {
                        dialogInterface.dismiss();
                    }
                })
                .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener()
                {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i)
                    {
                        saveImage();
                        dialogInterface.dismiss();
                    }
                })
                .show();
    }

    private void saveImage() {
        File externalStorageDirectory = Environment.getExternalStorageDirectory();
        File directory = new File(externalStorageDirectory, getString(R.string.app_name));
        if (!directory.exists())
            directory.mkdir();
        Bitmap drawingCache = mPhotoViewAttacher.getImageView().getDrawingCache();
        try {
            File file = new File(directory, new Date().getTime() + ".jpg");
            FileOutputStream fos = new FileOutputStream(file);
            drawingCache.compress(Bitmap.CompressFormat.JPEG, 100, fos);
            fos.flush();
            fos.close();
            // 通知图库刷新
            Intent intent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
            Uri uri = Uri.fromFile(file);
            intent.setData(uri);
            sendBroadcast(intent);
            SnackbarUtil.showMessage(mPicture, "已保存到" + file.getAbsolutePath());
        } catch (Exception e) {
            e.printStackTrace();
            SnackbarUtil.showMessage(mPicture, "啊偶, 出错了", "再试试", new View.OnClickListener()
            {
                @Override
                public void onClick(View view)
                {
                    saveImage();
                }
            });
        }
    }

    @Override
    protected void onDestroy()
    {
        super.onDestroy();
        if (mPhotoViewAttacher != null)
        {
            mPhotoViewAttacher.cleanup();
            mPhotoViewAttacher = null;
        }
    }
}
