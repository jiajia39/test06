package com.framework.emt.system.infrastructure.exception.task.schedule;

import com.baomidou.mybatisplus.annotation.TableName;
import com.framework.core.tenant.entity.TenantEntity;
import com.framework.emt.system.infrastructure.exception.task.schedule.constant.enums.ExecuteStatus;
import com.framework.emt.system.infrastructure.exception.task.schedule.constant.enums.TimeOutType;
import lombok.*;

import java.time.LocalDateTime;

/**
 * 异常任务定时计划 实体类
 *
 * @author ds_C
 * @since 2023-08-24
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("emt_exception_task_schedule")
public class ExceptionTaskSchedule extends TenantEntity {

    /**
     * 异常任务id
     */
    private Long exceptionTaskId;

    /**
     * 超时类型 0:响应超时 1:处理超时 2:协同超时 3:响应逐级上报 4:处理逐级上报 5:协同逐级上报
     */
    private TimeOutType timeOutType;

    /**
     * 超时表id
     */
    private Long sourceId;

    /**
     * 超时表的版本号 提报/协同版本号
     */
    private Integer sourceVersion;

    /**
     * 预计发送时间
     */
    private LocalDateTime planSendTime;

    /**
     * 执行状态 0:未执行 1:已执行 2.已取消
     */
    private ExecuteStatus executeStatus;

    /**
     * 发送模板id
     */
    private Long templateId;

    /**
     * 发送内容
     */
    private String sendContent;

    /**
     * 发送人id
     */
    private Long sendUserId;

    /**
     * 接收人id
     */
    private Long acceptUserId;

    /**
     * 逐级上报流程id
     */
    private Long processId;

    /**
     * 逐级上报流程名称
     */
    private String processName;

    /**
     * 超时时限
     */
    private Integer processTimeLimit;

    /**
     * 排序
     */
    private Integer sort;

    /**
     * 备注
     */
    private String remark;

    /**
     * 响应超时定时计划创建参数
     */
    public void initTimeout(Long exceptionTaskId, TimeOutType timeOutType, Long sourceId, Integer sourceVersion, LocalDateTime planSendTime,
                            Long templateId, String sendContent, Long sendUserId, Long acceptUserId, Integer processTimeLimit) {
        this.exceptionTaskId = exceptionTaskId;
        this.timeOutType = timeOutType;
        this.sourceId = sourceId;
        this.sourceVersion = sourceVersion;
        this.planSendTime = planSendTime;
        this.executeStatus = ExecuteStatus.NOT;
        this.templateId = templateId;
        this.sendContent = sendContent;
        this.sendUserId = sendUserId;
        this.acceptUserId = acceptUserId;
        this.processTimeLimit = processTimeLimit;
    }

    /**
     * 初始化上报超时定时计划创建参数
     */
    public void initTimeoutReport(Long exceptionTaskId, TimeOutType timeOutType, Long sourceId, Integer sourceVersion, Integer processTimeLimit, LocalDateTime planSendTime,
                                  Long templateId, String sendContent, Long sendUserId, String acceptUserId, Long processId, String processName) {
        this.exceptionTaskId = exceptionTaskId;
        this.timeOutType = timeOutType;
        this.sourceId = sourceId;
        this.sourceVersion = sourceVersion;
        this.planSendTime = planSendTime;
        this.executeStatus = ExecuteStatus.NOT;
        this.templateId = templateId;
        this.sendContent = sendContent;
        this.sendUserId = sendUserId;
        this.acceptUserId = Long.parseLong(acceptUserId);
        this.processId = processId;
        this.processName = processName;
        this.processTimeLimit = processTimeLimit;
    }

}
