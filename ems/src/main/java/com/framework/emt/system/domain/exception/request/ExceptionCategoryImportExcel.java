package com.framework.emt.system.domain.exception.request;

import com.alibaba.excel.annotation.ExcelProperty;
import com.framework.emt.system.infrastructure.constant.enums.EnableFlagEnum;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

/**
 * 异常分类 导入参数
 *
 * @author ds_C
 * @since 2023-07-21
 */
@Getter
@Setter
public class ExceptionCategoryImportExcel implements Serializable {

    @ExcelProperty(value = "父级异常分类名称")
    private String parentTitle;

    @ExcelProperty(value = "*异常分类名称（唯一）")
    private String title;

    @ExcelProperty(value = "备注")
    private String remark;

    @ApiModelProperty(value = "启用状态")
    private EnableFlagEnum enableFlag = EnableFlagEnum.ENABLE;

}
