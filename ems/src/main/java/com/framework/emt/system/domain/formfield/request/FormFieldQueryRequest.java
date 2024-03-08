package com.framework.emt.system.domain.formfield.request;

import com.framework.core.mybatisplus.support.Query;
import com.framework.emt.system.domain.formfield.constant.enums.BusinessTypeEnum;
import com.framework.emt.system.domain.formfield.constant.enums.FormFieldTypeEnum;
import com.framework.emt.system.infrastructure.common.validation.EnumValidator;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.validator.constraints.Length;

import java.io.Serializable;

/**
 * 异常表单字段表 查询条件
 *
 * @author 高佳
 * @since 2023-07-28
 */
@Getter
@Setter
public class FormFieldQueryRequest extends Query implements Serializable {
    @EnumValidator(enumClazz = BusinessTypeEnum.class, message = "业务类型 0:异常提报1:异常响应2:异常处理3:异常挂起4:异常协同 5:异常验收")
    @ApiModelProperty(value = "业务类型 0:异常提报1:异常响应2:异常处理3:异常挂起4:异常协同 5:异常验收")
    private Integer businessType;

    @EnumValidator(enumClazz = FormFieldTypeEnum.class, message = "字段类型 0:input:文本输入框 1:select:下拉选择框 2:datetime:日期时间选择框 3:radio:单选框 4:checkbox;多选框  5:switch:开关")
    @ApiModelProperty(value = "字段类型 0:input:文本输入框 1:select:下拉选择框 2:datetime:日期时间选择框 3:radio:单选框 4:checkbox;多选框  5:switch:开关")
    private Integer type;

    @ApiModelProperty(value = "表单编号")
    @Length(max = 20, message = "表单编号长度限制{max}")
    private String code;

    @ApiModelProperty(value = "字段属性名称")
    @Length(max = 50, message = "字段属性名称长度限制{max}")
    private String prop;

    @ApiModelProperty(value = "字段文本内容")
    @Length(max = 20, message = "字段文本内容长度限制{max}")
    private String label;

    @ApiModelProperty(value = "是否携带字典数据")
    private boolean carryDictData;

    @ApiModelProperty(value = "是否启用0 禁用 1启用")
    private Integer status;
}
