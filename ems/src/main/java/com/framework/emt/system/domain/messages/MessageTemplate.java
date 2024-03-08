package com.framework.emt.system.domain.messages;

import com.baomidou.mybatisplus.annotation.TableName;
import com.framework.core.tenant.entity.TenantEntity;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 消息模板
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("emt_message_template")
public class MessageTemplate extends TenantEntity {

    /**
     * 模版级别
     */
    @ApiModelProperty(value = "模版级别")
    private Integer level;

    /**
     * 模版编号
     */
    @ApiModelProperty(value = "模版编号")
    private String code;

    /**
     * 模版名称
     */
    @ApiModelProperty(value = "模版名称")
    private String name;

    /**
     * 模版内容
     */
    @ApiModelProperty(value = "模版内容")
    private String content;

    /**
     * 备注
     */
    @ApiModelProperty(value = "备注")
    private String remark;

    /**
     * 0:禁用 1:启用  保留字段
     */
    @ApiModelProperty(value = "启用标志 0:禁用 1:启用")
    private Integer enableFlag;

}
