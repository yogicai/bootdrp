package com.bootdo.config;

import com.bootdo.config.properties.ThymeleafProperties;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.thymeleaf.spring5.view.ThymeleafViewResolver;

import java.util.HashMap;
import java.util.Map;

/**
 * @author caiyz
 * @since 2017-02-04 08:56
 */
@Configuration
public class ThymeleafConfig {

    @Bean
    @ConfigurationProperties(prefix = "configure.cdn")
    public ThymeleafProperties thymeleafProperties() {
        return new ThymeleafProperties();
    }

    @Bean
    public ThymeleafViewResolver configureThymeleafStaticVars(ThymeleafViewResolver viewResolver, ThymeleafProperties thymeleafProperties) {
        if(viewResolver != null) {
            Map<String, Object> vars = new HashMap<>();
            vars.put("echartsUrl", thymeleafProperties.getEcharts());
            vars.put("bootstrapUrl", thymeleafProperties.getBootstrap());
            vars.put("bootstrapCssUrl", thymeleafProperties.getBootstrapCss());
            vars.put("jqueryUrl", thymeleafProperties.getJquery());
            vars.put("jqueryUIUrl", thymeleafProperties.getJqueryUI());
            vars.put("mathUrl", thymeleafProperties.getMath());
            viewResolver.setStaticVariables(vars);
        }
        return viewResolver;
    }
}

