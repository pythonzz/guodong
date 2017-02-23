package com.guodong.sun.guodong.entity.picture.gifvideo;

public class GifVideo {

    private int video_width;//   gif视频宽度
    private int video_height ;//   gif视频高度
    private String mp4_url   ;//      gif视频480P地址

    public int getVideo_width() {
        return video_width;
    }

    public void setVideo_width(int video_width) {
        this.video_width = video_width;
    }

    public int getVideo_height() {
        return video_height;
    }

    public void setVideo_height(int video_height) {
        this.video_height = video_height;
    }

    public String getMp4_url() {
        return mp4_url;
    }

    public void setMp4_url(String mp4_url) {
        this.mp4_url = mp4_url;
    }
}
