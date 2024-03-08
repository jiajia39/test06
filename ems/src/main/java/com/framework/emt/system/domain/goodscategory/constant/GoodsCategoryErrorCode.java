package com.framework.emt.system.domain.goodscategory.constant;

import com.framework.common.api.entity.IResultCode;
import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 异常分类 异常码
 *
 * @author makejava
 * @since 2024-02-22 13:24:42
 */
@Getter
@AllArgsConstructor
public enum GoodsCategoryErrorCode implements IResultCode {

    /**
     * 异常码
     */
    IS_EXIST(1001, "异常分类已存在"),

    NOT_FOUND(1002, "异常分类未查询到"),
    DELETE_NOT_EXIST(1003, "删除失败，异常分类未查询到"),
    NAME_IS_EXIST(1004, "异常分类名称已存在"),
    PARENT_ID_IS_NOT_EXIST(1005, "父分类不存在"),
    THE_MAXIMUM_CLASSIFICATION_LEVEL_IS_LEVEL_3(1006, "最大分类等级是第3级"),
    THERE_ARE_SUBCATEGORIES_UNDER_THIS_CATEGORY_THAT_CANNOT_BE_DELETED(1007, "该分类下有子分类不能删除"),;
   
   /**
     * code编码
     */
    final int code;

    /**
     * 中文信息描述
     */
    final String message;

}


