package com.framework.emt.system.domain.formfield.constant.code;

import com.framework.common.api.entity.IResultCode;
import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 异常表单异常码
 * code为3100-3200
 *
 * @author gaojia
 * @since 2023-07-28
 */
@Getter
@AllArgsConstructor
public enum FormFieldErrorCode implements IResultCode {

    /**
     * 异常码
     */
    NOT_EXIST_OR_ENABLE_DATA_CAN_NOT_DELETE(3100, "删除失败，不存在或已启用的数据无法删除"),
    FORM_FIELD_CODE_EXIST(3101, "异常表单编号已存在"),
    THE_LIST_OF_ABNORMAL_FORM_IDS_CONTAINS_IDS_THAT_DO_NOT_EXIST_IN_THE_FORM(3102, "异常表单id列表含有表单不存在的id"),
    FORM_FIELD_INFO_NOT_FIND(3103, "异常表单信息未查询到数据"),
    THE_SELECTION_BOX_OPTION_CANNOT_BE_EMPTY_AND_MUST_HAVE_AT_LEAST_TWO_OPTIONS(3104, "选择框选项不能为空，且至少是两个"),
    THE_MAXIMUM_AND_MINIMUM_VALUES_OF_THE_SLIDER_CANNOT_BE_EMPTY(3105, "滑块最大最小值不能为空"),

    THE_MAXIMUM_NUMBER_OF_FILES_AND_FILE_SIZE_LIMIT_CANNOT_BE_EMPTY(3106, "文件最大数目和文件大小限制不能为空"),

    THE_SUB_TYPE_OF_A_PARAGRAPH_CANNOT_BE_EMPTY(3107, "字段子类型不能为空"),

    THE_FORM_DOES_NOT_EXIST(3108, "表单不存在"),

    THIS_FORM_HAS_BEEN_REFERENCED_AND_CANNOT_BE_DELETED(3109, "该表单已被引用不能删除"),

    ;

    /**
     * code编码
     */
    final int code;

    /**
     * 中文信息描述
     */
    final String message;

}
