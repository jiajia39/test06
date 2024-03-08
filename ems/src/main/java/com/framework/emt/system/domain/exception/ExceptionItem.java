package com.framework.emt.system.domain.exception;

import com.baomidou.mybatisplus.annotation.TableName;
import com.framework.core.tenant.entity.TenantEntity;
import com.framework.emt.system.domain.exception.convert.constant.enums.PriorityEnum;
import com.framework.emt.system.domain.exception.convert.constant.enums.SeverityEnum;
import com.framework.emt.system.infrastructure.constant.enums.EnableFlagEnum;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 异常项 实体类
 *
 * @author ds_C
 * @since 2023-07-12
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("emt_exception_item")
public class ExceptionItem extends TenantEntity {

    /**
     * 异常分类id
     */
    private Long exceptionCategoryId;

    /**
     * 异常名称
     */
    private String title;

    /**
     * 异常编号 保留字段
     */
    private String code;

    /**
     * 紧急程度 0:一般、1:中等、2:紧急
     */
    private PriorityEnum priority;

    /**
     * 严重程度 0:一般、1:中等、2:严重
     */
    private SeverityEnum severity;

    /**
     * 响应时限 单位:分钟
     */
    private Integer responseDurationTime;

    /**
     * 处理时限 单位:分钟
     */
    private Integer handingDurationTime;

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

}
