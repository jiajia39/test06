package com.framework.emt.system.infrastructure.exception.task.task;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.extension.handlers.JacksonTypeHandler;
import com.framework.core.tenant.entity.TenantEntity;
import com.framework.emt.system.domain.exception.convert.constant.enums.CheckConditionEnum;
import com.framework.emt.system.domain.exception.convert.constant.enums.CheckModeEnum;
import com.framework.emt.system.domain.exception.convert.constant.enums.HandingModeEnum;
import com.framework.emt.system.domain.exception.convert.constant.enums.ResponseModeEnum;
import com.framework.emt.system.domain.formfield.response.FormFieldResponse;
import com.framework.emt.system.domain.tag.response.TagInfo;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.List;

/**
 * 异常任务配置 实体类
 *
 * @author ds_C
 * @since 2023-08-08
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName(value = "emt_exception_task_setting", autoResultMap = true)
public class ExceptionTaskSetting extends TenantEntity {

    /**
     * 异常流程id
     */
    private Long exceptionProcessId;

    /**
     * 流程名称
     */
    private String title;

    /**
     * 异常分类id
     */
    private Long exceptionCategoryId;

    /**
     * 提报附加字段
     */
    @TableField(typeHandler = JacksonTypeHandler.class)
    private List<FormFieldResponse> submitExtendFields;

    /**
     * 响应模式 0:不指定 1:固定人员 2:多个人员
     */
    private ResponseModeEnum responseMode;

    /**
     * 响应人id列表 最多30个
     */
    @TableField(typeHandler = JacksonTypeHandler.class)
    private List<Long> responseUserIds;

    /**
     * 响应附加字段
     */
    @TableField(typeHandler = JacksonTypeHandler.class)
    private List<FormFieldResponse> responseExtendFields;

    /**
     * 处理模式 0:不指定 1:固定人员 2:多个人员 3:响应同处理
     */
    private HandingModeEnum handingMode;

    /**
     * 处理人id列表 最多30个
     */
    @TableField(typeHandler = JacksonTypeHandler.class)
    private List<Long> handingUserIds;

    /**
     * 处理附加字段
     */
    @TableField(typeHandler = JacksonTypeHandler.class)
    private List<FormFieldResponse> handingExtendFields;

    /**
     * 挂起附加字段
     */
    @TableField(typeHandler = JacksonTypeHandler.class)
    private List<FormFieldResponse> pendingExtendFields;

    /**
     * 协同附加字段
     */
    @TableField(typeHandler = JacksonTypeHandler.class)
    private List<FormFieldResponse> cooperateExtendFields;

    /**
     * 验收模式 0:不指定 1:提报同验收 2:提报同验收多人 3:固定人员 4:多个人员
     */
    private CheckModeEnum checkMode;

    /**
     * 验收条件0.需要所有人验收1.仅需单人验收
     */
    private CheckConditionEnum checkCondition;

    /**
     * 验收人id列表 最多30个
     */
    @TableField(typeHandler = JacksonTypeHandler.class)
    private List<Long> checkUserIds;

    /**
     * 验收附加字段
     */
    @TableField(typeHandler = JacksonTypeHandler.class)
    private List<FormFieldResponse> checkExtendFields;

    /**
     * 响应超时上报流程id
     */
    private Long responseReportNoticeProcessId;

    /**
     * 响应超时上报流程名称
     */
    private String responseReportNoticeProcessName;

    /**
     * 处理超时上报流程id
     */
    private Long handingReportNoticeProcessId;

    /**
     * 处理超时上报流程名称
     */
    private String handingReportNoticeProcessName;

    /**
     * 异常原因项列表
     */
    @TableField(typeHandler = JacksonTypeHandler.class)
    private List<TagInfo> reasonItems;

    /**
     * 排序
     */
    private Integer sort;

    /**
     * 备注
     */
    private String remark;

}
