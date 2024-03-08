package com.framework.emt.system.domain.task.task.response;

import com.framework.emt.system.domain.exception.convert.constant.enums.CheckModeEnum;
import com.framework.emt.system.domain.exception.convert.constant.enums.HandingModeEnum;
import com.framework.emt.system.domain.exception.convert.constant.enums.ResponseModeEnum;
import com.framework.emt.system.infrastructure.common.response.BaseUserResponse;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.util.List;


/**
 * 异常任务配置 响应体
 *
 * @author gaojia
 * @since 2023-08-14
 */
@Getter
@Setter
public class TaskSettingResponse extends BaseUserResponse implements Serializable {


    @ApiModelProperty(value = "异常流程id")
    private Long exceptionProcessId;

    @ApiModelProperty(value = "流程名称")
    private String title;


    @ApiModelProperty(value = "异常分类id")
    private Long exceptionCategoryId;


    @ApiModelProperty(value = "提报附加字段")

    private String submitExtendFields;


    @ApiModelProperty(value = "响应模式 0:不指定 1:固定人员 2:多个人员")
    private ResponseModeEnum responseMode;


    @ApiModelProperty(value = " 响应人id列表 最多30个")
    private List<Long> responseUserIds;


    @ApiModelProperty(value = "响应附加字段")

    private String responseExtendFields;


    @ApiModelProperty(value = " 处理模式 0:不指定 1:固定人员 2:多个人员 3:响应同处理")
    private HandingModeEnum handingMode;


    @ApiModelProperty(value = "处理人id列表 最多30个")
    private String handingUserIds;

    @ApiModelProperty(value = "处理人id列表 最多30个")
    private List<Long> handingUserIdList;

    @ApiModelProperty(value = "处理附加字段")
    private String handingExtendFields;


    @ApiModelProperty(value = "挂起附加字段")
    private String pendingExtendFields;


    @ApiModelProperty(value = "协同附加字段")
    private String cooperateExtendFields;


    @ApiModelProperty(value = "验收模式 0:不指定 1:提报同验收 2:提报同验收多人 3:固定人员 4:多个人员")
    private CheckModeEnum checkMode;


    @ApiModelProperty(value = " 验收人id列表 最多30个")
    private String checkUserIds;


    @ApiModelProperty(value = "验收附加字段")
    private String checkExtendFields;


    @ApiModelProperty(value = "响应超时上报流程id")
    private Long responseReportNoticeProcessId;

    @ApiModelProperty(value = "响应超时上报流程名称")

    private String responseReportNoticeProcessName;


    @ApiModelProperty(value = "处理超时上报流程id")
    private Long handingReportNoticeProcessId;


    @ApiModelProperty(value = "处理超时上报流程名称")
    private String handingReportNoticeProcessName;


    @ApiModelProperty(value = "异常原因项列表")
    private String reasonItems;


    @ApiModelProperty(value = "排序")
    private Integer sort;


    @ApiModelProperty(value = "备注")
    private String remark;

}
