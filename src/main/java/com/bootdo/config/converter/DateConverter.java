package com.bootdo.config.converter;

import cn.hutool.core.date.DateUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.convert.converter.Converter;

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
 */
@Slf4j
public class DateConverter implements Converter<String, Date> {
    @Override
    public Date convert(String s) {
        return DateUtil.parse(s);
    }
}