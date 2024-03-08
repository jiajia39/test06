package com.framework.xxljob.model;

import lombok.Data;

import java.util.Date;

/**
 * @author yankunwu
 */
@Data
public class JobGroup {
    private int id;
    private String appName;
    private String title;
    /**
     * 执行器地址类型：0=自动注册、1=手动录入
     */
    private int addressType;
    /**
     * 执行器地址列表，多地址逗号分隔(手动录入)
     */
    private String addressList;
    private Date updateTime;
}
