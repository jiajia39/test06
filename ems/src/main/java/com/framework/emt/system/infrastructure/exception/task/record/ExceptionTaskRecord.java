package com.framework.emt.system.infrastructure.exception.task.record;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.extension.handlers.JacksonTypeHandler;
import com.framework.core.tenant.entity.TenantEntity;
import com.framework.emt.system.domain.formfield.response.FormFieldResponse;
import com.framework.emt.system.infrastructure.common.request.FileRequest;
import com.framework.emt.system.infrastructure.exception.task.check.constant.enums.CheckStatus;
import com.framework.emt.system.infrastructure.exception.task.check.constant.enums.CheckSubStatus;
import com.framework.emt.system.infrastructure.exception.task.record.constant.enums.TaskRecordNode;
import com.framework.emt.system.infrastructure.exception.task.record.constant.enums.TaskRecordType;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;

/**
 * 异常任务履历 实体类
 *
 * @author ds_C
 * @since 2023-08-08
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName(value = "emt_exception_task_record", autoResultMap = true)
public class ExceptionTaskRecord extends TenantEntity {

    /**
     * 异常编号
     */
    private String exceptionTaskCode;

    /**
     * 异常任务id
     */
    private Long exceptionTaskId;

    /**
     * 履历id
     */
    private Long recordId;

    /**
     * 履历节点 0:提报阶段 1:响应阶段 2:处理阶段 3:验收阶段
     */
    private TaskRecordNode recordNode;

    /**
     * 履历版本号
     */
    private Integer recordVersion;

    /**
     * 履历次数
     */
    private Integer recordNum;

    /**
     * 履历状态
     */
    private Integer recordStatus;

    /**
     * 履历子状态
     */
    private Integer recordSubStatus;

    /**
     * 操作类型
     */
    private TaskRecordType recordType;

    /**
     * 提交人id 0:系统
     */
    private Long submitUserId;

    /**
     * 接受人id
     */
    private Long acceptUserId;

    /**
     * 履历内容
     */
    private String recordContent;

    /**
     * 备注
     */
    private String recordRemark;

    /**
     * 附加数据
     */
    @TableField(typeHandler = JacksonTypeHandler.class)
    private List<FormFieldResponse> recordExtendDatas;

    /**
     * 附件列表
     */
    @TableField(typeHandler = JacksonTypeHandler.class)
    private List<FileRequest> recordFiles;

    /**
     * 履历时间
     */
    private LocalDateTime recordTime;

    public void checkPass(Long userId, Long checkId, Integer checkVersion, LocalDateTime submitTime) {
        this.recordType = TaskRecordType.ACCEPT;
        this.submitUserId = userId;
        this.recordId = checkId;
        this.recordVersion = checkVersion;
        this.recordStatus = CheckStatus.CHECK_PASSED.getCode();
        this.recordSubStatus = CheckSubStatus.CHECK_PASSED.getCode();
        this.recordTime = submitTime;
        this.recordExtendDatas = Collections.emptyList();
        this.recordFiles = Collections.emptyList();
    }

    public void checkReject(Long userId, Long checkId, Integer checkVersion, LocalDateTime rejectTime, String rejectReason) {
        this.recordType = TaskRecordType.REJECT;
        this.submitUserId = userId;
        this.recordId = checkId;
        this.recordVersion = checkVersion;
        this.recordStatus = CheckStatus.CHECK_REJECT.getCode();
        this.recordSubStatus = CheckSubStatus.CHECK_REJECT.getCode();
        this.recordTime = rejectTime;
        this.recordRemark = rejectReason;
        this.recordExtendDatas = Collections.emptyList();
        this.recordFiles = Collections.emptyList();
    }

}
