package com.framework.emt.system.domain.knowledge.request;

import cn.hutool.core.util.StrUtil;
import com.alibaba.excel.annotation.ExcelProperty;
import com.framework.emt.system.infrastructure.common.request.FileRequest;
import com.framework.emt.system.infrastructure.constant.enums.EnableFlagEnum;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.util.Collections;
import java.util.List;

/**
 * describe
 *
 * @author ds_C
 * @since 2023-09-06
 */
@Getter
@Setter
public class KnowledgeBaseImportExcel implements Serializable {

    @ExcelProperty(value = "*知识库标题（唯一）")
    private String title;

    @ExcelProperty(value = "*知识库分类名称")
    private String knowledgeBaseCategoryTitle;

    @ExcelProperty(value = "*异常项名称")
    private String exceptionItemTitle;

    @ExcelProperty(value = "关键词（顿号分割）")
    private String keyWords = StrUtil.EMPTY;

    @ExcelProperty(value = "*问题描述")
    private String problemDescription;

    @ExcelProperty(value = "*原因分析")
    private String problemAnalysis;

    @ExcelProperty(value = "*解决方案")
    private String suggestion;

    @ExcelProperty(value = "防止再发措施")
    private String preventRecurrenceMeasure;

    @ExcelProperty(value = "备注")
    private String remark;

    @ApiModelProperty(value = "禁用状态")
    private EnableFlagEnum enableFlag = EnableFlagEnum.FORBIDDEN;

    @ApiModelProperty(value = "文件列表")
    private List<FileRequest> files = Collections.emptyList();

}
