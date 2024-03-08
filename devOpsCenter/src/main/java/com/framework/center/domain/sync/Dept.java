package com.framework.center.domain.sync;

import cn.hutool.core.date.DatePattern;
import cn.hutool.core.date.DateUtil;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.Version;
import com.framework.common.auth.utils.AuthUtil;
import com.framework.core.tenant.entity.TenantEntity;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.Date;

/**
 * 说明：机构表
 * 对象名：Dept
 * 描述：实体类
 *
 * @author yankunw
 * @date 2021-10-09 11:38:35
 */
@EqualsAndHashCode(callSuper = true)
@Data
@TableName("ft_dept")
@ApiModel(value = "Dept对象", description = "机构表")
public class Dept extends TenantEntity {
    /**
     * 主键id
     */
    @TableId(value = "id",type = IdType.INPUT)
    @ApiModelProperty("主键id")
    private Long id;
    /**
     * 创建人
     */
    @ApiModelProperty("创建人")
    private Long createUser;
    /**
     * 创建部门
     */
    @ApiModelProperty("创建部门")
    private Long createDept;
    /**
     * 创建时间
     */
    @ApiModelProperty("创建时间")
    private Date createTime;
    /**
     * 更新人
     */
    @ApiModelProperty("更新人")
    private Long updateUser;
    /**
     * 更新时间
     */
    @ApiModelProperty("更新时间")
    private Date updateTime;
    /**
     * 业务状态 1 正常 2 禁用
     */
    @ApiModelProperty("业务状态")
    private Integer status;
    /**
     * 是否已删除 0 正常 1 已删除
     */
    @ApiModelProperty("是否已删除")
    private Integer isDeleted;
    /**
     * 数据版本
     */
    @ApiModelProperty("数据版本")
    @Version
    private Integer version;

    /**
     * 租户ID
     */
    @ApiModelProperty(value = "租户ID")
    private String tenantId;


    /**
     * 父主键
     */
    @ApiModelProperty(value = "父主键")
    private Long parentId;
    /**
     * 祖级列表
     */
    @ApiModelProperty(value = "祖级列表")
    private String ancestors;
    /**
     * 部门类型
     */
    @ApiModelProperty(value = "部门类型")
    private Integer deptCategory;
    /**
     * 部门名
     */
    @ApiModelProperty(value = "部门名")
    private String deptName;
    /**
     * 部门全称
     */
    @ApiModelProperty(value = "部门全称")
    private String fullName;
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
     * 第三方平台部门id
     */
    @ApiModelProperty(value = "第三方平台部门id")
    private String thirdPartyId;
}