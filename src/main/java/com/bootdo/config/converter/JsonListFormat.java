package com.bootdo.config.converter;

import cn.hutool.core.util.StrUtil;
import com.bootdo.config.converter.JsonListFormat.JacksonListDeserializer;
import com.fasterxml.jackson.annotation.JacksonAnnotationsInside;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.JsonToken;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.ser.ContextualSerializer;

import java.io.IOException;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.util.ArrayList;
import java.util.List;

/**
 * 日期范围查询格式转换
 * yyyy-MM-dd转 yyyy-MM-dd HH:mm:ss
 *
 * @author L
 * @since 2024-01-19 11:04
 */
@Retention(RetentionPolicy.RUNTIME)
@JacksonAnnotationsInside
@JsonDeserialize(using = JacksonListDeserializer.class)
public @interface JsonListFormat {

    class JacksonListDeserializer extends JsonDeserializer<Object> implements ContextualSerializer {
        @Override
        public List<Long> deserialize(JsonParser p, DeserializationContext ctxt) throws IOException, JsonProcessingException {
            List<Long> list = new ArrayList<>();

            JsonToken jsonToken = p.getCurrentToken();

            if (JsonToken.START_ARRAY.equals(jsonToken)) {
                // 处理数组
                while (p.currentToken() != JsonToken.END_ARRAY && p.currentToken() != JsonToken.FIELD_NAME) {
                    JsonToken token = p.nextToken();
                    if ((token == JsonToken.VALUE_STRING || token == JsonToken.VALUE_NUMBER_INT) && StrUtil.isNotBlank(p.getValueAsString())) {
                        list.add(p.getValueAsLong());
                    }
                }
            } else if (JsonToken.VALUE_STRING.equals(jsonToken) && StrUtil.isNotBlank(p.getValueAsString())) {
                // 处理单个字符串
                list.add(p.getValueAsLong());
                // 移动到下一个令牌
                // jackson BeanDeserializer 398会移到下一个token
                //p.nextToken();
            }

            return list;
        }

        @Override
        public JsonSerializer<?> createContextual(SerializerProvider prov, BeanProperty property) throws JsonMappingException {
            return null;
        }
    }

}