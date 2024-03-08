package com.framework.emt.system.domain.goods.constant;

import com.framework.common.api.entity.IResultCode;
import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 商品 异常码
 *
 * @author makejava
 * @since 2024-02-22 14:58:25
 */
@Getter
@AllArgsConstructor
public enum GoodsErrorCode implements IResultCode {

    /**
     * 异常码
     */
    IS_EXIST(1001, "商品已存在"),
    NOT_FOUND(1002, "商品未查询到"),
    DELETE_NOT_EXIST(1003, "删除失败，商品未查询到"),
    GOODS_CODE_EXIST(1004,"商品编号已存在");
   
   /**
     * code编码
     */
    final int code;

    /**
     * 中文信息描述
     */
    final String message;

}


