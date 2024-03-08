package com.framework.center.domain.login.response;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

@Data
@ApiModel(value = "账户的公司响应信息", description = "账户的公司响应信息")
public class MyCompanyVo implements Serializable {
    /**
     * 公司id
     */
    @ApiModelProperty(value = "公司id")
    private Long id;

    /**
     * 公司名称
     */
    @ApiModelProperty(value = "公司名称")
    private String name;

    /**
     * 公司名称
     */
    @ApiModelProperty(value = "微服务名称（应为字母或下划线组成）")
    private String microservices;
}
