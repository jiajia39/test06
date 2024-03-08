package com.framework.emt.system.domain.reportnoticeprocess.request;

import cn.hutool.core.collection.CollectionUtil;
import com.framework.emt.system.infrastructure.common.validation.UniqueElementsValidator;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.Collections;
import java.util.List;

/**
 * 上报流程推送 创建参数
 *
 * @author ds_C
 * @since 2023-07-12
 */
@Getter
@Setter
public class ReportNoticeProcessUserCreateRequest implements Serializable {

    @NotNull(message = "上报流程id不能为空")
    @ApiModelProperty(value = "上报流程id", required = true)
    private Long reportNoticeProcessId;

    @NotNull(message = "超出时限不能为空")
    @ApiModelProperty(value = "超出时限 单位:分钟", required = true)
    private Integer timeLimit;

    @NotEmpty(message = "上报人id列表不能为空")
    @UniqueElementsValidator(message = "上报人id不能重复")
    @ApiModelProperty(value = "上报人id列表", required = true)
    private List<Long> receiveUserIds;

    public ReportNoticeProcessUserCreateRequest init() {
        if (CollectionUtil.isEmpty(receiveUserIds)) {
            this.receiveUserIds = Collections.emptyList();
        }
        return this;
    }

}
