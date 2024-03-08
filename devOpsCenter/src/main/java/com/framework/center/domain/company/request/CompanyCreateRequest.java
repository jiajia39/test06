package com.framework.center.domain.company.request;

import com.framework.center.infrastructure.common.FileRequest;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;
import java.io.Serializable;

/**
 * 公司信息创建更新入参
 *
 * @author yankunw
 * @since 2023-04-12
 */
@Data
@ApiModel(value = "公司入参", description = "公司入参")
public class CompanyCreateRequest implements Serializable {
    /**
     * 公司名称
     */
    @NotBlank(message = "公司名称不能为空！")
    @Size(max = 255, message = "公司名称长度最大255")
    @ApiModelProperty(value = "公司名称", required = true)
    private String name;

    @ApiModelProperty(value = "联系人")
    @Size(max = 50, message = "联系人长度最大50")
    private String contacts;

    @NotBlank(message = "联系电话不能为空！")
    @Pattern(regexp = "[1-9][0-9]{10}", message = "手机号格式不正确")
    @ApiModelProperty(value = "联系电话", required = true)
    private String contactPhone;

    @ApiModelProperty(value = "邮箱")
    @Email(message = "邮箱格式不正确")
    private String email;

    @ApiModelProperty(value = "公司地址")
    @Size(max = 255, message = "公司地址长度最大255")
    private String address;

    @ApiModelProperty(value = "公司Logo")
    private FileRequest logo;
}
