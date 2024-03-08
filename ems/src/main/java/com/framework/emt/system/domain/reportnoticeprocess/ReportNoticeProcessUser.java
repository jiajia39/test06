package com.framework.emt.system.domain.reportnoticeprocess;

import com.baomidou.mybatisplus.annotation.TableName;
import com.framework.core.tenant.entity.TenantEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 上报流程推送 实体类
 *
 * @author ds_C
 * @since 2023-07-24
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("emt_report_notice_process_user")
public class ReportNoticeProcessUser extends TenantEntity {

    /**
     * 上报流程id
     */
    private Long reportNoticeProcessId;

    /**
     * 超出时限 单位:分钟
     */
    private Integer timeLimit;

    /**
     * 接收人id列表 接收人id列表(不受部门限制) 最多10个
     */
    private String receiveUserIds;

    /**
     * 排序
     */
    private Integer sort;

    /**
     * 备注
     */
    private String remark;

}
