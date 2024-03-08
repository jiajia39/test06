package com.framework.emt.system.domain.messages.request;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotNull;
import java.io.Serializable;

/**
 * 消息模板创建对象
 *
 * @author yankunw
 * @since 2023-07-20
 */
@Getter
@Setter
@ApiModel(value = "消息模板保存或修改参数", description = "消息模板保存或修改参数")
public class MessageTemplateRequest implements Serializable {
    /**
     * 模版级别
     */
    @NotNull(message = "模版级别不能为空！")
    @ApiModelProperty(value = "模版级别")
    private Integer level;

    /**
     * 模版编号
     */
    @NotNull(message = "模版编号不能为空！")
    @ApiModelProperty(value = "模版编号")
    private String code;

    /**
     * 模版名称
     */
    @NotNull(message = "模版名称不能为空！")
    @ApiModelProperty(value = "模版名称")
    private String name;

    /**
     * 模版内容
     */
    @NotNull(message = "模版内容不能为空！")
    @ApiModelProperty(value = "模版内容")
    private String content;

    /**
     * 备注
     */
    @ApiModelProperty(value = "备注")
    private String remark;
}
