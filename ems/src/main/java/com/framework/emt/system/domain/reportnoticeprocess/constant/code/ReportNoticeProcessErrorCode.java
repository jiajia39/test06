package com.framework.emt.system.domain.reportnoticeprocess.constant.code;

import com.framework.common.api.entity.IResultCode;
import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 上报流程异常码
 * code为2600-2700
 *
 * @author ds_C
 * @since 2023-07-12
 */
@Getter
@AllArgsConstructor
public enum ReportNoticeProcessErrorCode implements IResultCode {

    /**
     * 异常码
     */
    REPORT_NOTICE_PROCESS_INFO_NOT_FIND(2600, "未查询到超时上报流程"),
    REPORT_PROCESS_BE_EXCEPTION_PROCESS_CITE_CAN_NOT_DELETE(2601, "上报流程已被异常流程引用，无法删除"),
    RESPONSE_REPORT_NOTICE_PROCESS_INFO_NOT_FIND(2602, "响应超时上报流程信息未查询到数据"),
    HANDING_REPORT_NOTICE_PROCESS_INFO_NOT_FIND(2603, "处理超时上报流程信息未查询到数据"),
    REPORT_NOTICE_PROCESS_NAME_IS_EXIST(2604, "上报流程名称已存在");

    /**
     * code编码
     */
    final int code;

    /**
     * 中文信息描述
     */
    final String message;

}
