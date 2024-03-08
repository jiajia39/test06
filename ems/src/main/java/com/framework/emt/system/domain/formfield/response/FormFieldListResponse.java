package com.framework.emt.system.domain.formfield.response;

import io.swagger.annotations.ApiModelProperty;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import org.apache.commons.lang3.StringUtils;

import java.io.Serializable;

/**
 * 异常表单字段表 响应体
 *
 * @author gaojia
 * @since 2023-07-28
 */
@Getter
@Setter
@EqualsAndHashCode(of = {"prop"})
public class FormFieldListResponse implements Serializable {
    @ApiModelProperty(value = "字段属性名称")
    private String label;

    @ApiModelProperty(value = "默认值")
    private String value;

    @ApiModelProperty(value = "字段值")
    private String key = "";

    public FormFieldListResponse init(String label, String value, String key) {
        this.label = label;
        this.value = value;
        if (StringUtils.isNotBlank(key)) {
            this.key = key;
        }
        return this;
    }

}
