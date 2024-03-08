package com.framework.emt.system.domain.knowledge.response;

import com.framework.emt.system.domain.exception.convert.constant.enums.PriorityEnum;
import com.framework.emt.system.domain.exception.convert.constant.enums.SeverityEnum;
import com.framework.emt.system.domain.tag.response.TagResponse;
import com.framework.emt.system.infrastructure.common.request.FileRequest;
import com.framework.emt.system.infrastructure.constant.enums.EnableFlagEnum;
import com.framework.emt.system.infrastructure.common.response.BaseUserResponse;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.util.List;

/**
 * 知识库 响应体
 *
 * @author ds_C
 * @since 2023-07-14
 */
@Getter
@Setter
public class KnowledgeBaseResponse extends BaseUserResponse implements Serializable {

    @ApiModelProperty(value = "知识库id")
    private Long id;

    @ApiModelProperty(value = "知识库分类id")
    private Long knowledgeBaseCategoryId;

    @ApiModelProperty(value = "知识库分类")
    private String knowledgeBaseCategoryName;

    @ApiModelProperty(value = "异常项id")
    private Long exceptionItemId;

    @ApiModelProperty(value = "异常项")
    private String exceptionItem;

    @ApiModelProperty(value = "异常项紧急程度")
    private PriorityEnum exceptionItemPriority;

    @ApiModelProperty(value = "异常项严重程度")
    private SeverityEnum exceptionItemSeverity;

    @ApiModelProperty(value = "关键词列表")
    private List<TagResponse> keyWords;

    @ApiModelProperty(value = "知识库名称")
    private String title;

    @ApiModelProperty(value = "问题描述")
    private String problemDescription;

    @ApiModelProperty(value = "原因分析")
    private String problemAnalysis;

    @ApiModelProperty(value = "防止再发措施")
    private String preventRecurrenceMeasure;

    @ApiModelProperty(value = "解决建议")
    private String suggestion;

    @ApiModelProperty(value = "文件列表")
    private List<FileRequest> files;

    @ApiModelProperty(value = "知识库状态 0:禁用 1:启用")
    private EnableFlagEnum enableFlag;

    @ApiModelProperty(value = "排序")
    private Integer sort;

    @ApiModelProperty(value = "备注")
    private String remark;

}
