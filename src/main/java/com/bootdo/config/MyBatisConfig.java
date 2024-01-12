package com.bootdo.config;

import com.baomidou.mybatisplus.core.handlers.MetaObjectHandler;
import com.baomidou.mybatisplus.extension.plugins.MybatisPlusInterceptor;
import com.baomidou.mybatisplus.extension.plugins.inner.BlockAttackInnerInterceptor;
import com.baomidou.mybatisplus.extension.plugins.inner.PaginationInnerInterceptor;
import com.bootdo.core.mybatis.dbid.SnowyDatabaseIdProvider;
import com.bootdo.core.mybatis.fieldfill.CustomMetaObjectHandler;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * mybatis扩展插件配置
 *
 * @author xuyuxiang
 * @date 2020/3/18 10:49
 */
@Configuration
public class MyBatisConfig {

    /**
     * mybatis-plus分页插件
     *
     * @author xuyuxiang
     * @date 2020/3/31 15:42
     */
    @Bean
    public MybatisPlusInterceptor mybatisPlusInterceptor() {
        MybatisPlusInterceptor interceptor = new MybatisPlusInterceptor();
        interceptor.addInnerInterceptor(new PaginationInnerInterceptor());
        interceptor.addInnerInterceptor(new BlockAttackInnerInterceptor());
        return interceptor;
    }


    /**
     * 自定义公共字段自动注入
     *
     * @author xuyuxiang
     * @date 2020/3/31 15:42
     */
    @Bean
    public MetaObjectHandler metaObjectHandler() {
        return new CustomMetaObjectHandler();
    }

    /**
     * 数据库id选择器
     *
     * @author xuyuxiang
     * @date 2020/6/20 21:23
     */
    @Bean
    public SnowyDatabaseIdProvider snowyDatabaseIdProvider() {
        return new SnowyDatabaseIdProvider();
    }

}
