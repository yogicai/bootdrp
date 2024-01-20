package com.bootdo.config.converter;

import cn.hutool.core.date.DateUtil;
import com.bootdo.config.converter.JsonDateRangeFormat.JacksonDateDeserializer;
import com.fasterxml.jackson.annotation.JacksonAnnotationsInside;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

import java.io.IOException;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.util.Date;

/**
 * 日期范围查询格式转换
 * yyyy-MM-dd转 yyyy-MM-dd HH:mm:ss
 *
 * @author L
 * @since 2024-01-19 11:04
 */
@Retention(RetentionPolicy.RUNTIME)
@JacksonAnnotationsInside
@JsonDeserialize(using = JacksonDateDeserializer.class)
public @interface JsonDateRangeFormat {

    class JacksonDateDeserializer extends JsonDeserializer<Date> {
        @Override
        public Date deserialize(JsonParser p, DeserializationContext ctxt) throws IOException, JsonProcessingException {
            return DateUtil.parse(p.getValueAsString());
        }
    }

}