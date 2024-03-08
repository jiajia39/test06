package com.framework.emt.system.domain.tag;

import com.baomidou.mybatisplus.annotation.TableName;
import com.framework.core.tenant.entity.TenantEntity;
import com.framework.emt.system.domain.tag.constant.enums.TagTypeEnum;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

/**
 * 异常标签 实体类
 *
 * @author gaojia
 * @since 2023-08-2
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
@TableName("emt_tag")
public class Tag extends TenantEntity {

    /**
     * 内容
     */
    private String content;

    /**
     * 标签类型 0知识库标签 1 异常原因项
     */
    private TagTypeEnum type;

}
