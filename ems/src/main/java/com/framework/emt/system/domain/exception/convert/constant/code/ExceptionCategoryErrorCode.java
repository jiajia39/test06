package com.framework.emt.system.domain.exception.convert.constant.code;

import com.framework.common.api.entity.IResultCode;
import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 异常分类异常码
 * code为2000-2100
 *
 * @author ds_C
 * @since 2023-07-12
 */
@Getter
@AllArgsConstructor
public enum ExceptionCategoryErrorCode implements IResultCode {

    /**
     * 异常码
     */
    EXCEPTION_CATEGORY_INFO_NOT_FIND(2000, "异常分类信息未查询到数据"),
    THIS_EXCEPTION_CATEGORY_CONTAIN_CHILD_CATEGORY(2001, "删除失败，该异常分类下存在子分类无法删除"),
    EXCEPTION_CATEGORY_TITLE_IS_NOT_BLANK(2002, "异常分类名称不能为空"),
    THIS_EXCEPTION_CATEGORY_CONTAIN_EXCEPTION_ITEM(2003, "删除失败！该异常分类下存在异常项无法删除"),
    THIS_EXCEPTION_CATEGORY_CONTAIN_EXCEPTION_PROCESS(2004, "删除失败！该异常分类下存在异常流程无法删除"),
    THIS_EXCEPTION_CATEGORY_CONTAIN_REPORT_NOTICE_PROCESS(2005, "删除失败！该异常分类下存在上报流程无法删除"),
    EXCEPTION_CATEGORY_TITLE_MUST_UNIQUE(2006, "异常分类名称不能重复"),
    EXCEPTION_CATEGORY_TITLE_IS_EXIST(2007, "异常分类名称已存在"),
    EXCEl_CONTAIN_NOT_EXIST_EXCEPTION_CATEGORY_TITLE(2008, "Excel中包含不存在的异常分类名称，请你检查之后重试！"),
    EXCEPTION_CATEGORY_TITLE_LIST_CAN_NOT_CONTAIN_PARENT_TITLE(2009, "导入失败，异常分类名称列表不能包含父级异常分类名称"),
    THIS_EXCEPTION_CATEGORY_CONTAIN_EXCEPTION_TASK_SUBMIT(2010, "删除失败！该异常分类下存在未完成的异常提报无法删除"),
    TOP_TITLE_CAN_NOT_EQUALS(2011, "顶层异常分类名称不能重复"),
    SAME_PARENT_OF_CHILD_TITLE_CAN_NOT_EQUALS(2012, "同一父级下的异常分类名称不能重复，请你检查之后再试！");

    /**
     * code编码
     */
    final int code;

    /**
     * 中文信息描述
     */
    final String message;

}
