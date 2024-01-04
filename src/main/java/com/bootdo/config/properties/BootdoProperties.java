package com.bootdo.config.properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * @author L
 */
@Data
@Component
@ConfigurationProperties(prefix = "bootdo")
public class BootdoProperties {

    /**
     * 上传路径
     */
    private String uploadPath;

}
