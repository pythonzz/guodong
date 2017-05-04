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
 * Last modified 2017-05-04 15:05:52
 *
 * GitHub:   https://github.com/guodongAndroid
 * Website:  http://www.sunxiaoduo.com
 * Email:    sun33919135@gmail.com
 * QQ:       33919135
 */

package com.guodong.sun.guodong.uitls;

/**
 * Created by Administrator on 2016/12/15.
 */

public class StringUtils {

    public static String getUrlPicName(String s) {
        String str = "/guodong.webp";
        int index = s.lastIndexOf("/");
        if (index != -1) {
            str = s.substring(index, s.length());
            return str;
        }
        return str;
    }

    /**
     * 千转万、十万
     *
     * @param s
     * @return
     */
    public static String getStr2W(int s) {
        String str = String.valueOf(s);
        return getStr2W(str);
    }

    /**
     * 千转万、十万
     *
     * @param str
     * @return
     */
    public static String getStr2W(String str) {
        int length = str.length();
        if (length == 5) {
            str = str.substring(0, 1) + "." + str.substring(1, 2) + "万";
            return str;
        } else if (length == 6) {
            str = str.substring(0, 2) + "万";
            return str;
        }
        return str;
    }
}
