package com.bootdo.common.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.thymeleaf.spring5.view.ThymeleafViewResolver;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by PrimaryKey on 17/2/4.
 */
@Configuration
public class ThymeleafConfig {
    @Value("${configure.cdn.echarts}")
    private String echarts;
    @Value("${configure.cdn.bootstrap}")
    private String bootstrap;
    @Value("${configure.cdn.bootstrapCss}")
    private String bootstrapCss;
    @Value("${configure.cdn.jquery}")
    private String jquery;
    @Value("${configure.cdn.jqueryUI}")
    private String jqueryUI;
    @Value("${configure.cdn.math}")
    private String math;

    @Bean
    public ThymeleafViewResolver configureThymeleafStaticVars(ThymeleafViewResolver viewResolver) {
        if(viewResolver != null) {
            Map<String, Object> vars = new HashMap<>();
            vars.put("echartsUrl", echarts);
            vars.put("bootstrapUrl", bootstrap);
            vars.put("bootstrapCssUrl", bootstrapCss);
            vars.put("jqueryUrl", jquery);
            vars.put("jqueryUIUrl", jqueryUI);
            vars.put("mathUrl", math);
            viewResolver.setStaticVariables(vars);
        }
        return viewResolver;
    }
}

