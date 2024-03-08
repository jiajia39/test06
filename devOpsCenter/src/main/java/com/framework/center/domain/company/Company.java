package com.framework.center.domain.company;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.extension.handlers.JacksonTypeHandler;
import com.framework.center.infrastructure.common.FileRequest;
import com.framework.core.tenant.entity.TenantEntity;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;

/**
 * 公司表
 *
 * @TableName mss_company
 */
@EqualsAndHashCode(callSuper = true)
@TableName(value = "mss_company", autoResultMap = true)
@Data
public class Company extends TenantEntity implements Serializable {
    /**
     * 公司名称
     */
    @TableField(value = "name")
    private String name;

    @ApiModelProperty(value = "联系人")
    private String contacts;

    @ApiModelProperty(value = "联系电话")
    private String contactPhone;

    @ApiModelProperty(value = "邮箱")
    private String email;

    @ApiModelProperty(value = "公司地址")
    private String address;

    @ApiModelProperty(value = "公司Logo")
    @TableField(typeHandler = JacksonTypeHandler.class)
    private FileRequest logo;
}