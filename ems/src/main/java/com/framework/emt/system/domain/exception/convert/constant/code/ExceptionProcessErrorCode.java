package com.framework.emt.system.domain.exception.convert.constant.code;

import com.framework.common.api.entity.IResultCode;
import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 异常流程异常码
 * code为2200-2300
 *
 * @author ds_C
 * @since 2023-07-12
 */
@Getter
@AllArgsConstructor
public enum ExceptionProcessErrorCode implements IResultCode {

    /**
     * 异常码
     */
    NOT_FOUND(2200, "异常流程信息未查询到数据"),
    EXCEPTION_PROCESS_TITLE_IS_EXIST(2201, "异常流程名称已存在"),
    RESPONSE_MODE_IS_NOT_SPECIFIED_USER_NOT_HAVE(2202, "响应模式为不指定时，响应人不可选"),
    RESPONSE_MODE_IS_FIXED_PERSONNEL_USER_HAVE_ONLY_ONE(2203, "响应模式为固定人员时，响应人只可选一人"),
    RESPONSE_MODE_IS_MULTIPLE_PERSONNEL_USER_HAVE_MULTIPLE(2204, "响应模式为多个人员时，响应人可选多人"),
    HANDING_MODE_IS_NOT_SPECIFIED_USER_NOT_HAVE(2205, "处理模式为不指定时，处理人不可选"),
    HANDING_MODE_IS_FIXED_PERSONNEL_USER_HAVE_ONLY_ONE(2206, "处理模式为固定人员时，处理人只可选一人"),
    HANDING_MODE_IS_MULTIPLE_PERSONNEL_USER_HAVE_MULTIPLE(2207, "处理模式为多个人员时，处理人可选多人"),
    HANDING_MODE_IS_RESPONSE_AND_HANDING_USER_NOT_HAVE(2208, "处理模式为响应同处理时，处理人不可选"),
    CHECK_MODE_IS_NOT_SPECIFIED_USER_NOT_HAVE(2209, "验收模式为不指定时，验收人不可选"),
    CHECK_MODE_IS_FIXED_PERSONNEL_USER_HAVE_ONLY_ONE(2210, "验收模式为固定人员时，验收人只可选一人"),
    CHECK_MODE_IS_MULTIPLE_PERSONNEL_USER_HAVE_MULTIPLE(2211, "验收模式为多个人员时，验收人可选多人"),
    CHECK_MODE_IS_REPORT_AND_CHECK_USER_NOT_HAVE(2212, "验收模式为填报同验收时，验收人不可选"),
    CHECK_MODE_IS_REPORT_AND_CHECK_MULTIPLE_PEOPLE_USER_HAVE_MULTIPLE(2213, "验收模式为填报同验收多人时，验收人可选多人"),
    EXCEPTION_PROCESS_TAG_ID_OR_TAG_CONTENT_MUST_HAVE_ONE(2214, "异常原因项标签id或标签内容必须填写一个"),
    RESPONSE_USER_NOT_FOUND(2215, "未查询到响应人"),
    RESPONSE_USER_NOT_SAME(2216, "响应人与固定人员不是同一人"),
    RESPONSE_USER_NOT_IN_LIST(2217, "响应人不在响应人列表中"),
    EXCEPTION_REASON_ITEM_HAS_EXCEEDED_THE_MAXIMUM_VALUE(2218, "异常原因项已超出最大值"),
    THE_ADDITIONAL_FIELDS_REPORTED_HAVE_EXCEEDED_THE_MAXIMUM_VALUE(2219, "提报附加字段已超出最大值"),
    THE_ADDITIONAL_RESPONSE_HAVE_EXCEEDED_THE_MAXIMUM_VALUE(2220, "响应附加字段已超出最大值"),
    THE_ADDITIONAL_HANDING_HAVE_EXCEEDED_THE_MAXIMUM_VALUE(2221, "处理附加字段已超出最大值"),
    THE_ADDITIONAL_SUSPEND_HAVE_EXCEEDED_THE_MAXIMUM_VALUE(2222, "挂起附加字段已超出最大值"),
    THE_ADDITIONAL_COOPERATION_HAVE_EXCEEDED_THE_MAXIMUM_VALUE(2223, "协同附加字段已超出最大值"),
    THE_ADDITIONAL_CHECK_HAVE_EXCEEDED_THE_MAXIMUM_VALUE(2224, "验收附加字段已超出最大值"),
    RESPONDENT_HAS_EXCEEDED_THE_MAXIMUM_VALUE(2225, "响应人数已超出最大值"),
    THE_HANDLER_HAS_EXCEEDED_THE_MAXIMUM_VALUE(2226, "处理人数已超出最大值"),
    THE_ACCEPTANCE_PERSON_HAS_EXCEEDED_THE_MAXIMUM_VALUE(2227, "验收人数已超出最大值"),
    THIS_PROCESS_CONTAIN_SUBMIT(2228, "该异常流程下包含了未完成的异常提报，无法删除"),
    PART_PROCESS_CONTAIN_SUBMIT(2229, "部分异常流程下包含了异常提报，无法删除");

    /**
     * code编码
     */
    final int code;

    /**
     * 中文信息描述
     */
    final String message;

}
