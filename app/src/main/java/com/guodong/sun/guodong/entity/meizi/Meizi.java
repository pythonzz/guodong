package com.guodong.sun.guodong.entity.meizi;

import com.litesuits.orm.db.annotation.Column;
import com.litesuits.orm.db.annotation.NotNull;
import com.litesuits.orm.db.annotation.PrimaryKey;
import com.litesuits.orm.db.annotation.Table;
import com.litesuits.orm.db.annotation.Unique;
import com.litesuits.orm.db.enums.AssignType;

import java.io.Serializable;
import java.util.Date;

/**
 * Created by Administrator on 2016/10/9.
 */

@Table("Meizi")
public class Meizi implements Serializable
{
    @PrimaryKey(AssignType.AUTO_INCREMENT) @Column("_id") public long id;
    @Column("url") public String url;
    @Column("type") public String type;
    @Column("desc") public String desc;
    @Column("who") public String who;
    @Column("used") public boolean used;
    @Column("createdAt") public Date createdAt;
    @Column("publishedAt") public Date publishedAt;
}
