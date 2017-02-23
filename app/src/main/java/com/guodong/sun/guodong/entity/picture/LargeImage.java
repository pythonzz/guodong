package com.guodong.sun.guodong.entity.picture;


import java.util.List;

public class LargeImage {

    private List<UrlList> url_list;

    public List<UrlList> getUrl_list() {
        return url_list;
    }

    public void setUrl_list(List<UrlList> url_list) {
        this.url_list = url_list;
    }

    public static class UrlList {
        private String url;

        public String getUrl() {
            return url;
        }

        public void setUrl(String url) {
            this.url = url;
        }
    }
}
