package com.guodong.sun.guodong.uitls;

/**
 * Created by Administrator on 2016/9/24.
 */
public class MapUtils
{
    // Suppress default constructor for noninstantiability
    private MapUtils()
    {
        throw new AssertionError();
    }

    public static <K, V> java.util.HashMap<K, V> newHashMap()
    {
        return new java.util.HashMap<K, V>();
    }
}
