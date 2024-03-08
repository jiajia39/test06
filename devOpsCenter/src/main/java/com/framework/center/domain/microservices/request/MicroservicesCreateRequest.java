package com.framework.center.domain.microservices.request;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * 微服务创建更新入参
 *
 * @author yankunw
 * @since 2023-04-12
 */
@Data
@ApiModel(value = "微服务入参", description = "微服务入参")
public class MicroservicesCreateRequest implements Serializable {
    /**
     * 微服务名称
     */
    @ApiModelProperty(value = "微服务名称")
    private String microservices;
    /**
     * 所属公司id
     */
    @ApiModelProperty(value = "所属公司id")
    private Long companyId;
    /**
     * 微服务ip
     */
    @ApiModelProperty(value = "微服务ip")
    private String microservicesHost;
    /**
     * 微服务端口
     */
    @ApiModelProperty(value = "微服务端口")
    private String microservicesPort;
    /**
     * 微服务中文名称
     */
    @ApiModelProperty(value = "微服务中文名称")
    private String microservicesName;
    /**
     * 微服务部署目录
     */
    @ApiModelProperty(value = "微服务部署目录")
    private String scriptPath;
    /**
     * 微服务执行脚本
     */
    @ApiModelProperty(value = "微服务执行脚本")
    private String scriptFile;
    /**
     * 微服务启动命令
     */
    @ApiModelProperty(value = "微服务启动命令")
    private String startCommand;
    /**
     * 微服务停止命令
     */
    @ApiModelProperty(value = "微服务停止命令")
    private String stopCommand;
    /**
     * 微服务重启命令
     */
    @ApiModelProperty(value = "微服务重启命令")
    private String restartCommand;
}
