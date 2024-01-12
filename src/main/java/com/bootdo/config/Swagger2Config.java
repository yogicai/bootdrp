package com.bootdo.config;

import cn.hutool.core.util.ClassUtil;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Contact;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2WebMvc;

/**
 * ${DESCRIPTION}
 *
 * @author edison
 * @create 2017-01-02 23:53
 */
@Configuration
@EnableSwagger2WebMvc
public class Swagger2Config {

    @Bean
    public Docket createRestApi() {
        return new Docket(DocumentationType.SWAGGER_2)
                .apiInfo(apiInfo())
                .groupName("系统模块")
                .select()
                .apis(RequestHandlerSelectors.basePackage("com.bootdo.modular.system"))
                .build();
    }

    @Bean
    public Docket bizApi() {
        return new Docket(DocumentationType.SWAGGER_2)
                .apiInfo(apiInfo())
                .groupName("业务模块")
                .select()
                .apis( input -> !ClassUtil.getPackage(input.declaringClass()).startsWith("com.bootdo.modular.system"))
                .build();
    }

    private ApiInfo apiInfo() {
        return new ApiInfoBuilder()
                .title("功能测试")
                .contact(new Contact("Edison", "xxx@qq.com", "xxx@qq.com"))
                .version("1.0")
                .description("API 描述")
                .build();
    }
}