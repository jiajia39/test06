package com.framework.emt.system.domain.exception.convert.constant.code;

import com.framework.common.api.entity.IResultCode;
import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 异常项异常码
 * code为2100-2200
 *
 * @author ds_C
 * @since 2023-07-12
 */
@Getter
@AllArgsConstructor
public enum ExceptionItemErrorCode implements IResultCode {

    /**
     * 异常码
     */
    EXCEPTION_ITEM_INFO_NOT_FIND(2100, "异常项信息未查询到数据"),
    EXCEPTION_ITEM_PRIORITY_IS_NOT_BLANK(2101, "异常项紧急程度不能为空"),
    EXCEPTION_ITEM_SEVERITY_IS_NOT_BLANK(2102, "异常项严重程度不能为空"),
    EXCEPTION_ITEM_RESPONSE_DURATION_TIME_MUST_INTEGER(2103, "异常项响应时限必须是整数"),
    EXCEPTION_ITEM_HANDING_DURATION_TIME_MUST_INTEGER(2104, "异常项处理时限必须是整数"),
    EXCEPTION_ITEM_TITLE_IS_EXIST(2105, "异常项名称已存在"),
    EXCEPTION_ITEM_TITLE_MUST_UNIQUE(2106, "同一分类下异常项名称不能重复"),
    EXCEl_CONTAIN_NOT_EXIST_EXCEPTION_ITEM_PRIORITY(2107, "Excel中包含不存在异常项紧急程度，请您检查之后再试！"),
    EXCEl_CONTAIN_NOT_EXIST_EXCEPTION_ITEM_SEVERITY(2108, "Excel中包含不存在异常项严重程度，请您检查之后再试！"),
    EXCEPTION_ITEM_TITLE_IS_NOT_BLANK(2109, "异常项名称不能为空"),
    EXCEPTION_ITEM_RESPONSE_DURATION_TIME_IS_NOT_BLANK(2110, "异常项响应时限不能为空"),
    EXCEPTION_ITEM_HANDING_DURATION_TIME_IS_NOT_BLANK(2111, "异常项处理时限不能为空"),
    EXCEl_CONTAIN_NOT_EXIST_EXCEPTION_ITEM_STATUS(2112, "Excel中包含不存在异常项状态，请您检查之后再试！"),
    CATEGORY_ID_NOT_EQUALS(2113, "异常项对应的异常分类必须和异常流程下的异常分类相同"),
    THIS_ITEM_CONTAIN_IS_FINISH_SUBMIT(2114, "该异常项下包含了未完成的异常提报，无法删除"),
    PART_ITEM_CONTAIN_SUBMIT(2115, "部分异常项下包含了异常提报，无法删除"),
    EXCEl_CONTAIN_NOT_EXIST_ITEM_TITLE(2116, "Excel中包含不存在异常项名称，请您检查之后再试！"),
    PLEASE_SELECT_ITEM_BOTTOM_CATEGORY(2117, "请您选择异常项中的异常分类！");

    /**
     * code编码
     */
    final int code;

    /**
     * 中文信息描述
     */
    final String message;

}
