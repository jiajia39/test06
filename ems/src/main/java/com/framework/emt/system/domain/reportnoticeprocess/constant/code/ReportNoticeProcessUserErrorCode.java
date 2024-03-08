package com.framework.emt.system.domain.reportnoticeprocess.constant.code;

import com.framework.common.api.entity.IResultCode;
import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 上报流程推送异常码
 * code为2800-2900
 *
 * @author ds_C
 * @since 2023-07-12
 */
@Getter
@AllArgsConstructor
public enum ReportNoticeProcessUserErrorCode implements IResultCode {

    /**
     * 异常码
     */
    REPORT_NOTICE_PROCESS_USER_INFO_NOT_FIND(2800, "上报人及上报消息未查询到数据"),
    SAME_REPORT_NOTICE_PROCESS_OF_TIME_LIMIT_MUST_UNIQUE(2801, "同一个上报流程下的超出时限不能相同"),
    THE_NUMBER_OF_REPORTED_PERSONNEL_HAS_EXCEEDED_THE_MAXIMUM_VALUE(2802, "上报人数已超出最大值"),;

    /**
     * code编码
     */
    final int code;

    /**
     * 中文信息描述
     */
    final String message;

}
