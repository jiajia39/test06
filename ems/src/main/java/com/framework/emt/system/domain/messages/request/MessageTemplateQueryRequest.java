package com.framework.emt.system.domain.messages.request;

import com.framework.core.mybatisplus.support.Query;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.util.Date;

@Getter
@Setter
@ApiModel(value = "模板查询参数", description = "模板查询参数")
public class MessageTemplateQueryRequest extends Query implements Serializable {
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
     * 更新人
     */
    @ApiModelProperty("更新人")
    private Long updateUser;


    /**
     * 创建时间开始
     */
    @ApiModelProperty("创建时间开始")
    private Date createTimeStart;

    /**
     * 创建时间结束
     */
    @ApiModelProperty("创建时间结束")
    private Date createTimeEnd;

    /**
     * 更新时间开始
     */
    @ApiModelProperty("更新时间开始")
    private Date updateTimeStart;

    /**
     * 更新时间结束
     */
    @ApiModelProperty("更新时间结束")
    private Date updateTimeEnd;

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
