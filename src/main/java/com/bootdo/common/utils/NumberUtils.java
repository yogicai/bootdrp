package com.bootdo.common.utils;

import cn.hutool.core.util.StrUtil;

import java.math.BigDecimal;

/**
 * @Author: yogiCai
 */
public class NumberUtils extends org.apache.commons.lang.math.NumberUtils{

    public static BigDecimal toBigDecimal(BigDecimal price) {
        return price == null ? BigDecimal.ZERO : price;
    }

    public static BigDecimal toBigDecimal(String price) {
        return new BigDecimal(StrUtil.isEmpty(price) ? "0" : price);
    }

    public static BigDecimal add(BigDecimal b1, BigDecimal b2) {
        return add(b1, b2, 2);
    }

    public static BigDecimal add(BigDecimal b1, BigDecimal b2, int scale) {
        b1 = b1 == null ? BigDecimal.ZERO : b1;
        b2 = b2 == null ? BigDecimal.ZERO : b2;
        return b1.add(b2).setScale(scale, BigDecimal.ROUND_HALF_UP);
    }

    public static BigDecimal div(BigDecimal b1, BigDecimal b2) {
        return div(b1, b2, 2);
    }

    public static BigDecimal div(BigDecimal b1, BigDecimal b2, int scale) {
        if (b1 == null || b2 == null || b2.compareTo(BigDecimal.ZERO) <= 0) {
            return BigDecimal.ZERO;
        }
        return b1.divide(b2, scale, BigDecimal.ROUND_HALF_UP);
    }

    public static BigDecimal mul(BigDecimal b1, BigDecimal b2) {
        return mul(b1, b2, 2);
    }

    public static BigDecimal mul(BigDecimal b1, BigDecimal b2, int scale) {
        if (b1 == null || b2 == null) {
            return BigDecimal.ZERO;
        }
        return b1.multiply(b2).setScale(scale, BigDecimal.ROUND_HALF_UP);
    }

    public static BigDecimal subtract(BigDecimal b1, BigDecimal b2) {
        return subtract(b1, b2, 2);
    }

    public static BigDecimal subtract(BigDecimal b1, BigDecimal b2, int scale) {
        b1 = b1 == null ? BigDecimal.ZERO : b1;
        b2 = b2 == null ? BigDecimal.ZERO : b2;
        return b1.subtract(b2).setScale(scale, BigDecimal.ROUND_HALF_UP);
    }


    public static Integer roundInterval(BigDecimal b2, int scale) {
        if (scale <= 0 || b2 == null) return b2.intValue();
        int interval = Double.valueOf(Math.ceil(b2.doubleValue() / scale)).intValue();
        int intervalLen = String.valueOf(interval).length() - 1;
        int factor = Double.valueOf(Math.pow(10, intervalLen)).intValue();
        int intervalVal = Double.valueOf(Math.ceil(interval * 1.0 / factor)).intValue() * factor;
        return intervalVal;
    }

    public static Integer roundIntervalCeil(BigDecimal b2, int scale, int scaleCeil) {
        return roundInterval(b2, scale) * scaleCeil;
    }

    public static void main(String[] args) {
        System.out.println(roundIntervalCeil(BigDecimal.valueOf(7), 5, 6));
    }
}
