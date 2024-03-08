package com.framework.emt.system.infrastructure.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Data
@Configuration
@ConfigurationProperties("ding-talk")
public class DingTalkConfig {
    /**
     * clientId
     */
    private String clientId;

    private String clientSecret;

    private Long agentId;

    private String messageUrl;
}
