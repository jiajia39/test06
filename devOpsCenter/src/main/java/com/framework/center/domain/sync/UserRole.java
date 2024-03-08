package com.framework.center.domain.sync;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.framework.core.tenant.entity.TenantEntity;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 说明：用户角色表
 * 对象名：UserRole
 * 描述：实体类
 *
 * @author yankunw
 * @date 2021-10-09 11:41:42
 */
@EqualsAndHashCode(callSuper = true)
@Data
@TableName("ft_user_role")
@ApiModel(value = "UserRole对象", description = "用户角色表")
public class UserRole extends TenantEntity {
    /**
     * 主键id
     */
    @TableId(value = "id",type = IdType.INPUT)
    @ApiModelProperty("主键id")
    private Long id;
    /**
     * 用户ID
     */
    @ApiModelProperty(value = "用户ID")
    private Long userId;
    /**
     * 角色ID
     */
    @ApiModelProperty(value = "角色ID")
    private Long roleId;
}