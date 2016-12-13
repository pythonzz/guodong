package com.guodong.sun.guodong.entity.meizi;

import java.io.Serializable;
import java.util.Date;

/**
 * Created by Administrator on 2016/10/9.
 */

public class Meizi implements Serializable {
    public long id;
    public String url;
    public String type;
    public String desc;
    public String who;
    public boolean used;
    public Date createdAt;
    public Date publishedAt;
}
