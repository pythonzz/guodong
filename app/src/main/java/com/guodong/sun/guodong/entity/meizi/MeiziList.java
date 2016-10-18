package com.guodong.sun.guodong.entity.meizi;

import java.util.ArrayList;

/**
 * Created by Administrator on 2016/10/9.
 */

public class MeiziList extends BaseEntity
{
    public ArrayList<Meizi> results;

    public ArrayList<Meizi> getResults() {
        return results;
    }

    public void setResults(ArrayList<Meizi> results) {
        this.results = results;
    }
}
