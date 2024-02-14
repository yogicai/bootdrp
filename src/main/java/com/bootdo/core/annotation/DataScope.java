package com.bootdo.core.annotation;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.lang.annotation.*;

/**
 * 数据权限注解
 *
 * @author L
 * @since 2024-02-07 11:37
 */
@Inherited
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.METHOD})
public @interface DataScope {

    DataType[] type() default DataType.SHOP;

    @AllArgsConstructor
    @Getter
    enum DataType {

        SHOP("shop_no"),
        ;

        private final String column;
    }

}
