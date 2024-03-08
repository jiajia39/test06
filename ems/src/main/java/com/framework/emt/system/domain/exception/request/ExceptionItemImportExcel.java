package com.framework.emt.system.domain.exception.request;

import com.alibaba.excel.annotation.ExcelProperty;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

/**
 * 异常项 导入参数
 *
 * @author ds_C
 * @since 2023-07-21
 */
@Getter
@Setter
public class ExceptionItemImportExcel implements Serializable {

    @ExcelProperty(value = "*异常分类名称")
    private String exceptionCategoryTitle;

    @ExcelProperty(value = "*异常项名称（唯一）")
    private String title;

    @ExcelProperty(value = "*紧急程度")
    private String priorityName;

    @ExcelProperty(value = "*严重程度")
    private String severityName;

    @ExcelProperty(value = "*响应时限（单位：分钟）")
    private String responseDurationTimeStr;

    @ExcelProperty(value = "*处理时限（单位：分钟）")
    private String handingDurationTimeStr;

    @ExcelProperty(value = "备注")
    private String remark;

}
