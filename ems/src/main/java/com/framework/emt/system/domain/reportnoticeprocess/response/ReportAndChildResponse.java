package com.framework.emt.system.domain.reportnoticeprocess.response;

import com.framework.emt.system.infrastructure.common.response.BaseUserResponse;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

/**
 * 上报流程、上报流程子集 响应体
 *
 * @author ds_C
 * @since 2023-07-12
 */
@Getter
@Setter
public class ReportAndChildResponse extends BaseUserResponse implements Serializable {

    @ApiModelProperty(value = "上报流程id")
    private Long id;

    @ApiModelProperty(value = "上报流程名称")
    private String name;

    @ApiModelProperty(value = "上报流程数量")
    private Long reportCount;

    @ApiModelProperty(value = "异常流程数量")
    private Long exceptionProcessCount;

    @ApiModelProperty(value = "上报流程下是否存在异常流程")
    private Boolean hasChildren;

}
