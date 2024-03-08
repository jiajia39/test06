package com.framework.emt.system.domain.exception;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.extension.handlers.JacksonTypeHandler;
import com.framework.core.tenant.entity.TenantEntity;
import com.framework.emt.system.domain.exception.convert.constant.enums.CheckConditionEnum;
import com.framework.emt.system.domain.exception.convert.constant.enums.CheckModeEnum;
import com.framework.emt.system.domain.exception.convert.constant.enums.HandingModeEnum;
import com.framework.emt.system.domain.exception.convert.constant.enums.ResponseModeEnum;
import com.framework.emt.system.domain.formfield.response.FormFieldResponse;
import com.framework.emt.system.infrastructure.constant.enums.EnableFlagEnum;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.List;

/**
 * 异常流程 实体类
 *
 * @author ds_C
 * @since 2023-07-20
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName(value = "emt_exception_process", autoResultMap = true)
public class ExceptionProcess extends TenantEntity {

    /**
     * 异常分类id
     */
    private Long exceptionCategoryId;

    /**
     * 流程名称
     */
    private String title;

    /**
     * 流程编号 保留字段
     */
    private String code;

    /**
     * 提报附加字段
     */
    @TableField(typeHandler = JacksonTypeHandler.class)
    private List<FormFieldResponse> submitExtendField;

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
    private List<FormFieldResponse> responseExtendField;

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
    private List<FormFieldResponse> handingExtendField;

    /**
     * 挂起附加字段
     */
    @TableField(typeHandler = JacksonTypeHandler.class)
    private List<FormFieldResponse> pendingExtendField;

    /**
     * 协同附加字段
     */
    @TableField(typeHandler = JacksonTypeHandler.class)
    private List<FormFieldResponse> cooperateExtendField;

    /**
     * 验收模式 0:不指定 1:提报同验收 2:提报同验收多人 3:固定人员 4:多个人员
     */
    private CheckModeEnum checkMode;

    /**
     * 验收人id列表 最多30个
     */
    @TableField(typeHandler = JacksonTypeHandler.class)
    private List<Long> checkUserIds;

    /**
     * 验收附加字段
     */
    @TableField(typeHandler = JacksonTypeHandler.class)
    private List<FormFieldResponse> checkExtendField;

    /**
     * 响应超时上报流程id
     */
    private Long responseReportNoticeProcessId;

    /**
     * 处理超时上报流程id
     */
    private Long handingReportNoticeProcessId;

    /**
     * 状态 0:禁用 1:启用
     */
    private EnableFlagEnum enableFlag;

    /**
     * 排序
     */
    private Integer sort;

    /**
     * 备注
     */
    private String remark;

    /**
     * 验收条件0.需要所有人验收1.仅需单人验收
     */
    private CheckConditionEnum checkCondition;

}
