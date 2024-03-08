package com.framework.emt.system.domain.tagexception;

import com.baomidou.mybatisplus.annotation.TableName;
import com.framework.core.tenant.entity.TenantEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 异常标签关联表 实体类
 *
 * @author gaojia
 * @since 2023-08-2
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("emt_tag_exception")
public class TagException extends TenantEntity {

    /**
     * 异常标签id
     */
    private Long tagId;
    /**
     * 关联表id
     */
    private Long sourceId;
    /**
     * 异常关联类型 0知识库 1异常流程 2异常任务
     */
    private Integer sourceType;

}
