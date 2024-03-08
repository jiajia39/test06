package com.framework.xxljob.config;

import cn.hutool.http.HttpRequest;
import cn.hutool.http.HttpResponse;
import cn.hutool.json.JSONUtil;
import com.framework.xxljob.dto.HttpHeader;
import com.framework.xxljob.service.XxlJobService;
import com.framework.xxljob.service.impl.XxlJobServiceImpl;
import com.xxl.job.core.biz.model.ReturnT;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.net.HttpCookie;
import java.util.HashMap;
import java.util.Map;

@Slf4j
@Configuration
@ConditionalOnClass(XxlJobService.class)
@ConditionalOnProperty(prefix = "framework.xxl-job", value = "enable", havingValue = "true")
@EnableConfigurationProperties(XxlJobAdminProperties.class)
public class XxlJobAdminAutoConfigure {
    @Bean
    @ConditionalOnProperty(prefix = "framework.xxl-job", value = "enable", havingValue = "true")
    public XxlJobService xxlJobService(HttpHeader loginHeader, XxlJobAdminProperties xxlJobAdminProperties) {
        return new XxlJobServiceImpl(loginHeader, xxlJobAdminProperties);
    }

    @Bean("loginHeader")
    @ConditionalOnProperty(prefix = "framework.xxl-job", value = "enable", havingValue = "true")
    public HttpHeader httpRequest(XxlJobAdminProperties xxlJobAdminProperties) {
        try {
            String adminUrl = xxlJobAdminProperties.getAdminAddresses();
            String userName = xxlJobAdminProperties.getUserName();
            String password = xxlJobAdminProperties.getPassword();
            int connectionTimeOut = xxlJobAdminProperties.getConnectionTimeOut();
            Map<String, Object> paramMap = new HashMap<>();
            paramMap.put("userName", userName);
            paramMap.put("password", password);
            HttpResponse httpResponse = HttpRequest.post(adminUrl + "/login").form(paramMap).timeout(connectionTimeOut)
                    .execute();
            int status = httpResponse.getStatus();
            if (200 != status) {
                log.error("xxl-job登录失败");
                throw new RuntimeException("登录失败");
            }
            String body = httpResponse.body();
            ReturnT returnT = JSONUtil.toBean(body, ReturnT.class);
            if (200 != returnT.getCode()) {
                log.error("xxl-job登录失败：" + returnT.getMsg());
                throw new RuntimeException("登录失败:" + returnT.getMsg());
            }
            String cookieName = "XXL_JOB_LOGIN_IDENTITY";
            HttpCookie cookie = httpResponse.getCookie(cookieName);
            if (cookie == null) {
                throw new RuntimeException("没有获取到登录成功的cookie，请检查登录连接或者参数是否正确");
            }
            String headerValue = cookieName + "=" + cookie.getValue();
            HttpHeader cookie1 = new HttpHeader("Cookie", headerValue);
            log.info("xxl-job登录成功");
            return cookie1;
        } catch (Exception e) {
            log.error("xxl-job登录失败" + e.getMessage());
        }
        return null;
    }
}
