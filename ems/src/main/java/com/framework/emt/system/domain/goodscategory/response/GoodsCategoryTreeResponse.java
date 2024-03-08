package com.framework.emt.system.domain.goodscategory.response;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.extension.handlers.JacksonTypeHandler;
import com.framework.emt.system.infrastructure.common.request.FileRequest;
import com.framework.emt.system.infrastructure.common.response.BaseUserResponse;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.List;

/**
 * 异常分类 返回参数
 *
 * @author makejava
 * @since 2024-02-22 13:24:42
 */
@ApiModel(value = "异常分类返回参数")
@Data
@EqualsAndHashCode(callSuper = true)
public class GoodsCategoryTreeResponse extends BaseUserResponse {
    /**
     * id
     */
    @ApiModelProperty(value = "id")
    private Long id;
    /**
     * 父ID
     */
    @ApiModelProperty(value = "父ID")
    private Long parentId;

    /**
     * 父级ID路径 parentId1_parentId2_parentId3
     */
    @ApiModelProperty(value = "父级ID路径 parentId1_parentId2_parentId3")
    private String parentIdPath;

    /**
     * 分类名称
     */
    @ApiModelProperty(value = "分类名称")
    private String title;

    /**
     * 分类编号 保留字段
     */
    @ApiModelProperty(value = "分类编号 保留字段")
    private String code;

    /**
     * 状态 0:禁用 1:启用。保留字段
     */
    @ApiModelProperty(value = "状态 0:禁用 1:启用。保留字段")
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
    private String remark;

    /**
     * 分类图片
     */
    @ApiModelProperty(value = "分类图片")
    @TableField(typeHandler = JacksonTypeHandler.class)
    private List<FileRequest> categoryImg;

    /**
     * 层级
     */
    @ApiModelProperty(value = "层级")
    private Integer level;

    @ApiModelProperty(value = "商品数量")
    private Integer goodsCount;

    private List<GoodsCategoryTreeResponse> childList;

}


