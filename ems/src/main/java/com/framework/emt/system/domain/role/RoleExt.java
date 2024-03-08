package com.framework.emt.system.domain.role;

import com.baomidou.mybatisplus.annotation.TableName;
import com.framework.core.tenant.entity.TenantEntity;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
@TableName("emt_role_ext")
public class RoleExt extends TenantEntity {
    /**
     * 角色id
     */
    @ApiModelProperty(value = "角色id", required = true)
    private Long roleId;
    /**
     * 异常管理
     */
    @ApiModelProperty(value = "异常管理 0 否 1 是", required = true)
    private Integer exceptionManagement;
}
