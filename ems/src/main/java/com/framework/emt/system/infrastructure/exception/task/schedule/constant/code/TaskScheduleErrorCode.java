package com.framework.emt.system.infrastructure.exception.task.schedule.constant.code;

import com.framework.common.api.entity.IResultCode;
import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 异常任务定时计划异常码
 * code为3800-3900
 *
 * @author ds_C
 * @since 2023-08-24
 */
@Getter
@AllArgsConstructor
public enum TaskScheduleErrorCode implements IResultCode {

    /**
     * 异常码
     */
    NOT_FOUND(3800, "未查询到异常任务定时计划");

    /**
     * code编码
     */
    final int code;

    /**
     * 中文信息描述
     */
    final String message;

}
