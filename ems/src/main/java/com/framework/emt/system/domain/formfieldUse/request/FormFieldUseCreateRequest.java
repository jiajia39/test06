package com.framework.emt.system.domain.formfieldUse.request;

import com.framework.emt.system.domain.formfield.constant.enums.BusinessTypeEnum;
import com.framework.emt.system.infrastructure.common.validation.EnumValidator;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

/**
 * 异常单字段使用表 新增参数
 *
 * @author makejava
 * @since 2024-01-31 18:20:10
 */
@ApiModel(value = "异常单字段使用表新增参数")
@Data
public class FormFieldUseCreateRequest implements Serializable {

    /**
     * 业务id
     */
    @ApiModelProperty(value = "业务id")
    private Long bizId;

    /**
     * 0:异常提报 1:异常响应 2:异常处理 3:异常挂起 4:异常协同 5:异常验收
     */
    @NotNull(message = "业务类型不能为空")
    @EnumValidator(enumClazz = BusinessTypeEnum.class, message = "业务类型 0:异常提报1:异常响应2:异常处理3:异常挂起4:异常协同 5:异常验收")
    @ApiModelProperty(value = "业务类型 0:异常提报1:异常响应2:异常处理3:异常挂起4:异常协同 5:异常验收", required = true)
    private Integer businessType;

    /**
     * 是否必填
     */
    @ApiModelProperty(value = "是否必填")
    private Integer required;

    /**
     * 表单编号
     */
    @ApiModelProperty(value = "表单编号")
    @Length(max = 20, message = "表单编号长度不能大于{max}")
    private String fieldsCode;

    /**
     * 表单版本号
     */
    @ApiModelProperty(value = "表单版本号")
    private Integer fieldsVersion;

    /**
     * 表单值
     */
    @ApiModelProperty(value = "表单值")
    private String fieldsValue;

}


