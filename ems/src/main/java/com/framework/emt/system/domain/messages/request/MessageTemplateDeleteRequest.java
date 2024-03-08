package com.framework.emt.system.domain.messages.request;

import com.framework.emt.system.infrastructure.common.validation.UniqueElementsValidator;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotEmpty;
import java.io.Serializable;
import java.util.List;

/**
 * 消息模板创建对象
 *
 * @author yankunw
 * @since 2023-07-20
 */
@Getter
@Setter
@ApiModel(value = "消息模板删除参数", description = "消息模板删除参数")
public class MessageTemplateDeleteRequest implements Serializable {
    @NotEmpty(message = "模板id列表不能为空！")
    @UniqueElementsValidator(message = "模板id不能重复")
    @ApiModelProperty(value = "模板id列表")
    private List<Long> messageTemplateIdList;
}
