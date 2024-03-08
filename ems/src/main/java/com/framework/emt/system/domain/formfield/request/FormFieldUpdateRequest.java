package com.framework.emt.system.domain.formfield.request;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.framework.emt.system.domain.formfield.constant.enums.BusinessTypeEnum;
import com.framework.emt.system.domain.formfield.constant.enums.FormFieldTypeEnum;
import com.framework.emt.system.infrastructure.common.validation.EnumValidator;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.Range;

import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;

/**
 * 异常表单字段表 更新参数
 *
 * @author gaojia
 * @since 2023-07-28
 */
@Getter
@Setter
public class FormFieldUpdateRequest implements Serializable {

    @NotNull(message = "业务类型不能为空")
    @EnumValidator(enumClazz = BusinessTypeEnum.class, message = "业务类型 0:异常提报1:异常响应2:异常处理3:异常挂起4:异常协同 5:异常验收")
    @ApiModelProperty(value = "业务类型 0:异常提报1:异常响应2:异常处理3:异常挂起4:异常协同 5:异常验收", required = true)
    private Integer businessType;

    @JsonIgnore
    private String code;

    @ApiModelProperty(value = "字段文本内容")
    @Length(max = 20, message = "字段文本内容长度限制{max}")
    private String label;

    @NotNull(message = "字段属性名称不能为空")
    @ApiModelProperty(value = "字段属性名称", required = true)
    @Length(max = 50, message = "字段属性名称长度限制{max}")
    private String prop;

    @NotNull(message = "字段类型不能为空")
    @EnumValidator(enumClazz = FormFieldTypeEnum.class, message = "字段类型 0:input:文本输入框 1:select:下拉选择框 2:datetime:日期时间选择框 3:radio:单选框 4:checkbox;多选框  5:switch:开关")
    @ApiModelProperty(value = "字段类型 0:input:文本输入框 1:select:下拉选择框 2:datetime:日期时间选择框 3:radio:单选框 4:checkbox;多选框 5:switch:开关", required = true)
    private Integer type;

    @EnumValidator(enumClazz = FormFieldTypeEnum.class, message = "字段子类型 input:0:普通文本 1:文本区域 select:0:单选 1:多选")
    @ApiModelProperty(value = "字段子类型 input:0:普通文本 1:文本区域 select:0:单选 1:多选")
    private Integer subtype;

    @NotNull(message = "是否必填不能为空")
    @ApiModelProperty(value = "是否必填 0:否 1:是")
    @Range(min = 0, max = 1, message = "排序最大{max}")
    private Integer required;

    @ApiModelProperty(value = "默认值")
    @Length(max = 20, message = "默认值长度限制{max}")
    private String value;

    @ApiModelProperty(value = "最大长度")
    @Range(min = 0, max = 99999, message = "最大长度最大{max}")
    private Integer maxLength;

    @Range(min = 0, max = 99999, message = "排序最大{max}")
    @ApiModelProperty(value = "排序")
    private Integer sort;

    @ApiModelProperty(value = "滑块最大值")
    @Range(min = 0, max = 99999, message = "滑块最大值{max}")
    private BigDecimal sliderMax;

    @ApiModelProperty(value = "滑块最小值")
    @Range(min = 0, max = 99999, message = "滑块最小值{max}")
    private BigDecimal sliderMin;

    @ApiModelProperty(value = "文件最大数目")
    @Range(min = 0, max = 15, message = "文件最大数目{max}")
    private Integer fileMaxNum;

    @ApiModelProperty(value = "文件大小限制")
    private Integer fileMaxSize;

    @ApiModelProperty(value = "选择框选项")
    private List<String> selectListInfo;

    @ApiModelProperty(value = "状态")
    private Integer status;

}
