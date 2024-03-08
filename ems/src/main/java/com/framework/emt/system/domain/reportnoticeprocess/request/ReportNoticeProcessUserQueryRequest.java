package com.framework.emt.system.domain.reportnoticeprocess.request;

import com.framework.core.mybatisplus.support.Query;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotNull;
import java.io.Serializable;

/**
 * 上报流程推送 查询条件
 *
 * @author ds_C
 * @since 2023-07-24
 */
@Getter
@Setter
public class ReportNoticeProcessUserQueryRequest extends Query implements Serializable {

    @NotNull(message = "上报流程id不能为空")
    @ApiModelProperty(value = "上报流程id", required = true)
    private Long reportNoticeProcessId;

}
