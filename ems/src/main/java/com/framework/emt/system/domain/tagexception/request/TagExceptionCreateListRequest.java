package com.framework.emt.system.domain.tagexception.request;

import com.framework.emt.system.domain.tagexception.constant.enums.SourceTypeEnum;
import com.framework.emt.system.infrastructure.common.validation.EnumValidator;
import com.framework.emt.system.infrastructure.common.validation.UniqueElementsValidator;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.io.Serializable;
import java.util.List;

/**
 * 异常标签关联 创建参数
 *
 * @author gaojia
 * @since 2023-08-02
 */
@Getter
@Setter
@Accessors(chain = true)
public class TagExceptionCreateListRequest implements Serializable {

    @NotEmpty(message = "异常标签id列表不能为空")
    @ApiModelProperty(value = "异常标签id列表", required = true)
    @UniqueElementsValidator(message = "异常标签id不能重复")
    @Size(max = 100, message = "id列表数目最多100个")
    private List<Long> tagIdList;

    @NotNull(message = "关联表id不能为空")
    @ApiModelProperty(value = "关联表id", required = true)
    private Long sourceId;

    @NotNull(message = "异常关联类型不能为空")
    @EnumValidator(enumClazz = SourceTypeEnum.class, message = "异常关联类型 0知识库 1异常流程 2异常任务")
    @ApiModelProperty(value = "异常关联类型 0知识库 1异常流程 2异常任务", required = true)
    private Integer sourceType;

    public TagExceptionCreateListRequest(List<Long> tagIdList,
                                         Long sourceId,
                                         Integer sourceType) {
        this.tagIdList = tagIdList;
        this.sourceId = sourceId;
        this.sourceType = sourceType;
    }

}
