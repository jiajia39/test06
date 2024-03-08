package com.framework.emt.system.infrastructure.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Data
@Configuration
@ConfigurationProperties("framework.message.send")
public class MessageConfig {
    /**
     * 线程数量
     */
    private Integer threadCount;

    /**
     * 线程是否运行
     */
    private boolean threadRun=false;
}
