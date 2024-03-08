package com.framework.center.domain.account.request;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import java.io.Serializable;
import java.util.Date;

/**
 * 账户参数类
 *
 * @author yankunw
 * @since 2023-04-12
 */
@Data
@ApiModel(value = "账户参数", description = "账户参数")
public class AccountCreateRequest implements Serializable {
    /**
     * 账户
     */
    @NotBlank(message = "账户不能为空！")
    @ApiModelProperty(value = "账户", required = true)
    private String account;

    /**
     * 密码
     */
    @NotBlank(message = "密码不能为空！")
    @ApiModelProperty(value = "密码", required = true)
    private String password;

    /**
     * 姓名
     */
    @NotBlank(message = "姓名不能为空！")
    @ApiModelProperty(value = "姓名", required = true)
    private String name;

    /**
     * 邮箱
     */
    @ApiModelProperty(value = "邮箱")
    @Email(message = "邮箱格式不正确")
    private String email;

    /**
     * 手机
     */
    @ApiModelProperty(value = "手机")
    private String phone;

    /**
     * 生日
     */
    @ApiModelProperty(value = "生日")
    private Date birthday;

    /**
     * 性别
     */
    @ApiModelProperty(value = "性别")
    private Integer sex;

    @ApiModelProperty(value = "uid")
    private String uid;
}
