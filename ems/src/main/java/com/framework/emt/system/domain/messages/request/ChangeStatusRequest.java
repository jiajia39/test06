package com.framework.emt.system.domain.messages.request;

import com.framework.emt.system.infrastructure.common.validation.UniqueElementsValidator;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotEmpty;
import java.io.Serializable;
import java.util.List;

@Data
@ApiModel(value = "修改状态参数", description = "修改状态参数")
public class ChangeStatusRequest implements Serializable {
    @NotEmpty(message = "消息id列表不能为空！")
    @UniqueElementsValidator(message = "消息id不能重复")
    @ApiModelProperty(value = "消息id列表")
    private List<Long> messageIdList;
}
