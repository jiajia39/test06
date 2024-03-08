package com.framework.emt.system.domain.reportnoticeprocess;

import com.baomidou.mybatisplus.annotation.TableName;
import com.framework.core.tenant.entity.TenantEntity;
import com.framework.emt.system.infrastructure.constant.enums.EnableFlagEnum;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 上报流程 实体类
 *
 * @author ds_C
 * @since 2023-07-17
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("emt_report_notice_process")
public class ReportNoticeProcess extends TenantEntity {

    /**
     * 异常分类id
     */
    private Long exceptionCategoryId;

    /**
     * 通知流程名称
     */
    private String name;

    /**
     * 通知流程编号 保留字段
     */
    private String code;

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
