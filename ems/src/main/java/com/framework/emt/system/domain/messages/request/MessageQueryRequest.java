package com.framework.emt.system.domain.messages.request;

import com.framework.core.mybatisplus.support.Query;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.util.Date;

/**
 * 消息 创建参数
 *
 * @author yankunw
 * @since 2023-07-20
 */
@Getter
@Setter
@ApiModel(value = "消息查询参数", description = "消息查询参数")
public class MessageQueryRequest extends Query implements Serializable {
    /**
     * 业务类型
     */
    @ApiModelProperty(value = "业务类型 1.异常任务 2.协同任务 3.验收任务")
    private Integer businessType;


    /**
     * 接收人id列表
     */
    @ApiModelProperty(value = "接收人id列表")
    private Long receiveUserId;


    /**
     * 发送时间开始
     */
    @ApiModelProperty(value = "发送时间开始")
    private Date sendTimeStart;

    /**
     * 发送时间结束
     */
    @ApiModelProperty(value = "发送时间结束")
    private Date sendTimeEnd;


    /**
     * 接收状态
     */
    @ApiModelProperty(value = "接收状态 0.未读 1.已读 ")
    private Integer readState;

    /**
     * 已读时间开始
     */
    @ApiModelProperty(value = "已读时间开始")
    private Date readTimeStart;

    /**
     * 已读时间结束
     */
    @ApiModelProperty(value = "已读时间结束")
    private Date readTimeEnd;

    /**
     * 排序
     */
    @ApiModelProperty(value = "排序 1未读在前")
    private Integer orderBy;
}
