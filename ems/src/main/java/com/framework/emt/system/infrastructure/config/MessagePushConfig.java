package com.framework.emt.system.infrastructure.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Data
@Configuration
@ConfigurationProperties("framework.message.push")
public class MessagePushConfig {
    /**
     * uniPush推送地址
     */
    private String uniPushUrl="https://fc-mp-deb63d66-f470-45ab-8245-51d5890e9902.next.bspapp.com/send";

    private MessageWxConfig wx;
}
