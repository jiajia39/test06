package com.framework.emt.system.infrastructure.exception.task.check;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.extension.handlers.JacksonTypeHandler;
import com.framework.core.tenant.entity.TenantEntity;
import com.framework.emt.system.domain.formfield.response.FormFieldResponse;
import com.framework.emt.system.infrastructure.common.request.FileRequest;
import com.framework.emt.system.infrastructure.exception.task.check.constant.enums.CheckStatus;
import com.framework.emt.system.infrastructure.exception.task.check.constant.enums.CheckSubStatus;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.time.LocalDateTime;
import java.util.List;

/**
 * 异常任务验收 实体类
 *
 * @author ds_C
 * @since 2023-08-08
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName(value = "emt_exception_task_check", autoResultMap = true)
public class ExceptionTaskCheck extends TenantEntity {

    /**
     * 异常任务表id
     */
    private Long exceptionTaskId;

    /**
     * 验收版本号
     */
    private Integer checkVersion;

    /**
     * 验收人id
     */
    private Long userId;

    /**
     * 验收状态
     */
    private CheckStatus checkStatus;

    /**
     * 验收子状态
     */
    private CheckSubStatus checkSubstatus;

    /**
     * 提交附加数据
     */

    @TableField(typeHandler = JacksonTypeHandler.class)
    private List<FormFieldResponse> submitExtendDatas;
    /**
     * 验收时间
     */
    private LocalDateTime submitTime;

    /**
     * 拒绝原因
     */
    private String rejectReason;

    /**
     * 提交附件列表
     */
    @TableField(typeHandler = JacksonTypeHandler.class)
    private List<FileRequest> submitFiles;

    public void updateCheck(List<FileRequest> submitFiles, String rejectReason) {
        this.submitFiles = submitFiles;
        this.rejectReason = rejectReason;
        this.checkStatus = CheckStatus.CHECK_REJECT;
        this.checkSubstatus = CheckSubStatus.CHECK_REJECT;
    }


    public void updateStatus() {
        this.checkSubstatus = CheckSubStatus.WAIT_CHECK_EXPIRED;
    }


}
