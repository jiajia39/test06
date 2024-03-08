package com.framework.emt.system.domain.messages.response;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * 消息推送 响应体
 *
 * @author yankunw
 * @since 2023-07-21
 */
@Data
@ApiModel(value = "消息模板数据", description = "消息模板数据")
public class MessageTemplateResponse implements Serializable {
    /**
     * 主键id
     */
    @ApiModelProperty("主键id")
    private Long id;
    /**
     * 创建人
     */
    @ApiModelProperty("创建人")
    private Long createUser;

    /**
     * 创建人姓名
     */
    @ApiModelProperty("创建人姓名")
    private String createUserName;

    /**
     * 创建部门
     */
    @ApiModelProperty("创建部门")
    private Long createDept;
    /**
     * 创建时间
     */
    @ApiModelProperty("创建时间")
    private Date createTime;
    /**
     * 更新人
     */
    @ApiModelProperty("更新人")
    private Long updateUser;

    /**
     * 更新人姓名
     */
    @ApiModelProperty("更新人姓名")
    private String updateUserName;

    /**
     * 更新时间
     */
    @ApiModelProperty("更新时间")
    private Date updateTime;

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
