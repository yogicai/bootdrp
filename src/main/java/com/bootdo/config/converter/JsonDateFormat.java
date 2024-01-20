package com.bootdo.config.converter;

import cn.hutool.core.date.DatePattern;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.extra.spring.SpringUtil;
import org.springframework.stereotype.Component;

import java.text.DateFormat;
import java.text.FieldPosition;
import java.text.ParsePosition;
import java.util.Date;

/**
 * 日期兼容标准时间格式，包括：
 * <pre>
 *     yyyy-MM-dd HH:mm:ss.SSSSSS
 *     yyyy-MM-dd HH:mm:ss.SSS
 *     yyyy-MM-dd HH:mm:ss
 *     yyyy-MM-dd HH:mm
 *     yyyy-MM-dd
 * </pre>
 *
 * @author L
 * @since 2024-01-19 11:04
 */
@Component
public class JsonDateFormat extends DateFormat {

    @Override
    public StringBuffer format(Date date, StringBuffer toAppendTo, FieldPosition fieldPosition) {

        return toAppendTo.append(DateUtil.format(date, DatePattern.NORM_DATETIME_PATTERN));
    }

    @Override
    public Date parse(String source, ParsePosition pos) {
        //设置解析位置为字符串长度，表示解析完成
        pos.setIndex(StrUtil.length(source));
        return DateUtil.parse(source);
    }

    @Override
    public Object clone() {
        return SpringUtil.getBean(JsonDateFormat.class);
    }
}