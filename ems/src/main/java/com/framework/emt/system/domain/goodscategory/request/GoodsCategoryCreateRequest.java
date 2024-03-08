package com.framework.emt.system.domain.goodscategory.request;

import com.framework.emt.system.infrastructure.common.request.FileRequest;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.Range;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

/**
 * 异常分类 新增参数
 *
 * @author makejava
 * @since 2024-02-22 13:24:42
 */
@ApiModel(value = "异常分类新增参数")
@Data
public class GoodsCategoryCreateRequest implements Serializable {

    /**
     * 父ID
     */
    @ApiModelProperty(value = "父ID")
    private Long parentId;

    /**
     * 分类名称
     */
    @ApiModelProperty(value = "分类名称", required = true)
    @NotBlank(message = "分类名称不能为空")
    @Length(max = 20, message = "分类名称长度不能大于{max}")
    private String title;

    /**
     * 分类编号 保留字段
     */
    @ApiModelProperty(value = "分类编号 保留字段")
    @Length(max = 20, message = "分类编号 保留字段长度不能大于{max}")
    private String code;

    /**
     * 状态 0:禁用 1:启用。保留字段
     */
    @ApiModelProperty(value = "状态 0:禁用 1:启用。保留字段", required = true)
    @Range(max = 1, min = 0, message = "启用禁用最大值是${max},最小值是${min}")
    @NotNull(message = "状态不能为空")
    private Integer enableFlag;

    /**
     * 排序
     */
    @ApiModelProperty(value = "排序")
    private Integer sort;

    /**
     * 备注
     */
    @ApiModelProperty(value = "备注")
    @Length(max = 500, message = "备注长度不能大于{max}")
    private String remark;

    /**
     * 分类图片
     */
    @ApiModelProperty(value = "分类图片")
    private List<FileRequest> categoryImg;

}


