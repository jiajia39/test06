package com.framework.xxljob.config;

import com.framework.common.property.config.YamlPropertySourceFactory;
import com.xxl.job.core.executor.impl.XxlJobSpringExecutor;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;

/**
 * @author yankunwu
 */
@Slf4j
@Data
@PropertySource(value = "classpath:framework-xxl-job.yml", encoding = "utf-8", factory = YamlPropertySourceFactory.class)
@Component
@ConfigurationProperties(prefix = "framework.xxl-job")
public class XxlJobAdminProperties {
    private String adminAddresses;
    private String accessToken;
    private String appName;
    private String address;
    private String ip;
    private int port;
    private String logPath;
    private int logRetentionDays;
    private String userName = "admin";
    private String password = "123456";
    private int connectionTimeOut = 5000;
    private boolean enable = true;

    @Bean
    @ConditionalOnProperty(prefix = "framework.xxl-job", value = "enable", havingValue = "true")
    public XxlJobSpringExecutor xxlJobExecutor() {
        log.info(">>>>>>>>>>> xxl-job config init.");
        XxlJobSpringExecutor xxlJobSpringExecutor = new XxlJobSpringExecutor();
        xxlJobSpringExecutor.setAdminAddresses(adminAddresses);
        xxlJobSpringExecutor.setAppname(appName);
        xxlJobSpringExecutor.setAddress(address);
        xxlJobSpringExecutor.setIp(ip);
        xxlJobSpringExecutor.setPort(port);
        xxlJobSpringExecutor.setAccessToken(accessToken);
        xxlJobSpringExecutor.setLogPath(logPath);
        xxlJobSpringExecutor.setLogRetentionDays(logRetentionDays);
        return xxlJobSpringExecutor;
    }
}
