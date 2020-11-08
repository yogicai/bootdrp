package com.bootdo.common.config;

import com.bstek.ureport.console.UReportServlet;
import com.bstek.ureport.definition.datasource.BuildinDatasource;
import org.springframework.boot.web.servlet.ServletRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.ImportResource;

import javax.annotation.Resource;
import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;

/**
 * @author caiyz
 * @since 2020-07-12 23:45
 */
@Configuration
@ImportResource("classpath:ureport-console-context.xml")
public class UReportConfig {

    @Resource
    private DataSource dataSource;

    @Bean
    public ServletRegistrationBean buildUReport2Servlet(){
        return new ServletRegistrationBean(new UReportServlet(), "/ureport/*");
    }

    @Bean
    public BuildinDatasource buildinDatasource() {

        BuildinDatasource buildinDatasource = new BuildinDatasource() {
            Connection connection = null;
            @Override
            public String name() {
                return "buildinDatasource";
            }

            @Override
            public Connection getConnection() {
                try {
                    connection = dataSource.getConnection();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
                return connection;
            }
        };

        return buildinDatasource;
    }
}
