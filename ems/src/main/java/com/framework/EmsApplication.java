package com.framework;

import com.framework.common.boot.FrameworkApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@EnableTransactionManagement
@SpringBootApplication
public class EmsApplication {
    public static void main(String[] args) {
        FrameworkApplication.run("异常管理系统后台", EmsApplication.class, args);
    }
}