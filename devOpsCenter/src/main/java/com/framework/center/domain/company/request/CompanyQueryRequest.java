package com.framework.center.domain.company.request;

import com.framework.core.mybatisplus.support.Query;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;

/**
 * 公司列表查询入参
 *
 * @author yankunw
 * @since 2023-04-12
 */
@EqualsAndHashCode(callSuper = true)
@Data
@ApiModel(value = "公司列表查询", description = "公司列表查询")
public class CompanyQueryRequest extends Query implements Serializable {
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
