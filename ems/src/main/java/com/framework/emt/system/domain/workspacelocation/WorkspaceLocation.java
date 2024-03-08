package com.framework.emt.system.domain.workspacelocation;

import com.baomidou.mybatisplus.annotation.TableName;
import com.framework.core.tenant.entity.TenantEntity;
import com.framework.emt.system.infrastructure.constant.enums.EnableFlagEnum;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * 作业单元 实体类
 *
 * @author ds_C
 * @since 2023-07-12
 */
@Data
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = true)
@TableName("emt_workspace_location")
public class WorkspaceLocation extends TenantEntity {

    /**
     * 父ID
     */
    private Long parentId;

    /**
     * 父级ID路径 parentId1_parentId2_parentId3
     */
    private String parentIdPath;



    /**
     * 作业单元名称
     */
    private String title;

    /**
     * 空间坐标 数据用“，”隔开
     */
    private String spaceCoordinate;

    /**
     * 作业单元编号 保留字段
     */
    private String code;

    /**
     * 状态 0:禁用 1:启用。保留字段
     */
    private EnableFlagEnum enableFlag;

    /**
     * 排序
     */
    private Integer sort;

    /**
     * 备注
     */
    private String remark;

}
