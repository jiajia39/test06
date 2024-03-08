package com.framework.center.domain.sync;

import com.baomidou.mybatisplus.annotation.TableName;
import com.framework.core.tenant.entity.TenantEntity;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 说明：用户部门表
 * 对象名：UserDept
 * 描述：实体类
 *
 * @author yankunw
 * @date 2021-10-09 11:41:24
 */
@EqualsAndHashCode(callSuper = true)
@Data
@TableName("ft_user_dept")
@ApiModel(value = "UserDept对象", description = "用户部门表")
public class UserDept extends TenantEntity {
    /**
     * 用户ID
     */
    @ApiModelProperty(value = "用户ID")
    private Long userId;
    /**
     * 部门ID
     */
    @ApiModelProperty(value = "部门ID")
    private Long deptId;

}