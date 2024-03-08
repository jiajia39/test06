package com.framework.center.domain.sync.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Data
@Configuration
@ConfigurationProperties("framework.sso")
public class SsoConfig {
    /**
     * 访问地址
     */
    private String url;
    /**
     * 系统编号
     */
    private String systemId;
    /**
     * app key
     */
    private String appKey;
}
