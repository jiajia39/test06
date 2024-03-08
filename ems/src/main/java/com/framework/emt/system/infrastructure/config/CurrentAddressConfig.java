package com.framework.emt.system.infrastructure.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Data
@Configuration
@ConfigurationProperties("framework.current-address")
public class CurrentAddressConfig {
    /**
     * 当前网站访问地址前缀
     */
    private String url;
}
