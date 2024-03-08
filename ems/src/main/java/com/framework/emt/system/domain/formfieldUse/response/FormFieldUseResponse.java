package com.framework.emt.system.domain.formfieldUse.response;

import com.framework.emt.system.domain.formfield.constant.enums.BusinessTypeEnum;
import com.framework.emt.system.infrastructure.common.response.BaseUserResponse;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * 异常单字段使用表 实体类
 *
 * @author makejava
 * @since 2024-01-31 18:20:10
 */
@Data
public class FormFieldUseResponse extends BaseUserResponse implements Serializable {

    @ApiModelProperty(value = "业务id")
    private Long bizId;

    @ApiModelProperty(value = "业务类型")
    private BusinessTypeEnum businessType;


    @ApiModelProperty(value = "是否必填")
    private Integer required;


    @ApiModelProperty(value = "表单编号")
    private String fieldsCode;


    @ApiModelProperty(value = "表单版本号")
    private Integer fieldsVersion;


    @ApiModelProperty(value = "表单值")
    private String fieldsValue;
}


