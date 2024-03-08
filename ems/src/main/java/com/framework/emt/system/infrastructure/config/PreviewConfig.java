package com.framework.emt.system.infrastructure.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Data
@Configuration
@ConfigurationProperties("framework.preview")
public class PreviewConfig {
    /**
     * 预览地址
     */
    private String url;
}
