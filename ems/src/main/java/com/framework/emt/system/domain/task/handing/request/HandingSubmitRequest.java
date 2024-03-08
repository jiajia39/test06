package com.framework.emt.system.domain.task.handing.request;


import com.framework.emt.system.infrastructure.common.validation.UniqueElementsValidator;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.util.List;

/**
 * 异常任务申请验收
 *
 * @author gaojia
 * @since 2023-08-16
 */
@Getter
@Setter
public class HandingSubmitRequest implements Serializable {

    @ApiModelProperty(value = "验收人id列表")
    @UniqueElementsValidator(message = "验收人id不能重复")
    private List<Long> checkUserIds;

}
