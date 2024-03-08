package com.framework.emt.system.domain.formfieldUse;

import com.baomidou.mybatisplus.annotation.TableName;
import com.framework.core.tenant.entity.TenantEntity;
import com.framework.emt.system.domain.formfield.constant.enums.BusinessTypeEnum;
import lombok.EqualsAndHashCode;
import lombok.Data;

/**
 * 异常单字段使用表 实体类
 *
 * @author makejava
 * @since 2024-01-31 18:20:10
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("emt_form_field_use")
public class FormFieldUse extends TenantEntity {

    /**
     * 业务id
     */
    private Long bizId;

    /**
     * 0:异常提报 1:异常响应 2:异常处理 3:异常挂起 4:异常协同 5:异常验收
     */
    private BusinessTypeEnum businessType;

    /**
     * 是否必填
     */
    private Integer required;

    /**
     * 表单编号
     */
    private String fieldsCode;

    /**
     * 表单版本号
     */
    private Integer fieldsVersion;

    /**
     * 表单值
     */
    private String fieldsValue;


}


