package com.framework.emt.system.domain.exception.response;

import com.framework.emt.system.domain.exception.convert.constant.enums.CheckConditionEnum;
import com.framework.emt.system.domain.exception.convert.constant.enums.CheckModeEnum;
import com.framework.emt.system.domain.exception.convert.constant.enums.HandingModeEnum;
import com.framework.emt.system.domain.exception.convert.constant.enums.ResponseModeEnum;
import com.framework.emt.system.domain.formfield.response.FormFieldResponse;
import com.framework.emt.system.domain.tag.response.TagResponse;
import com.framework.emt.system.infrastructure.common.response.BaseUserResponse;
import com.framework.emt.system.infrastructure.common.response.UserInfoResponse;
import com.framework.emt.system.infrastructure.utils.DataHandleUtils;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.util.List;

/**
 * 异常流程 响应体
 *
 * @author ds_C
 * @since 2023-07-20
 */
@Getter
@Setter
public class ExceptionProcessResponse extends BaseUserResponse implements Serializable {

    @ApiModelProperty(value = "异常流程id")
    private Long id;

    @ApiModelProperty(value = "异常分类id")
    private Long exceptionCategoryId;

    @ApiModelProperty(value = "异常分类名称")
    private String exceptionCategoryName;

    @ApiModelProperty(value = "流程名称")
    private String title;

    @ApiModelProperty(value = "异常原因项列表")
    private List<TagResponse> exceptionReasonItems;

    @ApiModelProperty(value = "提报附加字段")
    private List<FormFieldResponse> submitExtendFieldList;

    @ApiModelProperty(value = "响应模式 0:不指定 1:固定人员 2:多个人员")
    private ResponseModeEnum responseMode;

    @ApiModelProperty(value = "响应人id列表")
    private List<Long> responseUserIds;

    @ApiModelProperty(value = "响应人id和姓名列表")
    private List<UserInfoResponse> responseUserIdOfNameList;

    @ApiModelProperty(value = "响应附加字段")
    private List<FormFieldResponse> responseExtendFieldList;

    @ApiModelProperty(value = "处理模式 0:不指定 1:固定人员 2:多个人员 3:响应同处理")
    private HandingModeEnum handingMode;

    @ApiModelProperty(value = "处理人id列表")
    private List<Long> handingUserIds;

    @ApiModelProperty(value = "处理人id和姓名列表")
    private List<UserInfoResponse> handingUserIdOfNameList;

    @ApiModelProperty(value = "处理附加字段")
    private List<FormFieldResponse> handingExtendFieldList;

    @ApiModelProperty(value = "挂起附加字段")
    private List<FormFieldResponse> pendingExtendFieldList;

    @ApiModelProperty(value = "协同附加字段")
    private List<FormFieldResponse> cooperateExtendFieldList;

    @ApiModelProperty(value = "验收模式 0:不指定 1:提报同验收 2:提报同验收多人 3:固定人员 4:多个人员")
    private CheckModeEnum checkMode;

    @ApiModelProperty(value = "验收条件0.需要所有人验收1.仅需单人验收")
    private CheckConditionEnum checkCondition;

    @ApiModelProperty(value = "验收人id列表")
    private List<Long> checkUserIds;

    @ApiModelProperty(value = "验收人id和姓名列表")
    private List<UserInfoResponse> checkUserIdOfNameList;

    @ApiModelProperty(value = "验收附加字段")
    private List<FormFieldResponse> checkExtendFieldList;

    @ApiModelProperty(value = "响应超时上报流程id")
    private Long responseReportNoticeProcessId;

    @ApiModelProperty(value = "响应超时上报流程名称")
    private String responseReportNoticeProcessName;

    @ApiModelProperty(value = "处理超时上报流程id")
    private Long handingReportNoticeProcessId;

    @ApiModelProperty(value = "处理超时上报流程名称")
    private String handingReportNoticeProcessName;

    @ApiModelProperty(value = "排序")
    private Integer sort;

    @ApiModelProperty(value = "备注")
    private String remark;

    public List<Long> getUserIds() {
        return DataHandleUtils.mergeElements(responseUserIds, handingUserIds, checkUserIds);
    }

}
