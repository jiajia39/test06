package com.framework;

import com.framework.common.boot.FrameworkApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@EnableTransactionManagement
@SpringBootApplication
public class DevOpsCenterApplication {
    public static void main(String[] args) {
        FrameworkApplication.run("异常管理系统运维中心", DevOpsCenterApplication.class, args);
    }
}