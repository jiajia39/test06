package com.framework.emt.system.domain.dept.constant.code;

import com.framework.common.api.entity.IResultCode;
import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 部门异常码
 * code为3400-3500
 *
 * @author gaojia
 * @since 2023-08-02
 */
@Getter
@AllArgsConstructor
public enum DeptErrorCode implements IResultCode {

    /**
     * 异常码
     */
    DEPT_INFO_NOT_FIND(3400, "部门信息未查询到数据");

    /**
     * code编码
     */
    final int code;

    /**
     * 中文信息描述
     */
    final String message;

}
