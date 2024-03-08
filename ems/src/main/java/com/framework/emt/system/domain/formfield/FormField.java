package com.framework.emt.system.domain.formfield;

import com.baomidou.mybatisplus.annotation.FieldStrategy;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.framework.core.tenant.entity.TenantEntity;
import com.framework.emt.system.domain.formfield.constant.enums.BusinessTypeEnum;
import com.framework.emt.system.domain.formfield.constant.enums.FormFieldSubTypeEnum;
import com.framework.emt.system.domain.formfield.constant.enums.FormFieldTypeEnum;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.math.BigDecimal;

/**
 * 异常表单 实体类
 *
 * @author gaojia
 * @since 2023-07-28
 */
@EqualsAndHashCode(callSuper = true)
@Data
@TableName("emt_form_field")
public class FormField extends TenantEntity {

    /**
     * 业务类型 0:异常提报1:异常响应2:异常处理3:异常挂起4:异常协同 5:异常验收
     */
    private BusinessTypeEnum businessType;

    /**
     * 表单编号
     */
    private String code;

    /**
     * 字段文本内容
     */
    private String label;

    /**
     * 字段属性名称
     */
    private String prop;

    /**
     * 字段类型 0:input:文本输入框 1:select:下拉选择框 2:datetime:日期时间选择框 3:radio:单选框 4:checkbox;多选框  5:switch:开关
     */
    private FormFieldTypeEnum type;

    /**
     * 字段子类型 input:0:普通文本 1:文本区域 select:0:单选 1:多选 文件 file:0:图片 1:文件
     */
    private FormFieldSubTypeEnum subtype;

    /**
     * 是否必填 0:否 1:是
     */
    private Integer required;

    /**
     * 默认值
     */
    private String value;

    /**
     * 最大长度
     */
    private Integer maxLength;

    /**
     * 排序
     */
    @TableField(updateStrategy = FieldStrategy.IGNORED)
    private Integer sort;

    /**
     * 滑块最大值
     */
    private BigDecimal sliderMax;

    /**
     * 滑块最小值
     */
    private BigDecimal sliderMin;

    /**
     * 文件最大数目
     */
    private Integer fileMaxNum;

    /**
     * 文件大小限制
     */
    private Integer fileMaxSize;

    /**
     * 选择框选项
     */
    private String selectList;

    /**
     * 字段版本号
     */
    private Integer fieldsVersion;

    /**
     * 是否最后版本
     */
    private Integer isLastVersion;
}
