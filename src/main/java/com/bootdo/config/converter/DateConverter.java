package com.bootdo.config.converter;

import cn.hutool.core.date.DateUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.convert.converter.Converter;

import java.util.Date;

/**
 * 自定义日期转换器
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