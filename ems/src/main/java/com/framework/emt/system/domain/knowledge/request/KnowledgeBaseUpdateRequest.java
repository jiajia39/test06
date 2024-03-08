package com.framework.emt.system.domain.knowledge.request;

import cn.hutool.core.collection.CollectionUtil;
import com.framework.emt.system.infrastructure.common.request.FileRequest;
import com.framework.emt.system.infrastructure.common.validation.EnumValidator;
import com.framework.emt.system.infrastructure.constant.enums.EnableFlagEnum;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.Range;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.Collections;
import java.util.List;

/**
 * 知识库 更新参数
 *
 * @author ds_C
 * @since 2023-07-14
 */
@Getter
@Setter
public class KnowledgeBaseUpdateRequest implements Serializable {

    @NotNull(message = "知识库分类id不能为空")
    @ApiModelProperty(value = "知识库分类id", required = true)
    private Long knowledgeBaseCategoryId;

    @NotNull(message = "异常项id不能为空")
    @ApiModelProperty(value = "异常项id", required = true)
    private Long exceptionItemId;

    @Length(max = 20, message = "知识库标题长度限制{max}")
    @NotBlank(message = "知识库标题不能为空")
    @ApiModelProperty(value = "知识库标题", required = true)
    private String title;

    @NotBlank(message = "问题描述不能为空")
    @Length(max = 500, message = "问题描述长度限制{max}")
    @ApiModelProperty(value = "问题描述", required = true)
    private String problemDescription;

    @NotBlank(message = "原因分析不能为空")
    @Length(max = 500, message = "原因分析长度限制{max}")
    @ApiModelProperty(value = "原因分析", required = true)
    private String problemAnalysis;

    @Length(max = 500, message = "防止再发措施长度限制{max}")
    @ApiModelProperty(value = "防止再发措施")
    private String preventRecurrenceMeasure;

    @NotBlank(message = "解决建议不能为空")
    @Length(max = 500, message = "解决建议长度限制{max}")
    @ApiModelProperty(value = "解决建议", required = true)
    private String suggestion;

    @ApiModelProperty(value = "文件列表 默认最多10个")
    private List<FileRequest> files;

    @NotNull(message = "知识库状态不能为空")
    @EnumValidator(enumClazz = EnableFlagEnum.class, message = "状态类型错误 0:禁用 1:启用")
    @ApiModelProperty(value = "知识库状态 0:禁用 1:启用", required = true)
    private Integer enableFlag;

    @Range(min = 0, max = 99999, message = "排序最大{max}")
    @ApiModelProperty(value = "排序")
    private Integer sort;

    @Length(max = 500, message = "备注长度限制{max}")
    @ApiModelProperty(value = "备注")
    private String remark;

    public KnowledgeBaseUpdateRequest init() {
        if (CollectionUtil.isEmpty(files)) {
            files = Collections.emptyList();
        }
        return this;
    }

}
