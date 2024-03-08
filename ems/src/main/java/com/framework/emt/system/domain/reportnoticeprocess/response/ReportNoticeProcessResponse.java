package com.framework.emt.system.domain.reportnoticeprocess.response;

import com.framework.emt.system.infrastructure.common.response.BaseUserResponse;
import com.framework.emt.system.infrastructure.common.response.UserInfoResponse;
import com.framework.emt.system.infrastructure.constant.enums.EnableFlagEnum;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.util.List;

/**
 * 上报流程、上报流程推送 响应体
 *
 * @author ds_C
 * @since 2023-07-12
 */
@Getter
@Setter
public class ReportNoticeProcessResponse extends BaseUserResponse implements Serializable {

    @ApiModelProperty(value = "上报流程id")
    private Long reportNoticeProcessId;

    @ApiModelProperty(value = "关联异常分类id")
    private Long exceptionCategoryId;

    @ApiModelProperty(value = "关联异常分类名称")
    private String exceptionCategoryName;

    @ApiModelProperty(value = "上报流程名称")
    private String name;

    @ApiModelProperty(value = "上报层级")
    private Long reportLevel;

    @ApiModelProperty(value = "上报人列表")
    private String receiveUserIds;

    @ApiModelProperty(value = "上报人列表")
    private List<UserInfoResponse> userInfoList;

    @ApiModelProperty(value = "上报人及上报消息id")
    private Long reportNoticeProcessUserId;

    @ApiModelProperty(value = "上报消息超出时限")
    private Integer timeLimit;

    @ApiModelProperty(value = "排序")
    private Integer sort;

    @ApiModelProperty(value = "流程状态")
    private EnableFlagEnum enableFlag;

}
