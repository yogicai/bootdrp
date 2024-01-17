package com.bootdo.config;

import com.bootdo.config.converter.DateConverter;
import com.bootdo.config.properties.BootdoProperties;
import com.bootdo.core.filter.xss.XssFilter;
import com.google.common.collect.Maps;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.format.FormatterRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import javax.annotation.Resource;
import java.util.Map;

@Configuration
@Import({cn.hutool.extra.spring.SpringUtil.class})
class WebMvcConfig implements WebMvcConfigurer {
    @Resource
    private BootdoProperties bootdoProperties;

    /**
     * 静态资源映射
     */
    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler("/files/**").addResourceLocations("file:///" + bootdoProperties.getUploadPath());
    }

    /**
     * 参数转换器
     */
    @Override
    public void addFormatters(FormatterRegistry registry) {
        //自定义日期转换器兼容 yyyy-MM-dd HH:mm:ss、yyyy-MM-dd
        registry.addConverter(new DateConverter());
    }

    /**
     * xss过滤拦截器
     */
    @Bean
    public FilterRegistrationBean<XssFilter> xssFilterRegistrationBean() {
        FilterRegistrationBean<XssFilter> filterRegistrationBean = new FilterRegistrationBean<>();
        filterRegistrationBean.setFilter(new XssFilter());
        filterRegistrationBean.setOrder(1);
        filterRegistrationBean.setEnabled(true);
        filterRegistrationBean.addUrlPatterns("/*");
        Map<String, String> initParameters = Maps.newHashMap();
        initParameters.put("excludes", "/favicon.ico,/img/*,/js/*,/css/*");
        initParameters.put("isIncludeRichText", "true");
        filterRegistrationBean.setInitParameters(initParameters);
        return filterRegistrationBean;
    }

}