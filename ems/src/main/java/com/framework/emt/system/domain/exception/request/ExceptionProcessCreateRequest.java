package com.framework.emt.system.domain.exception.request;

import cn.hutool.core.collection.CollectionUtil;
import com.framework.emt.system.domain.exception.convert.constant.enums.CheckModeEnum;
import com.framework.emt.system.domain.exception.convert.constant.enums.HandingModeEnum;
import com.framework.emt.system.domain.exception.convert.constant.enums.ResponseModeEnum;
import com.framework.emt.system.domain.exception.convert.constant.enums.CheckConditionEnum;
import com.framework.emt.system.infrastructure.common.request.ExtendFieldRequest;
import com.framework.emt.system.infrastructure.common.validation.EnumValidator;
import com.framework.emt.system.infrastructure.common.validation.UniqueElementsValidator;
import com.framework.emt.system.infrastructure.utils.DataHandleUtils;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.Range;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.io.Serializable;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * 异常流程 创建参数
 *
 * @author ds_C
 * @since 2023-07-20
 */
@Getter
@Setter
public class ExceptionProcessCreateRequest implements Serializable {

    @NotNull(message = "异常分类id不能为空")
    @ApiModelProperty(value = "异常分类id", required = true)
    private Long exceptionCategoryId;

    @NotBlank(message = "流程名称不能为空")
    @Length(max = 20, message = "流程名称长度限制{max}")
    @ApiModelProperty(value = "流程名称", required = true)
    private String title;

    @NotEmpty(message = "异常项原因项id列表不能为空")
    @UniqueElementsValidator(message = "异常原因项id不能重复")
    @ApiModelProperty(value = "异常原因项id列表", required = true)
    private List<Long> exceptionReasonItemIds;

    @ApiModelProperty(value = "提报附加字段id以及是否必填列表")
    private List<@Valid ExtendFieldRequest> submitExtendFieldList;

    @NotNull(message = "响应模式不能为空")
    @EnumValidator(enumClazz = ResponseModeEnum.class, message = "响应模式 0:不指定 1:固定人员 2:多个人员")
    @ApiModelProperty(value = "响应模式 0:不指定 1:固定人员 2:多个人员", required = true)
    private Integer responseMode;

    @Size(max = 30, message = "响应人最多{max}个")
    @UniqueElementsValidator(message = "响应人id不能重复")
    @ApiModelProperty(value = "响应人id列表 最多30个")
    private List<Long> responseUserIds;

    @ApiModelProperty(value = "响应附加字段id以及是否必填列表")
    private List<@Valid ExtendFieldRequest> responseExtendFieldList;

    @NotNull(message = "处理模式不能为空")
    @EnumValidator(enumClazz = HandingModeEnum.class, message = "处理模式 0:不指定 1:固定人员 2:多个人员 3:响应同处理")
    @ApiModelProperty(value = "处理模式 0:不指定 1:固定人员 2:多个人员 3:响应同处理", required = true)
    private Integer handingMode;

    @Size(max = 30, message = "处理人最多{max}个")
    @UniqueElementsValidator(message = "处理人id不能重复")
    @ApiModelProperty(value = "处理人id列表 最多30个")
    private List<Long> handingUserIds;

    @ApiModelProperty(value = "处理附加字段id列表以及是否必填")
    private List<@Valid ExtendFieldRequest> handingExtendFieldList;

    @ApiModelProperty(value = "挂起附加字段id以及是否必填列表")
    private List<@Valid ExtendFieldRequest> pendingExtendFieldList;

    @ApiModelProperty(value = "协同附加字段id以及是否必填列表")
    private List<@Valid ExtendFieldRequest> cooperateExtendFieldList;

    @NotNull(message = "验收模式不能为空")
    @EnumValidator(enumClazz = CheckModeEnum.class, message = "验收模式 0:不指定 1:固定人员 2:多个人员 3:填报同验收 4:填报同验收多人")
    @ApiModelProperty(value = "验收模式 0:不指定 1:固定人员 2:多个人员 3:填报同验收 4:填报同验收多人", required = true)
    private Integer checkMode;

    @NotNull(message = "验收条件不能为空")
    @EnumValidator(enumClazz = CheckConditionEnum.class, message = "验收条件0.需要所有人验收1.仅需单人验收")
    @ApiModelProperty(value = "验收条件0.需要所有人验收1.仅需单人验收", required = true)
    private Integer checkCondition;

    @Size(max = 30, message = "验收人最多{max}个")
    @UniqueElementsValidator(message = "验收人id不能重复")
    @ApiModelProperty(value = "验收人id列表 最多30个")
    private List<Long> checkUserIds;

    @ApiModelProperty(value = "验收附加字段id以及是否必填列表")
    private List<@Valid ExtendFieldRequest> checkExtendFieldList;

    @NotNull(message = "响应超时上报流程id不能为空")
    @ApiModelProperty(value = "响应超时上报流程id", required = true)
    private Long responseReportNoticeProcessId;

    @NotNull(message = "处理超时上报流程id不能为空")
    @ApiModelProperty(value = "处理超时上报流程id", required = true)
    private Long handingReportNoticeProcessId;

    @Range(min = 0, max = 99999, message = "排序最大{max}")
    @ApiModelProperty(value = "排序")
    private Integer sort;

    @Length(max = 500, message = "备注长度限制{max}")
    @ApiModelProperty(value = "备注")
    private String remark;

    public ExceptionProcessCreateRequest init() {
        if (CollectionUtil.isEmpty(exceptionReasonItemIds)) {
            exceptionReasonItemIds = Collections.emptyList();
        }
        if (CollectionUtil.isEmpty(responseUserIds)) {
            responseUserIds = Collections.emptyList();
        }
        if (CollectionUtil.isEmpty(handingUserIds)) {
            handingUserIds = Collections.emptyList();
        }
        if (CollectionUtil.isEmpty(checkUserIds)) {
            checkUserIds = Collections.emptyList();
        }
        if (CollectionUtil.isEmpty(submitExtendFieldList)) {
            submitExtendFieldList = Collections.emptyList();
        }
        if (CollectionUtil.isEmpty(responseExtendFieldList)) {
            responseExtendFieldList = Collections.emptyList();
        }
        if (CollectionUtil.isEmpty(handingExtendFieldList)) {
            handingExtendFieldList = Collections.emptyList();
        }
        if (CollectionUtil.isEmpty(pendingExtendFieldList)) {
            pendingExtendFieldList = Collections.emptyList();
        }
        if (CollectionUtil.isEmpty(cooperateExtendFieldList)) {
            cooperateExtendFieldList = Collections.emptyList();
        }
        if (CollectionUtil.isEmpty(checkExtendFieldList)) {
            checkExtendFieldList = Collections.emptyList();
        }
        return this;
    }

    public List<Long> extendFieldIdList() {
        List<ExtendFieldRequest> extendFieldList = DataHandleUtils.mergeElements(submitExtendFieldList,
                responseExtendFieldList, handingExtendFieldList, pendingExtendFieldList,
                cooperateExtendFieldList, checkExtendFieldList);
        return extendFieldList.stream().map(ExtendFieldRequest::getId).distinct().collect(Collectors.toList());
    }

    public List<Integer> exceptionProcessModes() {
        return Stream.of(responseMode, handingMode, checkMode).collect(Collectors.toList());
    }

    public List<List<Long>> exceptionProcessUserIds() {
        return Stream.of(responseUserIds, handingUserIds, checkUserIds).collect(Collectors.toList());
    }

}
