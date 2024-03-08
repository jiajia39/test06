package com.framework.center.domain.microservices.request;

import com.framework.core.mybatisplus.support.Query;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;

/**
 * 微服务查询入参
 *
 * @author yankunw
 * @since 2023-04-12
 */
@EqualsAndHashCode(callSuper = true)
@Data
@ApiModel(value = "微服务列表查询", description = "微服务列表查询")
public class MicroservicesQueryRequest extends Query implements Serializable {
    /**
     * 公司名称
     */
    @ApiModelProperty(value = "公司名称")
    private String name;

    /**
     * 微服务名称（应为字母或下划线组成）
     */
    @ApiModelProperty(value = "微服务名称")
    private String microservices;

}
