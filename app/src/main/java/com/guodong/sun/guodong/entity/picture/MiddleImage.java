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
 * Last modified 2017-05-04 15:03:36
 *
 * GitHub:   https://github.com/guodongAndroid
 * Website:  http://www.sunxiaoduo.com
 * Email:    sun33919135@gmail.com
 * QQ:       33919135
 */

package com.guodong.sun.guodong.entity.picture;

import java.util.List;

public class MiddleImage {

    private int width;
    private int r_height;
    private int r_width;
    private String uri;
    private int height;
    private List<UrlList> url_list;

    public int getWidth() {
        return width;
    }

    public void setWidth(int width) {
        this.width = width;
    }

    public int getR_height() {
        return r_height;
    }

    public void setR_height(int r_height) {
        this.r_height = r_height;
    }

    public int getR_width() {
        return r_width;
    }

    public void setR_width(int r_width) {
        this.r_width = r_width;
    }

    public String getUri() {
        return uri;
    }

    public void setUri(String uri) {
        this.uri = uri;
    }

    public int getHeight() {
        return height;
    }

    public void setHeight(int height) {
        this.height = height;
    }

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
