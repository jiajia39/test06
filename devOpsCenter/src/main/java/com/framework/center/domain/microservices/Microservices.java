package com.framework.center.domain.microservices;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.framework.core.tenant.entity.TenantEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;

/**
 * 微服务表
 *
 * @TableName mss_microservices
 */
@EqualsAndHashCode(callSuper = true)
@TableName(value = "mss_microservices")
@Data
public class Microservices extends TenantEntity implements Serializable {
    /**
     * 微服务名称
     */
    @TableField(value = "microservices")
    private String microservices;
    /**
     * 所属公司id
     */
    @TableField(value = "company_id")
    private Long companyId;
    /**
     * 微服务ip
     */
    @TableField(value = "microservices_host")
    private String microservicesHost;
    /**
     * 微服务端口
     */
    @TableField(value = "microservices_port")
    private String microservicesPort;
    /**
     * 微服务中文名称
     */
    @TableField(value = "microservices_name")
    private String microservicesName;
    /**
     * 微服务部署目录
     */
    @TableField(value = "script_path")
    private String scriptPath;
    /**
     * 微服务执行脚本
     */
    @TableField(value = "script_file")
    private String scriptFile;
    /**
     * 微服务启动命令
     */
    @TableField(value = "start_command")
    private String startCommand;
    /**
     * 微服务停止命令
     */
    @TableField(value = "stop_command")
    private String stopCommand;
    /**
     * 微服务重启命令
     */
    @TableField(value = "restart_command")
    private String restartCommand;
}