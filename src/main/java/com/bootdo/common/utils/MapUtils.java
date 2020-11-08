package com.bootdo.common.utils;

import com.google.common.collect.Lists;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

/**
 * @author: yogiCai
 * @create: 2018-03-04 15:59
 **/
public class MapUtils extends org.apache.commons.collections.MapUtils{

    public static List getList(final Map map, final Object key) {
        if (map != null) {
            Object answer = map.get(key);
            if (answer != null) {
                return (List)answer;
            }
        }
        return Lists.newArrayList();
    }

    public static BigDecimal getBigDecimal(final Map map, final Object key) {
        return NumberUtils.toBigDecimal(getString(map, key));
    }

    public static String getStringB(final Map map, final Object key, final String defaultValue) {
        String value = getString(map, key, defaultValue);
        return "".equals(value) ? defaultValue : value;
    }
}
