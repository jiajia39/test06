package com.framework.emt.system.domain.formfield.response;

import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.baomidou.mybatisplus.extension.handlers.JacksonTypeHandler;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.framework.admin.system.entity.DictBiz;
import com.framework.common.api.exception.ServiceException;
import com.framework.emt.system.domain.formfield.constant.enums.BusinessTypeEnum;
import com.framework.emt.system.domain.formfield.constant.enums.FormFieldSubTypeEnum;
import com.framework.emt.system.domain.formfield.constant.enums.FormFieldTypeEnum;
import com.framework.emt.system.domain.formfield.request.SelectListRequest;
import com.framework.emt.system.infrastructure.common.request.FileRequest;
import com.framework.emt.system.infrastructure.common.response.BaseUserResponse;
import io.swagger.annotations.ApiModelProperty;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * 异常表单字段表 响应体
 *
 * @author gaojia
 * @since 2023-07-28
 */
@Getter
@Setter
@EqualsAndHashCode(of = {"prop"})
public class FormFieldResponse extends BaseUserResponse implements Serializable {

    @ApiModelProperty(value = "异常表单字段id")
    private Long id;

    @ApiModelProperty(value = "表单编号")
    private String code;

    @ApiModelProperty(value = "字段文本内容")
    private String label;

    @ApiModelProperty(value = "字段属性名称")
    private String prop;

    @ApiModelProperty(value = "业务类型 0:异常提报1:异常响应2:异常处理3:异常挂起4:异常协同 5:异常验收")
    private BusinessTypeEnum businessType;

    @ApiModelProperty(value = "业务类型 0:异常提报1:异常响应2:异常处理3:异常挂起4:异常协同 5:异常验收")
    private String businessTypeName;
    @ApiModelProperty(value = "字段类型 0:input:文本输入框 1:select:下拉选择框 2:datetime:日期时间选择框 3:radio:单选框 4:checkbox;多选框  5:switch:开关 6 slider:滑块 7 upload:上传")
    private FormFieldTypeEnum type;

    @ApiModelProperty(value = "字段类型 0:input:文本输入框 1:select:下拉选择框 2:datetime:日期时间选择框 3:radio:单选框 4:checkbox;多选框  5:switch:开关  6 slider:滑块 7 upload:上传")
    private String typeName;

    @ApiModelProperty(value = "字段子类型 input:0:普通文本 1:文本区域 select:0:单选 1:多选  upload:0：不限制 1:图片（png、jpg、jpeg）  2:文件（pdf、excel、txt、word）")
    private FormFieldSubTypeEnum subtype;

    @ApiModelProperty(value = "字段子类型 input:0:普通文本 1:文本区域 select:0:单选 1:多选 upload:0：不限制 1:图片（png、jpg、jpeg）  2:文件（pdf、excel、txt、word）")
    private String subtypeName;

    @ApiModelProperty(value = "字典数据")
    @TableField(typeHandler = JacksonTypeHandler.class)
    private List<DictBiz> dictList = new ArrayList<>();

    @ApiModelProperty(value = "是否必填 0:否 1:是")
    private Integer required;

    @ApiModelProperty(value = "默认值")
    private String value;

    @ApiModelProperty(value = "最大长度")
    private Integer maxLength;

    @ApiModelProperty(value = "排序")
    private Integer sort;

    @ApiModelProperty(value = "字段值")
    private String key;

    @ApiModelProperty(value = "滑块最大值")
    private BigDecimal sliderMax;

    @ApiModelProperty(value = "滑块最小值")
    private BigDecimal sliderMin;

    @ApiModelProperty(value = "文件最大数目")
    private Integer fileMaxNum;

    @ApiModelProperty(value = "文件大小限制")
    private Integer fileMaxSize;

    @ApiModelProperty(value = "选择框选项")
    @JsonIgnore
    private String selectList;

    @ApiModelProperty(value = "选择框选项")
    private List<SelectListRequest> selectListInfo;

    @ApiModelProperty(value = "选择框选项")
    private Integer fieldsVersion;

    @ApiModelProperty(value = "是否最后版本")
    private Integer isLastVersion;

    @ApiModelProperty("状态")
    private Integer status;
    public void load() {
        this.subtypeName = Optional.ofNullable(subtype).map(FormFieldSubTypeEnum::getName).orElse(StrUtil.EMPTY);
        this.typeName = Optional.ofNullable(type).map(FormFieldTypeEnum::getName).orElse(StrUtil.EMPTY);
        this.businessTypeName = Optional.ofNullable(businessType).map(BusinessTypeEnum::getName).orElse(StrUtil.EMPTY);
    }

    public void validate(String key, String value) {
        if (FormFieldTypeEnum.INPUT.equals(type)) {
            if (maxLength > 0 && key.length() > maxLength) {
                throw new ServiceException(prop + " 长度超过:" + maxLength);
            }
        }
        if (FormFieldTypeEnum.SLIDER.equals(type)) {
            BigDecimal bigDecimal = new BigDecimal(key);
            if (ObjectUtil.isNotNull(sliderMin) && ObjectUtil.isNotNull(sliderMax) && (ObjectUtil.equal(bigDecimal.compareTo(sliderMax), 1) || ObjectUtil.equal(bigDecimal.compareTo(sliderMin), -1))) {
                throw new ServiceException(prop + "不能大于" + sliderMax + ",并且" + prop + "不能小于" + sliderMin);
            }
        }
        if (FormFieldTypeEnum.UPLOAD.equals(type)) {
            if (ObjectUtil.isNotNull(fileMaxNum)) {
                String substringFile = key.substring(1, key.length() - 1);
                if (StringUtils.isNotBlank(substringFile)) {
                    ObjectMapper objectMapper = new ObjectMapper();
                    try {
                        List<FileRequest> formFields = objectMapper.readValue(substringFile, new TypeReference<List<FileRequest>>() {
                        });
                        if (formFields.size() > fileMaxNum) {
                            throw new ServiceException(prop + "文件个数不能大于:" + fileMaxNum);
                        }
                    } catch (JsonProcessingException e) {
                    }
                }
            }
        }
        if (required == 1) {
            if (StrUtil.isBlank(key)) {
                throw new ServiceException(label + " 不能为空");
            }
        }

        this.key = key;
        this.value = value;
    }

}
