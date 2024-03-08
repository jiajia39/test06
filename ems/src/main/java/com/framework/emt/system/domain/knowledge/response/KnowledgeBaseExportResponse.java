package com.framework.emt.system.domain.knowledge.response;

import com.alibaba.excel.annotation.ExcelIgnore;
import com.alibaba.excel.annotation.ExcelProperty;
import com.alibaba.excel.annotation.write.style.ColumnWidth;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.util.Date;

/**
 * 知识库导出 响应体
 *
 * @author ds_C
 * @since 2023-07-15
 */
@Getter
@Setter
public class KnowledgeBaseExportResponse implements Serializable {

    @ColumnWidth(20)
    @ExcelProperty("知识库标题")
    private String title;

    @ColumnWidth(20)
    @ExcelProperty("知识库分类")
    private String knowledgeBaseCategory;

    @ColumnWidth(20)
    @ExcelProperty("异常项")
    private String exceptionItem;

    @ColumnWidth(20)
    @ExcelProperty("紧急程度")
    private String exceptionItemPriority;

    @ColumnWidth(20)
    @ExcelProperty("严重程度")
    private String exceptionItemSeverity;

    @ColumnWidth(20)
    @ExcelProperty("关键词")
    private String keywords;

    @ColumnWidth(20)
    @ExcelProperty("启用/禁用")
    private String statusName;

    @ExcelIgnore
    @ApiModelProperty(value = "创建人id")
    private Long createUser;

    @ColumnWidth(20)
    @ExcelProperty("创建人")
    private String createUserName;

    @ColumnWidth(20)
    @ExcelProperty("创建时间")
    private Date createTime;

    @ExcelIgnore
    @ApiModelProperty(value = "最后更新人id")
    private Long updateUser;

    @ColumnWidth(20)
    @ExcelProperty("最后更新人")
    private String updateUserName;

    @ColumnWidth(20)
    @ExcelProperty("最后更新时间")
    private Date updateTime;

}
